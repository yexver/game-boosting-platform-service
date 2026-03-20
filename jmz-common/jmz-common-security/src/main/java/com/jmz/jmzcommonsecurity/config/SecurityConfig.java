package com.jmz.jmzcommonsecurity.config;

import com.jmz.jmzcommonsecurity.filter.JwtAuthenticationTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@AutoConfiguration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JwtAuthenticationTokenFilter authenticationTokenFilter;
    private final LogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:8848",
                "http://localhost:9999",
                "http://localhost:5172",
                "http://localhost:5173"
        ));     //config.addAllowedOrigin("*"); //允许所有域名
        config.addAllowedHeader("*"); // 允许所有头部
        config.addAllowedMethod("*"); // 允许所有HTTP方法
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                        // 对于登录login 注册register 验证码captchaImage 允许匿名访问
                        .requestMatchers("/login", "/register", "/test/*", "/captchaImage","/sendVerificationCode","/homePage/**","/games/**"
                                ,"/orders/**","/systems/getAll","servers/getAll","/user/getUserInfoById/**","/alipay/**").permitAll()
                        // 静态资源，可匿名访问，这一行配置只放行了特定的静态资源路径和根路径的GET请求。
                        .requestMatchers(HttpMethod.GET, "/", "/avatar/**","/shopImage/**","/productImage/**","/*.html", "/profile/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs", "/druid/**").permitAll()
                        // 除上面外的所有请求全部需要鉴权认证
                        .anyRequest().authenticated())



                // 如果需要HTTP Basic认证，使用以下配置（不推荐用于生产环境）
                /*http.httpBasic(Customizer.withDefaults());*/
                .formLogin(AbstractHttpConfigurer::disable)
                //CSRF禁用                       //CSRF 保护：除非有特殊原因，否则不应禁用 CSRF 保护。可以通过生成和验证 CSRF Token 来增强安全性。
                .csrf(AbstractHttpConfigurer::disable)//csrf 全称：Cross-Site Request Forgery（跨站请求伪造）
                //作用：用于防止 CSRF 攻击。CSRF 攻击是指攻击者利用用户已认证的身份，在用户不知情的情况下提交恶意请求。
                //应用场景：在表单提交、API 请求等场景中，确保请求是由合法用户发起的，而不是由第三方恶意网站伪造的。
                // 禁用HTTP响应标头
                .headers((headersCustomizer) -> {
                    headersCustomizer.cacheControl(HeadersConfigurer.CacheControlConfig::disable).frameOptions(options -> options.sameOrigin());
                })
                // 基于 token 机制，所以不需要 Session
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                // 添加JWT filter
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 添加Logout filter
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler)
                )
                // 添加CORS filter
                .addFilterBefore(corsFilter(), JwtAuthenticationTokenFilter.class)
                .addFilterBefore(corsFilter(), LogoutFilter.class);

        return http.build();
    }
}
