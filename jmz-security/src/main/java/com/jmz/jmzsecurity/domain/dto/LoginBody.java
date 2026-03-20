package com.jmz.jmzsecurity.domain.dto;

import lombok.Data;

/**
 * 用户登录对象
 *
 */
@Data
public class LoginBody {
    /**
     * 用户电话
     */
    private String phone;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;

}
