package com.jmz.serveruser.controller;

import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveruser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private UserService userService;

    /**
     * 获取新增用户数折线图数据
     * @param days 最近多少天
     * @return 折线图数据
     */
    @GetMapping("/user-add-trend")
    public R getUserAddTrend(@RequestParam(value = "days", defaultValue = "7") int days) {
        return R.success(userService.getUserAddTrend(days));
    }

    /**
     * 获取用户总数和今日新增
     */
    @GetMapping("/user-stats")
    public R getUserStats() {
        return R.success(userService.getUserStats());
    }
}
