package com.jmz.serveruser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_jmz_transactions")
public class Transaction {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long operatorId;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;
    
    private String transactionNo;
    private Integer type; // 交易类型：1-充值，2-提现，3-订单收入，4-订单支出，5金，7-罚款
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private Integer status; // 交易状态：1-处理中，2-成功，3-失败
    private String remark;
    private Date createdAt;
    private Date updatedAt;
} 