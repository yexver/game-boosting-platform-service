package com.jmz.serverorder.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Data
public class ServersCreateDTO {
    @NotNull(message = "游戏ID不能为空")
    private Integer gameId;
    @NotNull(message = "系统ID不能为空")
    private Integer systemId;
    @NotBlank(message = "区服名称不能为空")
    private String name;
    private Integer sortOrder = 0;
} 