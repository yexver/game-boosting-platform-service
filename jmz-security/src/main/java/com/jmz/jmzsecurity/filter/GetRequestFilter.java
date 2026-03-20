package com.jmz.jmzsecurity.filter;

import com.jmz.jmzcommoncore.utils.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
@Component
public class GetRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, String[]> params = request.getParameterMap();
        //请求路经
        System.out.println("请求路经：" + request.getRequestURI());
        //打印请求参数
        System.out.println("请求参数：");
        for (Map.Entry<String, String[]> entry : params.entrySet())
        {
            System.out.println(entry.getKey() + ":" + StringUtils.join(entry.getValue(), ","));
        }
        filterChain.doFilter(request, response);
    }
}
