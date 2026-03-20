package com.jmz.serveruser.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmz.jmzcommoncore.utils.StringUtils;
import com.jmz.serveruser.dto.UserQueryDTO;
import com.jmz.serveruser.dto.CreateUserDTO;
import com.jmz.serveruser.dto.UpdateUserDTO;
import com.jmz.serveruser.entity.User;
import com.jmz.serveruser.entity.UserRole;
import com.jmz.serveruser.mapper.UserMapper;
import com.jmz.serveruser.mapper.UserRoleMapper;
import com.jmz.serveruser.service.UserService;
import com.jmz.serveruser.service.UserAccountService;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveruser.vo.LoginUserInfoVo;
import com.jmz.serveruser.vo.UserInfoVo;
import com.jmz.serveruser.vo.UserAddTrendVO;
import com.jmz.serveruser.vo.UserStatsVO;
import com.jmz.serveruser.vo.BoostingUserVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.jmz.serveruser.entity.UserAccount;
import java.util.Map;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.ArrayList;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public IPage<User> getUserInfoList(UserQueryDTO userQuery) {
        // 构建分页对象
        Page<User> page = new Page<>(userQuery.getCurrent(), userQuery.getSize());

        // 构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(userQuery.getUserId())) {
            wrapper.eq(User::getUserId, userQuery.getUserId());
        }
        if (StringUtils.isNotBlank(userQuery.getUsername())) {
            wrapper.like(User::getUsername, userQuery.getUsername());
        }
        if (StringUtils.isNotBlank(userQuery.getPhone())) {
            wrapper.like(User::getPhone, userQuery.getPhone());
        }
        if (userQuery.getStatus() != null) {
            wrapper.eq(User::getStatus, userQuery.getStatus());
        }
        if (StringUtils.isNotBlank(userQuery.getBeginCreateTime()) && StringUtils.isNotBlank(userQuery.getEndCreateTime())) {
            wrapper.between(User::getCreateTime, userQuery.getBeginCreateTime(), userQuery.getEndCreateTime());
        }

        // 执行查询
        return this.baseMapper.selectPage(page, wrapper);
    }

    @Transactional
    public R createUser(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setUserId(IdUtil.getSnowflake().nextId());
        user.setUsername(createUserDTO.getUsername());
        user.setPhone(createUserDTO.getPhone());
        user.setEmail(createUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        this.save(user);
        // 创建用户账户
        userAccountService.createAccountForUser(user.getUserId());
        // 处理角色
        if (createUserDTO.getRoles() != null && !createUserDTO.getRoles().isEmpty()) {
            List<UserRole> userRoles = createUserDTO.getRoles().stream()
                    .map(roleId -> {
                        UserRole ur = new UserRole();
                        ur.setUserId(user.getUserId());
                        ur.setRoleId(roleId);
                        return ur;
                    }).collect(Collectors.toList());
            // 批量插入
            userRoleMapper.insertBatchSomeColumn(userRoles);
        }
        return R.success("用户创建成功");
    }

    @Transactional
    public R updateUser(UpdateUserDTO updateUserDTO) {
        User user = this.getById(updateUserDTO.getUserId());
        if (user == null) return R.error("用户不存在");
        if (updateUserDTO.getUsername() != null) user.setUsername(updateUserDTO.getUsername());
        if (updateUserDTO.getPhone() != null) user.setPhone(updateUserDTO.getPhone());
        if (updateUserDTO.getEmail() != null) user.setEmail(updateUserDTO.getEmail());
        if (updateUserDTO.getNickname() != null) user.setNickname(updateUserDTO.getNickname());
        if (updateUserDTO.getAvatar() != null) user.setAvatar(updateUserDTO.getAvatar());
        if (updateUserDTO.getGender() != null) user.setGender(updateUserDTO.getGender());
        if (updateUserDTO.getStatus() != null) user.setStatus(updateUserDTO.getStatus());
        if (updateUserDTO.getPassword() != null) user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        user.setUpdateTime(new Date());
        this.updateById(user);
        // 处理角色
        if (updateUserDTO.getRoles() != null) {
            // 先删后加
            userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId()));
            if (!updateUserDTO.getRoles().isEmpty()) {
                List<UserRole> userRoles = updateUserDTO.getRoles().stream()
                        .map(roleId -> {
                            UserRole ur = new UserRole();
                            ur.setUserId(user.getUserId());
                            ur.setRoleId(roleId);
                            return ur;
                        }).collect(Collectors.toList());
                userRoleMapper.insertBatchSomeColumn(userRoles);
            }
        }
        return R.success("用户更新成功");
    }

    @Transactional
    public R deleteUserByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) return R.error("用户ID不能为空");

        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().in(UserRole::getUserId, userIds));
        // 删除账户
        userAccountService.remove(new LambdaQueryWrapper<UserAccount>().in(UserAccount::getUserId, userIds));
        this.remove(new LambdaQueryWrapper<User>().in(User::getUserId, userIds));
        return R.success("用户删除成功");
    }

    @Override
    public UserInfoVo getUserInfoById(Long userId) {
        UserInfoVo userInfo = userMapper.getUserInfo(userId);
        if (userInfo == null) {
            return null;
        }
        userInfo.setRole(userMapper.getUserRoleIdsByUserId(userId));
        userInfo.setPermission(userMapper.getUserPermissionIdsByUserId(userId));
        return userInfo;
    }

    @Override
    public void resetUserPassword(Long userId, String password) {
        //加密
        String encode = passwordEncoder.encode(password);
        User user = new User();
        user.setPassword(encode);
        this.update(user, new LambdaQueryWrapper<User>().eq(User::getUserId, userId));
    }

    @Override
    public List<UserAddTrendVO> getUserAddTrend(int days) {
        return userMapper.getUserAddTrend(days);
    }

    @Override
    public UserStatsVO getUserStats() {
        return userMapper.getUserStats();
    }

    @Override
    public Map<String, Object> getBoostingUsers(Integer gameId, String username, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<BoostingUserVO> list = userMapper.selectBoostingUsers(gameId, username, offset, pageSize);
        int total = userMapper.countBoostingUsers(gameId, username);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    @Override
    @Transactional
    public void importUsers(MultipartFile file) {
        System.out.println("开始导入用户");
        System.out.println("文件名: " + file.getOriginalFilename());
        System.out.println("文件大小: " + file.getSize() + " bytes");
        
        try (InputStream is = file.getInputStream()) {
            if (file.getSize() == 0) {
                throw new RuntimeException("上传的文件为空");
            }
            
            Workbook workbook = null;
            try {
                workbook = new XSSFWorkbook(is);
            } catch (Exception e) {
                try (InputStream is2 = file.getInputStream()) {
                    workbook = new HSSFWorkbook(is2);
                }
            }
            
            if (workbook == null) {
                throw new RuntimeException("无法解析Excel文件格式");
            }
            
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("Excel总行数: " + (sheet.getLastRowNum() + 1));
            
            if (sheet.getLastRowNum() == 0) {
                throw new RuntimeException("Excel文件只有表头，没有数据行");
            }
            
            List<User> userList = new ArrayList<>();
            List<UserRole> userRoleList = new ArrayList<>();
            
            // 从第1行开始读取数据（跳过表头）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                try {
                    String username = getCellValue(row.getCell(0));
                    String phone = getCellValue(row.getCell(1));
                    String email = getCellValue(row.getCell(2));
                    String password = getCellValue(row.getCell(3));
                    String roleIds = getCellValue(row.getCell(4)); // 角色ID，多个用逗号分隔
                    
                    if (username.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                        System.out.println("跳过第" + (i+1) + "行：必填字段为空");
                        continue;
                    }
                    
                    // 检查用户名和手机号是否已存在
                    if (this.count(new LambdaQueryWrapper<User>()
                            .eq(User::getUsername, username)
                            .or()
                            .eq(User::getPhone, phone)) > 0) {
                        System.out.println("跳过第" + (i+1) + "行：用户名或手机号已存在");
                        continue;
                    }
                    
                    User user = new User();
                    user.setUserId(IdUtil.getSnowflake().nextId());
                    user.setUsername(username);
                    user.setPhone(phone);
                    user.setEmail(email.isEmpty() ? null : email);
                    user.setPassword(passwordEncoder.encode(password));
                    user.setStatus(1); // 默认启用
                    user.setCreateTime(new Date());
                    user.setUpdateTime(new Date());
                    userList.add(user);
                    
                    // 处理角色
                    if (!roleIds.isEmpty()) {
                        String[] roleIdArray = roleIds.split(",");
                        for (String roleIdStr : roleIdArray) {
                            try {
                                Integer roleId = Integer.valueOf(roleIdStr.trim());
                                UserRole userRole = new UserRole();
                                userRole.setUserId(user.getUserId());
                                userRole.setRoleId(roleId);
                                userRoleList.add(userRole);
                            } catch (NumberFormatException e) {
                                System.out.println("无效的角色ID: " + roleIdStr);
                            }
                        }
                    }
                    
                } catch (Exception e) {
                    System.out.println("跳过第" + (i+1) + "行，错误: " + e.getMessage());
                }
            }
            
            System.out.println("准备保存用户数量: " + userList.size());
            
            // 批量保存用户
            if (!userList.isEmpty()) {
                this.saveBatch(userList);
                
                // 为每个用户创建账户
                for (User user : userList) {
                    userAccountService.createAccountForUser(user.getUserId());
                }
                
                // 批量保存用户角色关系
                if (!userRoleList.isEmpty()) {
                    userRoleMapper.insertBatchSomeColumn(userRoleList);
                }
                
                System.out.println("成功导入用户数量: " + userList.size());
            }
            
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("用户批量导入失败: " + e.getMessage());
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    @Override
    public void exportUsers(UserQueryDTO userQuery, HttpServletResponse response) throws IOException {
        // 构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(userQuery.getUserId())) {
            wrapper.eq(User::getUserId, userQuery.getUserId());
        }
        if (StringUtils.isNotBlank(userQuery.getUsername())) {
            wrapper.like(User::getUsername, userQuery.getUsername());
        }
        if (StringUtils.isNotBlank(userQuery.getPhone())) {
            wrapper.like(User::getPhone, userQuery.getPhone());
        }
        if (userQuery.getStatus() != null) {
            wrapper.eq(User::getStatus, userQuery.getStatus());
        }
        if (StringUtils.isNotBlank(userQuery.getBeginCreateTime()) && StringUtils.isNotBlank(userQuery.getEndCreateTime())) {
            wrapper.between(User::getCreateTime, userQuery.getBeginCreateTime(), userQuery.getEndCreateTime());
        }
        
        // 查询用户数据
        List<User> userList = this.list(wrapper);
        
        // 创建Excel工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("用户数据");
        
        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"用户ID", "用户名", "手机号", "邮箱", "昵称", "性别", "状态", "创建时间", "角色"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        
        // 填充数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            Row row = sheet.createRow(i + 1);
            
            row.createCell(0).setCellValue(user.getUserId().toString());
            row.createCell(1).setCellValue(user.getUsername());
            row.createCell(2).setCellValue(user.getPhone());
            row.createCell(3).setCellValue(user.getEmail() != null ? user.getEmail() : "");
            row.createCell(4).setCellValue(user.getNickname() != null ? user.getNickname() : "");
            row.createCell(5).setCellValue(user.getGender() != null ? (user.getGender() == 1 ? "男" : "女") : "");
            row.createCell(6).setCellValue(user.getStatus() != null ? (user.getStatus() == 1 ? "正常" : "禁用") : "");
            row.createCell(7).setCellValue(user.getCreateTime() != null ? sdf.format(user.getCreateTime()) : "");
            
            // 获取用户角色
            Set<Integer> roleIds = userMapper.getUserRoleIdsByUserId(user.getUserId());
            String roleStr = roleIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            row.createCell(8).setCellValue(roleStr);
        }
        
        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // 设置响应头
        String fileName = "用户数据_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        
        // 写入响应流
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
