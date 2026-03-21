package com.jmz.serveraccount.dto;

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
    private Integer type;

    private String remark;

    private Long orderId;
}
