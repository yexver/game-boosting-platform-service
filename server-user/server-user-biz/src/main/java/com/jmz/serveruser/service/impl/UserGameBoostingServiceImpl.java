package com.jmz.serveruser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmz.serveruser.mapper.UserGameBoostingMapper;
import com.jmz.serveruser.entity.UserGameBoosting;
import com.jmz.serveruser.service.UserGameBoostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserGameBoostingServiceImpl
    extends ServiceImpl<UserGameBoostingMapper, UserGameBoosting>
    implements UserGameBoostingService {
    @Autowired
    private UserGameBoostingMapper userGameBoostingMapper;

    @Override
    public List<Integer> getGameIdsByUserId(Long userId) {
        return userGameBoostingMapper.getGameIdsByUserId(userId);
    }

    @Override
    @Transactional
    public void setUserBoostingGames(Long userId, List<?> gameIds) {
        // 删除原有
        userGameBoostingMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.jmz.serveruser.entity.UserGameBoosting>().eq("user_id", userId));
        // 批量插入新
        if (gameIds != null && !gameIds.isEmpty()) {
            List<com.jmz.serveruser.entity.UserGameBoosting> list = new java.util.ArrayList<>();
            for (Object gid : gameIds) {
                if (gid == null) continue;
                Integer gameId = gid instanceof Integer ? (Integer) gid : Integer.valueOf(gid.toString());
                com.jmz.serveruser.entity.UserGameBoosting rel = new com.jmz.serveruser.entity.UserGameBoosting();
                rel.setUserId(userId);
                rel.setGameId(gameId);
                rel.setCreatedAt(new java.util.Date());
                rel.setUpdatedAt(new java.util.Date());
                list.add(rel);
            }
            if (!list.isEmpty()) {
                this.saveBatch(list);
            }
        }
    }
} 