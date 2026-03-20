package com.jmz.serverorder.exception;

import com.jmz.jmzcommoncore.constant.HttpStatus;
import com.jmz.jmzcommoncore.responseResult.R;
import com.jmz.serverorder.exception.exceptions.GameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class WebGlobalExceptionHandler {
    private Logger log = LoggerFactory.getLogger(WebGlobalExceptionHandler.class);
    
    /**
     * 处理游戏相关业务异常
     */
    @ExceptionHandler(GameException.class)
    public R handleGameException(GameException ex) {
        log.error("游戏业务异常: {}", ex.getMessage());
        return new R(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    
/*    @ExceptionHandler(AccessDeniedException.class)
    public R handleAccessDeniedException(AccessDeniedException ex) {
        log.error("权限不足", ex);
        return new R(HttpStatus.FORBIDDEN, ex.getMessage());
    }*/
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public R handleNotFoundException(Exception ex) {
        String path = ex instanceof NoHandlerFoundException ?
                ((NoHandlerFoundException)ex).getRequestURL() :
                ((NoResourceFoundException)ex).getResourcePath();

        log.error("资源不存在: {}", path);
        return new R(HttpStatus.NOT_FOUND, "请求路径不存在: " + path);
    }


    @ExceptionHandler(value = Exception.class)
    public R handleException(Exception e) {
        log.error("异常原因是:", e);
        R result = new R(HttpStatus.ERROR, e.getMessage());
        log.debug("返回结果为:" + result);
        return result;
    }

}
