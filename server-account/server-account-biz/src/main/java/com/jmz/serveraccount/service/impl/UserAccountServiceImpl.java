package com.jmz.serveraccount.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmz.jmzcommoncore.utils.StringUtils;
import com.jmz.serveraccount.dto.AccountAdjustDTO;
import com.jmz.serveraccount.dto.AccountQueryDTO;
import com.jmz.serveraccount.entity.Transaction;
import com.jmz.serveraccount.entity.UserAccount;
import com.jmz.serveraccount.enums.AccountTypeEnum;
import com.jmz.serveraccount.mapper.TransactionMapper;
import com.jmz.serveraccount.mapper.UserAccountMapper;
import com.jmz.serveraccount.service.UserAccountService;
import com.jmz.serveraccount.feign.UserAccountFeignClient;
import com.jmz.serveraccount.vo.AccountVO;
import com.jmz.serveraccount.vo.TransactionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.security.core.context.SecurityContextHolder;
import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveruser.entity.User;
import com.jmz.serveruser.feign.UserFeignClient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    @CacheEvict(value = "account", key = "'user:' + #userId")
    public boolean createAccountForUser(Long userId) {
        LambdaQueryWrapper<UserAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAccount::getUserId, userId);
        if (this.count(wrapper) > 0) {
            return true;
        }

        UserAccount account = new UserAccount();
        account.setUserId(userId);
        account.setBalance(BigDecimal.ZERO);
        account.setFrozenAmount(BigDecimal.ZERO);
        account.setTotalIncome(BigDecimal.ZERO);
        account.setTotalExpense(BigDecimal.ZERO);
        account.setVersion(0L);
        account.setCreatedAt(new Date());
        account.setUpdatedAt(new Date());

        return this.save(account);
    }

    @Override
    @Cacheable(value = "account", key = "'user:' + #userId")
    public UserAccount getAccountByUserId(Long userId) {
        LambdaQueryWrapper<UserAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAccount::getUserId, userId);
        return this.getOne(wrapper);
    }

    @Override
    public IPage<AccountVO> getAccountList(AccountQueryDTO queryDTO) {
        Page<AccountVO> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        return this.baseMapper.selectAccountListWithUser(page, queryDTO.getUserId(), queryDTO.getPhone());
    }

    @Override
    public AccountVO getAccountDetail(Long accountId) {
        UserAccount account = this.getById(accountId);
        if (account == null) {
            return null;
        }

        AccountVO vo = new AccountVO();
        BeanUtils.copyProperties(account, vo);

        try {
            R userResult = userFeignClient.getUserById(account.getUserId());
            if (userResult != null && userResult.get(R.DATA_TAG) != null) {
                User user = (User) userResult.get(R.DATA_TAG);
                if (user != null) {
                    vo.setUsername(user.getUsername());
                    vo.setPhone(user.getPhone());
                    vo.setNickname(user.getNickname());
                }
            }
        } catch (Exception ignored) {
        }

        return vo;
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @CacheEvict(value = "account", key = "'user:' + #adjustDTO.userId")
    public boolean adjustAccountBalance(AccountAdjustDTO adjustDTO) {
        UserAccount account = getAccountByUserId(adjustDTO.getUserId());
        if (account == null) {
            return false;
        }

        AccountTypeEnum typeEnum = adjustDTO.getType();
        if (typeEnum == null) {
            throw new RuntimeException("调整类型不能为空");
        }

        BigDecimal balanceBefore = account.getBalance();
        BigDecimal frozenBefore = account.getFrozenAmount();
        BigDecimal amount = adjustDTO.getAmount();
        Long currentVersion = account.getVersion();

        BigDecimal balanceAfter = balanceBefore;
        BigDecimal frozenAfter = frozenBefore;

        boolean success = executeBalanceOperation(adjustDTO, typeEnum, currentVersion);
        if (!success) {
            return false;
        }

        updateBalanceSnapshots(typeEnum, adjustDTO, balanceBefore, frozenBefore, balanceAfter, frozenAfter);

        // 插入交易流水记录
        insertTransactionRecord(adjustDTO, balanceBefore, frozenBefore, balanceAfter, frozenAfter);

        return true;
    }

    /**
     * 执行余额操作的核心逻辑
     */
    private boolean executeBalanceOperation(AccountAdjustDTO adjustDTO, AccountTypeEnum typeEnum, Long currentVersion) {
        if (typeEnum.isDeduct()) {
            return deductBalanceWithLock(adjustDTO, currentVersion);
        } else if (typeEnum.isFreeze()) {
            return freezeBalanceWithLock(adjustDTO, currentVersion);
        } else if (typeEnum == AccountTypeEnum.UNFREEZE) {
            return unfreezeBalanceWithLock(adjustDTO, currentVersion);
        } else if (typeEnum == AccountTypeEnum.FROZEN_DEDUCT) {
            return deductFrozenWithLock(adjustDTO, currentVersion);
        } else if (typeEnum.isAdd()) {
            return addBalance(adjustDTO);
        } else {
            throw new RuntimeException("不支持的账户操作类型: " + typeEnum);
        }
    }

    /**
     * 余额扣减（防止超扣）
     */
    private boolean deductBalanceWithLock(AccountAdjustDTO adjustDTO, Long currentVersion) {
        int rows = this.baseMapper.deductBalanceWithLock(
                adjustDTO.getUserId(), adjustDTO.getAmount(), currentVersion, adjustDTO.getIdempotencyKey());
        if (rows == 0) {
            throw new RuntimeException("余额不足或并发冲突，请稍后重试");
        }
        return true;
    }

    /**
     * 冻结资金
     */
    private boolean freezeBalanceWithLock(AccountAdjustDTO adjustDTO, Long currentVersion) {
        int rows = this.baseMapper.freezeBalanceWithLock(
                adjustDTO.getUserId(), adjustDTO.getAmount(), currentVersion);
        if (rows == 0) {
            throw new RuntimeException("余额不足，无法冻结资金");
        }
        return true;
    }

    /**
     * 解冻资金（双重校验余额）
     */
    private boolean unfreezeBalanceWithLock(AccountAdjustDTO adjustDTO, Long currentVersion) {
        UserAccount account = getAccountByUserId(adjustDTO.getUserId());
        if (account.getBalance().compareTo(adjustDTO.getAmount()) < 0) {
            throw new RuntimeException("余额不足，无法解冻");
        }
        int rows = this.baseMapper.unfreezeBalanceWithLock(
                adjustDTO.getUserId(), adjustDTO.getAmount(), currentVersion);
        if (rows == 0) {
            throw new RuntimeException("冻结金额不足，无法解冻");
        }
        return true;
    }

    /**
     * 扣除冻结金额
     */
    private boolean deductFrozenWithLock(AccountAdjustDTO adjustDTO, Long currentVersion) {
        int rows = this.baseMapper.deductFrozenWithLock(
                adjustDTO.getUserId(), adjustDTO.getAmount(), currentVersion);
        if (rows == 0) {
            throw new RuntimeException("冻结金额不足，无法扣除");
        }
        return true;
    }

    /**
     * 增加余额
     */
    private boolean addBalance(AccountAdjustDTO adjustDTO) {
        UserAccount account = getAccountByUserId(adjustDTO.getUserId());
        account.setBalance(account.getBalance().add(adjustDTO.getAmount()));
        account.setTotalIncome(account.getTotalIncome().add(adjustDTO.getAmount()));
        account.setUpdatedAt(new Date());
        if (!this.updateById(account)) {
            throw new RuntimeException("并发冲突，请稍后重试");
        }
        return true;
    }

    /**
     * 计算余额快照
     */
    private void updateBalanceSnapshots(AccountTypeEnum typeEnum, AccountAdjustDTO adjustDTO,
                                         BigDecimal balanceBefore, BigDecimal frozenBefore,
                                         BigDecimal balanceAfter, BigDecimal frozenAfter) {
        if (typeEnum.isDeduct()) {
            balanceAfter = balanceBefore.subtract(adjustDTO.getAmount());
        } else if (typeEnum.isFreeze()) {
            balanceAfter = balanceBefore.subtract(adjustDTO.getAmount());
            frozenAfter = frozenBefore.add(adjustDTO.getAmount());
        } else if (typeEnum == AccountTypeEnum.UNFREEZE) {
            balanceAfter = balanceBefore.add(adjustDTO.getAmount());
            frozenAfter = frozenBefore.subtract(adjustDTO.getAmount());
        } else if (typeEnum == AccountTypeEnum.FROZEN_DEDUCT) {
            frozenAfter = frozenBefore.subtract(adjustDTO.getAmount());
        } else if (typeEnum.isAdd()) {
            balanceAfter = balanceBefore.add(adjustDTO.getAmount());
        }
    }

    /**
     * 插入交易流水记录
     */
    private void insertTransactionRecord(AccountAdjustDTO adjustDTO,
                                         BigDecimal balanceBefore, BigDecimal frozenBefore,
                                         BigDecimal balanceAfter, BigDecimal frozenAfter) {
        Transaction transaction = new Transaction();
        transaction.setUserId(adjustDTO.getUserId());
        transaction.setTransactionNo(IdUtil.getSnowflakeNextIdStr());
        transaction.setType(adjustDTO.getType().getCode());
        transaction.setAmount(adjustDTO.getAmount());
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setFrozenBefore(frozenBefore);
        transaction.setFrozenAfter(frozenAfter);
        transaction.setIdempotencyKey(adjustDTO.getIdempotencyKey());
        transaction.setStatus(2);
        transaction.setRemark(adjustDTO.getRemark());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());
        transaction.setOrderId(adjustDTO.getOrderId());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof LoginUser) {
            transaction.setOperatorId(((LoginUser) principal).getUserId());
        }

        transactionMapper.insert(transaction);
    }

    @Override
    public List<TransactionVO> getTransactionsByUserId(Long userId) {
        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Transaction::getUserId, userId);
        wrapper.orderByDesc(Transaction::getCreatedAt);

        List<Transaction> transactions = transactionMapper.selectList(wrapper);

        return transactions.stream().map(transaction -> {
            TransactionVO vo = new TransactionVO();
            BeanUtils.copyProperties(transaction, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public IPage<TransactionVO> getTransactionsByUserIdPaged(Long userId, Integer current, Integer size) {
        Page<Transaction> page = new Page<>(current, size);
        LambdaQueryWrapper<Transaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Transaction::getUserId, userId);
        wrapper.orderByDesc(Transaction::getCreatedAt);

        Page<Transaction> transactionPage = transactionMapper.selectPage(page, wrapper);

        return transactionPage.convert(transaction -> {
            TransactionVO vo = new TransactionVO();
            BeanUtils.copyProperties(transaction, vo);
            return vo;
        });
    }
}
