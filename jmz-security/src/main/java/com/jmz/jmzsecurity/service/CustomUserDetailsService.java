package com.jmz.jmzsecurity.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import com.jmz.jmzsecurity.mapper.PermissionMapper;
import com.jmz.jmzsecurity.mapper.RoleMapper;
import com.jmz.jmzsecurity.mapper.UserMapper;
import com.jmz.serveruser.entity.Role;
import com.jmz.serveruser.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.DisabledException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    /**
     * 根据用户手机号加载用户信息
     *
     * @param userPhone
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userPhone) throws UsernameNotFoundException {
        // 1. 根据手机号查询用户
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, userPhone));

        // 2. 用户不存在处理
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + userPhone);
        }
        
        // 3. 检查用户状态 - 如果被禁用则不允许登录
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new DisabledException("您已被禁用");
        }

        // 4. 根据用户id查询角色集合
        List<Role> roles = roleMapper.selectRolesByUserId(user.getUserId());
        Set<String> roleKeywords = new HashSet<>(roles.size());

        // 5.先批量获取所有角色ID
        List<Integer> roleIds = roles.stream()
                .peek(role -> roleKeywords.add(role.getKeyword()))
                .map(Role::getId)
                .collect(Collectors.toList());

        // 6. 批量查询权限（需在PermissionMapper实现批量查询）
        // 批量获取
        Set<String> permissionKeywords = new HashSet<>();
        if (!roleIds.isEmpty()) {
            permissionKeywords.addAll(permissionMapper.selectPermissionKeywordsByRoleIds(roleIds));
        }
        // 封装
        LoginUser loginUser = new LoginUser();
        loginUser.setRoles(roleKeywords);
        loginUser.setPermissions(permissionKeywords);
        loginUser.setUsername(user.getUsername());
        loginUser.setUserId(user.getUserId());
        loginUser.setPhone(user.getPhone());
        // 设置用户状态
        //密码
        loginUser.setPassword(user.getPassword());
        return loginUser;
    }

}
