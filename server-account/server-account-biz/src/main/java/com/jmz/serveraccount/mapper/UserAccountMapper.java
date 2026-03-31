package com.jmz.serveraccount.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jmz.serveraccount.entity.UserAccount;
import com.jmz.serveraccount.vo.AccountVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface UserAccountMapper extends BaseMapper<UserAccount> {

    IPage<AccountVO> selectAccountListWithUser(IPage<AccountVO> page,
                                                @Param("userId") Long userId,
                                                @Param("phone") String phone);

    int deductBalanceWithLock(@Param("userId") Long userId,
                              @Param("amount") BigDecimal amount,
                              @Param("version") Long version,
                              @Param("idempotencyKey") String idempotencyKey);

    int freezeBalanceWithLock(@Param("userId") Long userId,
                               @Param("amount") BigDecimal amount,
                               @Param("version") Long version);

    int unfreezeBalanceWithLock(@Param("userId") Long userId,
                                 @Param("amount") BigDecimal amount,
                                 @Param("version") Long version);

    int deductFrozenWithLock(@Param("userId") Long userId,
                              @Param("amount") BigDecimal amount,
                              @Param("version") Long version);
}
