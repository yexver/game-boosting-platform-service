package com.jmz.serveruser.dto;

import lombok.Data;

@Data
public class UserQueryDTO {
    private Long current;      // 当前页码
    private Long size;         // 每页大小
    private String userId;     // 用户ID
    private String username;   // 用户名
    private String phone;      // 手机号
    private Integer status;    // 状态
    private String beginCreateTime; // 开始时间
    private String endCreateTime;   // 结束时间
}
