/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * TagChannelRelationDO.java
 * 
 */
package com.caicai.ottx.dal.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : tag_channel_relation的 model 类
 * 
 * @author 	Yanghq
 * @date 	2019年09月20日
 */
public class TagChannelRelationDO implements Serializable {
    /**  类的 seri version id */
    private static final long serialVersionUID = 1L;

    /** 字段:ID */
    private Long id;

    /** 字段:TAG_ID */
    private Long tagId;

    /** 字段:CHANNEL_ID */
    private Long channelId;

    /** 字段:DESCRIPTION */
    private String description;

    /** 字段:GMT_CREATE */
    private Date gmtCreate;

    /** 字段:GMT_MODIFIED */
    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}