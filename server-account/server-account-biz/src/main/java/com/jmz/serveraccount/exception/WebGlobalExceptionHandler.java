package com.jmz.serveraccount.exception;

import com.jmz.jmzcommoncore.constant.HttpStatus;
import com.jmz.jmzcommoncore.responseResult.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class WebGlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(WebGlobalExceptionHandler.class);

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public R handleNotFoundException(Exception ex) {
        String path = ex instanceof NoHandlerFoundException ?
                ((NoHandlerFoundException) ex).getRequestURL() :
                ((NoResourceFoundException) ex).getResourcePath();

        log.error("资源不存在: {}", path);
        return new R(HttpStatus.NOT_FOUND, "请求路径不存在: " + path);
    }

    @ExceptionHandler(value = Exception.class)
    public R handleException(Exception e) {
        log.error("异常原因是:", e);
        return new R(HttpStatus.ERROR, e.getMessage());
    }
}
