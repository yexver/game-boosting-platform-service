package com.jmz.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阿里云百炼大模型服务
 * 通义千问API调用服务
 */
@Service
public class AliCloudAIService {

    private static final String API_KEY = "sk-511158a3874a4ad09da4b6ec85a01ef8";
    private static final String BASE_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AliCloudAIService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 调用通义千问大模型
     * @param userMessage 用户输入的消息
     * @return AI回复的消息
     */
    public String askQuestion(String userMessage) {
        try {
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + API_KEY);

            // 构建请求体
            Map<String, Object> requestBody = buildRequestBody(userMessage);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 发送请求
            ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return parseResponse(response.getBody());
            } else {
                return "抱歉，AI服务暂时不可用，请稍后再试。状态码：" + response.getStatusCode();
            }

        } catch (Exception e) {
            System.err.println("调用阿里云百炼API失败: " + e.getMessage());
            e.printStackTrace();
            return "抱歉，AI服务出现异常，请稍后再试。错误信息：" + e.getMessage();
        }
    }

    /**
     * 构建请求体
     */
    private Map<String, Object> buildRequestBody(String userMessage) {
        Map<String, Object> requestBody = new HashMap<>();
        
        // 模型参数
        requestBody.put("model", "qwen-turbo");
        
        // 输入参数
        Map<String, Object> input = new HashMap<>();
        
        // 消息列表
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", userMessage);
        
        input.put("messages", List.of(message));
        requestBody.put("input", input);
        
        // 参数设置
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("result_format", "message");
        parameters.put("max_tokens", 1500);
        parameters.put("temperature", 0.8);
        parameters.put("top_p", 0.8);
        requestBody.put("parameters", parameters);

        return requestBody;
    }

    /**
     * 解析API响应
     */
    private String parseResponse(String responseBody) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            
            // 检查是否有错误
            if (rootNode.has("code")) {
                String errorCode = rootNode.get("code").asText();
                String errorMessage = rootNode.has("message") ? rootNode.get("message").asText() : "未知错误";
                return "AI服务错误 [" + errorCode + "]: " + errorMessage;
            }
            
            // 解析正常响应
            JsonNode output = rootNode.get("output");
            if (output != null && output.has("choices")) {
                JsonNode choices = output.get("choices");
                if (choices.isArray() && choices.size() > 0) {
                    JsonNode firstChoice = choices.get(0);
                    JsonNode message = firstChoice.get("message");
                    if (message != null && message.has("content")) {
                        return message.get("content").asText();
                    }
                }
            }
            
            return "抱歉，无法解析AI回复，请稍后再试。";
            
        } catch (Exception e) {
            System.err.println("解析响应失败: " + e.getMessage());
            return "抱歉，解析AI回复时出现错误。";
        }
    }

    /**
     * 带上下文的对话（支持多轮对话）
     * @param messages 对话历史
     * @return AI回复
     */
    public String chatWithContext(List<Map<String, String>> messages) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + API_KEY);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "qwen-turbo");
            
            Map<String, Object> input = new HashMap<>();
            input.put("messages", messages);
            requestBody.put("input", input);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("result_format", "message");
            parameters.put("max_tokens", 1500);
            parameters.put("temperature", 0.8);
            parameters.put("top_p", 0.8);
            requestBody.put("parameters", parameters);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return parseResponse(response.getBody());
            } else {
                return "抱歉，AI服务暂时不可用。";
            }

        } catch (Exception e) {
            System.err.println("调用阿里云百炼API失败: " + e.getMessage());
            return "抱歉，AI服务出现异常。";
        }
    }
}
