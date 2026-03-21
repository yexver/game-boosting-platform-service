package com.jmz.serveraccount.dto;

import lombok.Data;
import java.util.Date;

@Data
public class TransactionQueryDTO {

    private Integer current = 1;

    private Integer size = 10;

    private Long userId;

    private Long operatorId;

    private Integer type;

    private Date startTime;

    private Date endTime;
}
