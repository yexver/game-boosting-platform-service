package com.jmz.serverorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("tb_jmz_systems")
public class Systems {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer sortOrder;
    private String icon;
    private Date createdAt;
    private Date updatedAt;
} 