package com.jmz.serverorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 游戏-系统关联实体
 * 对应表：tb_jmz_game_systems
 */
@Data
@TableName("tb_jmz_game_systems")
public class GameSystems {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("game_id")
    private Integer gameId;

    @TableField("system_id")
    private Integer systemId;
}
