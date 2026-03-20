package com.jmz.serveruser.controller;

import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveruser.dto.CreateUserDTO;
import com.jmz.serveruser.dto.UpdateUserDTO;
import com.jmz.serveruser.dto.UserQueryDTO;
import com.jmz.serveruser.entity.User;
import com.jmz.serveruser.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import com.jmz.serveruser.service.UserGameBoostingService;
import java.util.Collections;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserGameBoostingService userGameBoostingService;

    /**
     * 获取用户信息
     * @param userId 用户Id
     * @return
     */
    @PostMapping("/getUserInfoById/{id}")
    public R getUserById(@PathVariable("id") Long userId) {
        return R.success("获取用户信息成功", userService.getUserInfoById(userId));
    }

    /**
     * 获取用户信息集
     * @param userQuery 用户迷糊分页查询条件
     * @return
     */
    @PostMapping("/getUserInfoList")
    public R getUserInfoList(@RequestBody UserQueryDTO userQuery) {
        return R.success(userService.getUserInfoList(userQuery));
    }

    /**
     * 新增用户
     */
    @PostMapping("/createUser")
    public R createUser(@RequestBody CreateUserDTO createUserDTO) {
        return userService.createUser(createUserDTO);
    }

    /**
     * 编辑用户
     */
    @PostMapping("/updateUser")
    public R updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        return userService.updateUser(updateUserDTO);
    }

    /**
     *
     * @param userId
     * @param password
     * @return
     */
    @PostMapping("/{userId}/password")
    public R resetUserPassword( @PathVariable Long userId,
                                @RequestParam String password) {
        userService.resetUserPassword(userId,password);
        return R.success();
    }

    /**
     * 批量删除用户
     */
    @PostMapping("/deleteUserByIds")
    public R deleteUserByIds(@RequestBody List<Long> userIds) {
        System.out.println("userIds:"+userIds);
        return userService.deleteUserByIds(userIds);
    }

    /**
     * 开启/关闭代打功能，并设置代打游戏
     */
    @PostMapping("/boosting-switch")
    public R switchUserBoosting(@RequestBody Map<String, Object> data) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getUserId();
        Object enabledObj = data.get("is_boosting_enabled");
        Integer isBoostingEnabled = enabledObj == null ? null : Integer.valueOf(enabledObj.toString());
        if (isBoostingEnabled == null) {
            return R.error("is_boosting_enabled 不能为空");
        }
        // 更新用户表 is_boosting_enabled 字段
        User user = new User();
        user.setUserId(userId);
        user.setIsBoostingEnabled(isBoostingEnabled);
        userService.updateById(user);
        // 如果开启，且有 boosting_game_ids，则更新关联表
        if (isBoostingEnabled == 1 && data.get("boosting_game_ids") instanceof List) {
            List<?> ids = (List<?>) data.get("boosting_game_ids");
            userGameBoostingService.setUserBoostingGames(userId, ids);
        } else if (isBoostingEnabled == 0) {
            userGameBoostingService.setUserBoostingGames(userId, Collections.emptyList());
        }
        return R.success("操作成功");
    }

    /**
     * 批量导入用户
     */
    @PostMapping("/import")
    public R importUsers(@RequestParam("file") MultipartFile file) {
        userService.importUsers(file);
        return R.success("用户导入成功");
    }

    /**
     * 导出用户数据
     */
    @PostMapping("/export")
    public void exportUsers(@RequestBody UserQueryDTO userQuery, HttpServletResponse response) throws IOException {
        userService.exportUsers(userQuery, response);
    }
}


