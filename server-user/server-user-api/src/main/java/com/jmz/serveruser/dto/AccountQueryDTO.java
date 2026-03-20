package com.jmz.serveruser.dto;

import lombok.Data;

@Data
public class AccountQueryDTO {
    private Integer current = 1;
    private Integer size = 10;
    private Long userId;
    private String username;
    private String phone;
    private String nickname;
} 