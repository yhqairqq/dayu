/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * AutoKeeperClusterDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import com.alibaba.otter.shared.common.utils.OtterToStringStyle;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : autokeeper_cluster的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Setter
@Getter
@Alias("AutoKeeperClusterDO")
public class AutoKeeperClusterDO implements Serializable {
    private Long   id;
    private String clusterName;
    private String serverList; // 机器列表
    private String description; // 描述
    private Date   gmtCreate;
    private Date   gmtModified;
    public String toString() {
        return ToStringBuilder.reflectionToString(this, OtterToStringStyle.DEFAULT_STYLE);
    }
}
