package com.jmz.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 接单推荐结果VO
 */
@Data
public class TakeRecommendVO {

    /**
     * 推荐订单列表
     */
    private List<RecommendedOrderVO> recommendedOrders;

    /**
     * 总收益预估
     */
    private BigDecimal totalEstimatedIncome;

    /**
     * 风险分析
     */
    private String riskAnalysis;

    /**
     * 收益策略建议
     */
    private String strategyAdvice;

    /**
     * 警告信息列表
     */
    private List<String> warnings;
}
