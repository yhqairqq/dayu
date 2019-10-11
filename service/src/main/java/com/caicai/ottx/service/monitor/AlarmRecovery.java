package com.caicai.ottx.service.monitor;

import com.alibaba.otter.shared.common.model.config.alarm.AlarmRule;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface AlarmRecovery {

    /**
     * 根据规则，强制进行recovery操作
     */
     void recovery(Long channelId);

    /**
     * 根据规则，强制进行recovery操作
     */
     void recovery(AlarmRule alarmRule);

    /**
     * 根据规则+触发次数，进行recovery操作
     */
     void recovery(AlarmRule alarmRule, long alarmCount);
}
