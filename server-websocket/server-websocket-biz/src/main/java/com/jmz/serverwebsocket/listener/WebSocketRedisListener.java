package com.jmz.serverwebsocket.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmz.serverwebsocket.manager.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Map;

/**
 * Redis Pub/Sub 监听器
 * <p>
 * 负责监听集群中的 WebSocket 事件：
 * 1. 用户上下线事件
 * 2. 跨服务消息转发
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketRedisListener {

    private final RedisMessageListenerContainer listenerContainer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 延迟注入避免循环依赖
    private WebSocketSessionManager sessionManager;

    @org.springframework.beans.factory.annotation.Autowired
    public void setSessionManager(@Lazy WebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    // 事件频道
    private static final String CHANNEL_EVENTS = "websocket:events";
    // 消息转发频道前缀（用于接收定向消息）
    private static final String CHANNEL_FORWARD_PREFIX = "websocket:forward:";

    @PostConstruct
    public void init() {
        // 监听集群事件频道
        listenerContainer.addMessageListener(
                new ClusterEventListener(),
                new ChannelTopic(CHANNEL_EVENTS)
        );
        log.info("WebSocket Redis 监听器初始化完成，监听频道: {}", CHANNEL_EVENTS);
    }

    /**
     * 启动监听指定服务实例的转发消息
     */
    public void startListeningForServer(String serverId) {
        String channel = CHANNEL_FORWARD_PREFIX + serverId;
        listenerContainer.addMessageListener(
                new ForwardMessageListener(),
                new ChannelTopic(channel)
        );
        log.info("开始监听消息转发频道: {}", channel);
    }

    /**
     * 停止监听指定服务实例的转发消息
     */
    public void stopListeningForServer(String serverId) {
        String channel = CHANNEL_FORWARD_PREFIX + serverId;
        listenerContainer.removeMessageListener(
                new ForwardMessageListener(),
                new ChannelTopic(channel)
        );
        log.info("停止监听消息转发频道: {}", channel);
    }

    /**
     * 集群事件监听器
     */
    private class ClusterEventListener implements MessageListener {
        @Override
        public void onMessage(Message message, byte[] pattern) {
            try {
                String body = new String(message.getBody());
                Map<String, Object> event = objectMapper.readValue(body, Map.class);

                String eventType = (String) event.get("type");
                String sourceServerId = (String) event.get("serverId");

                log.debug("收到集群事件: type={}, sourceServer={}", eventType, sourceServerId);

                switch (eventType) {
                    case "user_online":
                        handleUserOnline(event);
                        break;
                    case "user_offline":
                        handleUserOffline(event);
                        break;
                    default:
                        log.warn("未知的集群事件类型: {}", eventType);
                }

            } catch (Exception e) {
                log.error("处理集群事件失败: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 消息转发监听器
     */
    private class ForwardMessageListener implements MessageListener {
        @Override
        public void onMessage(Message message, byte[] pattern) {
            if (sessionManager == null) {
                log.warn("SessionManager 还未初始化，忽略转发消息");
                return;
            }
            try {
                String body = new String(message.getBody());
                Map<String, Object> data = objectMapper.readValue(body, Map.class);

                String action = (String) data.get("action");
                if ("forward_message".equals(action)) {
                    Long targetUserId = parseLong(data.get("targetUserId"));
                    String type = (String) data.get("type");
                    @SuppressWarnings("unchecked")
                    Map<String, Object> messageData = (Map<String, Object>) data.get("data");
                    String sourceServerId = (String) data.get("sourceServerId");

                    sessionManager.handleForwardedMessage(targetUserId, type, messageData, sourceServerId);
                    log.debug("收到转发消息: targetUserId={}, type={}, sourceServer={}",
                            targetUserId, type, sourceServerId);
                }

            } catch (Exception e) {
                log.error("处理转发消息失败: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * 处理用户上线事件
     */
    private void handleUserOnline(Map<String, Object> event) {
        Long userId = parseLong(event.get("userId"));
        String sessionId = (String) event.get("sessionId");
        String serverId = (String) event.get("serverId");
        log.info("集群用户上线通知: userId={}, sessionId={}, serverId={}", userId, sessionId, serverId);
    }

    /**
     * 处理用户下线事件
     */
    private void handleUserOffline(Map<String, Object> event) {
        Long userId = parseLong(event.get("userId"));
        String sessionId = (String) event.get("sessionId");
        String serverId = (String) event.get("serverId");
        log.info("集群用户下线通知: userId={}, sessionId={}, serverId={}", userId, sessionId, serverId);
    }

    /**
     * 解析 Long 类型
     */
    private Long parseLong(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Long) return (Long) obj;
        if (obj instanceof Integer) return ((Integer) obj).longValue();
        if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
