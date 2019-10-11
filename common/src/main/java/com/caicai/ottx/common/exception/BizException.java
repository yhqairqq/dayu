package com.caicai.ottx.common.exception;

import com.caicai.ottx.common.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by huaseng on 2019/8/20.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {

    private int code;    //异常业务编码

    /**
     * 默认异常构造器.
     */
    public BizException() {
        this(ErrorCode.EC_500, ErrorCode.EM_500);
    }

    /**
     * 根据错误码和错误信息构造异常.
     *
     * @param code    错误码
     * @param message 异常信息.
     */
    public BizException(final int code, final String message) {
        super(message);
        this.code = code;
    }

    /**
     * 根据异常信息和原生异常构造对象.
     *
     * @param code    错误码
     * @param message 异常信息.
     * @param cause   原生异常.
     */
    public BizException(final int code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 根据异常信息和原生异常构造对象.
     *
     * @param message 异常信息.
     * @param cause   原生异常.
     */
    public BizException(final String message, final Throwable cause) {
        this(ErrorCode.EC_500, message, cause);
    }

    /**
     * 根据异常信息构造对象.
     *
     * @param message 异常信息.
     */
    public BizException(final String message) {
        this(ErrorCode.EC_500, message);
    }


    /**
     * 根据原生异常构造对象.
     *
     * @param cause 原生异常.
     */
    public BizException(final Throwable cause) {
        this(ErrorCode.EC_500, ErrorCode.EM_500, cause);
    }

    @Override
    public String toString() {
        return "BizException{" +
                "code='" + code + '\'' +
                ",message='" + getMessage() + '\'' +
                "} ";
    }
}
