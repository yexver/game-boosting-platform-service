package com.jmz.serveruser.controller;

import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveruser.dto.AccountAdjustDTO;
import com.jmz.serveruser.dto.AccountQueryDTO;
import com.jmz.serveruser.service.UserAccountService;
import com.jmz.serveruser.vo.AccountVO;
import com.jmz.serveruser.vo.TransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class UserAccountController {
    
    @Autowired
    private UserAccountService userAccountService;
    
    /**
     * 分页查询账户列表
     */
    @PostMapping("/list")
    public R getAccountList(@RequestBody AccountQueryDTO queryDTO) {
        return R.success("查询成功", userAccountService.getAccountList(queryDTO));
    }
    
    /**
     * 查询账户详情
     */
    @GetMapping("/{id}")
    public R getAccountDetail(@PathVariable("id") Long accountId) {
        AccountVO accountVO = userAccountService.getAccountDetail(accountId);
        if (accountVO == null) {
            return R.error("账户不存在");
        }
        return R.success("查询成功", accountVO);
    }
    
    /**
     * 账户余额调整
     */
    @PostMapping("/adjust")
    public R adjustAccountBalance(@Validated @RequestBody AccountAdjustDTO adjustDTO) {
        boolean success = userAccountService.adjustAccountBalance(adjustDTO);
        if (success) {
            return R.success("余额调整成功");
        } else {
            return R.error("余额调整失败，请检查余额是否充足");
        }
    }
    
    /**
     * 解冻/扣除冻结金额
     */
    @PostMapping("/unfreeze")
    public R unfreezeAccount(@Validated @RequestBody AccountAdjustDTO adjustDTO) {
        // type=8 表示解冻/扣除冻结金额
        adjustDTO.setType(8);
        boolean success = userAccountService.adjustAccountBalance(adjustDTO);
        if (success) {
            return R.success("解冻/扣除冻结金额成功");
        } else {
            return R.error("解冻/扣除冻结金额失败，请检查冻结金额是否充足");
        }
    }
    
    /**
     * 查询用户交易流水
     */
    @GetMapping("/{userId}/transactions")
    public R getTransactionsByUserId(@PathVariable("userId") Long userId) {
        List<TransactionVO> transactions = userAccountService.getTransactionsByUserId(userId);
        return R.success("查询成功", transactions);
    }


} 