package com.caicai.ottx.service.config.channel.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huaseng on 2019/9/20.
 */
@Data
public class Tag implements Serializable{

    private Long id;

    /** 字段:NAME */
    private String name;

    /** 字段:DESCRIPTION */
    private String description;

    /** 字段:GMT_CREATE */
    private Date gmtCreate;

    /** 字段:GMT_MODIFIED */
    private Date gmtModified;

    /** 字段:SUB_NAME */
    private String subName;

    private long channelId;
}
