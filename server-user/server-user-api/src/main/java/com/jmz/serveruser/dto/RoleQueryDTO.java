package com.jmz.serveruser.dto;

import lombok.Data;

@Data
public class RoleQueryDTO {
    private String current;
    private String size;
    private Integer roleId;
    private String name;
    private String keyword;
} 