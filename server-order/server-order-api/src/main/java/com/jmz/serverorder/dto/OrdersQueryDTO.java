package com.jmz.serverorder.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrdersQueryDTO {
    private String title; // 订单标题、订单号或发单人模糊搜索
    private Integer gameId;
    private Integer systemId;
    private Integer serverId;
    private Integer status;// 订单状态
    private Long managerId;// 客服ID
    private Integer type; // 代练类型
    private Double priceMin;
    private Double priceMax;
    private Integer sort; // 排序方式
    private Date startAt;
    private Date actualAt;
    private Date createdAt;
    private Date updatedAt;
    //0	默认（按创建时间倒序）
    //1	代练价从高到低
    //2	代练价从低到高
    //3	安全保证金从低到高
    //4	安全保证金从高到低
    //5	效率保证金从低到高
    //6	效率保证金从高到低
    private Long publisherId; // 发布者ID
    private Long takerId; // 接单者ID
    private Integer page = 1;
    private Integer pageSize = 10;
    private String beginCreateTime; // 下单开始时间（格式：yyyy-MM-dd）
    private String endCreateTime;   // 下单结束时间（格式：yyyy-MM-dd）
    private String orderNo;         // 订单号（精确查询）
    private String minAmount;       // 最小金额
    private String maxAmount;      // 最大金额
} 