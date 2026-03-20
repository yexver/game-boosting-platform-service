package com.jmz.serverorder.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SubmitOrderDTO {
    private Integer gameId;
    private Integer systemId;
    private Integer serverId; // 可为null
    //用户密码
    private String password;
    private Integer boostingType;
    private String title;
    private String description;
    private String accountInfo;
    private BigDecimal price;
    private BigDecimal securityDeposit; // 可为null
    private BigDecimal efficiencyDeposit; // 可为null
    private Integer timeLimit;
} 