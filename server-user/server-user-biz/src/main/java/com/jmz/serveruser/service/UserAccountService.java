package com.jmz.serveruser.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jmz.serveruser.dto.AccountAdjustDTO;
import com.jmz.serveruser.dto.AccountQueryDTO;
import com.jmz.serveruser.entity.UserAccount;
import com.jmz.serveruser.vo.AccountVO;
import com.jmz.serveruser.vo.TransactionVO;

import java.util.List;

public interface UserAccountService extends IService<UserAccount> {
    
    /**
     * 根据用户ID创建账户
     * @param userId 用户ID
     * @return 是否创建成功
     */
    boolean createAccountForUser(Long userId);
    
    /**
     * 根据用户ID获取账户信息
     * @param userId 用户ID
     * @return 账户信息
     */
    UserAccount getAccountByUserId(Long userId);
    
    /**
     * 分页查询账户列表
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<AccountVO> getAccountList(AccountQueryDTO queryDTO);
    
    /**
     * 根据账户ID获取账户详情
     * @param accountId 账户ID
     * @return 账户详情
     */
    AccountVO getAccountDetail(Long accountId);
    
    /**
     * 账户余额调整
     * @param adjustDTO 调整信息
     * @return 是否成功
     */
    boolean adjustAccountBalance(AccountAdjustDTO adjustDTO);
    
    /**
     * 根据用户ID查询交易流水
     * @param userId 用户ID
     * @return 交易流水列表
     */
    List<TransactionVO> getTransactionsByUserId(Long userId);

}