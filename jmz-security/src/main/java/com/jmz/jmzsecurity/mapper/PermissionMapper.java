package com.jmz.jmzsecurity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmz.serveruser.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 根据角色ID查询权限关键词列表
     * @param roleId 角色ID
     * @return 权限关键词列表
     */
    @Select("SELECT DISTINCT p.keyword FROM tb_jmz_permission p " +
            "INNER JOIN tb_jmz_role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    Set<String> selectPermissionKeywordsByRoleId(Integer roleId);

    // PermissionMapper新增方法示例：
    @Select("<script>" +
            "SELECT DISTINCT p.keyword FROM tb_jmz_permission p " +
            "JOIN tb_jmz_role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id IN " +
            "<foreach item='id' collection='roleIds' open='(' separator=',' close=')'>#{id}</foreach>" +
            "</script>")
    Set<String> selectPermissionKeywordsByRoleIds(@Param("roleIds") List<Integer> roleIds);}
