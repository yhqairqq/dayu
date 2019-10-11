/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * CanalDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.alibaba.otter.canal.instance.manager.model.CanalStatus;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : canal的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("CanalDO")
public class CanalDO implements Serializable {

    private static final long serialVersionUID = 9148286590254926037L;

    private Long              id;                                     // 唯一标示id
    private String            name;                                   // canal名字
    private String            description;                            // 描述
    private CanalStatus status;
    private CanalParameter    parameters;                             // 参数定义
    private Date              gmtCreate;                              // 创建时间
    private Date              gmtModified;                            // 修改时间

}
