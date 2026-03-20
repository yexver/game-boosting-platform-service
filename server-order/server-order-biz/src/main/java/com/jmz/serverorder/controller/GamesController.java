package com.jmz.serverorder.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serverorder.dto.GameCreateDTO;
import com.jmz.serverorder.dto.GameQueryDTO;
import com.jmz.serverorder.dto.GameUpdateDTO;
import com.jmz.serverorder.service.GamesService;
import com.jmz.serverorder.vo.GameVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 游戏管理控制器
 * 
 * 基础信息:
 * - 基础路径: /games
 * - 认证方式: Token认证 (isToken: true)
 * - 防重复提交: 关闭 (repeatSubmit: false)
 */
@RestController
@RequestMapping("/games")
public class GamesController {

    @Autowired
    private GamesService gamesService;

    /**
     * 获取游戏列表
     * 
     * @param queryDTO 查询参数
     * @return 游戏列表数据
     */
    @GetMapping
    public R getGameList(GameQueryDTO queryDTO) {
        IPage<GameVO> result = gamesService.getGameList(queryDTO);
        return R.success(result);
    }

    /**
     * 新增游戏
     * 
     * @param createDTO 游戏数据
     * @param icon 游戏图标文件
     * @return 创建结果
     */
    @PostMapping
    public R createGame(@Validated @RequestPart("gameData") GameCreateDTO createDTO,
                       @RequestPart(value = "icon", required = false) MultipartFile icon) {
        boolean result = gamesService.createGame(createDTO, icon);
        return R.success(result);
    }

    /**
     * 更新游戏
     * 
     * @param id 游戏ID
     * @param updateDTO 更新的游戏数据
     * @param icon 游戏图标文件
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public R updateGame(@PathVariable Integer id, 
                       @Validated @RequestPart("gameData") GameUpdateDTO updateDTO,
                       @RequestPart(value = "icon", required = false) MultipartFile icon) {

        boolean result = gamesService.updateGame(id, updateDTO, icon);
        return R.success(result);
    }

    /**
     * 删除游戏
     * 
     * @param id 游戏ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public R deleteGame(@PathVariable Integer id) {
        boolean result = gamesService.deleteGame(id);
        return R.success(result);
    }

    /**
     * 切换游戏状态
     * 
     * @param id 游戏ID
     * @param status 目标状态 (0: 禁用, 1: 启用)
     * @return 状态切换结果
     */
    @PatchMapping("/{id}/status")
    public R changeGameStatus(@PathVariable Integer id, 
                             @RequestParam Integer status) {
        boolean result = gamesService.changeGameStatus(id, status);
        return R.success(result);
    }

    /**
     * 获取游戏详情（含已绑定系统ID列表）
     */
    @GetMapping("getGameDetail/{id}")
    public R getGameDetail(@PathVariable Integer id) {
        GameVO detail = gamesService.getGameDetail(id);
        return R.success(detail);
    }

    @GetMapping("/getAll")
    public R getAllGames() {
        return R.success(gamesService.list());
    }
}
