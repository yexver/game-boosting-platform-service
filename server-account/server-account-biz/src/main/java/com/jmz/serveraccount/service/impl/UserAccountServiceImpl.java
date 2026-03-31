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
    public boolean adjustAccountBalance(AccountAdjustDTO adjustDTO) {
        UserAccount account = getAccountByUserId(adjustDTO.getUserId());
        if (account == null) {
            return false;
        }

        BigDecimal balanceBefore = account.getBalance();
        BigDecimal frozenBefore = account.getFrozenAmount();
        BigDecimal amount = adjustDTO.getAmount();
        Long currentVersion = account.getVersion();

        AccountTypeEnum typeEnum = adjustDTO.getType();
        if (typeEnum == null) {
            throw new RuntimeException("调整类型不能为空");
        }

        // 记录操作后的余额快照（各操作类型不同）
        BigDecimal balanceAfter = balanceBefore;
        BigDecimal frozenAfter = frozenBefore;

        // 根据操作类型分发到对应的原子方法
        if (typeEnum.isDeduct()) {
            // 扣减类：余额+冻结 >= 扣减金额，防止超扣
            int rows = this.baseMapper.deductBalanceWithLock(
                    adjustDTO.getUserId(), amount, currentVersion, adjustDTO.getIdempotencyKey());
            if (rows == 0) {
                throw new RuntimeException("余额不足或并发冲突，请稍后重试");
            }
            balanceAfter = balanceBefore.subtract(amount);

        } else if (typeEnum.isFreeze()) {
            // 冻结类：余额足够才冻结
            int rows = this.baseMapper.freezeBalanceWithLock(
                    adjustDTO.getUserId(), amount, currentVersion);
            if (rows == 0) {
                throw new RuntimeException("余额不足，无法冻结资金");
            }
            balanceAfter = balanceBefore.subtract(amount);
            frozenAfter = frozenBefore.add(amount);

        } else if (typeEnum == AccountTypeEnum.UNFREEZE) {
            // 解冻类：冻结足够才解冻，同时校验余额不超限（双重校验）
            if (balanceBefore.compareTo(amount) < 0) {
                throw new RuntimeException("余额不足，无法解冻");
            }
            int rows = this.baseMapper.unfreezeBalanceWithLock(
                    adjustDTO.getUserId(), amount, currentVersion);
            if (rows == 0) {
                throw new RuntimeException("冻结金额不足，无法解冻");
            }
            balanceAfter = balanceBefore.add(amount);
            frozenAfter = frozenBefore.subtract(amount);

        } else if (typeEnum == AccountTypeEnum.FROZEN_DEDUCT) {
            // 冻结扣除
            int rows = this.baseMapper.deductFrozenWithLock(
                    adjustDTO.getUserId(), amount, currentVersion);
            if (rows == 0) {
                throw new RuntimeException("冻结金额不足，无法扣除");
            }
            frozenAfter = frozenBefore.subtract(amount);

        } else if (typeEnum.isAdd()) {
            // 加款类：直接更新（加款无超扣风险）
            account.setBalance(balanceBefore.add(amount));
            account.setTotalIncome(account.getTotalIncome().add(amount));
            account.setUpdatedAt(new Date());
            if (!this.updateById(account)) {
                throw new RuntimeException("并发冲突，请稍后重试");
            }
            balanceAfter = balanceBefore.add(amount);

        } else {
            throw new RuntimeException("不支持的账户操作类型: " + typeEnum);
        }

        // 插入交易流水记录
        Transaction transaction = new Transaction();
        transaction.setUserId(adjustDTO.getUserId());
        transaction.setTransactionNo(IdUtil.getSnowflakeNextIdStr());
        transaction.setType(typeEnum.getCode());
        transaction.setAmount(amount);
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

        return true;
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
}
