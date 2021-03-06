/*
 * Copyright (C) 2010-2101 Alibaba Group Holding Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.caicai.ottx.service.monitor.impl;

import com.alibaba.otter.shared.common.model.config.alarm.AlarmRule;
import com.alibaba.otter.shared.common.model.config.alarm.MonitorName;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.common.model.statistics.delay.DelayStat;
import com.caicai.ottx.service.config.pipeline.PipelineService;
import com.caicai.ottx.service.statistics.delay.DelayStatService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @version 4.1.0
 */
public class DelayStatRuleMonitor extends AbstractRuleMonitor {

    private static final String DELAY_TIME_MESSAGE        = "pid(%s):%s delay_time:%s seconds";
    private static final String DELAY_UPDATE_MESSAGE      = "pid(%s):%s delay %s seconds no update";
    private static final String DELAY_TIME_UPDATE_MESSAGE = "pid(%s):%s delay_time:%s seconds, but delay %s seconds no update";

    @Autowired
    private DelayStatService delayStatService;
    @Autowired
    private PipelineService pipelineService;

    @Override
    public void explore(List<AlarmRule> rules) {
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }

        // 进入到监控项级别的rule，pipelineId一定是相同的
        Long pipelineId = rules.get(0).getPipelineId();
        Pipeline pipeline = pipelineService.findById(pipelineId);
        DelayStat delayStat = delayStatService.findRealtimeDelayStat(pipelineId);
        Long delayTime = 0L; // seconds
        Long delayUpdate = 0L;
        if (delayStat.getDelayTime() != null) {
            delayTime = delayStat.getDelayTime() / 1000;
        }
        if (delayStat.getGmtCreate() != null) {
            delayUpdate = (new Date().getTime() - delayStat.getGmtCreate().getTime()) / 1000;
        }

        boolean delayTimeFlag = false;
        boolean delayUpdateFlag = false;
        for (AlarmRule rule : rules) {
            if (rule.getMonitorName().isDelayTime()) {
                delayTimeFlag |= checkDelayTime(rule, delayTime);
                if (delayTimeFlag) { //如果出现超时，再check下是否因为最后更新时间过久了
                    delayUpdateFlag |= checkDelayTime(rule, delayUpdate);//检查delay统计的最后更新时间，这也做为delay监控的一部分
                }
            }
        }

        if (delayTimeFlag && !delayUpdateFlag) {
            logRecordAlarm(pipelineId, MonitorName.DELAYTIME, String.format(DELAY_TIME_MESSAGE, pipelineId,pipeline.getName(), delayTime));
        } else if (delayTimeFlag && delayUpdateFlag) {
            logRecordAlarm(pipelineId, MonitorName.DELAYTIME,
                           String.format(DELAY_TIME_UPDATE_MESSAGE, pipelineId,pipeline.getName(), delayTime, delayUpdate));
        } else if (delayUpdateFlag) {
            logRecordAlarm(pipelineId, MonitorName.DELAYTIME,
                           String.format(DELAY_UPDATE_MESSAGE, pipelineId,pipeline.getName(), delayUpdate));
        }
    }

    private boolean checkDelayTime(AlarmRule rule, Long delayTime) {

        if (!inPeriod(rule)) {
            return false;
        }

        String matchValue = rule.getMatchValue();
        matchValue = StringUtils.substringBeforeLast(matchValue, "@");
        Long maxDelayTime = Long.parseLong(StringUtils.trim(matchValue));
        if (delayTime >= maxDelayTime) {
            sendAlarm(rule, String.format(DELAY_TIME_MESSAGE, rule.getPipelineId(), delayTime));
            return true;
        }
        return false;
    }

    public void setDelayStatService(DelayStatService delayStatService) {
        this.delayStatService = delayStatService;
    }

}
