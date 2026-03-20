package com.jmz.serveruser.controller;

import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.jmzcommonsecuritydomain.domain.LoginUser;
import com.jmz.serveruser.dto.IdentityAuditDTO;
import com.jmz.serveruser.dto.IdentityVerificationDTO;
import com.jmz.serveruser.entity.UserIdentityVerification;
import com.jmz.serveruser.service.UserIdentityVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/identity")
public class UserIdentityVerificationController {
    
    @Autowired
    private UserIdentityVerificationService identityService;
    
    /**
     * 获取用户实名认证信息
     */
    @GetMapping("/info")
    public R getIdentityInfo() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserIdentityVerification identity = identityService.getUserIdentityInfo(loginUser.getUserId());
        return R.success("获取成功", identity);
    }
    
    /**
     * 提交实名认证
     */
    @PostMapping("/submit")
    public R submitIdentity(@Validated IdentityVerificationDTO dto,
                           @RequestParam("idCardFront") MultipartFile idCardFront,
                           @RequestParam("idCardBack") MultipartFile idCardBack,
                           @RequestParam(value = "facePhoto", required = false) MultipartFile facePhoto) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return identityService.submitIdentityVerification(loginUser.getUserId(), dto, idCardFront, idCardBack, facePhoto);
    }
    
    /**
     * 获取实名认证列表（管理员）
     */
    @PostMapping("/list")
    public R getIdentityList(@RequestBody Map<String, Object> params) {
        Integer page = (Integer) params.getOrDefault("page", 1);
        Integer pageSize = (Integer) params.getOrDefault("pageSize", 10);
        Integer status = (Integer) params.get("status");
        
        Map<String, Object> result = identityService.getIdentityList(page, pageSize, status);
        return R.success("获取成功", result);
    }
    
    /**
     * 审核实名认证
     */
    @PostMapping("/audit")
    public R auditIdentity(@Validated @RequestBody IdentityAuditDTO auditDTO) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return identityService.auditIdentity(loginUser.getUserId(), auditDTO);
    }
}