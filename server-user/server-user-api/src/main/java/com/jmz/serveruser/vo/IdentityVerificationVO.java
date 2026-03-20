package com.jmz.serveruser.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IdentityVerificationVO {
    private Long id;
    private Long userId;
    private String username;
    private String realName;
    private String idCardNumber;
    private String idCardFrontUrl;
    private String idCardBackUrl;
    private String facePhotoUrl;
    private Integer verificationStatus;
    private String rejectReason;
    private String verifierName;
    private LocalDateTime verifiedAt;
    private LocalDateTime createdAt;
}