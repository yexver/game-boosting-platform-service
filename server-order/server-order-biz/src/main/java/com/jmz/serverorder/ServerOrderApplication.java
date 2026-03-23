package com.jmz.serverorder;

import com.jmz.serverorder.config.FeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Server-Order 应用程序启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.jmz.jmzfile.feign", "com.jmz.serveruser.feign", "com.jmz.serveraccount.feign", "com.jmz.serverwebsocket.feign"}, defaultConfiguration = FeignConfig.class)
@MapperScan("com.jmz.serverorder.mapper")
public class ServerOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerOrderApplication.class, args);
    }
}