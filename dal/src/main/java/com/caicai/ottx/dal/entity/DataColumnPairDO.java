/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * DataColumnPairDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : column_pair的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("DataColumnPairDO")
public class DataColumnPairDO implements Serializable {
    private static final long serialVersionUID = 194553152360180533L;
    private Long              id;
    private String            sourceColumnName;                      // 源字段
    private String            targetColumnName;                      // 目标字段
    private Long              dataMediaPairId;
    private Date              gmtCreate;
    private Date              gmtModified;

}
