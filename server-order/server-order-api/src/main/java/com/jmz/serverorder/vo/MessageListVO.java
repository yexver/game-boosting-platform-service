package com.jmz.serverorder.vo;

import lombok.Data;
import java.util.Date;

@Data
public class MessageListVO {
    private Long id;
    private Long senderId;
    private Integer senderType;
    private String senderUsername;
    private String senderNickname;
    private String senderAvatar;
    private String content;
    private Integer messageType;
    private Boolean isRead;
    private Date createdAt;
    private Long orderId;
} 