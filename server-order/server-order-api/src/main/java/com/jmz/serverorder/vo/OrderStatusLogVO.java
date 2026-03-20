package com.jmz.serverorder.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderStatusLogVO {
    private Long id;
    private Long orderId;
    private Integer fromStatus;
    private Integer toStatus;
    private Long operatorId;
    private Integer operatorType;
    private String remark;
    private String imageUrls;
    private Date createdAt;
    private BigDecimal price;
    private BigDecimal deposit;
    
    // 操作者信息
    private String operatorName;
    private String operatorAvatar;
} 