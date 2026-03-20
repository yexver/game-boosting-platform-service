package com.jmz.jmzsecurity.service;

import com.jmz.jmzcommoncore.utils.StringUtils;
import com.jmz.jmzcommonredis.utils.RedisUtils;
import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import com.jmz.jmzsecurity.constants.RedisStorageConstants;
import com.jmz.jmzsecurity.constants.UserConstants;
import com.jmz.jmzsecurity.exception.base.BaseException;
import com.jmz.serveruser.dto.CreateUserDTO;
import com.jmz.serveruser.feign.UserFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {
    @Value("${captcha.enable}")
    boolean captchaEnable;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final UserFeignClient userFeignClient;
    /**
     * 登录验证
     *
     * @param userPhone 用户手机号
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String userPhone, String password, String code, String uuid) {
        // 验证码校验
        validateCaptcha(userPhone, code, uuid);
        // 登录前置校验
        loginPreCheck(userPhone, password);
        // 用户验证
        Authentication authentication = null;
        try
        {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPhone, password);//用手机号代替名字
            // 该方法会去调用UserDetailsService.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        }
        catch (Exception e)
        {
            System.out.println("登录异常类型: " + e.getClass().getName());
            System.out.println("登录异常消息: " + e.getMessage());
            
            if (e instanceof BadCredentialsException)
            {
                throw new BaseException("userLogin","user.password.not.match", null, null);
            }
            else if (e instanceof DisabledException)
            {
                throw new BaseException("userLogin","user.blocked", null, null);
            }
            else if (e instanceof InternalAuthenticationServiceException)
            {
                // 检查内部异常消息
                String message = e.getMessage();
                if (message != null && message.contains("禁用")) {
                    throw new BaseException("userLogin","user.blocked", null, null);
                } else {
                    throw new BaseException("userLogin","user.not.exists", null, null);
                }
            }
            else if (e instanceof UsernameNotFoundException)
            {
                throw new BaseException("userLogin","user.not.exists", null, null);
            }
            else
            {
                throw new BaseException("userLogin","user.not.exists", null, null);
            }
        }
        LoginUser user = (LoginUser) authentication.getPrincipal();
        // 生成token
        return jwtTokenProvider.generateToken(user);
    }

    /**
     * 校验验证码
     *
     * @param userPhone 用户手机号
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String userPhone, String code, String uuid)
    {
        System.out.println("code======"+code);
        System.out.println(uuid);

        if (captchaEnable)
        {
            String verifyKey = RedisStorageConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = (String) RedisUtils.get(verifyKey);
            RedisUtils.del(verifyKey);
            if (captcha == null)
            {
                throw new BaseException("userLogin","user.jcaptcha.expire", null, null);
            }
            if (!code.equalsIgnoreCase(captcha))
            {
                throw new BaseException("userLogin","user.jcaptcha.error", null, null);
            }
        }
    }

    /**
     * 登录前置校验
     * @param userPhone 用户手机号
     * @param password 用户密码
     */
    public void loginPreCheck(String userPhone, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(userPhone) || StringUtils.isEmpty(password))
        {
            throw new BaseException("user","user.password.empty", null, null);
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            Object[] args = {UserConstants.PASSWORD_MIN_LENGTH, UserConstants.PASSWORD_MAX_LENGTH};
            throw new BaseException("user","password.not.range", args, null);
        }
        // 用户名不在指定范围内 错误
        if (userPhone.length() < UserConstants.USERNAME_MIN_LENGTH
                || userPhone.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            Object[] args = {UserConstants.USERNAME_MIN_LENGTH, UserConstants.USERNAME_MAX_LENGTH};
            throw new BaseException("user","user.not.range", args, null);
        }
    }

}
