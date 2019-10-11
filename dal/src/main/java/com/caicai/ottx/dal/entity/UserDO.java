/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * UserDO.java
 * 
 */
package com.caicai.ottx.dal.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : user的 model 类
 * 
 * @author 	Yanghq
 * @date 	2019年08月20日
 */
public class UserDO implements Serializable {
    /**  类的 seri version id */
    private static final long serialVersionUID = 1L;

    /** 字段:ID */
    private Long id;

    /** 字段:USERNAME */
    private String username;

    /** 字段:PASSWORD */
    private String password;

    /** 字段:AUTHORIZETYPE */
    private String authorizetype;

    /** 字段:DEPARTMENT */
    private String department;

    /** 字段:REALNAME */
    private String realname;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getAuthorizetype() {
        return authorizetype;
    }

    public void setAuthorizetype(String authorizetype) {
        this.authorizetype = authorizetype == null ? null : authorizetype.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
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