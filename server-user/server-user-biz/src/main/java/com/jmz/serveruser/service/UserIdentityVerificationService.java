package com.jmz.serveruser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jmz.serveruser.entity.UserIdentityVerification;
import com.jmz.serveruser.dto.IdentityVerificationDTO;
import com.jmz.serveruser.dto.IdentityAuditDTO;
import com.jmz.serveruser.vo.IdentityVerificationVO;
import com.jmz.jmzcommoncore.responseResult.R;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface UserIdentityVerificationService extends IService<UserIdentityVerification> {
    
    /**
     * 获取用户实名认证信息
     */
    UserIdentityVerification getUserIdentityInfo(Long userId);
    
    /**
     * 提交实名认证
     */
    R submitIdentityVerification(Long userId, IdentityVerificationDTO dto, 
                               MultipartFile idCardFront, MultipartFile idCardBack, 
                               MultipartFile facePhoto) throws Exception;
    
    /**
     * 获取实名认证列表（管理员）
     */
    Map<String, Object> getIdentityList(Integer page, Integer pageSize, Integer status);
    
    /**
     * 审核实名认证
     */
    R auditIdentity(Long verifierId, IdentityAuditDTO auditDTO);
}