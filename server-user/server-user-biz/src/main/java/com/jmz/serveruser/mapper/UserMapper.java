package com.jmz.serveruser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmz.serveruser.entity.User;
import com.jmz.serveruser.vo.LoginUserInfoVo;
import com.jmz.serveruser.vo.UserInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;
import com.jmz.serveruser.vo.UserAddTrendVO;
import com.jmz.serveruser.vo.UserStatsVO;
import com.jmz.serveruser.vo.BoostingUserVO;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 使用lambdaQuery创建wrapper条件，查询用户信息
     * @param userId 用户ID
     * @return LoginUserInfoVo 用户信息
     */
    LoginUserInfoVo getLoginUserInfo(Long userId);

    Set<String> getUserRoleKeywordsByUserId(Long userId);
    Set<Integer> getUserRoleIdsByUserId(Long userId);

    Set<String> getUserPermissionKeywordsByUserId(Long userId);
    Set<Integer> getUserPermissionIdsByUserId(Long userId);

    UserInfoVo getUserInfo(Long userId);

    int deleteBatchIds(List<Long> userIds);

    /**
     * 查询最近N天每天的新增用户数
     * @param days 最近多少天
     * @return 每天的日期和新增用户数
     */
    List<UserAddTrendVO> getUserAddTrend(int days);

    /**
     * 查询用户总数和今日新增用户数
     */
    UserStatsVO getUserStats();

    /**
     * 分页查询代练用户列表
     */
    List<BoostingUserVO> selectBoostingUsers(@Param("gameId") Integer gameId, @Param("username") String username, @Param("offset") int offset, @Param("pageSize") int pageSize);
    int countBoostingUsers(@Param("gameId") Integer gameId, @Param("username") String username);
}