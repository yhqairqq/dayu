/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * DataMediaPairDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import com.alibaba.otter.shared.common.model.config.data.ColumnPairMode;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : data_media_pair的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("DataMediaPairDO")
public class DataMediaPairDO implements Serializable {
    private static final long serialVersionUID = -7771432925148858183L;
    private Long              id;
    private Long              sourceDataMediaId;
    private Long              targetDataMediaId;
    private Long              pullWeight;                              // 介质A中获取数据的权重
    private Long              pushWeight;                              // 介质B中写入数据的权重
    private String            resolver;                                // 关联数据解析类
    private String            filter;                                  // 数据过滤处理类
    private ColumnPairMode columnPairMode;
    private Long              pipelineId;                              // 同步任务id
    private Date              gmtCreate;
    private Date              gmtModified;



}
