package com.jmz.serverorder.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrdersListVO {
    private Long id;
    private String orderNo;
    private Long publisherId;
    private String title;
    private Integer gameId;
    private Integer systemId;
    private Integer serverId;
    private Integer boostingType;
    private BigDecimal price;
    private BigDecimal securityDeposit;
    private BigDecimal efficiencyDeposit;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
    private String publisherUsername;
    private String publisherAvatar;
    private Integer publisherOrderCount30d;
    private Double publisherManagerRate30d;
    private String gameName;
    private String systemName;
    private String serverName;
    private Long takerId;
    private String takerUsername;
    private String takerAvatar;
    private Date startAt;
    private Date actualAt;
    private String remark;
    private Integer paid;
}

