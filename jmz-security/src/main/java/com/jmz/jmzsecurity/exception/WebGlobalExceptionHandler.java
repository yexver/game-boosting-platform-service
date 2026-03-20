package com.jmz.jmzsecurity.exception;


import com.jmz.jmzcommoncore.constant.HttpStatus;
import com.jmz.jmzcommoncore.responseResult.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class WebGlobalExceptionHandler {
    private Logger log = LoggerFactory.getLogger(WebGlobalExceptionHandler.class);
    @ExceptionHandler(AccessDeniedException.class)
    public R handleAccessDeniedException(AccessDeniedException ex) {
        log.error("权限不足", ex);
        return new R(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public R handleAuthenticationException(AuthenticationException ex) {
        log.error("登录失败", ex);
        return new R(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }
    @ExceptionHandler(value = Exception.class)
    public R handleException(Exception e) {
        log.error("异常原因是:", e);
        R result = new R(HttpStatus.ERROR, "异常原因是:" + e.getMessage());
        log.debug("返回结果为:" + result);
        return result;
    }
}
