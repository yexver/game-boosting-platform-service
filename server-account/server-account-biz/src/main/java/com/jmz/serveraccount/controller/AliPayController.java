package com.jmz.serveraccount.controller;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveraccount.config.AliPayConfig;
import com.jmz.serveraccount.dto.AccountAdjustDTO;
import com.jmz.serveraccount.service.UserAccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/alipay")
public class AliPayController {

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    @Resource
    private AliPayConfig aliPayConfig;

    @Resource
    private UserAccountService userAccountService;

    @GetMapping("/pay")
    public void pay(String url, Long userId, BigDecimal price, HttpServletResponse httpResponse) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", UUID.randomUUID());
        bizContent.set("total_amount", price);
        bizContent.set("subject", String.valueOf(userId));
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());
        request.setReturnUrl(url);

        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @PostMapping("/notify")
    public void payNotify(HttpServletRequest request) throws Exception {
        if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
            Long userId = Long.valueOf(request.getParameter("subject"));
            BigDecimal amount = new BigDecimal(request.getParameter("total_amount"));

            AccountAdjustDTO dto = new AccountAdjustDTO();
            dto.setUserId(userId);
            dto.setAmount(amount);
            dto.setType(1);
            dto.setRemark("用户自主充值");
            userAccountService.adjustAccountBalance(dto);
        }
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
        dto.setType(2);
        dto.setRemark("用户提现");
        boolean success = userAccountService.adjustAccountBalance(dto);
        return success ? R.success("提现成功") : R.error("提现失败");
    }
}
