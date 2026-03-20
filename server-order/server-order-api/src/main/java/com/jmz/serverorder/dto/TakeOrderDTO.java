package com.jmz.serverorder.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * 接单DTO
 */
@Data
public class TakeOrderDTO {
    
    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;
} 