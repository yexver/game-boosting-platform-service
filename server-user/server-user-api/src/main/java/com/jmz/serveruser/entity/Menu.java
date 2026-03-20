package com.jmz.serveruser.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_jmz_menu")
public class Menu {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String path;
    private String component;
    private Integer parentId;
    private Integer type;
    private String icon;
    private String redirect;
    private Integer orderNum;
    private String permission;

    // 子菜单列表 - 不存在于数据库表中
    @TableField(exist = false)
    private List<Menu> children;
}