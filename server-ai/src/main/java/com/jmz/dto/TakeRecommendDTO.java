package com.jmz.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 接单推荐请求DTO
 */
@Data
public class TakeRecommendDTO {

    /**
     * 用户ID（代练师）
     */
    private Long userId;

    /**
     * 擅长游戏ID列表
     */
    private List<Integer> preferredGameIds;

    /**
     * 擅长游戏名称列表
     */
    private List<String> preferredGameNames;

    /**
     * 最低价格要求
     */
    private BigDecimal minPrice;

    /**
     * 最高时限要求（小时）
     */
    private Integer maxTimeLimit;

    /**
     * 目标收益
     */
    private BigDecimal targetIncome;

    /**
     * 可用时间（小时）
     */
    private Integer availableTime;

    /**
     * 历史完成率
     */
    private BigDecimal completionRate;

    /**
     * 平均收益
     */
    private BigDecimal avgIncome;

    /**
     * 代练类型偏好：1-代练，2-陪练，null-不限
     */
    private Integer boostingType;

    /**
     * 可接订单列表（用于匹配）
     */
    private List<OrderSimpleVO> availableOrders;
}
