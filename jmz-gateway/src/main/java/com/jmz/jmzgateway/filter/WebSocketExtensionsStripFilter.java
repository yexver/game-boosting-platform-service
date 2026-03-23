package com.jmz.jmzgateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 在 WebSocket 升级请求中移除 {@code Sec-WebSocket-Extensions} 头。
 * <p>
 * Spring Cloud Gateway 对 WebSocket 采用双跳代理：客户端 → 网关 → 后端。
 * permessage-deflate 压缩协商在两段链路上无法保持一致，常表现为：浏览器刚收到
 * {@code 101 Switching Protocols}，下一秒就收到关闭码 {@code 1002}（协议错误）。
 * 去掉 {@code Sec-WebSocket-Extensions} 可避免压缩帧在链路上出现，从而消除此问题。
 * <p>
 * 注意：网关的 {@code WebsocketRoutingFilter} 向转发端发送握手时会过滤掉所有 {@code sec-websocket*} 头，
 * 若浏览器与网关入站仍协商了 permessage-deflate，而后端未协商，仍会 1002，故必须在入站请求上尽早去掉扩展头。
 */
@Component
public class WebSocketExtensionsStripFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().pathWithinApplication().value();
        if (isWebSocketUpgradePath(path)) {
            ServerHttpRequest mutated = request.mutate()
                    .headers(h -> h.remove("Sec-WebSocket-Extensions"))
                    .build();
            return chain.filter(exchange.mutate().request(mutated).build());
        }
        return chain.filter(exchange);
    }

    private static boolean isWebSocketUpgradePath(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        return "/ws".equals(path) || path.startsWith("/ws/");
    }

    @Override
    public int getOrder() {
        // 必须在 WebsocketRoutingFilter（LOWEST_PRECEDENCE - 1）之前执行，且尽量靠前，避免其它逻辑缓存原始头
        return Ordered.HIGHEST_PRECEDENCE + 50;
    }
}
