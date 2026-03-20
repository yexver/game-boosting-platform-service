package com.jmz.serverorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_jmz_orders")
public class Orders {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long publisherId;
    private Long takerId;
    private Long managerId;
    private Integer gameId;
    private Integer systemId;
    private Integer serverId;
    private String title;
    private Integer boostingType;
    private String description;
    private String accountInfo;
    private BigDecimal price;
    private BigDecimal platformFee; // 订单彻底完成时平台获取金额
    private BigDecimal actualAmount; // 订单完成时实际结算给接单方的金额
    private BigDecimal securityDeposit;
    private BigDecimal efficiencyDeposit;
    private Integer status; //订单状态：1-未接手，2-代练中，3-待验收，4-验收中，5-已完成，6-已撤销，8-撤销中，9-待介入，10-介入中，12-已仲裁，13-强制撤销
    private Integer timeLimit;
    private Date startAt;
    private Date actualAt;
    private Date createdAt;
    private Date updatedAt;
} 