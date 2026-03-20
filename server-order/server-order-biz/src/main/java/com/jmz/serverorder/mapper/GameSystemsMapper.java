package com.jmz.serverorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmz.serverorder.entity.GameSystems;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GameSystemsMapper extends BaseMapper<GameSystems> {
    // 批量插入
    int insertBatch(@Param("list") List<GameSystems> list);
    // 根据gameId删除
    int deleteByGameId(@Param("gameId") Integer gameId);
} 