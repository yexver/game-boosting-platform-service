package com.jmz.jmzcommonsecurity.service;

import com.jmz.jmzcommonredis.utils.RedisUtils;
import com.jmz.jmzcommonsecurity.constant.RedisStorageConstants;
import com.jmz.jmzcommonsecurity.utils.JwtUtil;

import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;


import java.util.List;


public class TokenService {
    @Value("${jwt.secretKey:KbPeShVmYq3t6w9z$C&F)H@McQfTjWnZr4u7x!A%D*G-JaNdRgU}")
    private String jwtSecret;

    @Value("${jwt.expiration:36000}")
    private int jwtExpirationInMs;

    @Value("${jwt.header:Authorization}")
    private String jwtHeader;


    // 验证 token
    public boolean validateToken(String token) {

        //解析token
        Claims claims = JwtUtil.parseJWT(jwtSecret, token);

        //解析获取的key
        String tokenKey = claims.get("tokenKey").toString();
        String redisTokenKey = RedisStorageConstants.USER_TOKEN_KEY + tokenKey;

        //如果
        LoginUser loginUser = (LoginUser) RedisUtils.get(redisTokenKey);
        return loginUser != null;
    }

    //刷新 token 过期时间
    public void refreshToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        refreshToken(token);
    }
    public void refreshToken(String token) {
        Claims claims = JwtUtil.parseJWT(jwtSecret, token);
        //获取redisTokenKey
        String redisTokenKey = RedisStorageConstants.USER_TOKEN_KEY + claims.get("tokenKey").toString();
        //查询redisTokenKey有效期
        long expireTime = RedisUtils.getExpire(redisTokenKey);
        //如果token还有小于一天的有效期，则刷新
        //一天的时间戳为86400000
        long timeOfADay = 86400000L;
        if(expireTime < timeOfADay){
            //获取用户信息
            LoginUser loginUser = (LoginUser) RedisUtils.get(redisTokenKey);
            //刷新 redisTokenKey
            RedisUtils.expire(RedisStorageConstants.USER_TOKEN_KEY + loginUser.getToken(), jwtExpirationInMs);
            //刷新 findTokenKey
            RedisUtils.expire(RedisStorageConstants.FIND_TOKEN_KEY + loginUser.getUserId(), jwtExpirationInMs);
        }
    }

    //删除 token
    public void del(HttpServletRequest request) {
        String token = request.getHeader(jwtHeader);
        deleteToken(token);
    }
    public void deleteToken(String token) {
        Claims claims = JwtUtil.parseJWT(jwtSecret, token);
        String redisTokenKey = RedisStorageConstants.USER_TOKEN_KEY + claims.get("tokenKey");
        LoginUser loginUser = (LoginUser) RedisUtils.get(redisTokenKey);
        System.out.println("退出用户：");
        System.out.println(loginUser);
        RedisUtils.del(redisTokenKey);
        RedisUtils.del(RedisStorageConstants.FIND_TOKEN_KEY + loginUser.getUserId());
    }

    //获取loginUser
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = request.getHeader(jwtHeader);
        return getLoginUser(token);
    }
    public LoginUser getLoginUser(String token) {
        Claims claims = JwtUtil.parseJWT(jwtSecret, token);
        String redisTokenKey = RedisStorageConstants.USER_TOKEN_KEY + claims.get("tokenKey");
        return (LoginUser)RedisUtils.get(redisTokenKey);
    }


    //更新用户信息
    public void updateUserInfo(LoginUser loginUser) {
        String redisTokenKey = RedisStorageConstants.USER_TOKEN_KEY + loginUser.getToken();
        RedisUtils.set(redisTokenKey, loginUser, jwtExpirationInMs);
    }

    //批量 更新用户信息
    public void updateUserInfo(List<Long> userIds) {
        userIds.forEach(userId -> {
            String findTokenKey = RedisStorageConstants.FIND_TOKEN_KEY + userId;
            String redisTokenKey = (String) RedisUtils.get(findTokenKey);
            LoginUser loginUser = (LoginUser) RedisUtils.get(redisTokenKey);
            RedisUtils.set(redisTokenKey, loginUser, jwtExpirationInMs);
        });
    }



}
