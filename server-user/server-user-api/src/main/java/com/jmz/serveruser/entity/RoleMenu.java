package com.jmz.serveruser.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_jmz_role_menu")
public class RoleMenu {
    private Integer roleId;
    private Integer menuId;
} 