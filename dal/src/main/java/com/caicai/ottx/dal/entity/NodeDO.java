/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * NodeDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import com.alibaba.otter.shared.common.model.config.node.NodeParameter;
import com.alibaba.otter.shared.common.model.config.node.NodeStatus;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : node的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("NodeDO")
public class NodeDO implements Serializable {
    private static final long serialVersionUID = 9148286590254926037L;
    private Long              id;                                     // 唯一标示id
    private String            name;                                   // 机器名字
    private String            ip;                                     // 机器ip
    private Long              port;                                   // 和manager对应的通讯端口
    private NodeStatus status;                                 // 对应状态
    private String            description;                            // 详细描述
    private NodeParameter parameters;                             // node对应参数信息
    private Date              gmtCreate;                              // 创建时间
    private Date              gmtModified;                            // 修改时间
}
