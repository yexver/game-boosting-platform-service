package com.jmz.serveraccount;

import com.jmz.serveraccount.config.FeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.jmz.jmzfile.feign", "com.jmz.serveruser.feign"}, defaultConfiguration = FeignConfig.class)
@MapperScan("com.jmz.serveraccount.mapper")
public class ServerAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerAccountApplication.class, args);
    }
}
