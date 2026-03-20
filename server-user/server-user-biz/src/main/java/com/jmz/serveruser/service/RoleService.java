package com.jmz.serveruser.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveruser.dto.RoleQueryDTO;
import com.jmz.serveruser.entity.Role;
import com.jmz.serveruser.entity.Permission;
import com.jmz.serveruser.vo.MenuVo;

import java.util.List;

public interface RoleService extends IService<Role> {
    
    /**
     * 分页查询角色列表
     */
    IPage<Role> getRoleList(RoleQueryDTO roleQuery);
    
    /**
     * 获取所有角色
     */
    List<Role> getAllRoles();
    
    /**
     * 创建角色
     */
    R createRole(Role role);
    
    /**
     * 更新角色
     */
    R updateRole(Role role);
    
    /**
     * 删除角色
     */
    R deleteRoles(List<Integer> ids);
    
    /**
     * 获取角色的权限列表
     */
    List<Permission> getRolePermissions(Integer roleId);
    
    /**
     * 设置角色的权限
     */
    R setRolePermissions(Integer roleId, List<Integer> permissionIds);
    
    /**
     * 获取角色的菜单列表
     */
    List<MenuVo> getRoleMenus(Integer roleId);
    
    /**
     * 设置角色的菜单
     */
    R setRoleMenus(Integer roleId, List<Integer> menuIds);
}
