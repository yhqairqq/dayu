/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * TableHistoryStatDO.java
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
 * 表 : table_history_stat的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Setter
@Getter
@Alias("TableHistoryStatDO")
public class TableHistoryStatDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long              id;
    private Date              startTime;
    private Date              endTime;
    private Long              fileSize;
    private Long              fileCount;
    private Long              insertCount;
    private Long              updateCount;
    private Long              deleteCount;
    private Long              dataMediaPairId;
    private Long              pipelineId;
    private Date              gmtCreate;
    private Date              gmtModified;
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, OtterToStringStyle.DEFAULT_STYLE);
    }
}
