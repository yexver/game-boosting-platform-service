package com.jmz.serveruser.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmz.serveruser.entity.Transaction;
import com.jmz.serveruser.dto.TransactionQueryDTO;
import com.jmz.serveruser.mapper.TransactionMapper;
import com.jmz.serveruser.service.TransactionService;
import com.jmz.serveruser.vo.TransactionVO;
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