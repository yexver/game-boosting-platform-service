package com.jmz.serveruser.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import com.jmz.serveruser.dto.CreateMenuDTO;
import com.jmz.serveruser.dto.UpdateMenuDTO;
import com.jmz.serveruser.entity.Menu;
import com.jmz.serveruser.service.MenuService;
import com.jmz.serveruser.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.jmz.serveruser.dto.MenuQueryDTO;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取菜单列表（支持分页和条件查询）
     */
    @GetMapping
    public R getMenuList(MenuQueryDTO menuQueryDTO) {
        return menuService.getMenuList(menuQueryDTO);
    }

    /**
     * 根据ID获取菜单详情
     */
    @GetMapping("/{id}")
    public R getMenuById(@PathVariable Integer id) {
        Menu menu = menuService.getById(id);
        return menu != null ? R.success(menu) : R.error("菜单不存在");
    }

    /**
     * 创建菜单
     */
    @PostMapping
    public R createMenu(@RequestBody CreateMenuDTO createMenuDTO) {
        return menuService.createMenu(createMenuDTO);
    }

    /**
     * 更新菜单
     */
    @PutMapping("/{id}")
    public R updateMenu(@PathVariable Integer id, @RequestBody UpdateMenuDTO updateMenuDTO) {
        updateMenuDTO.setId(id);
        return menuService.updateMenu(updateMenuDTO);
    }

    /**
     * 批量删除菜单
     */
    @DeleteMapping
    public R deleteMenuByIds(@RequestBody List<Integer> ids) {
        boolean removed = menuService.removeByIds(ids);
        return removed ? R.success("删除成功") : R.error("删除失败");
    }

    /**
     * 获取菜单树（如有需要）
     */
    @GetMapping("/tree")
    public R getMenuTree() {
        List<MenuVo> menuTree = menuService.getMenuTree();
        return R.success(menuTree);
    }

    /**
     * 获取当前登录用户的动态路由树结构
     * @return
     */
    @PostMapping("/getUserMenus")
    public R getUserMenus() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return R.success(menuService.getMenusByUserId(loginUser.getUserId()));
    }
}
