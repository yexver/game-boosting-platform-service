package com.jmz.serverorder.dto;

import lombok.Data;

/**
 * 游戏查询DTO
 */
@Data
public class GameQueryDTO {

    /**
     * 页码，默认1
     */
    private Integer pageNum = 1;

    /**
     * 每页数量，默认10
     */
    private Integer pageSize = 10;

    /**
     * 游戏名称（模糊查询）
     */
    private String name;

    /**
     * 游戏状态 (0: 禁用, 1: 启用)
     */
    private Integer status;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
} 