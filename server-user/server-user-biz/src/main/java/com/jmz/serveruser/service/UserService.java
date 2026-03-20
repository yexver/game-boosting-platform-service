package com.jmz.serveruser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmz.serveruser.dto.UserQueryDTO;
import com.jmz.serveruser.entity.User;
import com.jmz.serveruser.dto.CreateUserDTO;
import com.jmz.serveruser.dto.UpdateUserDTO;
import java.util.List;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveruser.vo.UserInfoVo;
import com.jmz.serveruser.vo.UserAddTrendVO;
import com.jmz.serveruser.vo.UserStatsVO;
import com.jmz.serveruser.vo.BoostingUserVO;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService extends IService<User> {
    Object getUserInfoList(UserQueryDTO userQuery);
    R createUser(CreateUserDTO createUserDTO);
    R updateUser(UpdateUserDTO updateUserDTO);
    R deleteUserByIds(List<Long> userIds);

    UserInfoVo getUserInfoById(Long userId);

    void resetUserPassword(Long userId, String password);

    /**
     * 获取新增用户趋势数据
     * @param days 最近多少天
     * @return 每天的日期和新增用户数
     */
    List<UserAddTrendVO> getUserAddTrend(int days);

    /**
     * 获取用户总数和今日新增
     */
    UserStatsVO getUserStats();

    /**
     * 分页查询代练用户列表
     */
    Map<String, Object> getBoostingUsers(Integer gameId, String username, int page, int pageSize);

    /**
     * 批量导入用户
     * @param file Excel文件
     */
    void importUsers(MultipartFile file);

    /**
     * 导出用户数据
     * @param userQuery 查询条件
     * @param response HTTP响应
     */
    void exportUsers(UserQueryDTO userQuery, HttpServletResponse response) throws IOException;
}
