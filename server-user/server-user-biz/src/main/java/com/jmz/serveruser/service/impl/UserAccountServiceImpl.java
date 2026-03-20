package com.jmz.serveruser.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmz.jmzcommoncore.utils.StringUtils;
import com.jmz.serveruser.dto.AccountAdjustDTO;
import com.jmz.serveruser.dto.AccountQueryDTO;
import com.jmz.serveruser.entity.Transaction;
import com.jmz.serveruser.entity.User;
import com.jmz.serveruser.entity.UserAccount;
import com.jmz.serveruser.mapper.TransactionMapper;
import com.jmz.serveruser.mapper.UserAccountMapper;
import com.jmz.serveruser.mapper.UserMapper;
import com.jmz.serveruser.service.UserAccountService;
import com.jmz.serveruser.vo.AccountVO;
import com.jmz.serveruser.vo.TransactionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import io.seata.spring.annotation.GlobalTransactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private TransactionMapper transactionMapper;
    
    @Override
    public boolean createAccountForUser(Long userId) {        // 检查是否已存在账户
        LambdaQueryWrapper<UserAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAccount::getUserId, userId);
        if (this.count(wrapper) > 0) {
            return true; // 账户已存在，返回成功
        }
        
        // 创建新账户
        UserAccount account = new UserAccount();
        account.setUserId(userId);
        account.setBalance(BigDecimal.ZERO);
        account.setFrozenAmount(BigDecimal.ZERO);
        account.setTotalIncome(BigDecimal.ZERO);
        account.setTotalExpense(BigDecimal.ZERO);
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
        
        // 使用自定义Mapper方法进行关联查询
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
        
        // 查询用户信息
        User user = userMapper.selectById(account.getUserId());
        if (user != null) {
            vo.setUsername(user.getUsername());
            vo.setPhone(user.getPhone());
            vo.setNickname(user.getNickname());
        }
        
        return vo;
    }
    
    @Override
    @Transactional  // 只需要本地事务
    public boolean adjustAccountBalance(AccountAdjustDTO adjustDTO) {
        // 查询账户
        UserAccount account = getAccountByUserId(adjustDTO.getUserId());
        if (account == null) {
            return false;
        }

        BigDecimal balanceBefore = account.getBalance();
        BigDecimal frozenBefore = account.getFrozenAmount();
        BigDecimal amount = adjustDTO.getAmount();

        // 根据类型调整余额和冻结金额
        if (adjustDTO.getType() == 1) { // 充值
            account.setBalance(balanceBefore.add(amount));
            account.setTotalIncome(account.getTotalIncome().add(amount));
        } else if (adjustDTO.getType() == 2) { // 提现
            if (balanceBefore.compareTo(amount) < 0) {
                throw new RuntimeException("余额不足，无法提现");
            }
            account.setBalance(balanceBefore.subtract(amount));
            account.setTotalExpense(account.getTotalExpense().add(amount));
        } else if (adjustDTO.getType() == 6) { // 解冻资金（退回余额）
            if (frozenBefore.compareTo(amount) < 0) {
                throw new RuntimeException("冻结金额不足，无法解冻");
            }
            account.setFrozenAmount(frozenBefore.subtract(amount));
            account.setBalance(balanceBefore.add(amount)); // 解冻后退回余额
        } else if (adjustDTO.getType() == 7) { // 冻结资金
            if (balanceBefore.compareTo(amount) < 0) {
                throw new RuntimeException("余额不足，无法冻结资金");
            }
            account.setBalance(balanceBefore.subtract(amount));
            account.setFrozenAmount(frozenBefore.add(amount));
        } else if (adjustDTO.getType() == 8) { // 解冻/扣除冻结金额
            if (frozenBefore.compareTo(amount) < 0) {
                throw new RuntimeException("冻结金额不足，无法解冻/扣除");
            }
            account.setFrozenAmount(frozenBefore.subtract(amount));
            account.setTotalExpense(account.getTotalExpense().add(amount)); // 添加总支出计算
            // 解冻并扣除：不退回余额，直接从冻结金额中扣除
        } else if (adjustDTO.getType() == 9) { // 扣款 
            if (balanceBefore.compareTo(amount) < 0) {
                throw new RuntimeException("余额不足，无法扣款");
            }
            account.setBalance(balanceBefore.subtract(amount));
            account.setTotalExpense(account.getTotalExpense().add(amount));
        }

        account.setUpdatedAt(new Date());
        boolean success = this.updateById(account);

        if (success) {
            // 记录交易流水
            Transaction transaction = new Transaction();
            transaction.setUserId(adjustDTO.getUserId());
            transaction.setTransactionNo(IdUtil.getSnowflakeNextIdStr());
            transaction.setType(adjustDTO.getType());
            transaction.setAmount(amount);
            transaction.setBalanceBefore(balanceBefore);
            transaction.setBalanceAfter(account.getBalance());
            transaction.setStatus(2); // 成功
            transaction.setRemark(adjustDTO.getRemark());
            transaction.setCreatedAt(new Date());
            transaction.setUpdatedAt(new Date());
            transaction.setOrderId(adjustDTO.getOrderId()); // 新增：记录订单ID
            // 获取当前操作者id
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof LoginUser) {
                transaction.setOperatorId(((LoginUser) principal).getUserId());
            }
            transactionMapper.insert(transaction);
        }

        return success;
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
