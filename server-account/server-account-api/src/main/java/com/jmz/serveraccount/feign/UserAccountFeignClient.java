package com.jmz.serveraccount.feign;

import com.jmz.jmzcommoncore.responseResult.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.jmz.serveraccount.dto.AccountAdjustDTO;

@FeignClient(name = "server-account", path = "/account", contextId = "userAccountFeignClient")
public interface UserAccountFeignClient {

    @PostMapping("/adjust")
    R adjustAccountBalance(@RequestBody AccountAdjustDTO adjustDTO);

    @PostMapping("/unfreeze")
    R unfreezeAccount(@RequestBody AccountAdjustDTO adjustDTO);

    @GetMapping("/user/{userId}")
    R getAccountByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/{id}")
    R getAccountDetail(@PathVariable("id") Long accountId);

    @PostMapping("/create")
    R createAccountForUser(@RequestParam("userId") Long userId);

    @DeleteMapping("/user/{userId}")
    R deleteAccountByUserId(@PathVariable("userId") Long userId);
}
