package com.jmz.serveruser.controller;

import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import com.jmz.serveruser.service.LoginUserService;
import com.jmz.serveruser.service.UserService;
import com.jmz.serveraccount.feign.UserAccountFeignClient;
import com.jmz.serveruser.service.UserGameBoostingService;
import com.jmz.serveraccount.vo.AccountVO;
import com.jmz.serveruser.dto.UpdateUserDTO;
import com.jmz.serveruser.vo.UserInfoVo;
import com.jmz.jmzfile.feign.RemoteFileService;
import lombok.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import com.jmz.serveruser.entity.User;
import com.jmz.serveraccount.entity.UserAccount;

@RestController

public class LoginUserController {
    @Autowired
    private LoginUserService loginUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAccountFeignClient userAccountFeignClient;
    @Autowired
    private RemoteFileService remoteFileService;
    @Autowired
    private UserGameBoostingService userGameBoostingService;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 获取当前用户信息
     * @return
     */
    @PostMapping("/getLoginUserInfo")
    @ResponseBody
    public R getLoginUserInfo() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return R.success("获取用户信息成功", loginUserService.getInfo(loginUser.getUserId()));
    }

    /**
     * 获取当前用户信息和账户信息
     */
    @PostMapping("/getUserProfile")
    @ResponseBody
    public R getUserProfile() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfoVo userInfo = userService.getUserInfoById(loginUser.getUserId());
        R accountResult = userAccountFeignClient.getAccountByUserId(loginUser.getUserId());
        AccountVO accountVO = null;
        if (accountResult != null && accountResult.isSuccess() && accountResult.get(R.DATA_TAG) != null) {
            UserAccount account = objectMapper.convertValue(accountResult.get(R.DATA_TAG), UserAccount.class);
            Long accountId = account.getId();
            R detailResult = userAccountFeignClient.getAccountDetail(accountId);
            if (detailResult != null && detailResult.isSuccess()) {
                accountVO = objectMapper.convertValue(detailResult.get(R.DATA_TAG), AccountVO.class);
            }
        }
        return R.success("获取用户信息成功", new Object[]{userInfo, accountVO});
    }

    /**
     * 更新当前用户信息和/或头像
     * @param updateUserDTO 用户信息（可选）
     * @param avatar 头像图片（可选）
     * @return
     */
    @PostMapping("/updateUserProfile")
    @ResponseBody
    public R updateUserProfile(@RequestPart(value = "user", required = false) UpdateUserDTO updateUserDTO,
                               @RequestPart(value = "avatar", required = false) MultipartFile avatar) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean hasUpdate = false;
        if (updateUserDTO != null) {
            updateUserDTO.setUserId(loginUser.getUserId());
            userService.updateUser(updateUserDTO);
            hasUpdate = true;
        }
        if (avatar != null && !avatar.isEmpty()) {
            // 调用文件服务上传头像
            R uploadResult = remoteFileService.upload(avatar);
            System.out.println("Upload result: " + uploadResult);
            System.out.println("Upload success: " + uploadResult.isSuccess());
            System.out.println("Upload data: " + uploadResult.get(R.DATA_TAG));
            
            if (uploadResult.isSuccess() && uploadResult.get(R.DATA_TAG) != null) {
                String avatarUrl = uploadResult.get(R.DATA_TAG).toString();
                System.out.println("Avatar URL: " + avatarUrl);
                UpdateUserDTO avatarDTO = new UpdateUserDTO();
                avatarDTO.setUserId(loginUser.getUserId());
                avatarDTO.setAvatar(avatarUrl);
                userService.updateUser(avatarDTO);
                hasUpdate = true;
            } else {
                return R.error("头像上传失败");
            }
        }
        if (!hasUpdate) {
            return R.error("未提交任何可更新内容");
        }
        return R.success("更新成功");
    }

    /**
     * 获取当前用户已选的代打游戏ID集合
     */
    @GetMapping("/boosting-games")
    public R getUserBoostingGames() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return R.success(userGameBoostingService.getGameIdsByUserId(loginUser.getUserId()));
    }

    /**
     * 分页查询代练用户列表
     */
    @GetMapping("/getBoostingUserList")
    public R getBoostingUserList(@RequestParam(value = "gameId", required = false) Integer gameId,
                                 @RequestParam(value = "username", required = false) String username,
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return R.success(userService.getBoostingUsers(gameId, username, page, pageSize));
    }

    @Data
    public static class BoostingSwitchDTO {
        private Integer is_boosting_enabled;
        private List<Integer> boosting_game_ids;
    }

    /**
     * 开启/关闭代打功能，并设置代打游戏
     */
    @PostMapping("/boosting-switch")
    public R switchUserBoosting(@RequestBody BoostingSwitchDTO data) {
        Integer isBoostingEnabled = data.getIs_boosting_enabled();
        List<Integer> boostingGameIds = data.getBoosting_game_ids();
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getUserId();
        if (isBoostingEnabled == null) {
            return R.error("is_boosting_enabled 不能为空");
        }
        // 更新用户表 is_boosting_enabled 字段
        User user = new User();
        user.setUserId(userId);
        System.out.println("is_boosting_enabled: =====================" + isBoostingEnabled);
        user.setIsBoostingEnabled(isBoostingEnabled);
        userService.updateById(user);
        // 如果开启，且有 boosting_game_ids，则更新关联表
        if (isBoostingEnabled == 1 && boostingGameIds != null && !boostingGameIds.isEmpty()) {
            userGameBoostingService.setUserBoostingGames(userId, boostingGameIds);
        } /*else if (isBoostingEnabled == 0) {
            userGameBoostingService.setUserBoostingGames(userId, Collections.emptyList());
        }*/
        return R.success("操作成功");
    }

    
}
