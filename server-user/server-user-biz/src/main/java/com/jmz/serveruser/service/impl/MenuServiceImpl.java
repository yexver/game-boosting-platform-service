package com.jmz.serveruser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmz.serveruser.dto.CreateMenuDTO;
import com.jmz.serveruser.dto.MenuQueryDTO;
import com.jmz.serveruser.dto.UpdateMenuDTO;
import com.jmz.serveruser.entity.Menu;
import com.jmz.serveruser.mapper.MenuMapper;
import com.jmz.serveruser.service.MenuService;
import com.jmz.serveruser.util.MenuTreeUtil;
import com.jmz.serveruser.vo.MenuVo;
import com.jmz.jmzcommoncore.responseResult.R;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Menu> getMenusByUserId(Long userId) {
        return MenuTreeUtil.buildTree(this.baseMapper.selectMenusByUserId(userId));
    }

    @Override
    public List<MenuVo> getMenuTree() {
        List<Menu> menus = this.list();
        return buildMenuTree(menus);
    }

    @Override
    public R createMenu(CreateMenuDTO createMenuDTO) {
        Menu menu = new Menu();
        menu.setName(createMenuDTO.getName());
        menu.setPath(createMenuDTO.getPath());
        menu.setComponent(createMenuDTO.getComponent());
        menu.setParentId(createMenuDTO.getParentId());
        menu.setType(createMenuDTO.getType());
        menu.setIcon(createMenuDTO.getIcon());
        menu.setOrderNum(createMenuDTO.getOrderNum());
        menu.setPermission(createMenuDTO.getPermission());
        menu.setHidden(createMenuDTO.getHidden());
        boolean saved = this.save(menu);
        return saved ? R.success("创建成功") : R.error("创建失败");
    }

    @Override
    public R updateMenu(UpdateMenuDTO updateMenuDTO) {
        Menu menu = this.getById(updateMenuDTO.getId());
        if (menu == null) {
            return R.error("菜单不存在");
        }
        menu.setName(updateMenuDTO.getName());
        menu.setPath(updateMenuDTO.getPath());
        menu.setComponent(updateMenuDTO.getComponent());
        menu.setParentId(updateMenuDTO.getParentId());
        menu.setType(updateMenuDTO.getType());
        menu.setIcon(updateMenuDTO.getIcon());
        menu.setOrderNum(updateMenuDTO.getOrderNum());
        menu.setPermission(updateMenuDTO.getPermission());
        menu.setHidden(updateMenuDTO.getHidden());
        boolean updated = this.updateById(menu);
        return updated ? R.success("更新成功") : R.error("更新失败");
    }

    @Override
    public R getMenuList(MenuQueryDTO menuQueryDTO) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        if (menuQueryDTO.getName() != null && !menuQueryDTO.getName().isEmpty()) {
            wrapper.like(Menu::getName, menuQueryDTO.getName());
        }
        if (menuQueryDTO.getPath() != null && !menuQueryDTO.getPath().isEmpty()) {
            wrapper.like(Menu::getPath, menuQueryDTO.getPath());
        }
        if (menuQueryDTO.getType() != null) {
            wrapper.eq(Menu::getType, menuQueryDTO.getType());
        }
        Page<Menu> pageObj = new Page<>(menuQueryDTO.getPage(), menuQueryDTO.getSize());
        IPage<Menu> resultPage = this.page(pageObj, wrapper);
        List<java.util.Map<String, Object>> result = resultPage.getRecords().stream()
            .map(m -> {
                java.util.Map<String, Object> map = new java.util.HashMap<>();
                map.put("id", m.getId());
                map.put("name", m.getName());
                map.put("path", m.getPath());
                map.put("component", m.getComponent());
                map.put("parent_id", m.getParentId());
                map.put("type", m.getType());
                map.put("icon", m.getIcon());
                map.put("order_num", m.getOrderNum());
                map.put("permission", m.getPermission());
                map.put("hidden", m.getHidden());
                return map;
            })
            .collect(Collectors.toList());
        java.util.Map<String, Object> resp = new java.util.HashMap<>();
        resp.put("records", result);
        resp.put("total", resultPage.getTotal());
        resp.put("current", resultPage.getCurrent());
        resp.put("size", resultPage.getSize());
        return R.success(resp);
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