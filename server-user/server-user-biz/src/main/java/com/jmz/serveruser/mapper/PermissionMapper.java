package com.jmz.serveruser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmz.serveruser.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    
    /**
     * 根据角色ID查询权限列表
     */
    @Select("SELECT p.* FROM tb_jmz_permission p " +
            "JOIN tb_jmz_role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    List<Permission> getPermissionsByRoleId(@Param("roleId") Integer roleId);
}
