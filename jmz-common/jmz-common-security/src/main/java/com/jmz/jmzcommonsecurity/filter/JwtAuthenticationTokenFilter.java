package com.jmz.jmzcommonsecurity.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmz.jmzcommoncore.constant.HttpStatus;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.jmzcommoncore.utils.StringUtils;
import com.jmz.jmzcommonsecurity.service.TokenService;
import com.jmz.jmzcommonsecurity.utils.ServletUtils;

import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;


/**
 * token过滤器 验证token有效性
 *
 * @author ruoyi
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    // 引入自定义token服务
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain chain) throws ServletException, IOException {
        //请求头
        System.out.println("请求头：" + request.getHeaderNames());
        System.out.println("请求头Authorization：" + request.getHeader("Authorization"));
        System.out.println("请求头Content-Type：" + request.getHeader("Content-Type"));
        Map<String, String[]> params = ServletUtils.getParams(request);
        //打印请求参数
        System.out.println("请求参数：");
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            System.out.println(entry.getKey() + ":" + StringUtils.join(entry.getValue(), ","));
        }

        //如果携带 token
        if (StringUtils.isNotEmpty(request.getHeader("Authorization"))) {
            System.out.println("携带token");
            LoginUser loginUser = null;
            try {
                loginUser = tokenService.getLoginUser(request);
            } catch (Exception e) {
                System.out.println("token验证失败");
            }
            System.out.println("loginUser:" + loginUser);

            if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                tokenService.refreshToken(request);//验证令牌有效期，相差不足一天，自动刷新缓存
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());// 创建用户认证对象
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request, response); // 验证成功，继续处理
            } else if (StringUtils.isNull(loginUser)) {
                // 返回错误信息
                response.setStatus(HttpStatus.SUCCESS);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                ObjectMapper mapper = new ObjectMapper();
                String jsonResponse = mapper.writeValueAsString(R.error(HttpStatus.UNAUTHORIZED, "登录已过期"));
                response.getWriter().write(jsonResponse);
                response.getWriter().flush();
                return; // 验证失败，直接返回，不继续处理
            }
        } else {
            // 没有携带 token 的情况
            // 这里需要根据你的业务逻辑决定是否需要认证
            // 如果是公开接口，可以继续处理；如果需要认证，则返回错误
            chain.doFilter(request, response);
        }
    }


}
