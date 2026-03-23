package com.jmz.serverwebsocket.websocket;

import com.jmz.jmzcommoncore.utils.StringUtils;
import com.jmz.jmzcommonsecurity.constant.RedisStorageConstants;
import com.jmz.jmzcommonsecurity.utils.JwtUtil;
import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * WebSocket 认证拦截器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String query = request.getURI().getQuery();
        String token = null;

        if (StringUtils.isNotEmpty(query)) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("token=")) {
                    token = URLDecoder.decode(param.substring(6), StandardCharsets.UTF_8);
                    break;
                }
            }
        }

        if (StringUtils.isEmpty(token)) {
            log.warn("WebSocket 连接失败: token 为空");
            return false;
        }

        try {
            if (!JwtUtil.checkToken(secretKey, token)) {
                log.warn("WebSocket 连接失败: token 验证失败");
                return false;
            }

            Map<String, Object> claims = JwtUtil.getUserInfo(secretKey, token);
            // 登录 JWT 只存 tokenKey，真实用户在 Redis：user_token_key:{tokenKey}
            Object tokenKeyObj = claims != null ? claims.get("tokenKey") : null;
            if (tokenKeyObj == null) {
                log.warn("WebSocket 连接失败: JWT 中无 tokenKey");
                return false;
            }
            String redisKey = RedisStorageConstants.USER_TOKEN_KEY + tokenKeyObj;
            Object cached = redisTemplate.opsForValue().get(redisKey);
            Long userId = extractUserId(cached);
            if (userId != null) {
                attributes.put("userId", userId);
                attributes.put("token", token);
                log.info("WebSocket 握手成功: userId={}", userId);
                return true;
            }

            log.warn("WebSocket 连接失败: Redis 中无登录用户或 userId 为空, key={}", redisKey);
            return false;

        } catch (Exception e) {
            log.error("WebSocket 认证异常: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }

    private static Long extractUserId(Object cached) {
        if (cached == null) {
            return null;
        }
        if (cached instanceof LoginUser loginUser) {
            return loginUser.getUserId();
        }
        if (cached instanceof Map<?, ?> map) {
            Object id = map.get("userId");
            if (id instanceof Number n) {
                return n.longValue();
            }
            if (id instanceof String s) {
                try {
                    return Long.parseLong(s);
                } catch (NumberFormatException ignored) {
                    return null;
                }
            }
        }
        return null;
    }
}
