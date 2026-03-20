package com.jmz.jmzsecurity.config;

import com.jmz.jmzsecurity.filter.GetRequestFilter;
import com.jmz.jmzsecurity.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public GetRequestFilter getRequestFilter() {
        return new GetRequestFilter();
    }


    //密码加密方式
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        //配置权限
        http.authorizeHttpRequests((requests) -> requests
                // 对于登录login 注册register 验证码captchaImage 允许匿名访问
                .requestMatchers("/login", "/register", "/test/*","/file/*", "/captchaImage","/sendVerificationCode","/sendSms").permitAll()
                // 静态资源，可匿名访问，这一行配置只放行了特定的静态资源路径和根路径的GET请求。
                .requestMatchers(HttpMethod.GET, "/", "/avatar/**","/shopImage/**","/productImage/**","/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated())

                // 添加自定义过滤器
                .addFilterBefore(getRequestFilter(), UsernamePasswordAuthenticationFilter.class)


                .sessionManagement(session -> session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))//禁用session
                .formLogin(AbstractHttpConfigurer::disable) // 禁用表单登录
                .csrf(AbstractHttpConfigurer::disable);// 禁用 CSRF;


        return http.build();
    }
}