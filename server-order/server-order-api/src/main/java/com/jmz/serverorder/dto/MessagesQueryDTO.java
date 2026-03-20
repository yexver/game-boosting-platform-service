package com.jmz.serverorder.dto;

import lombok.Data;

@Data
public class MessagesQueryDTO {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Integer type; // 消息类型筛选，可选 (1-文本，2-图片，3-文件，4-系统通知)
} 