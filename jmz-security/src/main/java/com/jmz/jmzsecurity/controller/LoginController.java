package com.jmz.jmzsecurity.controller;

import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.jmzsecurity.domain.dto.LoginBody;
import com.jmz.jmzsecurity.domain.dto.RegisterDTO;
import com.jmz.jmzsecurity.service.LoginService;
import com.jmz.jmzsecurity.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final RegisterService registerService;

    @PostMapping("/login")
    @ResponseBody
    public R login(@RequestBody LoginBody loginBody) {
        System.out.println(loginBody);
        String token = loginService.login(loginBody.getPhone(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
        return R.success("登录成功", token);
    }

    @PostMapping("/register")
    @ResponseBody
    public R register(@RequestBody RegisterDTO registerDTO) {
        return R.success("注册成功",registerService.register(registerDTO));
    }

}
