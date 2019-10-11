/**
 *
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 *
 * AlarmRuleDO.java
 *
 */
package com.caicai.ottx.dal.entity;

import com.alibaba.otter.shared.common.model.config.alarm.AlarmRuleStatus;
import com.alibaba.otter.shared.common.model.config.alarm.MonitorName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 表 : alarm_rule的 model 类
 *
 * @author 	Yanghq
 * @date 	2019年08月23日
 */
@Data
@Alias("AlarmRuleDO")
public class AlarmRuleDO implements Serializable {
    private static final long  serialVersionUID   = -1284784362325347636L;
    private Long               id;
    private Long               pipelineId;
    private AlarmRuleStatus status;
    private MonitorName monitorName;
    private String             receiverKey;
    private String             matchValue;
    private AlarmRuleParameter alarmRuleParameter = new AlarmRuleParameter();
    private String             description;
    private Date               gmtCreate;
    private Date               gmtModified;


}
