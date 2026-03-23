package com.jmz.serverwebsocket.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmz.serverwebsocket.listener.WebSocketRedisListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.net.InetAddress;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

/**
 * WebSocket 会话管理器
 * <p>
 * 职责：
 * 1. 管理内存中的 WebSocketSession（实际消息发送）
 * 2. 同步会话元数据到 Redis（跨服务路由索引）
 * 3. 处理集群环境下的会话事件通知
 */
@Slf4j
@Component
@EnableScheduling
public class WebSocketSessionManager {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebSocketRedisListener redisListener;

    // ==================== Redis Key 常量 ====================

    /** 用户会话 Hash: websocket:session:user:{userId} */
    private static final String KEY_USER_SESSION = "websocket:session:user:";

    /** 会话ID → 服务ID 映射: websocket:session:server:{sessionId} */
    private static final String KEY_SESSION_SERVER = "websocket:session:server:";

    /** 全局活跃会话ID集合: websocket:sessions:active */
    private static final String KEY_ACTIVE_SESSIONS = "websocket:sessions:active";

    /** 集群事件发布频道: websocket:events */
    private static final String CHANNEL_EVENTS = "websocket:events";

    // ==================== 配置 ====================

    /** 会话超时时间（秒），默认 30 分钟 */
    @Value("${websocket.session.timeout:1800}")
    private long sessionTimeoutSeconds;

    /** 心跳间隔（毫秒），默认 25 秒 */
    @Value("${websocket.heartbeat.interval:25000}")
    private long heartbeatIntervalMs;

    /** 服务实例标识 */
    private String serverId;

    // ==================== 内存存储 ====================

    /** userId → WebSocketSession 映射 */
    private final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    /** sessionId → WebSocketSession 映射（用于快速通过 sessionId 查找） */
    private final Map<String, WebSocketSession> sessionIdMap = new ConcurrentHashMap<>();

    /** 所有活跃会话集合 */
    private final Set<WebSocketSession> allSessions = new CopyOnWriteArraySet<>();

    /** 本服务实例的会话ID集合（用于服务重启时清理） */
    private final Set<String> localSessionIds = new CopyOnWriteArraySet<>();

    // ==================== 初始化 ====================

    @PostConstruct
    public void init() {
        // 生成服务实例唯一标识
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String processId = String.valueOf(ProcessHandle.current().pid());
            serverId = hostAddress + ":" + processId;
        } catch (Exception e) {
            serverId = UUID.randomUUID().toString();
            log.warn("获取本机地址失败，使用随机ID: {}", serverId);
        }

        log.info("WebSocketSessionManager 初始化完成，服务实例ID: {}", serverId);

        // 启动时清理旧的本服务会话记录（防止上次异常关闭残留）
        cleanupOrphanedSessions();
    }

    // ==================== 核心操作：连接管理 ====================

    /**
     * 注册新连接
     */
    public void registerSession(Long userId, WebSocketSession session) {
        String sessionId = session.getId();

        // 1. 内存存储
        userSessions.put(userId, session);
        sessionIdMap.put(sessionId, session);
        allSessions.add(session);
        localSessionIds.add(sessionId);

        // 2. Redis 存储会话元数据
        saveSessionToRedis(userId, sessionId);

        // 3. 记录活跃会话
        stringRedisTemplate.opsForSet().add(KEY_ACTIVE_SESSIONS, sessionId);

        log.info("WebSocket 会话注册: userId={}, sessionId={}, serverId={}",
                userId, sessionId, serverId);

        // 4. 发布上线事件（通知集群）
        publishSessionEvent("user_online", userId, sessionId);
    }

    /**
     * 移除连接
     */
    public void removeSession(WebSocketSession session) {
        String sessionId = session.getId();
        Long userId = getUserIdFromSession(session);

        // 1. 内存移除
        if (userId != null) {
            userSessions.remove(userId);
        }
        sessionIdMap.remove(sessionId);
        allSessions.remove(session);
        localSessionIds.remove(sessionId);

        // 2. Redis 清理
        if (userId != null) {
            stringRedisTemplate.delete(KEY_USER_SESSION + userId);
        }
        stringRedisTemplate.delete(KEY_SESSION_SERVER + sessionId);
        stringRedisTemplate.opsForSet().remove(KEY_ACTIVE_SESSIONS, sessionId);

        log.info("WebSocket 会话移除: userId={}, sessionId={}", userId, sessionId);

        // 3. 发布下线事件
        if (userId != null) {
            publishSessionEvent("user_offline", userId, sessionId);
        }
    }

    // ==================== 核心操作：消息发送 ====================

    /**
     * 向指定用户发送消息
     *
     * @param userId 用户ID
     * @param message 消息内容（Map，会自动添加 type 字段）
     */
    public void sendToUser(Long userId, String type, Map<String, Object> message) {
        // 1. 先检查本地是否有该用户的会话
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            sendViaSession(session, type, message);
            return;
        }

        // 2. 本地没有，查询 Redis 查找用户所在的服务器
        String targetServerId = getServerIdByUserId(userId);
        if (targetServerId != null && !targetServerId.equals(serverId)) {
            // 用户在其他服务器，通过集群事件转发
            forwardToServer(userId, targetServerId, type, message);
        } else {
            log.debug("用户不在线或连接已关闭: userId={}", userId);
        }
    }

    /**
     * 广播消息给所有在线用户
     */
    public void broadcast(String type, Map<String, Object> message) {
        // 先本地广播
        List<WebSocketSession> closedSessions = new ArrayList<>();

        for (WebSocketSession session : allSessions) {
            if (session.isOpen()) {
                sendViaSession(session, type, message);
            } else {
                closedSessions.add(session);
            }
        }

        // 清理已关闭的会话
        closedSessions.forEach(this::removeSession);

        log.debug("广播消息完成，本地在线用户数: {}", allSessions.size());
    }

    /**
     * 向指定会话发送消息
     */
    private void sendViaSession(WebSocketSession session, String type, Map<String, Object> data) {
        try {
            Map<String, Object> message = new HashMap<>(data);
            message.put("type", type);
            session.sendMessage(new org.springframework.web.socket.TextMessage(
                    objectMapper.writeValueAsString(message)));
            log.debug("发送消息成功: userId={}, type={}", getUserIdFromSession(session), type);
        } catch (Exception e) {
            log.error("发送消息失败: sessionId={}, error={}", session.getId(), e.getMessage());
            removeSession(session);
        }
    }

    // ==================== 核心操作：会话查询 ====================

    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(Long userId) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            return true;
        }
        // 本地没有，查询 Redis
        return checkUserOnlineInRedis(userId);
    }

    /**
     * 获取在线用户数
     */
    public int getOnlineUserCount() {
        return userSessions.size();
    }

    /**
     * 获取指定用户的会话
     */
    public Optional<WebSocketSession> getSession(Long userId) {
        return Optional.ofNullable(userSessions.get(userId));
    }

    /**
     * 获取本地所有会话
     */
    public Collection<WebSocketSession> getAllLocalSessions() {
        return new ArrayList<>(allSessions);
    }

    /**
     * 获取当前服务ID
     */
    public String getServerId() {
        return serverId;
    }

    // ==================== Redis 辅助方法 ====================

    /**
     * 保存会话元数据到 Redis
     */
    private void saveSessionToRedis(Long userId, String sessionId) {
        try {
            Map<String, String> sessionData = new HashMap<>();
            sessionData.put("sessionId", sessionId);
            sessionData.put("serverId", serverId);
            sessionData.put("userId", String.valueOf(userId));
            sessionData.put("connectedAt", Instant.now().toString());
            sessionData.put("lastHeartbeat", Instant.now().toString());

            // 使用 Hash 存储用户会话信息
            String userKey = KEY_USER_SESSION + userId;
            redisTemplate.opsForHash().putAll(userKey, sessionData);
            redisTemplate.expire(userKey, sessionTimeoutSeconds, TimeUnit.SECONDS);

            // sessionId → serverId 映射
            String serverKey = KEY_SESSION_SERVER + sessionId;
            redisTemplate.opsForValue().set(serverKey, serverId, sessionTimeoutSeconds, TimeUnit.SECONDS);

            log.debug("会话元数据已保存到 Redis: userId={}, sessionId={}", userId, sessionId);
        } catch (Exception e) {
            log.error("保存会话到 Redis 失败: userId={}, error={}", userId, e.getMessage());
        }
    }

    /**
     * 更新心跳时间
     */
    public void updateHeartbeat(Long userId) {
        String userKey = KEY_USER_SESSION + userId;
        redisTemplate.opsForHash().put(userKey, "lastHeartbeat", Instant.now().toString());
        // 续期 TTL
        redisTemplate.expire(userKey, sessionTimeoutSeconds, TimeUnit.SECONDS);

        String sessionId = userSessions.get(userId) != null ?
                userSessions.get(userId).getId() : null;
        if (sessionId != null) {
            String serverKey = KEY_SESSION_SERVER + sessionId;
            redisTemplate.expire(serverKey, sessionTimeoutSeconds, TimeUnit.SECONDS);
        }

        log.debug("心跳更新: userId={}", userId);
    }

    /**
     * 根据 userId 获取服务ID
     */
    private String getServerIdByUserId(Long userId) {
        String userKey = KEY_USER_SESSION + userId;
        Object serverId = redisTemplate.opsForHash().get(userKey, "serverId");
        return serverId != null ? serverId.toString() : null;
    }

    /**
     * 检查用户在 Redis 中是否在线
     */
    private boolean checkUserOnlineInRedis(Long userId) {
        String userKey = KEY_USER_SESSION + userId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(userKey));
    }

    // ==================== 集群通信 ====================

    /**
     * 发布会话事件（用户上下线）
     */
    private void publishSessionEvent(String eventType, Long userId, String sessionId) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("type", eventType);
            event.put("userId", userId);
            event.put("sessionId", sessionId);
            event.put("serverId", serverId);
            event.put("timestamp", System.currentTimeMillis());

            String message = objectMapper.writeValueAsString(event);
            stringRedisTemplate.convertAndSend(CHANNEL_EVENTS, message);

            log.debug("发布集群事件: eventType={}, userId={}", eventType, userId);
        } catch (JsonProcessingException e) {
            log.error("发布集群事件失败: {}", e.getMessage());
        }
    }

    /**
     * 转发消息到指定服务器
     */
    private void forwardToServer(Long userId, String targetServerId, String type, Map<String, Object> message) {
        try {
            Map<String, Object> forwardMsg = new HashMap<>();
            forwardMsg.put("action", "forward_message");
            forwardMsg.put("targetUserId", userId);
            forwardMsg.put("sourceServerId", serverId);
            forwardMsg.put("type", type);
            forwardMsg.put("data", message);
            forwardMsg.put("timestamp", System.currentTimeMillis());

            String msgStr = objectMapper.writeValueAsString(forwardMsg);
            // 使用 serverId 作为 channel 定向发送
            stringRedisTemplate.convertAndSend("websocket:forward:" + targetServerId, msgStr);

            log.debug("消息已转发到服务器: targetServerId={}, userId={}", targetServerId, userId);
        } catch (Exception e) {
            log.error("转发消息失败: userId={}, error={}", userId, e.getMessage());
        }
    }

    // ==================== 集群消息转发处理 ====================

    /**
     * 处理从其他服务转发过来的消息
     */
    public void handleForwardedMessage(Long targetUserId, String type, Map<String, Object> data, String sourceServerId) {
        log.info("收到转发消息: targetUserId={}, type={}, sourceServer={}", targetUserId, type, sourceServerId);

        // 查找目标用户的本地会话并发送消息
        WebSocketSession session = userSessions.get(targetUserId);
        if (session != null && session.isOpen()) {
            sendViaSession(session, type, data);
        } else {
            log.warn("转发消息的目标用户不在本机: userId={}", targetUserId);
        }
    }

    // ==================== 定时任务 ====================

    /**
     * 定时清理孤儿会话（服务异常退出后残留的会话记录）
     * 每 5 分钟执行一次
     */
    @Scheduled(fixedRate = 300000)
    public void cleanupOrphanedSessions() {
        try {
            // 1. 清理已过期的用户会话记录
            Set<String> activeSessionIds = stringRedisTemplate.opsForSet().members(KEY_ACTIVE_SESSIONS);
            if (activeSessionIds == null || activeSessionIds.isEmpty()) {
                return;
            }

            List<String> expiredSessions = new ArrayList<>();
            for (String sessionId : activeSessionIds) {
                String serverKey = KEY_SESSION_SERVER + sessionId;
                if (!Boolean.TRUE.equals(stringRedisTemplate.hasKey(serverKey))) {
                    expiredSessions.add(sessionId);
                }
            }

            if (!expiredSessions.isEmpty()) {
                stringRedisTemplate.opsForSet().remove(KEY_ACTIVE_SESSIONS,
                        expiredSessions.toArray());
                log.info("清理孤儿会话记录: {} 个", expiredSessions.size());
            }

            // 2. 清理本服务的孤立会话（本地已关闭但 Redis 仍有记录的）
            for (String sessionId : localSessionIds) {
                if (!sessionIdMap.containsKey(sessionId)) {
                    stringRedisTemplate.delete(KEY_SESSION_SERVER + sessionId);
                    localSessionIds.remove(sessionId);
                }
            }
        } catch (Exception e) {
            log.error("清理孤儿会话失败: {}", e.getMessage());
        }
    }

    /**
     * 定时检查并移除本地已断开的会话
     * 每 10 秒执行一次
     */
    @Scheduled(fixedRate = 10000)
    public void cleanupDeadSessions() {
        List<WebSocketSession> deadSessions = new ArrayList<>();

        for (WebSocketSession session : allSessions) {
            if (!session.isOpen()) {
                deadSessions.add(session);
            }
        }

        for (WebSocketSession session : deadSessions) {
            removeSession(session);
            log.debug("清理已断开会话: sessionId={}", session.getId());
        }
    }

    // ==================== 工具方法 ====================

    /**
     * 从 session 中获取 userId
     */
    public Long getUserIdFromSession(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        Object userId = attributes.get("userId");
        return parseUserId(userId);
    }

    /**
     * 解析 userId（支持多种类型）
     */
    public Long parseUserId(Object obj) {
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
