package com.jmz.serveruser.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_jmz_role_permission")
public class RolePermission {
    private Integer roleId;
    private Integer permissionId;
} 