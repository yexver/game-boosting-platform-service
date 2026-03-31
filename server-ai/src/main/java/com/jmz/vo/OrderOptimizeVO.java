package com.jmz.vo;

import lombok.Data;

import java.util.List;

/**
 * 订单优化结果VO
 */
@Data
public class OrderOptimizeVO {

    /**
     * 优化后的标题
     */
    private String optimizedTitle;

    /**
     * 优化后的描述
     */
    private String optimizedDescription;

    /**
     * 改进建议列表
     */
    private List<String> suggestions;

    /**
     * 预估接单率
     */
    private Integer estimatedRate;

    /**
     * AI优化说明
     */
    private String analysis;
}
