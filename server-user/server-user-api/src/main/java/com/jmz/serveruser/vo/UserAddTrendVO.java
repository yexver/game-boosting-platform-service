package com.jmz.serveruser.vo;

import lombok.Data;
 
@Data
public class UserAddTrendVO {
    private String date; // 日期，格式如 yyyy-MM-dd
    private Integer count; // 新增用户数
} 