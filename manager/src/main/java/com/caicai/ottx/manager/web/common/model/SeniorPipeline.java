package com.caicai.ottx.manager.web.common.model;

import com.alibaba.otter.shared.arbitrate.model.MainStemEventData;
import com.alibaba.otter.shared.arbitrate.model.PositionEventData;
import com.alibaba.otter.shared.common.model.config.alarm.AlarmRule;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.common.model.statistics.delay.DelayStat;
import com.alibaba.otter.shared.common.model.statistics.throughput.ThroughputStat;
import lombok.Data;

import java.util.List;

/**
 * Created by huaseng on 2019/9/11.
 */
@Data
public class SeniorPipeline extends Pipeline {

    private DelayStat delayStat;
    private MainStemEventData mainStemEventData;
    private ThroughputStat throughputStat;
    private PositionEventData positionEventData;
    private List<AlarmRule> alarmRules;
    private Channel channel;
}
