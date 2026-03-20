package com.jmz.serveruser.vo;

import lombok.Data;

import java.util.Set;

@Data
public class UserInfoVo {
    private Long userId;
    private String username;
    private String phone;
    private String email;
    private String avatar;
    private String nickname;
    private Integer gender;
    private Integer status;
    private Set<Integer> role;
    private Set<Integer> permission;
    private Integer isBoostingEnabled; // 是否开启代练
    private Integer identityVerified; // 实名认证状态：0-未认证，1-已认证
}
