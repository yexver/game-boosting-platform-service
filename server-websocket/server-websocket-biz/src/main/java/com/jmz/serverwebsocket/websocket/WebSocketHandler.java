package com.jmz.serverwebsocket.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmz.jmzcommoncore.utils.StringUtils;
import com.jmz.serverwebsocket.manager.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

/**
 * WebSocket 消息处理器
 * <p>
 * 会话管理已委托给 WebSocketSessionManager：
 * - 内存存储 WebSocketSession
 * - Redis 存储会话元数据（路由索引）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final WebSocketSessionManager sessionManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        Object userIdObj = attributes.get("userId");

        if (userIdObj != null) {
            Long userId = sessionManager.parseUserId(userIdObj);
            if (userId != null) {
                // 委托给 SessionManager 注册会话
                sessionManager.registerSession(userId, session);
                log.info("WebSocket 连接建立: userId={}, sessionId={}", userId, session.getId());
                sendMessage(session, "connected", "WebSocket 连接成功");
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            log.debug("收到消息: {}", payload);

            Map<String, Object> data = objectMapper.readValue(payload, Map.class);
            String type = (String) data.get("type");

            switch (type != null ? type : "unknown") {
                case "ping":
                    handlePing(session, data);
                    break;
                case "message":
                    handleChatMessage(session, data);
                    break;
                case "read":
                    handleMessageRead(session, data);
                    break;
                case "order_status":
                    handleOrderStatusQuery(session, data);
                    break;
                default:
                    log.warn("未知的消息类型: {}", type);
            }

        } catch (Exception e) {
            log.error("处理消息异常: {}", e.getMessage(), e);
            sendMessage(session, "error", "消息处理失败: " + e.getMessage());
        }
    }

    private void handlePing(WebSocketSession session, Map<String, Object> data) throws IOException {
        // 更新心跳
        Long userId = sessionManager.getUserIdFromSession(session);
        if (userId != null) {
            sessionManager.updateHeartbeat(userId);
        }

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                Map.of("type", "pong", "timestamp", System.currentTimeMillis())
        )));
    }

    private void handleChatMessage(WebSocketSession session, Map<String, Object> data) throws IOException {
        Long receiverId = sessionManager.parseUserId(data.get("receiverId"));
        Long senderId = sessionManager.getUserIdFromSession(session);
        String content = (String) data.get("content");
        Integer messageType = parseInteger(data.get("messageType"));
        Long orderId = sessionManager.parseUserId(data.get("orderId"));

        if (receiverId == null || StringUtils.isEmpty(content)) {
            sendMessage(session, "error", "消息参数不完整");
            return;
        }

        // 通过 SessionManager 发送（支持跨服务）
        sessionManager.sendToUser(receiverId, "message", Map.of(
                "senderId", senderId != null ? senderId : 0,
                "receiverId", receiverId,
                "content", content,
                "messageType", messageType != null ? messageType : 1,
                "orderId", orderId != null ? orderId : "",
                "timestamp", System.currentTimeMillis()
        ));

        sendMessage(session, "message_sent", Map.of(
                "receiverId", receiverId,
                "timestamp", System.currentTimeMillis()
        ));
    }

    private void handleMessageRead(WebSocketSession session, Map<String, Object> data) throws IOException {
        Long messageId = sessionManager.parseUserId(data.get("messageId"));
        Long readerId = sessionManager.getUserIdFromSession(session);

        if (messageId == null) {
            sendMessage(session, "error", "消息ID不能为空");
            return;
        }

        Long senderId = sessionManager.parseUserId(data.get("senderId"));
        if (senderId != null) {
            // 通过 SessionManager 发送（支持跨服务）
            sessionManager.sendToUser(senderId, "message_read", Map.of(
                    "messageId", messageId,
                    "readerId", readerId,
                    "timestamp", System.currentTimeMillis()
            ));
        }

        sendMessage(session, "read_ack", Map.of("messageId", messageId));
    }

    private void handleOrderStatusQuery(WebSocketSession session, Map<String, Object> data) throws IOException {
        Long orderId = sessionManager.parseUserId(data.get("orderId"));
        if (orderId == null) {
            sendMessage(session, "error", "订单ID不能为空");
            return;
        }
        sendMessage(session, "order_status", Map.of(
                "orderId", orderId,
                "status", 0,
                "timestamp", System.currentTimeMillis()
        ));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 委托给 SessionManager 移除会话
        sessionManager.removeSession(session);
        log.info("WebSocket 连接关闭: sessionId={}, status={}", session.getId(), status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        Long userId = sessionManager.getUserIdFromSession(session);
        log.error("WebSocket 传输错误: userId={}, sessionId={}, error={}",
                userId, session.getId(), exception.getMessage());
    }

    /**
     * 向指定用户发送消息（对外暴露的接口）
     */
    public void sendToUser(Long userId, String type, Map<String, Object> data) {
        sessionManager.sendToUser(userId, type, data);
    }

    /**
     * 广播消息给所有在线用户
     */
    public void broadcast(String type, Map<String, Object> data) {
        sessionManager.broadcast(type, data);
    }

    /**
     * 通知订单状态更新
     */
    public void notifyOrderStatusUpdate(Long orderId, Long publisherId, Long takerId, Integer newStatus) {
        Map<String, Object> data = Map.of(
                "orderId", orderId,
                "status", newStatus,
                "timestamp", System.currentTimeMillis()
        );
        if (publisherId != null) {
            sessionManager.sendToUser(publisherId, "order_status_update", data);
        }
        if (takerId != null && !takerId.equals(publisherId)) {
            sessionManager.sendToUser(takerId, "order_status_update", data);
        }
    }

    /**
     * 通知新消息
     */
    public void notifyNewMessage(Long receiverId, Map<String, Object> messageData) {
        sessionManager.sendToUser(receiverId, "message", messageData);
    }

    /**
     * 获取在线用户数
     */
    public int getOnlineUserCount() {
        return sessionManager.getOnlineUserCount();
    }

    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(Long userId) {
        return sessionManager.isUserOnline(userId);
    }

    /**
     * 获取当前服务ID
     */
    public String getServerId() {
        return sessionManager.getServerId();
    }

    private void sendMessage(WebSocketSession session, String type, Object data) throws IOException {
        Map<String, Object> message = new java.util.HashMap<>();
        message.put("type", type);
        if (data instanceof String) {
            message.put("message", data);
        } else if (data instanceof Map) {
            message.putAll((Map<String, Object>) data);
        }
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
    }

    private Integer parseInteger(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Integer) return (Integer) obj;
        if (obj instanceof Long) return ((Long) obj).intValue();
        if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
