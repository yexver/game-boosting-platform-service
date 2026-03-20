package com.jmz.serverorder.dto;

import lombok.Data;

@Data
public class SystemQueryDTO {
    private String name;
    private Integer page = 1;
    private Integer pageSize = 10;
} 