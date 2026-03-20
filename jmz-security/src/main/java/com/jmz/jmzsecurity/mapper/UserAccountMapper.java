package com.jmz.jmzsecurity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmz.serveruser.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {
} 