package com.caicai.ottx.service.position.impl;

import com.alibaba.otter.shared.arbitrate.ArbitrateViewService;
import com.alibaba.otter.shared.arbitrate.model.PositionEventData;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.caicai.ottx.service.config.pipeline.PipelineService;
import com.caicai.ottx.service.position.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huaseng on 2019/9/26.
 */
@Service
@Slf4j
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private ArbitrateViewService arbitrateViewService;

    @Override
    public void remove(long PipelineId) {
        Pipeline pipeline = pipelineService.findById(PipelineId);
        String destination = pipeline.getParameters().getDestinationName();
        short clientId = pipeline.getParameters().getMainstemClientId();
        PositionEventData position = arbitrateViewService.getCanalCursor(destination, clientId);
        log.warn("remove pipelineId[{}] position \n {}", PipelineId, position); // 记录一下日志
        arbitrateViewService.removeCanalCursor(destination, clientId);
    }
}
