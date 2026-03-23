package com.jmz.serverwebsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Server-WebSocket 应用程序启动类
 */
@SpringBootApplication(scanBasePackages = {
        "com.jmz.serverwebsocket",
        "com.jmz.jmzcommoncore",
        "com.jmz.jmzcommonsecurity"
})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.jmz.serveruser.feign", "com.jmz.serveraccount.feign"})
public class ServerWebSocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerWebSocketApplication.class, args);
    }
}
