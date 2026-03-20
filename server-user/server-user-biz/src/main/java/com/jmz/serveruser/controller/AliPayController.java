package com.jmz.serveruser.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;

import com.alipay.api.request.AlipayTradeRefundRequest;
//import com.example.entity.Orders;
//import com.example.service.OrdersService;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveruser.config.AliPayConfig;
import com.jmz.serveruser.dto.AccountAdjustDTO;
import com.jmz.serveruser.service.UserAccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//  https://natapp.cn/
// ekihat7647@sandbox.com
@RestController
@RequestMapping("/alipay")
public class AliPayController {

    // 支付宝沙箱网关地址
    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    //签名方式
    private static final String SIGN_TYPE = "RSA2";

    @Resource
    private AliPayConfig aliPayConfig;

    //@Resource
    //private OrdersService ordersService;

    @Resource
    private UserAccountService userAccountService;

    @GetMapping("/pay")  //  /alipay/pay?orderNo=xxx
    public void pay(String url,Long userId, BigDecimal price, HttpServletResponse httpResponse) throws Exception {
        // 查询订单信息
        //Orders orders = ordersService.selectByOrderNo(orderNo);
        /*if (orders == null) {
            return;
        }*/
        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        // 2. 创建 Request并设置Request参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();  // 发送请求的 Request类
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", UUID.randomUUID());  // 我们自己生成的订单编号
        System.out.println(userId);
        bizContent.set("total_amount",price); // 订单的总金额
        bizContent.set("subject", String.valueOf(userId));   // 支付的名称
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");  // 固定配置
        request.setBizContent(bizContent.toString());
        request.setReturnUrl(url); // 支付完成后自动跳转到本地页面的路径
        // 执行请求，拿到响应的结果，返回给浏览器
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody(); // 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @PostMapping("/notify")  // 注意这里必须是POST接口
    public void payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }
            Long userId = Long.valueOf(request.getParameter("subject"));
            BigDecimal amount = new BigDecimal(request.getParameter("total_amount"));
                // 构造充值DTO
                AccountAdjustDTO dto = new AccountAdjustDTO();
                dto.setUserId(userId);
                dto.setAmount(amount);
                dto.setType(1); // 1=充值/增加余额
                dto.setRemark("用户自主充值");
                userAccountService.adjustAccountBalance(dto);


        }
    }

    /**
     * 提现接口
     */
    @PostMapping("/withdraw")
    public R withdraw(@RequestBody Map<String, Object> data) {
        Long userId = null;
        BigDecimal amount = null;
        String password = null;
        if (data.get("userId") instanceof Number) {
            userId = ((Number) data.get("userId")).longValue();
        } else if (data.get("userId") instanceof String) {
            userId = Long.valueOf((String) data.get("userId"));
        }
        if (data.get("amount") != null) {
            amount = new BigDecimal(data.get("amount").toString());
        }
        if (data.get("password") != null) {
            password = data.get("password").toString();
        }
        if (userId == null || amount == null || password == null) {
            return R.error("参数不完整");
        }
        // TODO: 校验用户密码（如有密码加密存储，需查用户表比对）
        // 这里只做简单示例，实际应查用户表校验密码
        // 扣款
        AccountAdjustDTO dto = new AccountAdjustDTO();
        dto.setUserId(userId);
        dto.setAmount(amount);
        dto.setType(2); // 2=提现/扣款
        dto.setRemark("用户提现");
        boolean success = userAccountService.adjustAccountBalance(dto);
        return success ? R.success("提现成功") : R.error("提现失败");
    }

    /**
     * 退款接口
     */
 /*   @PutMapping("/refund")
    public Result refund(String orderNo) {
        Orders orders = ordersService.selectByOrderNo(orderNo);
        if (ObjectUtil.isNull(orders)) {
            throw new CustomException("500", "未找到订单");
        }
        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        // 2. 创建 Request并设置Request参数
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", orders.getOrderNo());  // 我们自己生成的订单编号  必须是不重复的退款订单号
        bizContent.set("refund_amount", orders.getTotal()); // 订单的总金额
        bizContent.set("trade_no", orders.getPayNo()); // 支付宝支付订单号
        bizContent.set("out_request_no", IdUtil.fastSimpleUUID());   // 随机数
        request.setBizContent(bizContent.toString());
        try {
            // 退款调用接口
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("订单号" + orderNo + "退款成功");
            }
            Orders dbOrder = ordersService.selectByOrderNo(orderNo);
            dbOrder.setStatus("已退款");
            ordersService.updateById(dbOrder);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            System.err.println("退款失败");
        }

        return Result.success();
    }*/


}