/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * SystemParameterDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import com.alibaba.otter.shared.common.model.config.parameter.SystemParameter;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : system_parameter的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("SystemParameterDO")
public class SystemParameterDO implements Serializable {
    private static final long serialVersionUID = 9148286590254926037L;
    private Long              id;                                     // 唯一标示id
    private SystemParameter value;                                  // 系统参数值
    private Date              gmtCreate;                              // 创建时间
    private Date              gmtModified;                            // 修改时间
}
