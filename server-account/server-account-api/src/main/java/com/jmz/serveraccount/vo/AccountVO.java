package com.jmz.serveraccount.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AccountVO {
    private Long id;
    private Long userId;
    private String username;
    private String phone;
    private String nickname;
    private BigDecimal balance;
    private BigDecimal frozenAmount;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private Date createdAt;
    private Date updatedAt;
}
