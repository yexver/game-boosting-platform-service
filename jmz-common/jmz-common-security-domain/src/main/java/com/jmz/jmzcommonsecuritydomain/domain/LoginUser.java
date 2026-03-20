package com.jmz.jmzcommonsecuritydomain.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {

    private Long userId;
    private String username;
    private String phone;
    private String password;
    //角色
    private Set<String> roles;
    //权限
    private Set<String> permissions;
    //菜单
    //private Set<String> menus;
    private Integer status; // 添加用户状态字段

    /**
     * token
     */
    private String Token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    public LoginUser(String phone, String password) {
        this.username = phone;
        this.password = password;
    }
    /**
     * 获取权限(角色、权限)
     * @return 权限
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 添加角色（自动加 ROLE_ 前缀）
        if (roles != null) {
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        }

        // 添加权限
        if (permissions != null) {
            permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
        }

        return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true; // 账户未过期
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true; // 账户未锁定
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true; // 凭证未过期
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return status == null || status == 1; // 1表示启用，0表示禁用
    }

    // 添加getter和setter
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
