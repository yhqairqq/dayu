/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * DataMediaDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : data_media的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("DataMediaDO")
public class DataMediaDO implements Serializable {
    private static final long serialVersionUID = 1830886218829190716L;

    private Long              id;
    private String            name;                                   // 介质名称
    private String            namespace;                              // 介质类型
    private String            properties;
    private Long              dataMediaSourceId;
    private Date              gmtCreate;
    private Date              gmtModified;
    private String            topic;

}
