package com.caicai.ottx.service.config.pipeline;

import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.caicai.ottx.service.common.baseservice.GenericService;

import java.util.List;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface PipelineService extends GenericService<Pipeline> {

     List<Pipeline> listByChannelIds(Long... channelIds);

     List<Pipeline> listByChannelIdsWithoutOther(Long... channelIds);

     List<Pipeline> listByChannelIdsWithoutColumn(Long... channelIds);

     List<Pipeline> listByNodeId(Long nodeId);

     boolean hasRelation(Long nodeId);

     List<Pipeline> listByDestinationWithoutOther(String destination);

}
