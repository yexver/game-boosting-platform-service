package com.jmz.serveruser.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.jmzcommoncore.utils.StringUtils;
import com.jmz.serveruser.entity.Permission;
import com.jmz.serveruser.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取所有权限的平铺列表（id、name）
     */
    @GetMapping("/all")
    public R listAllPermissions() {
        List<Permission> permissions = permissionService.list();
        List<Map<String, Object>> result = permissions.stream()
            .map(p -> {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("id", p.getId());
                map.put("name", p.getName());
                map.put("description", p.getDescription());
                map.put("keyword", p.getKeyword());
                return map;
            })
            .collect(Collectors.toList());
        return R.success(result);
    }

    /**
     * 模糊分页查询获取权限列表
     * @param current 当前页
     * @param size 分页大小
     * @param name 搜索权限名
     * @param keyword 权限keyword
     * @return R
     */
    @GetMapping
    public R getPermissionList(
            @RequestParam int current,
            @RequestParam int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String keyword
    ) {
        Page<Permission> page = new Page<>(current, size);
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Permission::getName, name);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Permission::getKeyword, keyword);
        }
        permissionService.page(page, wrapper);
        return R.success(page);
    }

    /**
     * 根据ID获取权限详情
     */
    @GetMapping("/{id}")
    public R getPermissionById(@PathVariable Integer id) {
        Permission permission = permissionService.getById(id);
        return permission != null ? R.success(permission) : R.error("权限不存在");
    }

    /**
     * 创建权限
     */
    @PostMapping
    public R createPermission(@RequestBody Permission permission) {
        boolean saved = permissionService.save(permission);
        return saved ? R.success("创建成功") : R.error("创建失败");
    }

    /**
     * 更新权限
     */
    @PutMapping("/{id}")
    public R updatePermission(@PathVariable Integer id, @RequestBody Permission permission) {
        permission.setId(id);
        boolean updated = permissionService.updateById(permission);
        return updated ? R.success("更新成功") : R.error("更新失败");
    }

    /**
     * 批量删除权限
     */
    @DeleteMapping
    public R deletePermissionByIds(@RequestBody List<Integer> ids) {
        boolean removed = permissionService.removeByIds(ids);
        return removed ? R.success("删除成功") : R.error("删除失败");
    }
}