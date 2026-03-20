package com.jmz.serveruser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmz.serveruser.dto.TransactionQueryDTO;
import com.jmz.serveruser.entity.Transaction;
import com.jmz.serveruser.vo.TransactionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
 
@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {
    IPage<TransactionVO> selectTransactionPage(Page<TransactionVO> page, @Param("query") TransactionQueryDTO queryDTO);
} 