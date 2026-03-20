package com.jmz.serverorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmz.serverorder.entity.Orders;
import com.jmz.serverorder.vo.OrdersListVO;
import com.jmz.serverorder.vo.TakeOrderDetailVO;
import com.jmz.serverorder.dto.OrdersQueryDTO;
import com.jmz.serverorder.vo.GameOrderDistributionVO;
import com.jmz.serverorder.vo.IncomeTrendVO;
import com.jmz.serverorder.vo.OrderStatusPieVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface OrdersMapper extends BaseMapper<Orders> {
    List<OrdersListVO> selectOrderInfoList(
        @Param("query") OrdersQueryDTO queryDTO,
        @Param("offset") int offset,
        @Param("pageSize") int pageSize
    );

    int countOrderInfoList(@Param("query") OrdersQueryDTO queryDTO);
    
    /**
     * 查询接单详情
     */
    TakeOrderDetailVO selectTakeOrderDetail(@Param("orderId") Long orderId);

    int countOrdersByPublisherIn30Days(@org.apache.ibatis.annotations.Param("publisherId") Long publisherId);
    int countManagerOrdersByPublisherIn30Days(@org.apache.ibatis.annotations.Param("publisherId") Long publisherId);

    /**
     * 游戏订单分布
     */
    List<GameOrderDistributionVO> getGameOrderDistribution(@Param("days") Integer days);

    /**
     * 平台收入趋势
     */
    List<IncomeTrendVO> getIncomeTrend(@Param("days") Integer days);

    /**
     * 订单状态占比
     */
    List<OrderStatusPieVO> getOrderStatusPie(@Param("days") Integer days);
}