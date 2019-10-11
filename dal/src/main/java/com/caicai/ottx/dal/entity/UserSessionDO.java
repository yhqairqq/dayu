/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * UserSessionDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 表 : sys_user_session的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月21日
 */
@NoArgsConstructor
public class UserSessionDO implements Serializable {
    /**  类的 seri version id */
    private static final long serialVersionUID = 1L;

    /** 字段:id，主键Id */
    private Long id;

    /** 字段:user_id，用户Id */
    private Long userId;

    /** 字段:token，用户登录token */
    private String token;


    public UserSessionDO(Long id, Long userId, String token, Integer timeout, Long loginTime, Boolean archive, Long createdBy, Long modifiedBy, Long created, Long modified) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.timeout = timeout;
        this.loginTime = loginTime;
        this.archive = archive;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.created = created;
        this.modified = modified;
    }

    public UserSessionDO(Long userId, String token){
        this.userId = userId;
        this.token = token;
    }

    /** 字段:timeout，有效时间 */
    private Integer timeout = 30;

    /** 字段:login_time，登录时间 */
    private Long loginTime;

    /** 字段:archive，删除标志 0 - 正常; 1 - 已删除 */
    private Boolean archive;

    /** 字段:created_by，创建人Id */
    private Long createdBy;

    /** 字段:modified_by，修改人Id */
    private Long modifiedBy;

    /** 字段:created，创建时间 */
    private Long created;

    /** 字段:modified，最后修改时间 */
    private Long modified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getModified() {
        return modified;
    }

    public void setModified(Long modified) {
        this.modified = modified;
    }
}
