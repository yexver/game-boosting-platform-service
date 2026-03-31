package com.jmz.vo;

import lombok.Data;

import java.util.List;

/**
 * 发单推荐结果VO
 */
@Data
public class PostRecommendVO {

    /**
     * 价格建议
     */
    private PriceRecommendVO priceRecommend;

    /**
     * 订单优化建议
     */
    private OrderOptimizeVO orderOptimize;

    /**
     * 综合建议
     */
    private String comprehensiveAdvice;

    /**
     * 注意事项
     */
    private List<String> notes;
}
