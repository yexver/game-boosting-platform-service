package com.jmz.serveraccount.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmz.serveraccount.entity.Transaction;
import com.jmz.serveraccount.mapper.TransactionMapper;
import com.jmz.serveraccount.service.TransactionService;
import com.jmz.serveraccount.dto.TransactionQueryDTO;
import com.jmz.serveraccount.vo.TransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public IPage<TransactionVO> getTransactionPage(TransactionQueryDTO queryDTO) {
        Page<TransactionVO> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        return transactionMapper.selectTransactionPage(page, queryDTO);
    }
}
