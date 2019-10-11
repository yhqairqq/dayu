package com.caicai.ottx.common;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by huaseng on 2019/8/20.
 */
@Data
public final class ApiResult<T> implements Serializable {

    public static final Integer SUCCESS = 0;
    // 错误，返回错误信息
    public static final Integer ERROR   = 1;

    private Integer state;   // 状态码: 0 - 正确; 1 - 失败
    private T       data;  // 返回结果
    private String  message; // 错误信息
    private int  code;    // 错误码

    public ApiResult() {
        this.state = SUCCESS;
        this.data = null;
    }

    public ApiResult(T result) {
        this.state = SUCCESS;
        this.data = result;
    }

    public ApiResult(int code, String message) {
        this.state = ERROR;
        this.code = code;
        this.message = message;
    }


    public static <T> ApiResult<T> success(T data){
        return new ApiResult<>(data);
    }

    public static <T> ApiResult<T> failed(int code, String message){
        return new ApiResult<>(code, message);
    }

    public static <T> ApiResult<T> failed(String message){
        return failed(ErrorCode.EC_500, message);
    }
}
