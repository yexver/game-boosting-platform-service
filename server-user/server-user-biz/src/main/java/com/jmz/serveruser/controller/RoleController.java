package com.jmz.serveruser.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveruser.dto.RoleQueryDTO;
import com.jmz.serveruser.entity.Role;
import com.jmz.serveruser.entity.Permission;
import com.jmz.serveruser.service.RoleService;
import com.jmz.serveruser.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping
    public R getRoleList(RoleQueryDTO roleQuery) {
        IPage<Role> page = roleService.getRoleList(roleQuery);
        return R.success(page);
    }

    /**
     * 获取所有角色
     */
    @GetMapping("/all")
    public R getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return R.success(roles);
    }

    /**
     * 创建角色
     */
    @PostMapping
    public R createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    public R updateRole(@PathVariable Integer id, @RequestBody Role role) {
        role.setId(id);
        return roleService.updateRole(role);
    }

    /**
     * 删除角色
     */
    @DeleteMapping
    public R deleteRoles(@RequestBody List<Integer> ids) {
        return roleService.deleteRoles(ids);
    }

    /**
     * 获取角色的权限ID列表（用于前端回显）
     */
    @GetMapping("/{roleId}/permissions")
    public R getRolePermissionIds(@PathVariable Integer roleId) {
        List<Integer> permissionIds = roleService.getRolePermissions(roleId)
            .stream().map(p -> p.getId()).collect(Collectors.toList());
        return R.success(permissionIds);
    }

    /**
     * 分配权限给角色
     */
    @PostMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('permission:manage')")
    public R setRolePermissions(@PathVariable Integer roleId, @RequestBody List<Integer> permissionIds) {
        return roleService.setRolePermissions(roleId, permissionIds);
    }

    /**
     * 获取角色的菜单列表
     */
    @GetMapping("/{roleId}/menus")
    public R getRoleMenus(@PathVariable Integer roleId) {
        List<MenuVo> menus = roleService.getRoleMenus(roleId);
        return R.success(menus);
    }

    /**
     * 设置角色的菜单
     */
    @PostMapping("/{roleId}/menus")
    @PreAuthorize("hasAuthority('permission:manage')")
    public R setRoleMenus(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        return roleService.setRoleMenus(roleId, menuIds);
    }
}
