package com.jmz.serveruser.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionVO {
    private Long id;
    private Long userId;
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
    private Long operatorId;
} 