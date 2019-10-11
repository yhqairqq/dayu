package com.caicai.ottx.service.monitor;

import com.alibaba.otter.shared.common.model.config.alarm.AlarmRule;

import java.util.List;

/**
 * 被动监控者，由其他人喂给他需要监控的数据
 * Created by huaseng on 2019/8/23.
 */
public interface PassiveMonitor {
     void feed(Object data, Long pipelineId);

     void feed(Object data, List<AlarmRule> rules);
}
