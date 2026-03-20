package com.jmz.serveruser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmz.serveruser.entity.Permission;
import com.jmz.serveruser.mapper.PermissionMapper;
import com.jmz.serveruser.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    public List<Permission> getPermissionTree() {
        return this.list();
    }
}
