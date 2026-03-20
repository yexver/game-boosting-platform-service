package com.jmz.serveruser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmz.serveruser.entity.UserAccount;
import com.jmz.serveruser.vo.AccountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {
    
    /**
     * 分页查询账户列表（关联用户信息）
     */
    IPage<AccountVO> selectAccountListWithUser(Page<AccountVO> page, @Param("userId") Long userId, String phone);
} 