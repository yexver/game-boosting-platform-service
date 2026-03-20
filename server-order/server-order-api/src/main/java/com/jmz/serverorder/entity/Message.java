package com.jmz.serverorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("tb_jmz_messages")
public class Message {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long senderId;
    private Long receiverId;
    private Integer senderType;
    private Integer messageType;
    private String content;
    private String fileUrl;
    private String jumpUrl;
    private Integer isRead;
    private Date createdAt;
} 