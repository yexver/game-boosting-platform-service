package com.jmz.serveruser.service.impl;

import com.jmz.serveruser.mapper.UserMapper;
import com.jmz.serveruser.service.LoginUserService;
import com.jmz.serveruser.vo.LoginUserInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LoginUserServiceImpl implements LoginUserService {
    private final UserMapper userMapper;
    @Override
    public LoginUserInfoVo getInfo(Long userId) {
        LoginUserInfoVo userInfo = userMapper.getLoginUserInfo(userId);
        if (userInfo == null) {
            return null;
        }
        userInfo.setRole(userMapper.getUserRoleKeywordsByUserId(userId));
        userInfo.setPermission(userMapper.getUserPermissionKeywordsByUserId(userId));
        return userInfo;
    }


}
