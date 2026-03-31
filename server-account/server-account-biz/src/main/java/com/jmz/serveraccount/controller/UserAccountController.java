package com.jmz.serveraccount.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveraccount.dto.AccountAdjustDTO;
import com.jmz.serveraccount.dto.AccountQueryDTO;
import com.jmz.serveraccount.entity.UserAccount;
import com.jmz.serveraccount.enums.AccountTypeEnum;
import com.jmz.serveraccount.service.UserAccountService;
import com.jmz.serveraccount.vo.AccountVO;
import com.jmz.serveraccount.vo.TransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @PostMapping("/list")
    public R getAccountList(@RequestBody AccountQueryDTO queryDTO) {
        return R.success("查询成功", userAccountService.getAccountList(queryDTO));
    }

    @GetMapping("/user/{userId}")
    public R getAccountByUserId(@PathVariable("userId") Long userId) {
        UserAccount account = userAccountService.getAccountByUserId(userId);
        if (account == null) {
            return R.error("账户不存在");
        }
        return R.success("查询成功", account);
    }

    @GetMapping("/{id}")
    public R getAccountDetail(@PathVariable("id") Long accountId) {
        AccountVO accountVO = userAccountService.getAccountDetail(accountId);
        if (accountVO == null) {
            return R.error("账户不存在");
        }
        return R.success("查询成功", accountVO);
    }

    @PostMapping("/adjust")
    public R adjustAccountBalance(@Validated @RequestBody AccountAdjustDTO adjustDTO) {
        boolean success = userAccountService.adjustAccountBalance(adjustDTO);
        if (success) {
            return R.success("余额调整成功");
        } else {
            return R.error("余额调整失败，请检查余额是否充足");
        }
    }

    @PostMapping("/unfreeze")
    public R unfreezeAccount(@Validated @RequestBody AccountAdjustDTO adjustDTO) {
        adjustDTO.setType(AccountTypeEnum.FROZEN_DEDUCT);
        boolean success = userAccountService.adjustAccountBalance(adjustDTO);
        if (success) {
            return R.success("解冻/扣除冻结金额成功");
        } else {
            return R.error("解冻/扣除冻结金额失败，请检查冻结金额是否充足");
        }
    }

    @PostMapping("/create")
    public R createAccountForUser(@RequestParam Long userId) {
        boolean success = userAccountService.createAccountForUser(userId);
        return success ? R.success("账户创建成功") : R.success("账户已存在");
    }

    @DeleteMapping("/user/{userId}")
    public R deleteAccountByUserId(@PathVariable("userId") Long userId) {
        userAccountService.remove(new LambdaQueryWrapper<UserAccount>().eq(UserAccount::getUserId, userId));
        return R.success("账户删除成功");
    }

    @GetMapping("/{userId}/transactions")
    public R getTransactionsByUserId(@PathVariable("userId") Long userId) {
        List<TransactionVO> transactions = userAccountService.getTransactionsByUserId(userId);
        return R.success("查询成功", transactions);
    }
}
