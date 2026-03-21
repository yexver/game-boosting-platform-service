package com.jmz.serveraccount.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionVO {

    private Long id;

    private Long userId;

    private Long orderId;

    private String transactionNo;

    private Integer type;

    private BigDecimal amount;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    private Integer status;

    private String remark;

    private Date createdAt;

    private Date updatedAt;

    private Long operatorId;
}
