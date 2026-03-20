package com.jmz.serverorder.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderSimpleDetailVO {
    // 订单基本信息
    private Long id;
    private String orderNo;
    private String title;
    private Integer gameId;
    private Integer systemId;
    private Integer serverId;
    private Integer boostingType;
    private String description;
    private Integer timeLimit;
    private BigDecimal price;
    private BigDecimal securityDeposit;
    private BigDecimal efficiencyDeposit;
    private Integer status;
    private Date startAt; // 开始时间
    private Date actualAt; // 完成时间
    private Date createdAt;
    private String accountInfo; // 游戏账号信息
    private String gameIcon; // 游戏图标地址
    private String systemIcon; // 系统图标地址

    // 游戏、系统、服务器名称
    private String gameName;
    private String systemName;
    private String serverName;

    // 发单人信息
    private SimpleUserVO publisher;
    // 接单人信息
    private SimpleUserVO taker;
    
    // 订单状态日志
    private List<OrderStatusLogVO> statusLogs;

    @Data
    public static class SimpleUserVO {
        private Long userId;
        private String username;
        private String avatar;
    }
} 