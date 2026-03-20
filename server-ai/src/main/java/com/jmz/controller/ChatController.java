package com.jmz.controller;

import com.jmz.responseResult.R;
import com.jmz.service.AliCloudAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private AliCloudAIService aliCloudAIService;

    /**
     * 单轮对话接口
     * @param request 包含用户消息的请求
     * @return AI回复
     */
    @PostMapping
    public R chat(@RequestBody Map<String, String> request) {
        try {
            String message = request.get("message");

            if (message == null || message.trim().isEmpty()) {
                return R.error("消息内容不能为空");
            }

            String response = aliCloudAIService.askQuestion(message.trim());
            return R.success(response);

        } catch (Exception e) {
            System.err.println("聊天接口异常: " + e.getMessage());
            e.printStackTrace();
            return R.error("聊天服务异常，请稍后再试");
        }
    }

    /**
     * 多轮对话接口（支持上下文）
     * @param request 包含对话历史的请求
     * @return AI回复
     */
    @PostMapping("/context")
    public R chatWithContext(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> messages = (List<Map<String, String>>) request.get("messages");

            if (messages == null || messages.isEmpty()) {
                return R.error("对话历史不能为空");
            }

            String response = aliCloudAIService.chatWithContext(messages);
            return R.success(response);

        } catch (Exception e) {
            System.err.println("多轮对话接口异常: " + e.getMessage());
            e.printStackTrace();
            return R.error("聊天服务异常，请稍后再试");
        }
    }

    /**
     * 获取AI模型信息
     * @return 模型信息
     */
    @GetMapping("/info")
    public R getModelInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("model", "通义千问-Turbo");
        info.put("provider", "阿里云百炼");
        info.put("version", "v1.0");
        info.put("capabilities", List.of("文本生成", "问答对话", "多轮对话", "创意写作"));
        return R.success(info);
    }
}