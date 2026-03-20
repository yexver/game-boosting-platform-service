package com.jmz.serveruser.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jmz.serveruser.entity.Transaction;
import com.jmz.serveruser.dto.TransactionQueryDTO;
import com.jmz.serveruser.vo.TransactionVO;

public interface TransactionService extends IService<Transaction> {
    IPage<TransactionVO> getTransactionPage(TransactionQueryDTO queryDTO);
} 