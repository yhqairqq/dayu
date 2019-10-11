/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * DataMatrixDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : data_matrix的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("DataMatrixDO")
public class DataMatrixDO implements Serializable {
    private static final long serialVersionUID = 9148286590254926037L;
    private Long              id;                                     // 唯一标示id
    private String            groupKey;                               // groupKey
    private String            master;
    private String            slave;
    private String            description;                            // 描述
    private Date              gmtCreate;                              // 创建时间
    private Date              gmtModified;                            // 修改时间


}
