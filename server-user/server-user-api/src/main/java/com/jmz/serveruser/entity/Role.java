package com.jmz.serveruser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_jmz_role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String keyword;
    private String description;
}