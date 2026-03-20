package com.jmz.serveruser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmz.serveruser.dto.CreateMenuDTO;
import com.jmz.serveruser.dto.UpdateMenuDTO;
import com.jmz.serveruser.dto.MenuQueryDTO;
import com.jmz.serveruser.entity.Menu;
import com.jmz.serveruser.vo.MenuVo;
import com.jmz.jmzcommoncore.responseResult.R;

import java.util.List;

public interface MenuService extends IService<Menu> {

    List<Menu> getMenusByUserId(Long userId);

    /**
     * 获取菜单树
     */
    List<MenuVo> getMenuTree();

    /**
     * 创建菜单
     */
    R createMenu(CreateMenuDTO createMenuDTO);

    /**
     * 更新菜单
     */
    R updateMenu(UpdateMenuDTO updateMenuDTO);

    /**
     * 分页/条件查询菜单
     */
    R getMenuList(MenuQueryDTO menuQueryDTO);
}
