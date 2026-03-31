package com.jmz.controller;

import com.jmz.dto.PostRecommendDTO;
import com.jmz.dto.TakeRecommendDTO;
import com.jmz.responseResult.R;
import com.jmz.service.RecommendService;
import com.jmz.vo.OrderOptimizeVO;
import com.jmz.vo.PostRecommendVO;
import com.jmz.vo.PriceRecommendVO;
import com.jmz.vo.TakeRecommendVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * AI智能推荐控制器
 * 提供发单推荐、接单推荐等接口
 */
@Slf4j
@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    /**
     * 完整发单推荐（价格建议 + 订单优化）
     * @param dto 发单推荐请求参数
     * @return 发单推荐结果
     */
    @PostMapping("/post")
    public R postRecommend(@RequestBody PostRecommendDTO dto) {
        try {
            log.info("收到发单推荐请求: {}", dto);
            PostRecommendVO result = recommendService.postRecommend(dto);
            return R.success("推荐成功", result);
        } catch (Exception e) {
            log.error("发单推荐接口异常: {}", e.getMessage(), e);
            return R.error("推荐服务异常，请稍后再试");
        }
    }

    /**
     * 单独获取价格建议
     * @param dto 价格建议请求参数
     * @return 价格建议结果
     */
    @PostMapping("/price")
    public R getPriceRecommend(@RequestBody PostRecommendDTO dto) {
        try {
            log.info("收到价格建议请求: {}", dto);
            PriceRecommendVO result = recommendService.getPriceRecommend(dto);
            return R.success("价格分析成功", result);
        } catch (Exception e) {
            log.error("价格建议接口异常: {}", e.getMessage(), e);
            return R.error("价格分析服务异常，请稍后再试");
        }
    }

    /**
     * 单独获取订单优化建议
     * @param dto 订单优化请求参数
     * @return 订单优化结果
     */
    @PostMapping("/optimize")
    public R getOrderOptimize(@RequestBody PostRecommendDTO dto) {
        try {
            log.info("收到订单优化请求: {}", dto);
            OrderOptimizeVO result = recommendService.getOrderOptimize(dto);
            return R.success("订单优化成功", result);
        } catch (Exception e) {
            log.error("订单优化接口异常: {}", e.getMessage(), e);
            return R.error("订单优化服务异常，请稍后再试");
        }
    }

    /**
     * 接单推荐（订单匹配 + 收益策略）
     * @param dto 接单推荐请求参数
     * @return 接单推荐结果
     */
    @PostMapping("/take")
    public R takeRecommend(@RequestBody TakeRecommendDTO dto) {
        try {
            log.info("收到接单推荐请求: userId={}", dto.getUserId());
            TakeRecommendVO result = recommendService.takeRecommend(dto);
            return R.success("推荐成功", result);
        } catch (Exception e) {
            log.error("接单推荐接口异常: {}", e.getMessage(), e);
            return R.error("推荐服务异常，请稍后再试");
        }
    }
}
