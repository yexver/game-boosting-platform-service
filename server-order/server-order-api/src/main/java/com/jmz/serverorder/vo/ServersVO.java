package com.jmz.serverorder.vo;

import lombok.Data;
import java.util.Date;

@Data
public class ServersVO {
    private Integer id;
    private Integer gameId;
    private Integer systemId;
    private String name;
    private Integer sortOrder;
    private Date createdAt;
    private Date updatedAt;
} 