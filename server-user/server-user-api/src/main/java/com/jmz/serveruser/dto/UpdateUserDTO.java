package com.jmz.serveruser.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UpdateUserDTO {
    private Long userId;
    private String username;
    private String phone;
    private String email;
    private String nickname;
    private String avatar;
    private Integer gender;
    private Integer status;
    private String password;
    private Set<Integer> roles;
} 