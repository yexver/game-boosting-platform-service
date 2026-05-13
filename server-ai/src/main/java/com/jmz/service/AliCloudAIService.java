package com.jmz.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阿里云百炼大模型服务
 * 通义千问API调用服务
 */
@Slf4j
@Service
public class AliCloudAIService {

    private static final String BASE_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    @Value("${spring.ai.dashscope.api-key:sk-511158a3874a4ad09da4b6ec85a01ef8}")
    private String apiKey;

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
            headers.set("Authorization", "Bearer " + apiKey);

            // 构建请求体
            Map<String, Object> requestBody = buildRequestBody(userMessage);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 发送请求
            ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return parseResponse(response.getBody());
            } else {
                log.error("AI服务调用失败: {}", response.getStatusCode());
                return "抱歉，AI服务暂时不可用，请稍后再试。";
            }

        } catch (Exception e) {
            log.error("调用阿里云百炼API失败: {}", e.getMessage());
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

        // 消息列表 - 添加系统提示词
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", buildSystemPrompt());

        Map<String, String> userMessageMap = new HashMap<>();
        userMessageMap.put("role", "user");
        userMessageMap.put("content", userMessage);

        input.put("messages", List.of(systemMessage, userMessageMap));
        requestBody.put("input", input);

        // 参数设置
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("result_format", "message");
        parameters.put("max_tokens", 1500);
        parameters.put("temperature", 0.7);
        parameters.put("top_p", 0.8);
        requestBody.put("parameters", parameters);

        return requestBody;
    }

    /**
     * 构建系统提示词
     */
    private String buildSystemPrompt() {
        return """
                你是一个游戏代练平台（代练侠）的智能客服助手，专注于解答用户关于游戏代练订单的问题。

                平台功能说明：
                1. 发单：用户可以发布代练订单，设置代练要求、价格、保证金等
                2. 接单：代练人员可以接取订单
                3. 订单状态：待接单 -> 代练中 -> 待验收 -> 已完成
                4. 撤销流程：发单者或接单者可以申请撤销
                5. 客服介入：当双方有争议时可以申请客服介入

                订单状态码说明：
                - 1: 待接单
                - 2: 代练中
                - 3: 待验收
                - 4: 验收中
                - 5: 已完成
                - 6: 已撤销
                - 7: 撤销中
                - 8: 待介入
                - 9: 介入中
                - 10: 已仲裁

                内容安全规则（严格遵守）：
                - 禁止回复涉及政治敏感话题的内容
                - 禁止回复涉及色情、暴力、血腥内容
                - 禁止回复涉及赌博、诈骗等违法内容
                - 禁止回复涉及人身攻击、歧视性言论
                - 如用户输入包含上述违规内容，请礼貌拒绝并引导用户提问与代练相关的问题

                请用简洁、友好的语言回答用户的问题。如果遇到无法解答的问题，请引导用户联系人工客服。
                """;
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
                log.error("AI返回错误: {} - {}", errorCode, errorMessage);
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

            // 尝试其他格式
            if (output != null && output.has("text")) {
                return output.get("text").asText();
            }

            return "抱歉，无法解析AI回复，请稍后再试。";

        } catch (Exception e) {
            log.error("解析响应失败: {}", e.getMessage());
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
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "qwen-turbo");

            Map<String, Object> input = new HashMap<>();

            // 添加系统提示词
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", buildSystemPrompt());

            // 构建消息列表
            java.util.ArrayList<Map<String, String>> allMessages = new ArrayList<>();
            allMessages.add(systemMessage);
            allMessages.addAll(messages);

            input.put("messages", allMessages);
            requestBody.put("input", input);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("result_format", "message");
            parameters.put("max_tokens", 1500);
            parameters.put("temperature", 0.7);
            parameters.put("top_p", 0.8);
            requestBody.put("parameters", parameters);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return parseResponse(response.getBody());
            } else {
                log.error("AI多轮对话调用失败: {}", response.getStatusCode());
                return "抱歉，AI服务暂时不可用。";
            }

        } catch (Exception e) {
            log.error("调用阿里云百炼API失败: {}", e.getMessage());
            return "抱歉，AI服务出现异常。";
        }
    }

    /**
     * 流式对话（返回完整文本，适合前端流式展示）
     */
    public String chatStream(String userMessage) {
        // 流式接口返回完整内容，前端可自行处理流式展示
        return askQuestion(userMessage);
    }
}
