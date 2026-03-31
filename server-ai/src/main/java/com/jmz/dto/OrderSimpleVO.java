package com.jmz.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单简要信息（用于推荐匹配）
 */
@Data
public class OrderSimpleVO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 游戏ID
     */
    private Integer gameId;

    /**
     * 订单标题
     */
    private String title;

    /**
     * 当前段位
     */
    private String currentRank;

    /**
     * 目标段位
     */
    private String targetRank;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 时限（小时）
     */
    private Integer timeLimit;

    /**
     * 代练类型：1-代练，2-陪练
     */
    private Integer boostingType;

    /**
     * 订单状态
     */
    private Integer status;
}
