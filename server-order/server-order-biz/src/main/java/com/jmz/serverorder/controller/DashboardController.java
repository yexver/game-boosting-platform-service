package com.jmz.serverorder.controller;

import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serverorder.service.OrdersService;
import com.jmz.serverorder.vo.GameOrderDistributionVO;
import com.jmz.serverorder.vo.IncomeTrendVO;
import com.jmz.serverorder.vo.OrderStatusPieVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 游戏订单分布
     */
    @GetMapping("/game-order-distribution")
    public R getGameOrderDistribution(@RequestParam(value = "days", defaultValue = "7") Integer days) {
        return R.success(ordersService.getGameOrderDistribution(days));
    }

    /**
     * 平台收入趋势
     */
    @GetMapping("/income-trend")
    public R getIncomeTrend(@RequestParam(value = "days", defaultValue = "7") Integer days) {
        return R.success(ordersService.getIncomeTrend(days));
    }

    /**
     * 订单状态占比
     */
    @GetMapping("/order-status-pie")
    public R getOrderStatusPie(@RequestParam(value = "days", defaultValue = "7") Integer days) {
        return R.success(ordersService.getOrderStatusPie(days));
    }

}
