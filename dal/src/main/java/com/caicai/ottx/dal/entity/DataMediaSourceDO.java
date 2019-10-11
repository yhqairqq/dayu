/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * DataMediaSourceDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import com.alibaba.otter.shared.common.model.config.data.DataMediaType;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : data_media_source的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("DataMediaSourceDO")
public class DataMediaSourceDO implements Serializable {
    private static final long serialVersionUID = 5123273832849527936L;
    private Long              id;
    private String            name;
    private DataMediaType type;
    private String            properties;
    private Date              gmtCreate;
    private Date              gmtModified;
}
