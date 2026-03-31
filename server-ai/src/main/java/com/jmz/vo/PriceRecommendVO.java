package com.jmz.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 价格建议结果VO
 */
@Data
public class PriceRecommendVO {

    /**
     * 最低价（成本价）
     */
    private BigDecimal minPrice;

    /**
     * 最高价（市场价）
     */
    private BigDecimal maxPrice;

    /**
     * 推荐价（性价比最优）
     */
    private BigDecimal recommendedPrice;

    /**
     * 建议安全保证金
     */
    private BigDecimal securityDeposit;

    /**
     * 建议效率保证金
     */
    private BigDecimal efficiencyDeposit;

    /**
     * 市场参考均价
     */
    private BigDecimal marketAvgPrice;

    /**
     * 价格影响因素列表
     */
    private List<String> priceFactors;

    /**
     * AI分析说明
     */
    private String analysis;
}
