package com.jmz.controller;

import com.jmz.responseResult.R;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class AIController {

    private final ChatClient chatClient;


/*    public AIController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }*/

    // 注入 ChatClient.Builder 和 ChatMemory
    public AIController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new PromptChatMemoryAdvisor(chatMemory)) // 添加 ChatMemoryAdvisor
                .build();
    }


    @GetMapping("/chat")
    public R chat(@RequestParam("message") String message) {
        System.out.println("Received message: " + message);
        return R.success(chatClient.prompt()
                .system("你是一个游戏代练平台（代练侠）的智能客服助手，专注于解答用户关于游戏代练订单的发单，接单，问题订单如何处理等问答。")
                .user(message)
                .call()
                .content());
    }

}