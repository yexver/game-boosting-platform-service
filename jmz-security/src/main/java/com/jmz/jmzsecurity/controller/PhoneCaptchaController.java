package com.jmz.jmzsecurity.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jmz.jmzsecurity.util.HttpUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import com.jmz.jmzsecurity.constants.RedisStorageConstants;
import com.jmz.jmzcommonredis.utils.RedisUtils;
import com.jmz.jmzcommoncore.responseResult.R;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PhoneCaptchaController {
    @Value("${aliyun.sms.host}")
    private String smsHost;
    @Value("${aliyun.sms.path}")
    private String smsPath;
    @Value("${aliyun.sms.method}")
    private String smsMethod;
    @Value("${aliyun.sms.appcode}")
    private String smsAppcode;
    @Value("${aliyun.sms.template_id}")
    private String smsTemplateId;

    @PostMapping("/sendSms")
    public R sendSms(@RequestParam String phone) {
        // 生成4位随机验证码
        String code = String.valueOf(1000 + new Random().nextInt(9000));
        // 存入Redis，有效期5分钟
        String redisKey = RedisStorageConstants.PHONE_CAPTCHA_CODE_KEY + phone;
        RedisUtils.set(redisKey, code, 300);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + smsAppcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("content", "code:" + code);
        bodys.put("template_id", smsTemplateId);
        bodys.put("phone_number", phone);
        try {
            HttpResponse response = HttpUtils.doPost(smsHost, smsPath, smsMethod, headers, querys, bodys);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result);
            String status = jsonNode.has("status") ? jsonNode.get("status").asText() : null;
            Map<String, Object> data = new HashMap<>();
            data.put("code", code);
            data.put("result", result);
            System.out.println("短信发送结果: " + status);
            if ("OK".equals(status)) {
                return R.success("短信发送成功", status);
            } else {
                String errorMsg = jsonNode.has("message") ? jsonNode.get("message").asText() : result;
                return R.error("短信发送失败: " + errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("短信发送失败: " + e.getMessage());
        }
    }
}
