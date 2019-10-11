package com.caicai.ottx.common;

import lombok.Data;

/**
 * Created by huaseng on 2019/8/19.
 */
@Data
public class GenericResult<T> {

    protected int code = 0;

    protected String message;

    protected T data;

    public static GenericResult success() {
        return new GenericResult();
    }

    public static GenericResult error(String message) {
        GenericResult genericResult = new GenericResult();
        genericResult.setCode(-1);
        genericResult.setMessage(message);
        return genericResult;
    }
    public static <T> GenericResult<T> success(T data){
        GenericResult genericResult = new GenericResult();
        genericResult.setData(data);
        return genericResult;
    }

}
