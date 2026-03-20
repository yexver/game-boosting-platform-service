package com.jmz.serveruser.vo;

import lombok.Data;

import java.util.Set;

@Data
public class LoginUserInfoVo {
    private Long userId;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private String nickname;
    private Integer gender;
    private Integer status;
    private Set<String> role;
    private Set<String> permission;
    private Integer identityVerified; // 实名认证状态
}
