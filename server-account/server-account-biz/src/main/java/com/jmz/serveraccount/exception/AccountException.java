package com.jmz.serveraccount.exception;

/**
 * 账户业务异常
 */
public class AccountException extends RuntimeException {

    private final String code;

    public AccountException(String message) {
        super(message);
        this.code = "ACCOUNT_ERROR";
    }

    public AccountException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static AccountException balanceInsufficient() {
        return new AccountException("BALANCE_INSUFFICIENT", "余额不足");
    }

    public static AccountException frozenInsufficient() {
        return new AccountException("FROZEN_INSUFFICIENT", "冻结金额不足");
    }

    public static AccountException concurrentConflict() {
        return new AccountException("CONCURRENT_CONFLICT", "并发冲突，请稍后重试");
    }

    public static AccountException accountNotFound() {
        return new AccountException("ACCOUNT_NOT_FOUND", "账户不存在");
    }

    public static AccountException invalidOperation() {
        return new AccountException("INVALID_OPERATION", "不支持的账户操作类型");
    }
}
