package com.jmz.serverorder.vo;

import lombok.Data;
import java.util.Date;

@Data
public class SystemVO {
    private Integer id;
    private String name;
    private Integer sortOrder;
    private String icon;
    private Date createdAt;
    private Date updatedAt;
} 