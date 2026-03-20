package com.jmz.serverorder.dto;

import lombok.Data;

/**
 * 游戏更新DTO
 */
@Data
public class GameUpdateDTO {

    /**
     * 游戏名称
     */
    private String name;

    /**
     * 游戏状态 (0: 禁用, 1: 启用)
     */
    private Integer status;

    /**
     * 排序权重
     */
    private Integer sortOrder;

    /**
     * 关联系统ID列表
     */
    private java.util.List<Integer> systemIds;
} 