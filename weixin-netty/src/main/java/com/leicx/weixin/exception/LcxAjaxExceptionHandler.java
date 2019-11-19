package com.leicx.weixin.exception;

import com.leicx.weixin.util.LcxJSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常信息捕捉(ajax请求)
 * @author daxiong
 * @date 2019-10-18 14:45
 * @return
 **/
@RestControllerAdvice
public class LcxAjaxExceptionHandler {

    /**
     * @author daxiong
     * @date 2019-06-18 16:29
     * @param request
     * @param response
     * @param e
     * @return com.leicx.util.LcxJSONResult
     **/
    @ExceptionHandler(value = Exception.class)
    public LcxJSONResult errorHandler(HttpServletRequest request,
                                      HttpServletResponse response, Exception e) {
        e.printStackTrace();

        return LcxJSONResult.errorException(e.getMessage());
    }
}
