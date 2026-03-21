package com.jmz.serveraccount.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveraccount.dto.TransactionCreateDTO;
import com.jmz.serveraccount.dto.TransactionQueryDTO;
import com.jmz.serveraccount.service.TransactionService;
import com.jmz.serveraccount.vo.TransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public R getUserTransactions(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long operatorId,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        TransactionQueryDTO queryDTO = new TransactionQueryDTO();
        queryDTO.setUserId(userId);
        queryDTO.setOperatorId(operatorId);
        queryDTO.setType(type);
        queryDTO.setCurrent(current);
        queryDTO.setSize(size);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = null, end = null;
        if (StringUtils.isNotBlank(startTime)) {
            try {
                start = sdf.parse(startTime);
            } catch (Exception e) {
                return R.error("startTime格式错误，必须为yyyy-MM-dd HH:mm:ss");
            }
        }
        if (StringUtils.isNotBlank(endTime)) {
            try {
                end = sdf.parse(endTime);
            } catch (Exception e) {
                return R.error("endTime格式错误，必须为yyyy-MM-dd HH:mm:ss");
            }
        }
        queryDTO.setStartTime(start);
        queryDTO.setEndTime(end);
        IPage<TransactionVO> page = transactionService.getTransactionPage(queryDTO);
        return R.success("查询成功", page);
    }
}
