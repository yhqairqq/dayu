/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * ChannelDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import com.alibaba.otter.shared.common.model.config.channel.ChannelParameter;
import com.alibaba.otter.shared.common.model.config.channel.ChannelStatus;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : channel的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月22日
 */

@Data
@Alias("ChannelDO")
public class ChannelDO implements Serializable {
    private static final long serialVersionUID = 3708730560311969117L;
    private Long              id;                                     // 唯一标示id
    private String            name;                                   // channel命名
    private ChannelStatus status;                                 // 运行状态
    private String            description;                            // 描述信息
    private ChannelParameter  parameters;                             // 配置参数
    private Date              gmtCreate;
    private Date              gmtModified;

}
