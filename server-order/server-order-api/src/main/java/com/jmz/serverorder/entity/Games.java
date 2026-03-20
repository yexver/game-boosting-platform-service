package com.jmz.serverorder.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 游戏实体类
 * 对应数据库表: tb_jmz_games
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jmz_games")
public class Games {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 游戏名称
     */
    @TableField("name")
    private String name;

    /**
     * 游戏图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 状态：1-启用，0-禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;
}
