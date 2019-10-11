package com.caicai.ottx.service.statistics.stage;

import com.alibaba.otter.shared.common.model.statistics.stage.ProcessStat;

import java.util.Date;
import java.util.List;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface ProcessStatService {
     List<ProcessStat> listRealtimeProcessStat(Long pipelineId);

     List<ProcessStat> listRealtimeProcessStat(Long channelId, Long pipelineId);

     List<ProcessStat> listTimelineProcessStat(Long pipelineId, Date start, Date end);

     void createProcessStat(ProcessStat stat);
}
