package com.caicai.ottx.service.monitor;

import com.alibaba.otter.shared.common.model.config.alarm.AlarmRule;

import java.util.List;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface Monitor {
     void explore();

     void explore(Long... pipelineIds);

     void explore(List<AlarmRule> rules);
}
