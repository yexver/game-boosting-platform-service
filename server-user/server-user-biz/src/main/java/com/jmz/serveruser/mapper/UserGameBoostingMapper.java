package com.jmz.serveruser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmz.serveruser.entity.UserGameBoosting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserGameBoostingMapper extends BaseMapper<UserGameBoosting> {
    @Select("SELECT game_id FROM tb_jmz_user_game_boosting WHERE user_id = #{userId}")
    List<Integer> getGameIdsByUserId(Long userId);
} 