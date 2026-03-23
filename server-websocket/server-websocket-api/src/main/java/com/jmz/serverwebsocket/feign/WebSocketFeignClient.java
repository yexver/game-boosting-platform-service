package com.jmz.serverwebsocket.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * WebSocket 服务 Feign 客户端
 * 供其他服务（如 server-order）通过 HTTP 调用 server-websocket 的 WebSocket 推送能力
 */
@FeignClient(name = "server-websocket", path = "/ws", fallbackFactory = WebSocketFeignClientFallbackFactory.class)
public interface WebSocketFeignClient {

    /**
     * 查询指定用户是否在线
     */
    @GetMapping("/online/{userId}")
    Boolean isUserOnline(@PathVariable("userId") Long userId);

    /**
     * 获取当前在线用户数
     */
    @GetMapping("/online/count")
    Integer getOnlineUserCount();

    /**
     * 推送消息给指定用户（通用类型）
     */
    @PostMapping("/push")
    void pushMessage(@RequestParam("userId") Long userId,
                     @RequestParam("type") String type,
                     @RequestBody Map<String, Object> data);

    /**
     * 推送新消息通知给接收者
     */
    @PostMapping("/push/message")
    void pushNewMessage(@RequestParam("receiverId") Long receiverId,
                        @RequestBody Map<String, Object> messageData);

    /**
     * 推送订单状态更新通知
     */
    @PostMapping("/push/order")
    void pushOrderStatusUpdate(@RequestParam("orderId") Long orderId,
                                @RequestParam("publisherId") Long publisherId,
                                @RequestParam(value = "takerId", required = false) Long takerId,
                                @RequestParam("status") Integer status);

    /**
     * 广播消息给所有在线用户
     */
    @PostMapping("/broadcast")
    void broadcast(@RequestParam("type") String type,
                  @RequestBody Map<String, Object> data);
}
