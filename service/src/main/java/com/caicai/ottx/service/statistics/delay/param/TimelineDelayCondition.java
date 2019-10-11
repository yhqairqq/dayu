package com.caicai.ottx.service.statistics.delay.param;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huaseng on 2019/9/2.
 */
@Data
public class TimelineDelayCondition implements Serializable{
    private static final long serialVersionUID = -6780593963079707523L;
    private Long pipelineId;
    private Date start;
    private Date end;
}
