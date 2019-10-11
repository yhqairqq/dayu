package com.caicai.ottx.service.statistics.stage.impl;

import com.alibaba.otter.shared.arbitrate.ArbitrateViewService;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.statistics.stage.ProcessStat;
import com.caicai.ottx.service.config.channel.ChannelService;
import com.caicai.ottx.service.statistics.stage.ProcessStatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by huaseng on 2019/8/26.
 */
@Service
@Slf4j
public class ProcessStatServiceImpl implements ProcessStatService {

    @Autowired
    private ArbitrateViewService arbitrateViewService;
    @Autowired
    private ChannelService channelService;
    @Override
    public List<ProcessStat> listRealtimeProcessStat(Long pipelineId) {
        Channel channel = channelService.findByPipelineId(pipelineId);
        return listRealtimeProcessStat(channel.getId(), pipelineId);
    }

    @Override
    public List<ProcessStat> listRealtimeProcessStat(Long channelId, Long pipelineId) {
        return arbitrateViewService.listProcesses(channelId, pipelineId);
    }

    @Override
    public List<ProcessStat> listTimelineProcessStat(Long pipelineId, Date start, Date end) {
        return null;
    }

    @Override
    public void createProcessStat(ProcessStat stat) {

    }
}
