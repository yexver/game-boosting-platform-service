package com.jmz.jmzsecurity.test;

import com.jmz.jmzcommoncore.constant.Constants;
import com.jmz.jmzcommoncore.utils.IdUtils;
import com.jmz.jmzcommonredis.utils.RedisUtils;
import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import com.jmz.jmzsecurity.constants.RedisStorageConstants;

import com.jmz.jmzsecurity.util.JwtUtil;
;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;



@SpringBootTest  // 添加Spring测试注解
//@ExtendWith(SpringExtension.class)
public class TestClass {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    //@Test
    public void test01() {
        RedisUtils.set(RedisStorageConstants.CAPTCHA_CODE_KEY+ IdUtils.simpleUUID(), 2
                , Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
    }
    //@Test
    public void generateToken() {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(1L);
        loginUser.setUsername("admin");
        loginUser.setPassword("{noop}admin123");

        loginUser.setLoginTime(new Date().getTime());
        //检查是否存在（即已登录）
        if (RedisUtils.get(RedisStorageConstants.FIND_TOKEN_KEY + loginUser.getUserId())!=null){
            //删除
            RedisUtils.del(RedisStorageConstants.USER_TOKEN_KEY + loginUser.getToken());
            RedisUtils.del(RedisStorageConstants.FIND_TOKEN_KEY + loginUser.getUserId());
        }else if (RedisUtils.get(RedisStorageConstants.USER_TOKEN_KEY + loginUser.getToken())!=null){
            //删除
            RedisUtils.del(RedisStorageConstants.USER_TOKEN_KEY + loginUser.getToken());
        }

        //生成随机tokenKey
        String tokenKey = IdUtils.randomUUID();
        loginUser.setToken(tokenKey);
        loginUser.setLoginTime(new Date().getTime());
        //合成redisTokenKey
        String redisTokenKey = RedisStorageConstants.USER_TOKEN_KEY + tokenKey;
        //根据用id生成findTokenKey指向redisTokenKey（便于查找存用户信息的redis键值对）
        String findTokenKey = RedisStorageConstants.FIND_TOKEN_KEY + loginUser.getUserId();
        //存用户信息
        RedisUtils.set(redisTokenKey, loginUser, 360000);
        //存findTokenKey指向redisTokenKey
        RedisUtils.set(findTokenKey, redisTokenKey, 360000);

        //token只存tokenKey，从tokenKey获得redisTokenKey，redisTokenKey指向用户信息
        System.out.println(JwtUtil.createJwt("KbPeShVmYq3t6w9z$C&F)H@McQfTjWnZr4u7x!A%D*G-JaNdRgU", 360000,
                Map.of(
                        "tokenKey", tokenKey
                )
        ));
    }

    @Test
    //PasswordEncoder加密
    public void test02() {
        System.out.println(passwordEncoder.encode("123456"));
    }

    @Test
    //验证密码
    public void test03() {
        System.out.println(passwordEncoder.matches("123456",
                "$2a$10$P8BoTmRIiuRA8J8FngQ0C.qrHsLn6dKAcX0RIjUvpuiV2FWpayDjK"));
    }

}
