package com.jmz.jmzcommonsecurity.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.jmzcommonsecurity.service.TokenService;


import com.jmz.jmzcommonsecurity.utils.MessageUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{

    private final TokenService tokenService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
            // 删除用户缓存记录
            tokenService.del((HttpServletRequest) request);
        // 使用 Jackson 进行 JSON 序列化
        R result = R.success("退出成功");
        String jsonResponse = objectMapper.writeValueAsString(result);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
