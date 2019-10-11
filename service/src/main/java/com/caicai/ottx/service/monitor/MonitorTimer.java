package com.caicai.ottx.service.monitor;

import com.alibaba.otter.shared.common.model.config.alarm.MonitorName;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huaseng on 2019/8/23.
 */
public class MonitorTimer extends ConcurrentHashMap<MonitorName, Date> {

    private static final long serialVersionUID = -2129810461060521223L;

}
