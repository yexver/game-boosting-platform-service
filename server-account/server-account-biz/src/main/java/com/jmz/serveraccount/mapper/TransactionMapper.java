package com.jmz.serveraccount.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmz.serveraccount.dto.TransactionQueryDTO;
import com.jmz.serveraccount.entity.Transaction;
import com.jmz.serveraccount.vo.TransactionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {
    IPage<TransactionVO> selectTransactionPage(Page<TransactionVO> page, @Param("query") TransactionQueryDTO queryDTO);
}
