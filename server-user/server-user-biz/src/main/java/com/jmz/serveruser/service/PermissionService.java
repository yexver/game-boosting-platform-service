package com.jmz.serveruser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmz.serveruser.entity.Permission;

import java.util.List;

public interface PermissionService extends IService<Permission> {
    
    /**
     * 获取权限树
     */
    List<Permission> getPermissionTree();
}
