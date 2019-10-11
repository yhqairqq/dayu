/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * PipelineDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import com.alibaba.otter.shared.common.model.config.pipeline.PipelineParameter;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : pipeline的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("PipelineDO")
public class PipelineDO implements Serializable {
    private static final long serialVersionUID = -4894036418246404446L;
    private Long              id;
    private String            name;
    private PipelineParameter parameters;
    private String            description;                             // 描述信息
    private Long              channelId;                               // 对应关联的channel唯一标示id
    private Date              gmtCreate;
    private Date              gmtModified;
}
