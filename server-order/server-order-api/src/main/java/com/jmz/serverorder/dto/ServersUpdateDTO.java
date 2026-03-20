package com.jmz.serverorder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServersUpdateDTO {
    @NotNull(message = "区服ID不能为空")
    private Integer id;
    @NotNull(message = "游戏ID不能为空")
    private Integer gameId;
    @NotNull(message = "系统ID不能为空")
    private Integer systemId;
    @NotBlank(message = "区服名称不能为空")
    private String name;
    private Integer sortOrder = 0;
} 