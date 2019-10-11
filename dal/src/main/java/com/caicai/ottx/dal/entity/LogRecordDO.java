/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * LogRecordDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : log_record的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("LogRecordDO")
public class LogRecordDO implements Serializable {
    private static final long serialVersionUID = -4295402837089297629L;
    private Long              id;
    private Long              pipelineId;
    private Long              channelId;
    private Long              nid;
    private String            title;
    private String            message;
    private Date              gmtCreate;
    private Date              gmtModified;
}
