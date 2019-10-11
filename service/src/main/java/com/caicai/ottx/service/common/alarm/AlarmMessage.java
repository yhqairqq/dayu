package com.caicai.ottx.service.common.alarm;

import com.alibaba.otter.shared.common.utils.OtterToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by huaseng on 2019/8/23.
 */
public class AlarmMessage implements Serializable{
    private static final long serialVersionUID = 3033613829572954869L;
    private String            message;
    private String            receiveKey;
    public AlarmMessage(){

    }

    public AlarmMessage(String message, String receiveKey){
        this.message = message;
        this.receiveKey = receiveKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiveKey() {
        return receiveKey;
    }

    public void setReceiveKey(String receiveKey) {
        this.receiveKey = receiveKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, OtterToStringStyle.DEFAULT_STYLE);
    }
}
