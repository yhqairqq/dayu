/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * DataColumnPairGroupDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : column_pair_group的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("DataColumnPairGroupDO")
public class DataColumnPairGroupDO implements Serializable {
    private static final long serialVersionUID = 7205447225855754450L;
    private Long              id;
    private String            columnPairContent;
    private Long              dataMediaPairId;
    private Date              gmtCreate;
    private Date              gmtModified;


}
