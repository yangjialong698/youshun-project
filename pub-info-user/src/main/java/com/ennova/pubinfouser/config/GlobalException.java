package com.ennova.pubinfouser.config;

import com.ennova.pubinfocommon.entity.Callback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.List;



@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = Exception.class)
    public Callback handleException(Exception e) {
        String message;
        if (e instanceof HttpRequestMethodNotSupportedException) {
            message = "不支持的访问类型";
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            message = "不支持的媒体类型";
        } else if (e instanceof HttpMediaTypeNotAcceptableException) {
            message = "不支持的媒体返回类型";
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            message = "参数错误";
        } else if (e instanceof HttpMessageNotReadableException) {
            message = "参数错误";
        } else {
            message = "抱歉，服务器异常";
        }
        log.info("服务器异常信息 --> " + e.getMessage());
        return Callback.error(2, message);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Callback handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessage = new StringBuilder("Invalid Request:\n");

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(fieldError.getDefaultMessage()).append("\n");
        }
        log.info("服务器异常信息 -> " + errorMessage);
        return Callback.error(2, errorMessage.toString());
    }




}
