package com.jmz.serveruser.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.util.Date;

@Data
@TableName("tb_jmz_user_identity_verification")
public class UserIdentityVerification {
    
    // 字段常量定义
    public static final String ID = "id";
    public static final String USER_ID = "userId";
    public static final String REAL_NAME = "realName";
    public static final String ID_CARD_NUMBER = "idCardNumber";
    public static final String ID_CARD_FRONT_URL = "idCardFrontUrl";
    public static final String ID_CARD_BACK_URL = "idCardBackUrl";
    public static final String FACE_PHOTO_URL = "facePhotoUrl";
    public static final String VERIFICATION_STATUS = "verificationStatus";
    public static final String REJECT_REASON = "rejectReason";
    public static final String VERIFIER_ID = "verifierId";
    public static final String VERIFIED_AT = "verifiedAt";
    public static final String CREATED_AT = "createdAt";
    public static final String UPDATED_AT = "updatedAt";
    
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    
    private String realName;
    private String idCardNumber;
    private String idCardFrontUrl;
    private String idCardBackUrl;
    private String facePhotoUrl;
    private Integer verificationStatus; // 0-待审核，1-审核通过，2-审核拒绝
    private String rejectReason;
    
    @JsonSerialize(using = ToStringSerializer.class)
    private Long verifierId;
    
    private Date verifiedAt;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;
}

