package com.caicai.ottx.service.statistics.delay;

import com.alibaba.otter.shared.common.model.statistics.delay.DelayStat;
import com.caicai.ottx.service.statistics.delay.param.DelayStatInfo;
import com.caicai.ottx.service.statistics.delay.param.TopDelayStat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface DelayStatService {
     void createDelayStat(DelayStat stat);

     DelayStat findRealtimeDelayStat(Long pipelineId);

     Map<Long, DelayStatInfo> listTimelineDelayStat(Long pipelineId, Date start, Date end);

     List<TopDelayStat> listTopDelayStat(String searchKey, int topN);
}
