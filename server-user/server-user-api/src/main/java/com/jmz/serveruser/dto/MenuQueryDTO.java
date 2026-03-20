package com.jmz.serveruser.dto;

import lombok.Data;

@Data
public class MenuQueryDTO {
    private String name;
    private String path;
    private Integer type;
    private Integer page = 1;
    private Integer size = 1000;
} 