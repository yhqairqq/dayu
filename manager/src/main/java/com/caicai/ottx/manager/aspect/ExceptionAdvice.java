package com.caicai.ottx.manager.aspect;

import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.ErrorCode;
import com.caicai.ottx.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {
    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResult handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("缺少请求参数:{}", e.getMessage(), e);
        return new ApiResult(ErrorCode.EC_400, ErrorCode.EM_400);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResult handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("参数解析失败:{}", e.getMessage(), e);
        return new ApiResult(ErrorCode.EC_400, ErrorCode.EM_400);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数验证失败:{}", e.getMessage(), e);

        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return new ApiResult(ErrorCode.EC_400, message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResult handleBindException(BindException e) {
        log.error("参数绑定失败:{}", e.getMessage(), e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return new ApiResult(ErrorCode.EC_400, message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult handleServiceException(ConstraintViolationException e) {
        log.error("参数验证失败:{}", e.getMessage(), e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        return new ApiResult(ErrorCode.EC_400, message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ApiResult handleValidationException(ValidationException e) {
        log.error("参数验证失败:{}", e.getMessage(), e);
        return new ApiResult(ErrorCode.EC_400, ErrorCode.EM_400);
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("不支持当前请求方法", e.getMessage());
        return new ApiResult(ErrorCode.EC_405, ErrorCode.EM_405);
    }

    /**
     * 200 - 业务类型错误也是200
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BizException.class)
    public ApiResult handleBizException(BizException e) {
        if (e.getCode() == ErrorCode.EC_401) {
            /*授权失败时返回401*/
            String eMsg = e.getMessage();
            if (StringUtils.isBlank(eMsg)) {
                eMsg = ErrorCode.EM_401;
            }
            return new ApiResult(ErrorCode.EC_401, eMsg);
        }

        return new ApiResult(ErrorCode.EC_200, e.getMessage());
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public ApiResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        String errorMsg = e.getMessage();
        // 错误消息为空 或者消息长度超过一定长度的认定为未捕获到的异常
        if (StringUtils.isBlank(errorMsg) || StringUtils.length(errorMsg) > 50) {
            errorMsg = ErrorCode.EM_500;
        }
        return new ApiResult(ErrorCode.EC_200, errorMsg);
    }
}
