package com.jmz.jmzsecurity.service;

import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import com.jmz.jmzsecurity.constants.RedisStorageConstants;
import com.jmz.jmzsecurity.util.JwtUtil;
import com.jmz.jmzcommoncore.utils.IdUtils;
import com.jmz.jmzcommonredis.utils.RedisUtils;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secretKey:KbPeShVmYq3t6w9z$C&F)H@McQfTjWnZr4u7x!A%D*G-JaNdRgU}")
    private String jwtSecret;

    @Value("${jwt.expiration:36000}")
    private int jwtExpirationInMs;

    @Value("${jwt.header:Authorization}")
    private String jwtHeader;

    // 生成 token
    public String generateToken(LoginUser loginUser) {
        //检查是否存在（即已登录）
        if (RedisUtils.get(RedisStorageConstants.FIND_TOKEN_KEY + loginUser.getUserId())!=null){
            String redisTokenKey = (String) RedisUtils.get(RedisStorageConstants.FIND_TOKEN_KEY + loginUser.getUserId());
            //删除
            RedisUtils.del(redisTokenKey);
            RedisUtils.del(RedisStorageConstants.FIND_TOKEN_KEY + loginUser.getUserId());
        }

        //生成随机tokenKey
        String tokenKey = IdUtils.randomUUID();
        loginUser.setToken(tokenKey);
        loginUser.setLoginTime(new Date().getTime());
        loginUser.setExpireTime(new Date().getTime() + jwtExpirationInMs);
        //合成redisTokenKey
        String redisTokenKey = RedisStorageConstants.USER_TOKEN_KEY + tokenKey;
        //根据用id生成findTokenKey指向redisTokenKey（便于查找存用户信息的redis键值对）
        String findTokenKey = RedisStorageConstants.FIND_TOKEN_KEY + loginUser.getUserId();
        //存用户信息
        RedisUtils.set(redisTokenKey, loginUser, jwtExpirationInMs);
        //存findTokenKey指向redisTokenKey
        RedisUtils.set(findTokenKey, redisTokenKey, jwtExpirationInMs);

        //token只存tokenKey，从tokenKey获得redisTokenKey，redisTokenKey指向用户信息
        return JwtUtil.createJwtWithoutExpiration(jwtSecret,
                Map.of(
                        "tokenKey", tokenKey
                )
        );
    }


    //删除 token
    public void deleteToken(String token) {
        Claims claims = JwtUtil.parseJWT(jwtSecret, token);
        //获取redisTokenKey
        String redisTokenKey = RedisStorageConstants.USER_TOKEN_KEY + claims.get("tokenKey");
        LoginUser loginUser = (LoginUser)RedisUtils.get(redisTokenKey);
        RedisUtils.del(RedisStorageConstants.USER_TOKEN_KEY + loginUser.getToken());
        RedisUtils.del(RedisStorageConstants.FIND_TOKEN_KEY + loginUser.getUserId());
    }


}
