package com.jmz.serveruser.feign;

import com.jmz.jmzcommoncore.responseResult.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "server-user", path = "/account",contextId = "userAccountFeignClient")
public interface UserAccountFeignClient {
    @PostMapping("/adjust")
    R adjustAccountBalance(@RequestBody com.jmz.serveruser.dto.AccountAdjustDTO adjustDTO);
    @PostMapping("/unfreeze")
    R unfreezeAccount(@RequestBody com.jmz.serveruser.dto.AccountAdjustDTO adjustDTO);
}