package com.jmz.serveruser.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AccountAdjustDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    private Long operatorId;
    
    @NotNull(message = "调整金额不能为空")
    private BigDecimal amount;
    
    @NotNull(message = "调整类型不能为空")
    private Integer type; // 1充值，2提现，6解冻，7冻结，8解冻/扣除冻结,9为扣款
    
    private String remark; // 备注

    private Long orderId; // 订单ID
} 
