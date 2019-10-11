package com.caicai.ottx.service.statistics.throughput;

import com.alibaba.otter.shared.common.model.statistics.throughput.ThroughputStat;
import com.caicai.ottx.service.statistics.table.param.*;

import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface ThroughputStatService {
     Map<AnalysisType, ThroughputInfo> listRealtimeThroughput(RealtimeThroughputCondition condition);

     Map<Long, ThroughputInfo> listTimelineThroughput(TimelineThroughputCondition condition);

     List<ThroughputStat> listRealtimeThroughputByPipelineIds(List<Long> pipelineIds, int minute);

     ThroughputStat findThroughputStatByPipelineId(ThroughputCondition condition);

     void createOrUpdateThroughput(ThroughputStat item);
}
