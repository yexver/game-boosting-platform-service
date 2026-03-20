package com.jmz.serveruser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmz.jmzcommoncore.utils.StringUtils;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveruser.dto.RoleQueryDTO;
import com.jmz.serveruser.entity.Role;
import com.jmz.serveruser.entity.Permission;
import com.jmz.serveruser.entity.RolePermission;
import com.jmz.serveruser.entity.RoleMenu;
import com.jmz.serveruser.entity.Menu;
import com.jmz.serveruser.mapper.RoleMapper;
import com.jmz.serveruser.mapper.PermissionMapper;
import com.jmz.serveruser.mapper.RolePermissionMapper;
import com.jmz.serveruser.mapper.RoleMenuMapper;
import com.jmz.serveruser.mapper.MenuMapper;
import com.jmz.serveruser.service.RoleService;
import com.jmz.serveruser.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.seata.spring.annotation.GlobalTransactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private PermissionMapper permissionMapper;
    
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public IPage<Role> getRoleList(RoleQueryDTO roleQuery) {
        // 构建分页对象
        Page<Role> page = new Page<>(Integer.parseInt(roleQuery.getCurrent()), Integer.parseInt(roleQuery.getSize()));

        // 构建查询条件
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (roleQuery.getRoleId() != null) {
            wrapper.eq(Role::getId, roleQuery.getRoleId());
        }
        if (StringUtils.isNotBlank(roleQuery.getName())) {
            wrapper.like(Role::getName, roleQuery.getName());
        }
        if (StringUtils.isNotBlank(roleQuery.getKeyword())) {
            wrapper.like(Role::getKeyword, roleQuery.getKeyword());
        }

        // 执行查询
        return this.baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Role> getAllRoles() {
        return this.list();
    }

    @Override
    @Transactional
    public R createRole(Role role) {
        // 检查角色标识是否已存在
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getKeyword, role.getKeyword());
        if (this.count(wrapper) > 0) {
            return R.error("角色标识已存在");
        }
        
        this.save(role);
        return R.success("角色创建成功");
    }

    @Override
    @Transactional
    public R updateRole(Role role) {
        Role existingRole = this.getById(role.getId());
        if (existingRole == null) {
            return R.error("角色不存在");
        }
        
        // 检查角色标识是否已被其他角色使用
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getKeyword, role.getKeyword())
               .ne(Role::getId, role.getId());
        if (this.count(wrapper) > 0) {
            return R.error("角色标识已存在");
        }
        
        this.updateById(role);
        return R.success("角色更新成功");
    }

    @Override
    @Transactional
    public R deleteRoles(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return R.error("角色ID不能为空");
        }
        
        // 删除角色权限关联
        rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermission>().in(RolePermission::getRoleId, ids));
        
        // 删除角色菜单关联
        roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRoleId, ids));
        
        // 删除角色
        this.removeByIds(ids);
        
        return R.success("角色删除成功");
    }

    @Override
    public List<Permission> getRolePermissions(Integer roleId) {
        return permissionMapper.getPermissionsByRoleId(roleId);
    }

    @Override
    @Transactional
    @GlobalTransactional(rollbackFor = Exception.class)
    public R setRolePermissions(Integer roleId, List<Integer> permissionIds) {
        // 先删除该角色的所有权限
        rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId));
        
        // 添加新的权限关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<RolePermission> rolePermissions = permissionIds.stream()
                .map(permissionId -> {
                    RolePermission rp = new RolePermission();
                    rp.setRoleId(roleId);
                    rp.setPermissionId(permissionId);
                    return rp;
                }).collect(Collectors.toList());
            
            rolePermissionMapper.insertBatchSomeColumn(rolePermissions);
        }
        
        return R.success("角色权限设置成功");
    }

    @Override
    public List<MenuVo> getRoleMenus(Integer roleId) {
        List<Menu> menus = menuMapper.getMenusByRoleId(roleId);
        return buildMenuTree(menus);
    }

    @Override
    @Transactional
    public R setRoleMenus(Integer roleId, List<Integer> menuIds) {
        // 先删除该角色的所有菜单
        roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
        
        // 添加新的菜单关联
        if (menuIds != null && !menuIds.isEmpty()) {
            List<RoleMenu> roleMenus = menuIds.stream()
                .map(menuId -> {
                    RoleMenu rm = new RoleMenu();
                    rm.setRoleId(roleId);
                    rm.setMenuId(menuId);
                    return rm;
                }).collect(Collectors.toList());
            
            roleMenuMapper.insertBatchSomeColumn(roleMenus);
        }
        
        return R.success("角色菜单设置成功");
    }

    /**
     * 构建菜单树
     */
    private List<MenuVo> buildMenuTree(List<Menu> menus) {
        // 构建菜单树逻辑
        List<MenuVo> menuVos = menus.stream()
            .map(menu -> {
                MenuVo vo = new MenuVo();
                vo.setId(menu.getId());
                vo.setName(menu.getName());
                vo.setParentId(menu.getParentId());
                return vo;
            }).collect(Collectors.toList());
        
        // 这里可以添加树形结构构建逻辑
        return menuVos;
    }
}
