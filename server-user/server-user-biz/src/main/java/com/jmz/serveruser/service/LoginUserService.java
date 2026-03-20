package com.jmz.serveruser.service;

import com.jmz.serveruser.vo.LoginUserInfoVo;

public interface LoginUserService {
    LoginUserInfoVo getInfo(Long userId);
}
