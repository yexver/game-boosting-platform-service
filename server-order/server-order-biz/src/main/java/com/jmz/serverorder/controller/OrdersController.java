package com.jmz.serverorder.controller;

import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serverorder.dto.OrdersQueryDTO;
import com.jmz.serverorder.dto.SubmitOrderDTO;
import com.jmz.serverorder.dto.TakeOrderDTO;
import com.jmz.serverorder.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.jmz.serverorder.entity.OrderStatusLogs;
import com.jmz.serverorder.mapper.OrderStatusLogsMapper;
import com.jmz.serverorder.entity.Orders;
import com.jmz.serverorder.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.stream.Collectors;
import com.jmz.jmzfile.feign.RemoteFileService;
import com.jmz.jmzcommoncore.responseResult.R;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderStatusLogsMapper orderStatusLogsMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private RemoteFileService remoteFileService;

    @PostMapping("/submitOrder")
    public R submitOrder(@RequestBody SubmitOrderDTO dto) {
        return R.success(ordersService.submitOrder(dto));
    }

    @GetMapping("/orderInfoList")
    public R orderInfoList(OrdersQueryDTO queryDTO) {
        return R.success(ordersService.orderInfoList(queryDTO));
    }
    
    /**
     * 获取接单详情
     */
    @GetMapping("/take-order/{orderId}")
    public R getOrderDetail(@PathVariable Long orderId) {
        return R.success(ordersService.getTakeOrderDetail(orderId));
    }
    
    /**
     * 接单
     */
    @PostMapping("/take-order")
    public R takeOrder(@RequestBody TakeOrderDTO dto) {
        return R.success(ordersService.takeOrder(dto));
    }

    /**
     * 获取订单详情（包含发单人和接单人信息，精简字段）
     */
    @GetMapping("/{id}")
    public R getOrderSimpleDetail(@PathVariable Long id) {
        return R.success(ordersService.getOrderSimpleDetail(id));
    }

    /**
     * 申请撤销订单
     */
    @PostMapping("/apply-revoke")
    public R applyRevoke(
        @RequestParam Long orderId,
        @RequestParam BigDecimal price,
        @RequestParam BigDecimal deposit,
        @RequestParam(value = "remark", required = false) String remark,
        @RequestParam(value = "images", required = false) MultipartFile[] images
    ) throws Exception {
        return ordersService.applyRevoke(orderId, price, deposit, remark, images);
    }

    /**
     * 取消撤销申请
     */
    @PostMapping("/cancel-revoke")
    public R cancelRevoke(@RequestBody Map<String, Object> body) {
        Long orderId = null;
        if (body.get("orderId") instanceof Number) {
            orderId = ((Number) body.get("orderId")).longValue();
        } else if (body.get("orderId") instanceof String) {
            orderId = Long.valueOf((String) body.get("orderId"));
        }
        if (orderId == null) {
            return R.error("orderId不能为空");
        }
        return ordersService.cancelRevoke(orderId);
    }

    /**
     * 申请验收
     */
    @PostMapping("/apply-accept")
    public R applyAccept(
            @RequestParam Long orderId,
            @RequestParam(required = false) String remark,
            @RequestPart(value = "images", required = false) MultipartFile[] images) throws Exception {
        return ordersService.applyAccept(orderId, remark, images);
    }

    /**
     * 进行验收
     */
    @PostMapping("/verify-accept")
    public R verifyAccept(
        @RequestParam Long orderId,
        @RequestParam BigDecimal deposit,
        @RequestParam(value = "remark", required = false) String remark,
        @RequestParam(value = "images", required = false) MultipartFile[] images
    ) throws Exception {
        return ordersService.verifyAccept(orderId, deposit, remark, images);
    }

    /**
     * 撤销订单（发单者取消未接手的订单）
     */
    @PostMapping("/cancel-order")
    public R cancelOrder(@RequestBody Map<String, Object> body) {
        Long orderId = null;
        if (body.get("orderId") instanceof Number) {
            orderId = ((Number) body.get("orderId")).longValue();
        } else if (body.get("orderId") instanceof String) {
            orderId = Long.valueOf((String) body.get("orderId"));
        }
        if (orderId == null) {
            return R.error("orderId不能为空");
        }
        return ordersService.cancelOrder(orderId);
    }

    /**
     * 同意撤销
     */
    @PostMapping("/agree-revoke")
    public R agreeRevoke(@RequestBody Map<String, Object> body) {
        Long orderId = null;
        if (body.get("orderId") instanceof Number) {
            orderId = ((Number) body.get("orderId")).longValue();
        } else if (body.get("orderId") instanceof String) {
            orderId = Long.valueOf((String) body.get("orderId"));
        }
        if (orderId == null) {
            return R.error("orderId不能为空");
        }
        return ordersService.agreeRevoke(orderId);
    }

    /**
     * 拒绝撤销
     */
    @PostMapping("/disagree-revoke")
    public R disagreeRevoke(@RequestBody Map<String, Object> body) {
        Long orderId = null;
        if (body.get("orderId") instanceof Number) {
            orderId = ((Number) body.get("orderId")).longValue();
        } else if (body.get("orderId") instanceof String) {
            orderId = Long.valueOf((String) body.get("orderId"));
        }
        if (orderId == null) {
            return R.error("orderId不能为空");
        }
        return ordersService.disagreeRevoke(orderId);
    }

    /**
     * 介入订单
     */
    @PostMapping("/intervene")
    public R interveneOrder(@RequestBody Map<String, Object> body) {
        Long orderId = null;
        if (body.get("orderId") instanceof Number) {
            orderId = ((Number) body.get("orderId")).longValue();
        } else if (body.get("orderId") instanceof String) {
            orderId = Long.valueOf((String) body.get("orderId"));
        }
        if (orderId == null) {
            return R.error("orderId不能为空");
        }
        return ordersService.interveneOrder(orderId);
    }

    @PostMapping("/apply-intervene")
    public R applyIntervene(
        @RequestParam Long orderId,
        @RequestParam BigDecimal price,
        @RequestParam BigDecimal deposit,
        @RequestParam(value = "remark", required = false) String remark,
        @RequestParam(value = "images", required = false) MultipartFile[] images
    ) throws Exception {
        return ordersService.applyIntervene(orderId, price, deposit, remark, images);
    }

    /**
     * 仲裁订单
     */
    @PostMapping("/arbitrate")
    public R arbitrateOrder(@RequestBody Map<String, Object> data) throws Exception {
        Long orderId = null;
        java.math.BigDecimal payAmount = null;
        java.math.BigDecimal depositAmount = null;
        String remark = null;
        if (data.get("orderId") instanceof Number) {
            orderId = ((Number) data.get("orderId")).longValue();
        } else if (data.get("orderId") instanceof String) {
            orderId = Long.valueOf((String) data.get("orderId"));
        }
        if (data.get("payAmount") != null) {
            payAmount = new java.math.BigDecimal(data.get("payAmount").toString());
        }
        if (data.get("depositAmount") != null) {
            depositAmount = new java.math.BigDecimal(data.get("depositAmount").toString());
        }
        if (data.get("remark") != null) {
            remark = data.get("remark").toString();
        }
        if (orderId == null) {
            return R.error("orderId不能为空");
        }
        return ordersService.arbitrateOrder(orderId, payAmount, depositAmount, remark);
    }

    /**
     * 获取订单统计信息
     */
    @GetMapping("/statistics")
    public R getOrderStatistics(OrdersQueryDTO queryDTO) {
        return R.success(ordersService.getOrderStatistics(queryDTO));
    }

    /**
     * 批量删除订单
     */
    @PostMapping("/batch-delete")
    public R batchDeleteOrders(@RequestBody Map<String, Object> body) {
        Object idsObj = body.get("ids");
        if (idsObj == null) {
            return R.error("ids不能为空");
        }
        List<Long> ids;
        if (idsObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<Long> list = (List<Long>) idsObj;
            ids = list;
        } else {
            return R.error("ids格式错误");
        }
        return ordersService.batchDeleteOrders(ids);
    }

    /**
     * 修改订单备注
     */
    @PutMapping("/remark")
    public R updateOrderRemark(@RequestBody Map<String, Object> body) {
        Long orderId = null;
        String remark = null;
        if (body.get("orderId") instanceof Number) {
            orderId = ((Number) body.get("orderId")).longValue();
        } else if (body.get("orderId") instanceof String) {
            orderId = Long.valueOf((String) body.get("orderId"));
        }
        if (body.get("remark") != null) {
            remark = body.get("remark").toString();
        }
        if (orderId == null) {
            return R.error("orderId不能为空");
        }
        return ordersService.updateOrderRemark(orderId, remark);
    }

    /**
     * 导出订单列表（Excel）
     */
    @GetMapping("/export")
    public void exportOrders(OrdersQueryDTO queryDTO, HttpServletResponse response) {
        ordersService.exportOrders(queryDTO, response);
    }
} 