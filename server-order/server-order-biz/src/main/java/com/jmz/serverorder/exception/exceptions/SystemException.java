package com.jmz.serverorder.exception.exceptions;

import com.jmz.serverorder.exception.base.BaseException;

/**
 * 系统管理相关业务异常
 */
public class SystemException extends BaseException {
    public SystemException(String module, String code, Object[] args, String defaultMessage) {
        super(module, code, args, defaultMessage);
    }

    public SystemException(String module, String code, Object[] args) {
        super(module, code, args);
    }

    public SystemException(String module, String defaultMessage) {
        super(module, defaultMessage);
    }

    public SystemException(String code, Object[] args) {
        super(code, args);
    }

    public SystemException(String defaultMessage) {
        super(defaultMessage);
    }
}