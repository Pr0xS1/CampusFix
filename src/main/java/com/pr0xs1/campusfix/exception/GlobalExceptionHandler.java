package com.pr0xs1.campusfix.exception;

import com.pr0xs1.campusfix.pojo.Result;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace();
        return Result.error("服务器异常，请联系管理员");
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingParam(MissingServletRequestParameterException e) {
        return Result.error("缺少必要参数：" + e.getParameterName());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleMethod(HttpRequestMethodNotSupportedException e) {
        return Result.error("请求方法不支持：" + e.getMethod());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return Result.error("参数「" + e.getName() + "」格式不正确");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleUnreadable(HttpMessageNotReadableException e) {
        return Result.error("请求体格式错误，请检查 JSON");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Result handleNotFound(NoResourceFoundException e) {
        return Result.error("接口不存在：" + e.getResourcePath());
    }



}
