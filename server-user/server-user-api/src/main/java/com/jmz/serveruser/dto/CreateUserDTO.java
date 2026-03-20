package com.jmz.serveruser.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CreateUserDTO {
    private String username;
    private String phone;
    private String email;
    private String password;
    private String nickname;
    private  Integer gender;
    private  Integer status;
    private List<Integer> roles;
} 