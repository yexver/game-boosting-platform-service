package com.jmz.serverorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.jmzfile.feign.RemoteFileService;
import com.jmz.serverorder.dto.GameCreateDTO;
import com.jmz.serverorder.dto.GameQueryDTO;
import com.jmz.serverorder.dto.GameUpdateDTO;
import com.jmz.serverorder.entity.Games;
import com.jmz.serverorder.entity.GameSystems;
import com.jmz.serverorder.exception.exceptions.GameException;
import com.jmz.serverorder.mapper.GamesMapper;
import com.jmz.serverorder.mapper.GameSystemsMapper;
import com.jmz.serverorder.service.GamesService;
import com.jmz.serverorder.util.GameConstants;
import com.jmz.serverorder.util.GameUtil;
import com.jmz.serverorder.vo.GameVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import io.seata.spring.annotation.GlobalTransactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import com.jmz.serverorder.entity.Servers;
import com.jmz.serverorder.mapper.ServersMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 游戏服务实现类
 */
@Slf4j
@Service
public class GamesServiceImpl extends ServiceImpl<GamesMapper, Games> implements GamesService {

    @Autowired
    private RemoteFileService remoteFileService;

    @Autowired
    private GameSystemsMapper gameSystemsMapper;

    @Autowired
    private ServersMapper serversMapper;

    @Override
    public IPage<GameVO> getGameList(GameQueryDTO queryDTO) {
        // 参数验证
        if (queryDTO.getPageNum() == null || queryDTO.getPageNum() < 1) {
            queryDTO.setPageNum(GameConstants.Default.PAGE_NUM);
        }
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) {
            queryDTO.setPageSize(GameConstants.Default.PAGE_SIZE);
        }
        
        // 构建查询条件
        LambdaQueryWrapper<Games> queryWrapper = new LambdaQueryWrapper<>();
        
        // 游戏名称模糊查询
        if (StringUtils.hasText(queryDTO.getName())) {
            queryWrapper.like(Games::getName, queryDTO.getName());
        }
        
        // 状态查询
        if (GameUtil.isValidStatus(queryDTO.getStatus())) {
            queryWrapper.eq(Games::getStatus, queryDTO.getStatus());
        }
        
        // 时间范围查询
        if (StringUtils.hasText(queryDTO.getStartTime())) {
            LocalDateTime startTime = GameUtil.parseDateTime(queryDTO.getStartTime());
            if (startTime != null) {
                queryWrapper.ge(Games::getCreatedAt, startTime);
            }
        }
        if (StringUtils.hasText(queryDTO.getEndTime())) {
            LocalDateTime endTime = GameUtil.parseDateTime(queryDTO.getEndTime());
            if (endTime != null) {
                queryWrapper.le(Games::getCreatedAt, endTime);
            }
        }
        
        // 按排序和创建时间排序
        queryWrapper.orderByAsc(Games::getSortOrder)
                   .orderByDesc(Games::getCreatedAt);
        
        // 分页查询
        Page<Games> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        IPage<Games> gamesPage = this.page(page, queryWrapper);
        
        // 转换为VO
        List<GameVO> gameVOList = gamesPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        // 构建返回结果
        Page<GameVO> resultPage = new Page<>(gamesPage.getCurrent(), gamesPage.getSize(), gamesPage.getTotal());
        resultPage.setRecords(gameVOList);
        
        return resultPage;
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public boolean createGame(GameCreateDTO createDTO, MultipartFile icon) {
        // 参数验证
        if (!StringUtils.hasText(createDTO.getName())) {
            throw new GameException(GameConstants.ErrorMessage.GAME_NAME_EMPTY);
        }
        
        // 检查游戏名称是否已存在
        LambdaQueryWrapper<Games> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Games::getName, createDTO.getName());
        if (this.count(queryWrapper) > 0) {
            throw new GameException(GameConstants.ErrorMessage.GAME_NAME_EXISTS);
        }
        
        // 处理图标文件上传
        String iconUrl = null;
        if (icon != null && !icon.isEmpty()) {
            try {
                R uploadResult = remoteFileService.upload(icon);
                if (uploadResult.isSuccess()) {
                    iconUrl = (String) uploadResult.get(R.DATA_TAG);
                } else {
                    throw new GameException("图标文件上传失败: " + uploadResult.get(R.MSG_TAG));
                }
            } catch (Exception e) {
                throw new GameException("图标文件上传异常: " + e.getMessage());
            }
        }
        
        Games games = new Games();
        games.setName(createDTO.getName());
        games.setIcon(iconUrl);
        games.setStatus(createDTO.getStatus() != null ? createDTO.getStatus() : GameConstants.Default.STATUS);
        games.setSortOrder(createDTO.getSortOrder() != null ? createDTO.getSortOrder() : GameConstants.Default.SORT_ORDER);
        games.setCreatedAt(new Date());
        games.setUpdatedAt(new Date());
        boolean saved = this.save(games);
        if (saved && createDTO.getSystemIds() != null && !createDTO.getSystemIds().isEmpty()) {
            List<GameSystems> relations = createDTO.getSystemIds().stream()
                .map(sysId -> {
                    GameSystems gs = new GameSystems();
                    gs.setGameId(games.getId());
                    gs.setSystemId(sysId);
                    return gs;
                })
                .collect(Collectors.toList());
            gameSystemsMapper.insertBatch(relations);
        }
        return saved;
    }

    @Override
    public boolean updateGame(Integer id, GameUpdateDTO updateDTO, MultipartFile icon) {
        // 参数验证
        if (id == null || id <= 0) {
            throw new GameException(GameConstants.ErrorMessage.GAME_ID_EMPTY);
        }
        
        Games games = this.getById(id);
        if (games == null) {
            throw new GameException(GameConstants.ErrorMessage.GAME_NOT_FOUND);
        }
        
        // 如果更新游戏名称，检查是否与其他游戏重名
        if (StringUtils.hasText(updateDTO.getName()) &&
            !updateDTO.getName().equals(games.getName())) {
            LambdaQueryWrapper<Games> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Games::getName, updateDTO.getName())
                       .ne(Games::getId, id);
            if (this.count(queryWrapper) > 0) {
                throw new GameException(GameConstants.ErrorMessage.GAME_NAME_EXISTS);
            }
        }
        
        // 处理图标文件上传
        if (icon != null && !icon.isEmpty()) {
            try {
                // 如果原来有图标，先删除旧图标
                if (StringUtils.hasText(games.getIcon())) {
                    try {
                        remoteFileService.ossDelete(games.getIcon());
                    } catch (Exception e) {
                        // 删除旧图标失败不影响新图标上传
                        log.warn("删除旧图标失败: {}", e.getMessage());
                    }
                }
                
                // 上传新图标
                R uploadResult = remoteFileService.upload(icon);
                if (uploadResult.isSuccess()) {
                    games.setIcon((String) uploadResult.get(R.DATA_TAG));
                } else {
                    throw new GameException("图标文件上传失败: " + uploadResult.get(R.MSG_TAG));
                }
            } catch (Exception e) {
                throw new GameException("图标文件上传异常: " + e.getMessage());
            }
        }
        
        // 更新字段
        if (StringUtils.hasText(updateDTO.getName())) {
            games.setName(updateDTO.getName());
        }
        if (GameUtil.isValidStatus(updateDTO.getStatus())) {
            games.setStatus(updateDTO.getStatus());
        }
        if (updateDTO.getSortOrder() != null) {
            games.setSortOrder(updateDTO.getSortOrder());
        }
        games.setUpdatedAt(new Date());
        boolean updated = this.updateById(games);
        if (updated) {
            gameSystemsMapper.deleteByGameId(id);
            if (updateDTO.getSystemIds() != null && !updateDTO.getSystemIds().isEmpty()) {
                List<GameSystems> relations = updateDTO.getSystemIds().stream()
                    .map(sysId -> {
                        GameSystems gs = new GameSystems();
                        gs.setGameId(id);
                        gs.setSystemId(sysId);
                        return gs;
                    })
                    .collect(Collectors.toList());
                gameSystemsMapper.insertBatch(relations);
            }
        }
        return updated;
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public boolean deleteGame(Integer id) {
        // 参数验证
        if (id == null || id <= 0) {
            throw new GameException(GameConstants.ErrorMessage.GAME_ID_EMPTY);
        }
        
        Games games = this.getById(id);
        if (games == null) {
            throw new GameException(GameConstants.ErrorMessage.GAME_NOT_FOUND);
        }
        
        // 删除游戏图标文件
        if (StringUtils.hasText(games.getIcon())) {
            try {
                remoteFileService.ossDelete(games.getIcon());
            } catch (Exception e) {
                // 删除文件失败不影响游戏删除
                log.warn("删除游戏图标失败: {}", e.getMessage());
            }
        }
        
        // 1. 删除所有区服
        serversMapper.delete(new LambdaQueryWrapper<Servers>().eq(Servers::getGameId, id));

        // 2. 删除所有多对多关联
        gameSystemsMapper.deleteByGameId(id);

        // 3. 删除游戏本身
        return this.removeById(id);
    }

    @Override
    public boolean changeGameStatus(Integer id, Integer status) {
        // 参数验证
        if (id == null || id <= 0) {
            throw new GameException(GameConstants.ErrorMessage.GAME_ID_EMPTY);
        }
        if (!GameUtil.isValidStatus(status)) {
            throw new GameException(GameConstants.ErrorMessage.INVALID_STATUS);
        }
        
        Games games = this.getById(id);
        if (games == null) {
            throw new GameException(GameConstants.ErrorMessage.GAME_NOT_FOUND);
        }
        
        games.setStatus(status);
        games.setUpdatedAt(new Date());
        return this.updateById(games);
    }

    @Override
    public GameVO getGameDetail(Integer id) {
        Games games = this.getById(id);
        if (games == null) {
            throw new GameException("游戏不存在");
        }
        GameVO vo = convertToVO(games);

        // 查询已绑定的系统ID列表
        List<Integer> systemIds = gameSystemsMapper.selectList(
                new LambdaQueryWrapper<GameSystems>().eq(GameSystems::getGameId, id)
        ).stream().map(GameSystems::getSystemId).collect(Collectors.toList());
        vo.setSystemIds(systemIds);

        // 如需返回系统详细信息，可查 systemsMapper
        // List<Systems> systems = systemsMapper.selectBatchIds(systemIds);
        // vo.setSystems(systems);

        return vo;
    }

    /**
     * 转换为VO
     */
    private GameVO convertToVO(Games games) {
        GameVO vo = new GameVO();
        BeanUtils.copyProperties(games, vo);
        return vo;
    }
} 
