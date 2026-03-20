package com.jmz.serverorder.vo;

import lombok.Data;
import java.util.Map;

@Data
public class UnreadCountVO {
    private Integer totalUnread;
    private Map<String, Integer> unreadByUser;
} 