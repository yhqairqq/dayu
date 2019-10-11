package com.caicai.ottx.service.monitor;

import com.alibaba.otter.shared.common.model.config.alarm.AlarmRule;
import com.caicai.ottx.service.common.alarm.AlarmMessage;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface AlarmController {
     AlarmMessage control(AlarmRule rule, String message, AlarmMessage data);
}
