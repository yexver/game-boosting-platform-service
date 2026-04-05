package com.jmz.serveraccount.feign.fallback;

import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serveraccount.dto.AccountAdjustDTO;
import com.jmz.serveraccount.feign.UserAccountFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserAccountFeignClientFallback implements UserAccountFeignClient {

    private static final Logger log = LoggerFactory.getLogger(UserAccountFeignClientFallback.class);

    @Override
    public R adjustAccountBalance(AccountAdjustDTO adjustDTO) {
        log.error("Feign调用账户服务失败，账户调整降级处理: userId={}, type={}",
                adjustDTO.getUserId(), adjustDTO.getType());
        return R.error(503, "账户服务暂不可用，请稍后重试");
    }

    @Override
    public R unfreezeAccount(AccountAdjustDTO adjustDTO) {
        log.error("Feign调用账户服务失败，解冻账户降级处理: userId={}", adjustDTO.getUserId());
        return R.error(503, "账户服务暂不可用，请稍后重试");
    }

    @Override
    public R getAccountByUserId(Long userId) {
        log.error("Feign调用账户服务失败，获取账户信息降级处理: userId={}", userId);
        return R.error(503, "账户服务暂不可用，请稍后重试");
    }

    @Override
    public R getAccountDetail(Long accountId) {
        log.error("Feign调用账户服务失败，获取账户详情降级处理: accountId={}", accountId);
        return R.error(503, "账户服务暂不可用，请稍后重试");
    }

    @Override
    public R createAccountForUser(Long userId) {
        log.error("Feign调用账户服务失败，创建账户降级处理: userId={}", userId);
        return R.error(503, "账户服务暂不可用，请稍后重试");
    }

    @Override
    public R deleteAccountByUserId(Long userId) {
        log.error("Feign调用账户服务失败，删除账户降级处理: userId={}", userId);
        return R.error(503, "账户服务暂不可用，请稍后重试");
    }
}
