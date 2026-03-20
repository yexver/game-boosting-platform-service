package com.jmz.serverorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmz.serveruser.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 * 用于订单服务中的用户信息查询
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
} 