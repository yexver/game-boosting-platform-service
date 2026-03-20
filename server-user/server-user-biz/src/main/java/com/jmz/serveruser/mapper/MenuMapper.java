package com.jmz.serveruser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmz.serveruser.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据用户ID查询所有菜单（含权限控制）
     */
    @Select("SELECT DISTINCT m.* FROM tb_jmz_menu m " +
            "JOIN tb_jmz_role_menu rm ON m.id = rm.menu_id " +
            "JOIN tb_jmz_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<Menu> selectMenusByUserId(@Param("userId") Long userId);
    
    /**
     * 根据角色ID查询菜单列表
     */
    @Select("SELECT m.* FROM tb_jmz_menu m " +
            "JOIN tb_jmz_role_menu rm ON m.id = rm.menu_id " +
            "WHERE rm.role_id = #{roleId}")
    List<Menu> getMenusByRoleId(@Param("roleId") Integer roleId);
}
