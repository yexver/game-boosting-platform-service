package com.jmz.serveruser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {
    @Bean
    public MySqlInjector sqlInjector() {
        return new MySqlInjector();
    }
}