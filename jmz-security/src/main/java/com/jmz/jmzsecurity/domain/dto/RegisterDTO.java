package com.jmz.jmzsecurity.domain.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    //用户名
    private String username;
    //手机号
    private String phone;
    //邮箱
    private String email;
    //密码
    private String password;
    //手机号验证码
    private String code;
}
