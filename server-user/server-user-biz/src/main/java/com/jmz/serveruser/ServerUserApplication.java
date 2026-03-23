package com.jmz.serveruser;

import com.jmz.serveruser.config.FeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.jmz.jmzfile.feign", "com.jmz.serveraccount.feign"}, defaultConfiguration = FeignConfig.class)
@MapperScan("com.jmz.serveruser.mapper")
public class ServerUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerUserApplication.class, args);
    }
}
