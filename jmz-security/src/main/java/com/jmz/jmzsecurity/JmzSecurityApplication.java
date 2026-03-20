package com.jmz.jmzsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.jmz.serveruser.feign"})
public class JmzSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(JmzSecurityApplication.class, args);

    }

}
