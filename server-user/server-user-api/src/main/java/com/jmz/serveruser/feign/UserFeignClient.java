package com.jmz.serveruser.feign;

import com.jmz.serveruser.dto.CreateUserDTO;
import com.jmz.jmzcommoncore.responseResult.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "server-user", path = "/user", contextId = "userFeignClient")
public interface UserFeignClient {
    @PostMapping("/create")
    @ResponseBody
    Object createUser(@RequestBody CreateUserDTO createUserDTO);

    /**
     * 获取用户信息
     * @param userId 用户Id
     * @return
     */
    @PostMapping("/getUserInfoById/{id}")
    R getUserById(@PathVariable("id") Long userId);
} 