package com.jmz.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 发单推荐请求DTO
 */
@Data
public class PostRecommendDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 游戏ID
     */
    private Integer gameId;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 系统ID（安卓QQ/微信等）
     */
    private Integer systemId;

    /**
     * 区服ID
     */
    private Integer serverId;

    /**
     * 当前段位
     */
    private String currentRank;

    /**
     * 目标段位
     */
    private String targetRank;

    /**
     * 代练类型：1-代练，2-陪练
     */
    private Integer boostingType;

    /**
     * 时限要求（小时）
     */
    private Integer timeLimit;

    /**
     * 订单标题
     */
    private String title;

    /**
     * 订单描述
     */
    private String description;

    /**
     * 期望价格（可选）
     */
    private BigDecimal expectedPrice;
}
