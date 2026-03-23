package com.jmz.jmzcommonfeign.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.util.StringUtils;

/**
 * Feign 全局配置：透传请求头中的 JWT Token
 * <p>
 * 所有引入此模块的服务，Feign 调用时会自动将当前请求的 Authorization 头透传给下游服务。
 * </p>
 */
@Configuration
public class FeignAutoConfiguration {

    @Bean
    public RequestInterceptor feignRequestInterceptor() {
        return template -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                String token = request.getHeader("Authorization");
                if (StringUtils.hasText(token)) {
                    template.header("Authorization", token);
                }
            }
        };
    }
}
