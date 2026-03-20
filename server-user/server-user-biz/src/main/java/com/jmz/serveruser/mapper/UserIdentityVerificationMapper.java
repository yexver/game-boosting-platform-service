package com.jmz.serveruser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jmz.serveruser.entity.UserIdentityVerification;
import com.jmz.serveruser.vo.IdentityVerificationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserIdentityVerificationMapper extends BaseMapper<UserIdentityVerification> {
    
    /**
     * 分页查询实名认证列表
     */
    List<IdentityVerificationVO> selectIdentityList(@Param("offset") int offset, 
                                                   @Param("pageSize") int pageSize, 
                                                   @Param("status") Integer status);
    
    /**
     * 查询总数
     */
    Long selectIdentityCount(@Param("status") Integer status);
}