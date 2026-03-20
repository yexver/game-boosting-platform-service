package com.jmz.serveruser.service;

import java.util.List;

public interface UserGameBoostingService {
    List<Integer> getGameIdsByUserId(Long userId);
    void setUserBoostingGames(Long userId, List<?> gameIds);
} 