package com.jmz.jmzsecurity.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import com.jmz.jmzsecurity.constants.RedisStorageConstants;
import com.jmz.jmzsecurity.domain.dto.RegisterDTO;
import com.jmz.jmzsecurity.mapper.UserMapper;
import com.jmz.jmzsecurity.mapper.UserAccountMapper;
import com.jmz.serveruser.entity.User;
import com.jmz.serveruser.entity.UserAccount;
import com.jmz.serveruser.feign.UserFeignClient;
import com.jmz.jmzcommonredis.utils.RedisUtils;
import com.jmz.jmzsecurity.exception.base.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import com.jmz.jmzsecurity.service.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    private final UserAccountMapper userAccountMapper;

    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String register(RegisterDTO registerDTO) {

        // 校验验证码
        /*String redisKey = RedisStorageConstants.PHONE_CAPTCHA_CODE_KEY + registerDTO.getPhone();
        Object codeInRedis = RedisUtils.get(redisKey);
        if (codeInRedis == null || !registerDTO.getCode().equals(codeInRedis.toString())) {
            throw new BaseException("register", "user.jcaptcha.error.or.expire", null, null);
        }*/
        //手机号是否已注册
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, registerDTO.getPhone());
        if (this.userMapper.selectCount(wrapper) > 0) {
            throw new BaseException("register", "phone.exists", null, null);
        }

        // 构造User实体
        User user = new User();
        user.setUserId(IdUtil.getSnowflake().nextId());
        user.setUsername(registerDTO.getUsername());
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setStatus(1); // 默认正常
        userMapper.insert(user);
        // 创建账户
        UserAccount account = new UserAccount();
        account.setUserId(user.getUserId());
        account.setBalance(BigDecimal.ZERO);
        account.setFrozenAmount(BigDecimal.ZERO);
        account.setTotalIncome(BigDecimal.ZERO);
        account.setTotalExpense(BigDecimal.ZERO);
        account.setCreatedAt(new Date());
        account.setUpdatedAt(new Date());
        userAccountMapper.insert(account);
        // 注册成功后删除验证码
        //RedisUtils.del(redisKey);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(registerDTO.getPhone(),registerDTO.getPassword());//用手机号代替名字
        // 该方法会去调用UserDetailsService.loadUserByUsername
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // 生成token
        return jwtTokenProvider.generateToken((LoginUser) authentication.getPrincipal());
    }
} 