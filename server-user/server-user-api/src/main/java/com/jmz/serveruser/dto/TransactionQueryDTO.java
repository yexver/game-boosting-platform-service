package com.jmz.serveruser.dto;

import lombok.Data;
import java.util.Date;

@Data
public class TransactionQueryDTO {
    private Integer current = 1;
    private Integer size = 10;
    private Long userId;
    private Long operatorId;
    private Integer type; // 交易类型
    private Date startTime; // 开始时间
    private Date endTime;   // 结束时间
} 