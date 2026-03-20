package com.jmz.serveruser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.jmzfile.feign.RemoteFileService;
import com.jmz.serveruser.dto.IdentityAuditDTO;
import com.jmz.serveruser.dto.IdentityVerificationDTO;
import com.jmz.serveruser.entity.User;
import com.jmz.serveruser.entity.UserIdentityVerification;
import com.jmz.serveruser.mapper.UserIdentityVerificationMapper;
import com.jmz.serveruser.mapper.UserMapper;
import com.jmz.serveruser.service.UserIdentityVerificationService;
import com.jmz.serveruser.vo.IdentityVerificationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserIdentityVerificationServiceImpl extends ServiceImpl<UserIdentityVerificationMapper, UserIdentityVerification> 
        implements UserIdentityVerificationService {

    @Autowired
    private UserIdentityVerificationMapper identityMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RemoteFileService remoteFileService;

    @Override
    public UserIdentityVerification getUserIdentityInfo(Long userId) {
        LambdaQueryWrapper<UserIdentityVerification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserIdentityVerification::getUserId, userId);
        return this.getOne(wrapper);
    }

    @Override
    @Transactional
    public R submitIdentityVerification(Long userId, IdentityVerificationDTO dto, 
                                      MultipartFile idCardFront, MultipartFile idCardBack, 
                                      MultipartFile facePhoto) throws Exception {
        
        // 检查是否已存在认证记录
        UserIdentityVerification existing = getUserIdentityInfo(userId);
        if (existing != null && existing.getVerificationStatus() == 1) {
            return R.error("您已通过实名认证，无需重复提交");
        }
        
        // 检查身份证号是否已被使用
        LambdaQueryWrapper<UserIdentityVerification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserIdentityVerification::getIdCardNumber, dto.getIdCardNumber())
               .ne(UserIdentityVerification::getUserId, userId);
        UserIdentityVerification duplicate = this.getOne(wrapper);
        if (duplicate != null) {
            return R.error("该身份证号已被其他用户使用");
        }
        
        try {
            // 上传文件
            R frontResult = remoteFileService.upload(idCardFront);
            if (!frontResult.isSuccess()) {
                return R.error("身份证正面照片上传失败");
            }
            
            R backResult = remoteFileService.upload(idCardBack);
            if (!backResult.isSuccess()) {
                return R.error("身份证反面照片上传失败");
            }
            
            String facePhotoUrl = null;
            if (facePhoto != null && !facePhoto.isEmpty()) {
                R faceResult = remoteFileService.upload(facePhoto);
                if (faceResult.isSuccess()) {
                    facePhotoUrl = (String) faceResult.get(R.DATA_TAG);
                }
            }
            
            // 保存或更新认证信息
            UserIdentityVerification identity = existing != null ? existing : new UserIdentityVerification();
            identity.setUserId(userId);
            identity.setRealName(dto.getRealName());
            identity.setIdCardNumber(dto.getIdCardNumber());
            identity.setIdCardFrontUrl((String) frontResult.get(R.DATA_TAG));
            identity.setIdCardBackUrl((String) backResult.get(R.DATA_TAG));
            identity.setFacePhotoUrl(facePhotoUrl);
            identity.setVerificationStatus(0); // 待审核
            identity.setRejectReason(null);
            identity.setVerifierId(null);
            identity.setVerifiedAt(null);
            
            this.saveOrUpdate(identity);
            
            return R.success("实名认证提交成功，请等待审核");
            
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @Override
    public Map<String, Object> getIdentityList(Integer page, Integer pageSize, Integer status) {
        int offset = (page - 1) * pageSize;
        
        List<IdentityVerificationVO> list = identityMapper.selectIdentityList(offset, pageSize, status);
        Long total = identityMapper.selectIdentityCount(status);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        
        return result;
    }

    @Override
    @Transactional
    public R auditIdentity(Long verifierId, IdentityAuditDTO auditDTO) {
        UserIdentityVerification identity = this.getById(auditDTO.getId());
        if (identity == null) {
            return R.error("认证记录不存在");
        }
        
        if (identity.getVerificationStatus() != 0) {
            return R.error("该认证已处理，无法重复审核");
        }
        
        // 更新认证状态
        identity.setVerificationStatus(auditDTO.getStatus());
        identity.setRejectReason(auditDTO.getRejectReason());
        identity.setVerifierId(verifierId);
        //identity.setVerifiedAt(new Date());
        
        this.updateById(identity);
        
        // 如果审核通过，更新用户表的认证状态
        if (auditDTO.getStatus() == 1) {
            User user = new User();
            user.setUserId(identity.getUserId());
            user.setIdentityVerified(1);
            userMapper.updateById(user);
        }
        
        return R.success("审核完成");
    }
}

