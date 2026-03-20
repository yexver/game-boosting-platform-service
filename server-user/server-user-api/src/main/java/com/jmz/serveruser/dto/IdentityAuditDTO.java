package com.jmz.serveruser.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class IdentityAuditDTO {
    @NotNull(message = "认证ID不能为空")
    private Long id;
    
    @NotNull(message = "审核状态不能为空")
    private Integer status; // 1-通过，2-拒绝
    
    private String rejectReason; // 拒绝原因
}