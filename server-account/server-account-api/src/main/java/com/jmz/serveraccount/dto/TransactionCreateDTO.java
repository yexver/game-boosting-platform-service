package com.jmz.serveraccount.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionCreateDTO {

    private Long userId;

    private Long operatorId;

    private Long orderId;

    private Integer type;

    private BigDecimal amount;

    private String remark;
}
