package com.ennova.pubinfopurchase.config;

import com.ennova.pubinfocommon.entity.Callback;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;


@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalException {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

     @Value("${spring.servlet.multipart.max-request-size}")
    private String maxRequestSize;

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
        log.info("服务器异常信息2 --> " + e);
        return Callback.error(2, message);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Callback handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        //StringBuilder errorMessage = new StringBuilder("Invalid Request:\n");
        //
        //for (FieldError fieldError : bindingResult.getFieldErrors()) {
        //    errorMessage.append(fieldError.getDefaultMessage()).append("\n");
        //}
        //log.info("服务器异常信息 -> " + errorMessage);
        //return Callback.error(2, errorMessage.toString());

         String message = "";
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            if (errors != null) {
                errors.forEach(p -> {
                    FieldError fieldError = (FieldError) p;
                    log.error("Data check failure : object{" + fieldError.getObjectName() + "},field{" + fieldError.getField() +
                            "},errorMessage{" + fieldError.getDefaultMessage() + "}");

                });
                if (errors.size() > 0) {
                    FieldError fieldError = (FieldError) errors.get(0);
                    message = fieldError.getDefaultMessage();
                }
            }
        }
        return Callback.error(2, message);
    }


    @ExceptionHandler(value = MultipartException.class)
    public Callback multipartExceptionHandler(MaxUploadSizeExceededException e) {
        String errorMsg = "";
        Throwable throwable = e.getCause();
        log.error("服务器异常信息 -> " + e.getMessage());
        // org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException
        if (throwable.getCause() instanceof FileUploadBase.FileSizeLimitExceededException) {
            errorMsg="上传文件过大[单个文件大小不得超过" + maxFileSize + "]";
        }
        // org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException
        if (throwable.getCause() instanceof FileUploadBase.SizeLimitExceededException) {
            errorMsg="上传文件过大[总上传大小不得超过" + maxRequestSize + "]";
        }else {
            errorMsg = "文件上传失败[服务器异常]";
        }

        log.info("上传文件异常信息 -> " + errorMsg);
        return Callback.error(2, errorMsg);
    }

    @ExceptionHandler({ConstraintViolationException.class, BindException.class})
    public Callback validateException(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        String msg = null;
        if(ex instanceof ConstraintViolationException){
            ConstraintViolationException constraintViolationException =
                (ConstraintViolationException)ex;
            Set<ConstraintViolation<?>> violations =
                constraintViolationException.getConstraintViolations();
            ConstraintViolation<?> next = violations.iterator().next();
            msg = next.getMessage();
        }else if(ex instanceof BindException){
            BindException bindException = (BindException)ex;
            msg = bindException.getBindingResult().getFieldError().getDefaultMessage();
        }
        return Callback.error(2,msg);
    }

    @ExceptionHandler(value = DateTimeParseException.class)
    public Callback dateTimeParseException(Exception ex) {
        ex.printStackTrace();
        String msg = null;
        if(ex instanceof DateTimeParseException){
            msg = "时间格式不正确, 转换异常!";
        } else {
            msg = "时间格式异常";
        }
        return Callback.error(2,msg);
    }


    @ExceptionHandler(HttpMessageConversionException.class)
    public Callback parameterTypeException(HttpMessageConversionException exception) {
        log.error(exception.getCause().getLocalizedMessage());
        return Callback.error(2,"请求参数据异常");
    }

    //@ExceptionHandler(ServletRequestBindingException.class)
    //public Callback servletRequestBindingException(ServletRequestBindingException ex) {
    //    log.error(ex.getLocalizedMessage());
    //    return Callback.error(2,"PathVariable未传递参数");
    //}


}
