package com.jmz.serveraccount.controller;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveraccount.config.AliPayConfig;
import com.jmz.serveraccount.dto.AccountAdjustDTO;
import com.jmz.serveraccount.enums.AccountTypeEnum;
import com.jmz.serveraccount.service.UserAccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/alipay")
public class AliPayController {

    private static final Logger log = LoggerFactory.getLogger(AliPayController.class);

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private AliPayConfig aliPayConfig;

    @Resource
    private UserAccountService userAccountService;

    @GetMapping("/pay")
    public void pay(String url, Long userId, BigDecimal price, HttpServletResponse httpResponse) throws Exception {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", UUID.randomUUID());
        bizContent.set("total_amount", price);
        bizContent.set("subject", "游戏代练平台账户充值-" + userId);
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());
        request.setReturnUrl(url);

        String form;
        try {
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            httpResponse.setContentType("text/plain;charset=UTF-8");
            httpResponse.getWriter().write("支付请求失败: " + e.getErrMsg());
            httpResponse.getWriter().flush();
            return;
        }
        httpResponse.setContentType("text/html;charset=UTF-8");
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @PostMapping("/notify")
    public String payNotify(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params, aliPayConfig.getAlipayPublicKey(), "UTF-8", "RSA2");
            if (!signVerified) {
                log.warn("支付宝回调签名验证失败");
                return "fail";
            }

            if ("TRADE_SUCCESS".equals(params.get("trade_status"))) {
                Long userId = Long.valueOf(params.get("subject"));
                BigDecimal amount = new BigDecimal(params.get("total_amount"));

                AccountAdjustDTO dto = new AccountAdjustDTO();
                dto.setUserId(userId);
                dto.setAmount(amount);
                dto.setType(AccountTypeEnum.RECHARGE);
                dto.setRemark("用户自主充值");
                dto.setIdempotencyKey(params.get("out_trade_no"));

                userAccountService.adjustAccountBalance(dto);
                log.info("支付宝充值回调处理成功: userId={}, amount={}", userId, amount);
            }
        } catch (Exception e) {
            log.error("支付宝回调处理异常", e);
            return "fail";
        }
        return "success";
    }

    @PostMapping("/withdraw")
    public R withdraw(@RequestBody Map<String, Object> data) {
        Long userId = null;
        BigDecimal amount = null;
        if (data.get("userId") instanceof Number) {
            userId = ((Number) data.get("userId")).longValue();
        } else if (data.get("userId") instanceof String) {
            userId = Long.valueOf((String) data.get("userId"));
        }
        if (data.get("amount") != null) {
            amount = new BigDecimal(data.get("amount").toString());
        }
        if (userId == null || amount == null) {
            return R.error("参数不完整");
        }

        AccountAdjustDTO dto = new AccountAdjustDTO();
        dto.setUserId(userId);
        dto.setAmount(amount);
        dto.setType(AccountTypeEnum.WITHDRAW);
        dto.setRemark("用户提现");
        dto.setIdempotencyKey("withdraw:" + userId + ":" + System.currentTimeMillis());

        try {
            boolean success = userAccountService.adjustAccountBalance(dto);
            return success ? R.success("提现成功") : R.error("提现失败");
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
}
