package com.jmz.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 推荐订单项VO
 */
@Data
public class RecommendedOrderVO {

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
     * 匹配度评分（0-100）
     */
    private Integer matchScore;

    /**
     * 推荐理由
     */
    private String reason;

    /**
     * 收益分析
     */
    private String profitAnalysis;

    /**
     * 风险提示
     */
    private String riskWarning;

    /**
     * 跳转链接
     */
    private String jumpUrl;
}
