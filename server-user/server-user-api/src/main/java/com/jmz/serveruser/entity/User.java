package com.jmz.serveruser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.util.Date;

@Data
@TableName("tb_jmz_user")
public class User {
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String avatar;
    private String nickname;
    private Integer gender;
    private Integer status;
    private Date createTime;
    private Date updateTime;
    private Integer isBoostingEnabled;
    private Integer identityVerified; // 实名认证状态
}
