package com.jmz.serveraccount.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jmz.serveraccount.dto.AccountAdjustDTO;
import com.jmz.serveraccount.dto.AccountQueryDTO;
import com.jmz.serveraccount.entity.UserAccount;
import com.jmz.serveraccount.vo.AccountVO;
import com.jmz.serveraccount.vo.TransactionVO;

import java.util.List;

public interface UserAccountService extends IService<UserAccount> {

    /**
     * 根据用户ID创建账户
     */
    boolean createAccountForUser(Long userId);

    /**
     * 根据用户ID获取账户信息
     */
    UserAccount getAccountByUserId(Long userId);

    /**
     * 分页查询账户列表
     */
    IPage<AccountVO> getAccountList(AccountQueryDTO queryDTO);

    /**
     * 根据账户ID获取账户详情
     */
    AccountVO getAccountDetail(Long accountId);

    /**
     * 账户余额调整
     */
    boolean adjustAccountBalance(AccountAdjustDTO adjustDTO);

    /**
     * 根据用户ID查询交易流水
     */
    List<TransactionVO> getTransactionsByUserId(Long userId);

    /**
     * 根据用户ID分页查询交易流水
     */
    IPage<TransactionVO> getTransactionsByUserIdPaged(Long userId, Integer current, Integer size);
}
