package com.jmz.serverorder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jmz.serverorder.dto.GameCreateDTO;
import com.jmz.serverorder.dto.GameQueryDTO;
import com.jmz.serverorder.dto.GameUpdateDTO;
import com.jmz.serverorder.entity.Games;
import com.jmz.serverorder.vo.GameVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 游戏服务接口
 */
public interface GamesService extends IService<Games> {

    /**
     * 分页查询游戏列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<GameVO> getGameList(GameQueryDTO queryDTO);

    /**
     * 创建游戏
     *
     * @param createDTO 创建参数
     * @param icon 游戏图标文件
     * @return 创建结果
     */
    boolean createGame(GameCreateDTO createDTO, MultipartFile icon);

    /**
     * 更新游戏
     *
     * @param id        游戏ID
     * @param updateDTO 更新参数
     * @param icon 游戏图标文件
     * @return 更新结果
     */
    boolean updateGame(Integer id, GameUpdateDTO updateDTO, MultipartFile icon);

    /**
     * 删除游戏
     *
     * @param id 游戏ID
     * @return 删除结果
     */
    boolean deleteGame(Integer id);

    /**
     * 切换游戏状态
     *
     * @param id     游戏ID
     * @param status 目标状态
     * @return 切换结果
     */
    boolean changeGameStatus(Integer id, Integer status);

    GameVO getGameDetail(Integer id);
} 