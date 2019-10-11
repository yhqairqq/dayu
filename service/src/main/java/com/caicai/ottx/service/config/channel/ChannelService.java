package com.caicai.ottx.service.config.channel;

import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.channel.ChannelStatus;
import com.caicai.ottx.service.common.baseservice.GenericService;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface ChannelService  extends GenericService<Channel>{
     Channel findByPipelineId(Long pipelineId);

     Channel findByIdWithoutColumn(Long pipelineId);

     List<Channel> listByPipelineIds(Long... pipelineIds);

     List<Channel> listByNodeId(Long nodeId);

     List<Channel> listOnlyChannels(Long... identities);

     List<Long> listAllChannelId();

     List<Channel> listByNodeId(Long nodeId, ChannelStatus... status);

     Page<Channel> listByConditionWithoutColumn(Map condition);

     void stopChannel(Long channelId);

     void notifyChannel(Long channelId);

     void startChannel(Long channelId);
}
