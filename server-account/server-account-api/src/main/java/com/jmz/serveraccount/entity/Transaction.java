package com.jmz.serveraccount.entity;

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

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer type;

    private BigDecimal amount;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    private BigDecimal frozenBefore;

    private BigDecimal frozenAfter;

    private String idempotencyKey;

    private Integer status;

    private String remark;

    private Date createdAt;

    private Date updatedAt;
}
