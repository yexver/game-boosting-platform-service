package com.jmz.serverorder.dto;

import lombok.Data;

@Data
public class SendMessageDTO {
    private Long receiverId; // 接收者用户ID (必填)
    private String content; // 消息内容 (可选，图片消息可为空)
    private Integer senderType; // 送者类型
    private Integer messageType; // 消息类型 (必填，1-文本，2-图片，3-文件)
    private Long orderId; // 关联订单ID (可选)
    private String imageUrls; // 多图片URL，多个用逗号分隔 (可选)
} 