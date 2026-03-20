package com.jmz.serveruser.util;

import com.jmz.serveruser.entity.Menu;

import java.util.*;
import java.util.stream.Collectors;

public class MenuTreeUtil {

    public static List<Menu> buildTree(List<Menu> menus) {
        Map<Integer, Menu> map = new HashMap<>();
        menus.forEach(menu -> map.put(menu.getId(), menu));

        List<Menu> roots = new ArrayList<>();

        menus.forEach(menu -> {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                roots.add(menu);
            } else {
                Menu parent = map.get(menu.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menu);
                }
            }
        });

        return sortMenus(roots);
    }

    private static List<Menu> sortMenus(List<Menu> menus) {
        return menus.stream()
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .map(menu -> {
                    if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                        menu.setChildren(sortMenus(menu.getChildren()));
                    }
                    return menu;
                })
                .collect(Collectors.toList());
    }
}