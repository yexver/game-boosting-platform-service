package com.jmz.serverorder.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 游戏VO
 */
@Data
public class GameVO {

    /**
     * 游戏ID
     */
    private Integer id;

    /**
     * 游戏名称
     */
    private String name;

    /**
     * 游戏图标
     */
    private String icon;

    /**
     * 游戏状态：1-启用，0-禁用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    private List<Integer> systemIds;
} 