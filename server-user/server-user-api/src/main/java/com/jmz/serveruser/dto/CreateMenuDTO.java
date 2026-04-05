package com.jmz.serveruser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateMenuDTO {
    private String name;
    private String path;
    private String component;
    @JsonProperty("parent_id")
    private Integer parentId;
    private Integer type;
    private String icon;
    @JsonProperty("order_num")
    private Integer orderNum;
    private String permission;
    private Integer hidden;
} 