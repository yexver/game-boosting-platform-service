package com.jmz.serverwebsocket.controller;

import com.jmz.serverwebsocket.websocket.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * WebSocket 控制器
 * 提供 WebSocket 连接状态查询和手动消息推送接口
 */
@Slf4j
@RestController
@RequestMapping("/ws")
@RequiredArgsConstructor
public class WebSocketController {

    private final WebSocketHandler webSocketHandler;

    /**
     * 查询指定用户是否在线
     */
    @GetMapping("/online/{userId}")
    public boolean isUserOnline(@PathVariable Long userId) {
        return webSocketHandler.isUserOnline(userId);
    }

    /**
     * 获取当前在线用户数
     */
    @GetMapping("/online/count")
    public int getOnlineUserCount() {
        return webSocketHandler.getOnlineUserCount();
    }

    /**
     * 推送消息给指定用户
     */
    @PostMapping("/push")
    public void pushMessage(@RequestParam Long userId,
                            @RequestParam String type,
                            @RequestBody Map<String, Object> data) {
        webSocketHandler.sendToUser(userId, type, data);
    }

    /**
     * 推送新消息通知
     */
    @PostMapping("/push/message")
    public void pushNewMessage(@RequestParam Long receiverId,
                               @RequestBody Map<String, Object> messageData) {
        webSocketHandler.notifyNewMessage(receiverId, messageData);
    }

    /**
     * 推送订单状态更新通知
     */
    @PostMapping("/push/order")
    public void pushOrderStatusUpdate(@RequestParam Long orderId,
                                      @RequestParam Long publisherId,
                                      @RequestParam(required = false) Long takerId,
                                      @RequestParam Integer status) {
        webSocketHandler.notifyOrderStatusUpdate(orderId, publisherId, takerId, status);
    }

    /**
     * 广播消息给所有在线用户
     */
    @PostMapping("/broadcast")
    public void broadcast(@RequestParam String type, @RequestBody Map<String, Object> data) {
        webSocketHandler.broadcast(type, data);
    }
}
