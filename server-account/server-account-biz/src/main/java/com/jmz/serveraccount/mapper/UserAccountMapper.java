package com.jmz.serveraccount.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmz.serveraccount.entity.UserAccount;
import com.jmz.serveraccount.vo.AccountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    IPage<AccountVO> selectAccountListWithUser(Page<AccountVO> page, @Param("userId") Long userId, @Param("phone") String phone);
}
