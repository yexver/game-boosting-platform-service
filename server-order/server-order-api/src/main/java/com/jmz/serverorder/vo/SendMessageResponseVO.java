package com.jmz.serverorder.vo;

import lombok.Data;
import java.util.Date;

@Data
public class SendMessageResponseVO {
    private Long id;
    private String content;
    private Long senderId;
    private Long receiverId;
    private Integer messageType;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    private Date createdAt;
} 