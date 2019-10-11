/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * DelayStatDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : delay_stat的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("DelayStatDO")
public class DelayStatDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long              id;
    private Long              delayTime;
    private Long              delayNumber;
    private Long              pipelineId;
    private Date              gmtCreate;
    private Date              gmtModified;
}
