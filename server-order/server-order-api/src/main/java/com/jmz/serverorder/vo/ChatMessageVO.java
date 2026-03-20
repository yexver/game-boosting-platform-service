package com.jmz.serverorder.vo;

import lombok.Data;
import java.util.Date;

@Data
public class ChatMessageVO {
    private Long id;
    private String content;
    private Long senderId;
    private Long receiverId;
    private Long orderId;
    private Integer messageType;
    private String fileUrl;
    private String jumpUrl;
    private Date createdAt;
} 