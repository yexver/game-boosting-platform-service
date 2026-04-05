package com.jmz.serveraccount.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;

    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(GATEWAY_URL, appId,
                appPrivateKey, FORMAT, CHARSET, alipayPublicKey, SIGN_TYPE);
    }
}
