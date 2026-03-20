package com.jmz.serverorder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serverorder.dto.SubmitOrderDTO;
import com.jmz.serverorder.dto.OrdersQueryDTO;
import com.jmz.serverorder.dto.TakeOrderDTO;
import com.jmz.serverorder.vo.OrdersListVO;
import com.jmz.serverorder.vo.TakeOrderDetailVO;
import com.jmz.serverorder.vo.GameOrderDistributionVO;
import com.jmz.serverorder.vo.IncomeTrendVO;
import com.jmz.serverorder.vo.OrderStatusPieVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrdersService {
    Object submitOrder(SubmitOrderDTO dto);

    /**
     * 订单列表
     */
    Map<String, Object> orderInfoList(OrdersQueryDTO queryDTO);
    
    /**
     * 获取接单详情
     */
    TakeOrderDetailVO getTakeOrderDetail(Long orderId);
    
    /**
     * 接单
     */
    Object takeOrder(TakeOrderDTO dto);

    /**
     * 获取订单详情（包含发单人和接单人信息，精简字段）
     */
    com.jmz.serverorder.vo.OrderSimpleDetailVO getOrderSimpleDetail(Long orderId);

    /**
     * 申请撤销订单
     */
    R applyRevoke(Long orderId, java.math.BigDecimal price, java.math.BigDecimal deposit, String remark, org.springframework.web.multipart.MultipartFile[] images) throws Exception;

    /**
     * 取消撤销申请
     */
    R cancelRevoke(Long orderId);

    /**
     * 申请验收
     */
    R applyAccept(Long orderId, String remark, org.springframework.web.multipart.MultipartFile[] images) throws Exception;

    /**
     * 进行验收
     */
    R verifyAccept(Long orderId, BigDecimal  deposit, String remark, org.springframework.web.multipart.MultipartFile[] images) throws Exception;

    /**
     * 发单者取消未接手的订单
     */
    R cancelOrder(Long orderId);

    /**
     * 同意撤销
     */
    R agreeRevoke(Long orderId);
    /**
     * 拒绝撤销
     */
    R disagreeRevoke(Long orderId);

    /**
     * 介入订单
     */
    R interveneOrder(Long orderId);

    /**
     * 用户申请平台介入
     */
    R applyIntervene(Long orderId, java.math.BigDecimal price, java.math.BigDecimal deposit, String remark, org.springframework.web.multipart.MultipartFile[] images) throws Exception;

    /**
     * 仲裁订单
     */
    R arbitrateOrder(Long orderId, java.math.BigDecimal payAmount, java.math.BigDecimal depositAmount, String remark) throws Exception;

    /**
     * 游戏订单分布
     */
    List<GameOrderDistributionVO> getGameOrderDistribution(Integer days);

    /**
     * 平台收入趋势
     */
    List<IncomeTrendVO> getIncomeTrend(Integer days);

    /**
     * 订单状态占比
     */
    List<OrderStatusPieVO> getOrderStatusPie(Integer days);
} 