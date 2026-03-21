package com.jmz.serveraccount.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jmz.serveraccount.entity.Transaction;
import com.jmz.serveraccount.dto.TransactionQueryDTO;
import com.jmz.serveraccount.vo.TransactionVO;

public interface TransactionService extends IService<Transaction> {
    IPage<TransactionVO> getTransactionPage(TransactionQueryDTO queryDTO);
}
