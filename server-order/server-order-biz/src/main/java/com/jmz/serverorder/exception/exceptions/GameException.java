package com.jmz.serverorder.exception.exceptions;

import com.jmz.serverorder.exception.base.BaseException;

/**
 * 游戏相关业务异常
 */
public class GameException extends BaseException {
    public GameException(String module, String code, Object[] args, String defaultMessage) {
        super(module, code, args, defaultMessage);
    }

    public GameException(String module, String code, Object[] args) {
        super(module, code, args);
    }

    public GameException(String module, String defaultMessage) {
        super(module, defaultMessage);
    }

    public GameException(String code, Object[] args) {
        super(code, args);
    }

    public GameException(String defaultMessage) {
        super(defaultMessage);
    }
} 