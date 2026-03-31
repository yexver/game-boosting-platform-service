package com.jmz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jmz.dto.OrderSimpleVO;
import com.jmz.dto.PostRecommendDTO;
import com.jmz.dto.TakeRecommendDTO;
import com.jmz.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * AI智能推荐服务
 * 复用 AliCloudAIService 实现发单推荐和接单推荐
 */
@Slf4j
@Service
public class RecommendService {

    @Autowired
    private AliCloudAIService aiService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 发单推荐：价格建议 + 订单优化
     */
    public PostRecommendVO postRecommend(PostRecommendDTO dto) {
        PostRecommendVO result = new PostRecommendVO();

        try {
            // 1. 价格建议
            PriceRecommendVO priceRecommend = getPriceRecommend(dto);
            result.setPriceRecommend(priceRecommend);

            // 2. 订单优化
            OrderOptimizeVO orderOptimize = getOrderOptimize(dto);
            result.setOrderOptimize(orderOptimize);

            // 3. 综合建议
            String comprehensiveAdvice = getComprehensiveAdvice(dto, priceRecommend, orderOptimize);
            result.setComprehensiveAdvice(comprehensiveAdvice);

            // 4. 注意事项
            List<String> notes = getPostNotes(dto);
            result.setNotes(notes);

        } catch (Exception e) {
            log.error("发单推荐服务异常: {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * 价格建议
     */
    public PriceRecommendVO getPriceRecommend(PostRecommendDTO dto) {
        String prompt = buildPricePrompt(dto);
        String aiResponse = aiService.askQuestion(prompt);

        return parsePriceRecommend(aiResponse, dto);
    }

    /**
     * 订单优化
     */
    public OrderOptimizeVO getOrderOptimize(PostRecommendDTO dto) {
        String prompt = buildOptimizePrompt(dto);
        String aiResponse = aiService.askQuestion(prompt);

        return parseOrderOptimize(aiResponse);
    }

    /**
     * 接单推荐：订单匹配 + 收益策略
     */
    public TakeRecommendVO takeRecommend(TakeRecommendDTO dto) {
        TakeRecommendVO result = new TakeRecommendVO();

        try {
            // 构建推荐Prompt
            String prompt = buildTakeRecommendPrompt(dto);
            String aiResponse = aiService.askQuestion(prompt);

            // 解析推荐结果
            List<RecommendedOrderVO> recommendedOrders = parseRecommendedOrders(aiResponse, dto.getAvailableOrders());

            // 设置推荐订单
            result.setRecommendedOrders(recommendedOrders);

            // 计算总收益预估
            BigDecimal totalIncome = calculateTotalIncome(recommendedOrders);
            result.setTotalEstimatedIncome(totalIncome);

            // 风险分析
            String riskAnalysis = analyzeRisk(recommendedOrders);
            result.setRiskAnalysis(riskAnalysis);

            // 收益策略建议
            String strategyAdvice = getStrategyAdvice(dto, recommendedOrders);
            result.setStrategyAdvice(strategyAdvice);

            // 警告信息
            List<String> warnings = getWarnings(recommendedOrders);
            result.setWarnings(warnings);

        } catch (Exception e) {
            log.error("接单推荐服务异常: {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * 构建价格建议Prompt
     */
    private String buildPricePrompt(PostRecommendDTO dto) {
        String boostingTypeName = dto.getBoostingType() == 1 ? "代练" : "陪练";
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个游戏代练平台（代练侠）的专业价格分析师。\n\n");
        prompt.append("请根据以下订单信息，给出合理的价格建议：\n");
        prompt.append("- 游戏：").append(dto.getGameName() != null ? dto.getGameName() : "未知").append("\n");
        prompt.append("- 代练类型：").append(boostingTypeName).append("\n");
        prompt.append("- 当前段位：").append(dto.getCurrentRank() != null ? dto.getCurrentRank() : "未知").append("\n");
        prompt.append("- 目标段位：").append(dto.getTargetRank() != null ? dto.getTargetRank() : "未知").append("\n");
        prompt.append("- 时限要求：").append(dto.getTimeLimit() != null ? dto.getTimeLimit() : "未说明").append("小时\n");

        if (dto.getExpectedPrice() != null) {
            prompt.append("- 期望价格：").append(dto.getExpectedPrice()).append("元\n");
        }

        prompt.append("\n请分析并给出JSON格式的价格建议，格式如下：\n");
        prompt.append("{\n");
        prompt.append("  \"minPrice\": 最低价(元),\n");
        prompt.append("  \"maxPrice\": 最高价(元),\n");
        prompt.append("  \"recommendedPrice\": 推荐价(元),\n");
        prompt.append("  \"securityDeposit\": 建议安全保证金(元),\n");
        prompt.append("  \"efficiencyDeposit\": 建议效率保证金(元),\n");
        prompt.append("  \"marketAvgPrice\": 市场参考均价(元),\n");
        prompt.append("  \"priceFactors\": [\"影响因素1\", \"影响因素2\"],\n");
        prompt.append("  \"analysis\": \"价格分析说明\"\n");
        prompt.append("}\n");
        prompt.append("\n只返回JSON，不要其他内容。");

        return prompt.toString();
    }

    /**
     * 构建订单优化Prompt
     */
    private String buildOptimizePrompt(PostRecommendDTO dto) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个游戏代练平台（代练侠）的订单优化专家。\n\n");
        prompt.append("请帮助用户优化以下订单信息，提高接单率：\n\n");
        prompt.append("原始标题：").append(dto.getTitle() != null ? dto.getTitle() : "未填写").append("\n");
        prompt.append("原始描述：").append(dto.getDescription() != null ? dto.getDescription() : "未填写").append("\n");

        if (dto.getExpectedPrice() != null) {
            prompt.append("订单价格：").append(dto.getExpectedPrice()).append("元\n");
        }

        prompt.append("\n请优化订单信息，返回JSON格式：\n");
        prompt.append("{\n");
        prompt.append("  \"optimizedTitle\": \"优化后的标题(吸引眼球，信息完整)\",\n");
        prompt.append("  \"optimizedDescription\": \"优化后的描述(规范清晰，格式美观)\",\n");
        prompt.append("  \"suggestions\": [\"改进建议1\", \"改进建议2\"],\n");
        prompt.append("  \"estimatedRate\": 预估接单率(0-100的整数),\n");
        prompt.append("  \"analysis\": \"优化分析说明\"\n");
        prompt.append("}\n");
        prompt.append("\n只返回JSON，不要其他内容。");

        return prompt.toString();
    }

    /**
     * 构建接单推荐Prompt
     */
    private String buildTakeRecommendPrompt(TakeRecommendDTO dto) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个游戏代练平台（代练侠）的智能订单匹配助手。\n\n");
        prompt.append("请根据代练师的画像，从订单列表中推荐最合适的订单：\n\n");

        // 代练师画像
        prompt.append("【代练师画像】\n");
        if (dto.getPreferredGameNames() != null && !dto.getPreferredGameNames().isEmpty()) {
            prompt.append("- 擅长游戏：").append(String.join("、", dto.getPreferredGameNames())).append("\n");
        }
        if (dto.getCompletionRate() != null) {
            prompt.append("- 历史完成率：").append(dto.getCompletionRate()).append("%\n");
        }
        if (dto.getAvgIncome() != null) {
            prompt.append("- 平均收益：").append(dto.getAvgIncome()).append("元/单\n");
        }
        if (dto.getTargetIncome() != null) {
            prompt.append("- 目标收益：").append(dto.getTargetIncome()).append("元\n");
        }
        if (dto.getAvailableTime() != null) {
            prompt.append("- 可用时间：").append(dto.getAvailableTime()).append("小时\n");
        }
        if (dto.getMinPrice() != null) {
            prompt.append("- 最低价格要求：").append(dto.getMinPrice()).append("元\n");
        }
        if (dto.getMaxTimeLimit() != null) {
            prompt.append("- 最长时限接受：").append(dto.getMaxTimeLimit()).append("小时\n");
        }
        prompt.append("\n");

        // 订单列表
        if (dto.getAvailableOrders() != null && !dto.getAvailableOrders().isEmpty()) {
            prompt.append("【可接订单列表】\n");
            int index = 1;
            for (OrderSimpleVO order : dto.getAvailableOrders()) {
                prompt.append(index).append(". ");
                prompt.append("订单号:").append(order.getOrderNo());
                prompt.append(" | 游戏:").append(order.getGameName());
                prompt.append(" | 标题:").append(order.getTitle());
                prompt.append(" | 段位:").append(order.getCurrentRank()).append("→").append(order.getTargetRank());
                prompt.append(" | 价格:").append(order.getPrice()).append("元");
                prompt.append(" | 时限:").append(order.getTimeLimit()).append("小时");
                prompt.append("\n");
                index++;
            }
            prompt.append("\n请从上述订单中推荐最合适的3个订单，返回JSON格式：\n");
            prompt.append("{\n");
            prompt.append("  \"recommendedOrders\": [\n");
            prompt.append("    {\n");
            prompt.append("      \"orderIndex\": 订单序号(1,2,3...),\n");
            prompt.append("      \"matchScore\": 匹配度(0-100的整数),\n");
            prompt.append("      \"reason\": \"推荐理由\",\n");
            prompt.append("      \"profitAnalysis\": \"收益分析\",\n");
            prompt.append("      \"riskWarning\": \"风险提示（如有）\"\n");
            prompt.append("    }\n");
            prompt.append("  ]\n");
            prompt.append("}\n");
        } else {
            prompt.append("【可接订单列表】暂无订单\n\n");
            prompt.append("返回JSON格式（空推荐）：\n");
            prompt.append("{\n");
            prompt.append("  \"recommendedOrders\": []\n");
            prompt.append("}\n");
        }

        prompt.append("\n只返回JSON，不要其他内容。");

        return prompt.toString();
    }

    /**
     * 解析价格建议结果
     */
    private PriceRecommendVO parsePriceRecommend(String aiResponse, PostRecommendDTO dto) {
        PriceRecommendVO vo = new PriceRecommendVO();

        try {
            // 尝试解析JSON
            JsonNode root = parseJson(aiResponse);

            if (root != null) {
                vo.setMinPrice(getBigDecimal(root, "minPrice", new BigDecimal("100")));
                vo.setMaxPrice(getBigDecimal(root, "maxPrice", new BigDecimal("500")));
                vo.setRecommendedPrice(getBigDecimal(root, "recommendedPrice", new BigDecimal("200")));
                vo.setSecurityDeposit(getBigDecimal(root, "securityDeposit", new BigDecimal("50")));
                vo.setEfficiencyDeposit(getBigDecimal(root, "efficiencyDeposit", new BigDecimal("30")));
                vo.setMarketAvgPrice(getBigDecimal(root, "marketAvgPrice", new BigDecimal("180")));
                vo.setPriceFactors(getStringList(root, "priceFactors"));
                vo.setAnalysis(getText(root, "analysis"));
            } else {
                // 解析失败，使用默认推荐
                setDefaultPriceRecommend(vo, dto);
            }
        } catch (Exception e) {
            log.error("解析价格建议失败: {}", e.getMessage());
            setDefaultPriceRecommend(vo, dto);
        }

        return vo;
    }

    /**
     * 设置默认价格建议
     */
    private void setDefaultPriceRecommend(PriceRecommendVO vo, PostRecommendDTO dto) {
        vo.setMinPrice(new BigDecimal("100"));
        vo.setMaxPrice(new BigDecimal("500"));
        vo.setRecommendedPrice(new BigDecimal("200"));
        vo.setSecurityDeposit(new BigDecimal("50"));
        vo.setEfficiencyDeposit(new BigDecimal("30"));
        vo.setMarketAvgPrice(new BigDecimal("180"));
        vo.setPriceFactors(List.of("根据段位差距定价", "根据时限要求调整"));
        vo.setAnalysis("这是基于常规情况的价格建议，实际价格可能因市场波动而有所不同。");
    }

    /**
     * 解析订单优化结果
     */
    private OrderOptimizeVO parseOrderOptimize(String aiResponse) {
        OrderOptimizeVO vo = new OrderOptimizeVO();

        try {
            JsonNode root = parseJson(aiResponse);

            if (root != null) {
                vo.setOptimizedTitle(getText(root, "optimizedTitle"));
                vo.setOptimizedDescription(getText(root, "optimizedDescription"));
                vo.setSuggestions(getStringList(root, "suggestions"));
                vo.setEstimatedRate(getInt(root, "estimatedRate", 70));
                vo.setAnalysis(getText(root, "analysis"));
            } else {
                setDefaultOrderOptimize(vo);
            }
        } catch (Exception e) {
            log.error("解析订单优化结果失败: {}", e.getMessage());
            setDefaultOrderOptimize(vo);
        }

        return vo;
    }

    /**
     * 设置默认订单优化
     */
    private void setDefaultOrderOptimize(OrderOptimizeVO vo) {
        vo.setOptimizedTitle("【代练订单】专业代练服务");
        vo.setOptimizedDescription("1. 全程手打，禁止开挂\n2. 保护账号安全\n3. 如有特殊情况及时沟通");
        vo.setSuggestions(List.of("建议添加具体段位要求", "建议明确完成时间"));
        vo.setEstimatedRate(60);
        vo.setAnalysis("这是基于常规情况的优化建议，实际效果可能因订单内容而异。");
    }

    /**
     * 解析推荐订单结果
     */
    private List<RecommendedOrderVO> parseRecommendedOrders(String aiResponse, List<OrderSimpleVO> availableOrders) {
        List<RecommendedOrderVO> result = new ArrayList<>();

        try {
            JsonNode root = parseJson(aiResponse);

            if (root != null && root.has("recommendedOrders")) {
                JsonNode orders = root.get("recommendedOrders");
                if (orders.isArray()) {
                    for (JsonNode item : orders) {
                        int orderIndex = getInt(item, "orderIndex", -1);
                        if (orderIndex > 0 && orderIndex <= (availableOrders != null ? availableOrders.size() : 0)) {
                            OrderSimpleVO orderSimple = availableOrders.get(orderIndex - 1);

                            RecommendedOrderVO vo = new RecommendedOrderVO();
                            vo.setOrderId(orderSimple.getOrderId());
                            vo.setOrderNo(orderSimple.getOrderNo());
                            vo.setGameName(orderSimple.getGameName());
                            vo.setTitle(orderSimple.getTitle());
                            vo.setCurrentRank(orderSimple.getCurrentRank());
                            vo.setTargetRank(orderSimple.getTargetRank());
                            vo.setPrice(orderSimple.getPrice());
                            vo.setTimeLimit(orderSimple.getTimeLimit());
                            vo.setMatchScore(getInt(item, "matchScore", 80));
                            vo.setReason(getText(item, "reason"));
                            vo.setProfitAnalysis(getText(item, "profitAnalysis"));
                            vo.setRiskWarning(getText(item, "riskWarning"));
                            vo.setJumpUrl("/order/detail/" + orderSimple.getOrderId());

                            result.add(vo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("解析推荐订单失败: {}", e.getMessage());
        }

        return result;
    }

    /**
     * 获取综合建议
     */
    private String getComprehensiveAdvice(PostRecommendDTO dto, PriceRecommendVO price, OrderOptimizeVO optimize) {
        StringBuilder advice = new StringBuilder();
        advice.append("综合AI分析，建议您：\n");

        if (price.getRecommendedPrice() != null) {
            advice.append("1. 将订单价格设置在").append(price.getRecommendedPrice())
                  .append("元左右，既有竞争力又能保证收益。\n");
        }

        if (optimize.getEstimatedRate() != null && optimize.getEstimatedRate() >= 70) {
            advice.append("2. 您的订单信息优化良好，预计接单率较高。\n");
        } else {
            advice.append("2. 建议进一步完善订单描述，可以参考AI优化建议。\n");
        }

        if (price.getSecurityDeposit() != null && price.getEfficiencyDeposit() != null) {
            advice.append("3. 建议设置安全保证金").append(price.getSecurityDeposit())
                  .append("元、效率保证金").append(price.getEfficiencyDeposit())
                  .append("元，以保障双方权益。\n");
        }

        return advice.toString();
    }

    /**
     * 获取发单注意事项
     */
    private List<String> getPostNotes(PostRecommendDTO dto) {
        List<String> notes = new ArrayList<>();
        notes.add("请确保游戏账号信息准确无误");
        notes.add("设置合理的时限，避免代练师无法按时完成");
        notes.add("保证金是对代练师的保障，建议按建议金额设置");
        notes.add("订单发布后可随时查看和修改");
        return notes;
    }

    /**
     * 计算总收益预估
     */
    private BigDecimal calculateTotalIncome(List<RecommendedOrderVO> orders) {
        if (orders == null || orders.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (RecommendedOrderVO order : orders) {
            if (order.getPrice() != null) {
                total = total.add(order.getPrice());
            }
        }
        return total;
    }

    /**
     * 风险分析
     */
    private String analyzeRisk(List<RecommendedOrderVO> orders) {
        if (orders == null || orders.isEmpty()) {
            return "暂无推荐订单，无法进行风险分析。";
        }

        StringBuilder analysis = new StringBuilder();
        analysis.append("根据推荐订单分析：\n");

        int highRiskCount = 0;
        for (RecommendedOrderVO order : orders) {
            if (order.getRiskWarning() != null && !order.getRiskWarning().isEmpty()) {
                highRiskCount++;
            }
        }

        if (highRiskCount == 0) {
            analysis.append("推荐的订单整体风险较低，建议您仔细阅读订单要求后接单。");
        } else {
            analysis.append("部分订单存在一定风险，请务必与发单用户充分沟通。");
        }

        return analysis.toString();
    }

    /**
     * 获取策略建议
     */
    private String getStrategyAdvice(TakeRecommendDTO dto, List<RecommendedOrderVO> orders) {
        StringBuilder advice = new StringBuilder();
        advice.append("收益策略建议：\n");

        if (orders != null && !orders.isEmpty()) {
            advice.append("1. 建议优先接取匹配度90%以上的订单，成功率更高。\n");
            advice.append("2. 合理安排时间，避免同时接取过多订单。\n");
            advice.append("3. 订单完成后及时验收，积累好评提升排名。\n");
        } else {
            advice.append("当前暂无完全匹配的订单，建议您适当放宽条件或等待新订单。\n");
        }

        return advice.toString();
    }

    /**
     * 获取警告信息
     */
    private List<String> getWarnings(List<RecommendedOrderVO> orders) {
        List<String> warnings = new ArrayList<>();

        if (orders != null) {
            for (RecommendedOrderVO order : orders) {
                if (order.getRiskWarning() != null && !order.getRiskWarning().isEmpty()) {
                    warnings.add("订单【" + order.getOrderNo() + "】：" + order.getRiskWarning());
                }
            }
        }

        if (warnings.isEmpty()) {
            warnings.add("暂无风险提示，请放心接单");
        }

        return warnings;
    }

    // ========== JSON解析辅助方法 ==========

    private JsonNode parseJson(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        try {
            // 尝试提取JSON（处理可能的前后文本）
            String jsonStr = text.trim();
            int jsonStart = jsonStr.indexOf("{");
            int jsonEnd = jsonStr.lastIndexOf("}");

            if (jsonStart >= 0 && jsonEnd > jsonStart) {
                jsonStr = jsonStr.substring(jsonStart, jsonEnd + 1);
                return objectMapper.readTree(jsonStr);
            }
        } catch (JsonProcessingException e) {
            log.warn("JSON解析失败: {}", e.getMessage());
        }

        return null;
    }

    private BigDecimal getBigDecimal(JsonNode node, String field, BigDecimal defaultValue) {
        try {
            JsonNode fieldNode = node.get(field);
            if (fieldNode != null && !fieldNode.isNull()) {
                if (fieldNode.isNumber()) {
                    return new BigDecimal(fieldNode.asText());
                }
            }
        } catch (Exception e) {
            log.warn("获取BigDecimal失败: {}", e.getMessage());
        }
        return defaultValue;
    }

    private int getInt(JsonNode node, String field, int defaultValue) {
        try {
            JsonNode fieldNode = node.get(field);
            if (fieldNode != null && !fieldNode.isNull()) {
                return fieldNode.asInt();
            }
        } catch (Exception e) {
            log.warn("获取int失败: {}", e.getMessage());
        }
        return defaultValue;
    }

    private String getText(JsonNode node, String field) {
        try {
            JsonNode fieldNode = node.get(field);
            if (fieldNode != null && !fieldNode.isNull()) {
                return fieldNode.asText();
            }
        } catch (Exception e) {
            log.warn("获取text失败: {}", e.getMessage());
        }
        return "";
    }

    private List<String> getStringList(JsonNode node, String field) {
        List<String> list = new ArrayList<>();
        try {
            JsonNode fieldNode = node.get(field);
            if (fieldNode != null && fieldNode.isArray()) {
                for (JsonNode item : fieldNode) {
                    list.add(item.asText());
                }
            }
        } catch (Exception e) {
            log.warn("获取string list失败: {}", e.getMessage());
        }
        return list;
    }
}
