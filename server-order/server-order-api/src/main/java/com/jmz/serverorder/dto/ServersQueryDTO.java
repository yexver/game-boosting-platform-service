package com.jmz.serverorder.dto;

import lombok.Data;

@Data
public class ServersQueryDTO {
    private String name;
    private Integer gameId;
    private Integer systemId;
    private Integer page = 1;
    private Integer pageSize = 10;
} 