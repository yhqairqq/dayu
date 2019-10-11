/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * SysResourceDO.java
 * 
 */
package com.caicai.ottx.dal.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : sys_resource的 model 类
 * 
 * @author 	Yanghq
 * @date 	2019年08月19日
 */
public class SysResourceDO implements Serializable {
    /**  类的 seri version id */
    private static final long serialVersionUID = 1L;

    /** 字段:id，主键Id */
    private Long id;

    /** 字段:parent_id，父节点Id */
    private Long parentId;

    /** 字段:name，资源名称 */
    private String name;

    /** 字段:path，资源路径 */
    private String path;

    /** 字段:icon，资源图标 */
    private String icon;

    /** 字段:type */
    private Boolean type;

    /** 字段:auth_code，权限编码 */
    private String authCode;

    /** 字段:level，层级 */
    private Integer level;

    /** 字段:comment，备注信息 */
    private String comment;

    /** 字段:status，删除标志 0 - 正常; 1 - 已删除 */
    private Boolean status;

    /** 字段:create_time，创建时间 */
    private Date createTime;

    /** 字段:update_time，最后修改时间 */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode == null ? null : authCode.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}