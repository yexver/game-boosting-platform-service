package com.jmz.serverorder.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 接单详情页VO
 */
@Data
public class TakeOrderDetailVO {
    // 基础订单信息
    private Long id;
    private String orderNo;
    private String title;
    private Integer status;
    private Date createdAt;
    private Date startAt;
    private Date actualAt;
    
    // 游戏信息
    private Integer gameId;
    private String gameName;
    private String gameIcon;
    private Integer systemId;
    private String systemName;
    private String systemIcon;
    private Integer serverId;
    private String serverName;
    
    // 代练信息
    private Integer boostingType;
    private String description;
    private String accountInfo;
    private Integer timeLimit;
    
    // 金额信息
    private BigDecimal price;
    private BigDecimal securityDeposit;
    private BigDecimal efficiencyDeposit;
    
    // 发布者信息
    private Long publisherId;
    private String publisherUsername;
    private String publisherAvatar;
    private Integer publisherOrderCount30d;
    private BigDecimal publisherManagerRate30d;
} 