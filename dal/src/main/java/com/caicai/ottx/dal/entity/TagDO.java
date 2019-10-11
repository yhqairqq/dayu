/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * TagDO.java
 * 
 */
package com.caicai.ottx.dal.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : tag的 model 类
 * 
 * @author 	Yanghq
 * @date 	2019年09月20日
 */
public class TagDO implements Serializable {
    /**  类的 seri version id */
    private static final long serialVersionUID = 1L;

    /** 字段:ID */
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName == null ? null : subName.trim();
    }
}