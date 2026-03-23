package com.jmz.serverwebsocket.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * WebSocket Feign 客户端降级工厂
 * 当 server-websocket 服务不可用时，避免影响主业务
 */
@Slf4j
@Component
public class WebSocketFeignClientFallbackFactory implements org.springframework.cloud.openfeign.FallbackFactory<WebSocketFeignClient> {

    @Override
    public WebSocketFeignClient create(Throwable cause) {
        log.warn("WebSocket 服务调用失败，降级处理: {}", cause.getMessage());
        return new WebSocketFeignClient() {
            @Override
            public Boolean isUserOnline(Long userId) {
                return false;
            }

            @Override
            public Integer getOnlineUserCount() {
                return 0;
            }

            @Override
            public void pushMessage(Long userId, String type, Map<String, Object> data) {
                log.debug("WebSocket 推送降级（pushMessage）: userId={}, type={}", userId, type);
            }

            @Override
            public void pushNewMessage(Long receiverId, Map<String, Object> messageData) {
                log.debug("WebSocket 推送降级（pushNewMessage）: receiverId={}", receiverId);
            }

            @Override
            public void pushOrderStatusUpdate(Long orderId, Long publisherId, Long takerId, Integer status) {
                log.debug("WebSocket 推送降级（pushOrderStatusUpdate）: orderId={}, status={}", orderId, status);
            }

            @Override
            public void broadcast(String type, Map<String, Object> data) {
                log.debug("WebSocket 广播降级（broadcast）: type={}", type);
            }
        };
    }
}
