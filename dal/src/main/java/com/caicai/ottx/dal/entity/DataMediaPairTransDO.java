/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * DataMediaPairTransDO.java
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
 * 表 : data_media_pair_trans的 model 类
 * 
 * @author 	Yanghq
 * @date 	2019年10月18日
 */
@Data
@Alias("DataMediaPairTransDO")
public class DataMediaPairTransDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long sourceDataMediaId;
    private Long targetDataMediaId;
    private ColumnPairMode columnPairMode;
    private Date gmtCreate;
    private Date gmtModified;
}