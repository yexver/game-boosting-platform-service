package com.jmz.serveruser.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
@TableName("tb_jmz_user_role")
public class UserRole {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private Integer roleId;
} 