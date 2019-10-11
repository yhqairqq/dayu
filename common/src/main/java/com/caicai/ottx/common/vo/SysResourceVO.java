package com.caicai.ottx.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huaseng on 2019/8/19.
 */
@Data
public class SysResourceVO implements Serializable {
    private static final long serialVersionUID = 6484014208327484694L;

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
}
