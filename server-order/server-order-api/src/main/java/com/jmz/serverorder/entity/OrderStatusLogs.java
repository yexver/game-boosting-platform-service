package com.jmz.serverorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tb_jmz_order_status_logs")
public class OrderStatusLogs {
    @TableId(value = "id", type = IdType.AUTO)
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
} 