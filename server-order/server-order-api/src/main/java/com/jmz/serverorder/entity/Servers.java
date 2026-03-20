package com.jmz.serverorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_jmz_game_servers")
public class Servers implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer gameId;
    private Integer systemId;
    private String name;
    private Integer sortOrder;
    private Date createdAt;
    private Date updatedAt;
}
