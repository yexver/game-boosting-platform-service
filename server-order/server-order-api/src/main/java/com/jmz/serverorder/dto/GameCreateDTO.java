package com.jmz.serverorder.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * 游戏创建DTO
 */
@Data
public class GameCreateDTO{

    /**
     * 游戏名称（必填）
     */
    @NotBlank(message = "游戏名称不能为空")
    private String name;

    /**
     * 游戏状态 (0: 禁用, 1: 启用)
     */
    private Integer status = 1;

    /**
     * 排序权重
     */
    private Integer sortOrder = 0;

    /**
     * 关联系统ID列表
     */
    private List<Integer> systemIds;
} 