package com.caicai.ottx.service.common.arbitrate;

import com.alibaba.otter.shared.arbitrate.impl.config.ArbitrateConfig;
import com.alibaba.otter.shared.arbitrate.impl.config.ArbitrateConfigRegistry;
import com.alibaba.otter.shared.common.model.config.ConfigException;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.node.Node;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.common.utils.cache.RefreshMemoryMirror;
import com.caicai.ottx.service.config.channel.ChannelService;
import com.caicai.ottx.service.config.node.NodeService;
import com.google.common.base.Function;
import com.google.common.collect.OtterMigrateMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/26.
 */
@Service
public class ArbitrateConfigImpl implements ArbitrateConfig, InitializingBean {

    private static final Long DEFAULT_PERIOD = 60 * 1000L;
    private Long timeout = DEFAULT_PERIOD;
    private RefreshMemoryMirror<Long, Channel> channelCache;
    private Map<Long, Long> channelMapping;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private NodeService nodeService;
    private RefreshMemoryMirror<Long, Node> nodeCache;

    public ArbitrateConfigImpl(){
        // 注册自己到arbitrate模块
        ArbitrateConfigRegistry.regist(this);
    }

    public Node currentNode() {
        return null;
    }

    public Node findNode(Long nid) {
        return nodeCache.get(nid);
    }

    public Channel findChannel(Long channelId) {
        return channelCache.get(channelId);
    }

    public Channel findChannelByPipelineId(Long pipelineId) {
        Long channelId = channelMapping.get(pipelineId);
        return channelCache.get(channelId);
    }

    public Pipeline findOppositePipeline(Long pipelineId) {
        Long channelId = channelMapping.get(pipelineId);
        Channel channel = channelCache.get(channelId);
        List<Pipeline> pipelines = channel.getPipelines();
        for (Pipeline pipeline : pipelines) {
            if (pipeline.getId().equals(pipelineId) == false) {// 这里假定pipeline只有两个
                return pipeline;
            }
        }
        return null;
    }

    public Pipeline findPipeline(Long pipelineId) {
        Long channelId = channelMapping.get(pipelineId);
        Channel channel = channelCache.get(channelId);
        List<Pipeline> pipelines = channel.getPipelines();
        for (Pipeline pipeline : pipelines) {
            if (pipeline.getId().equals(pipelineId)) {
                return pipeline;
            }
        }
        throw new ConfigException("no pipeline for pipelineId[" + pipelineId + "]");
    }

    public void afterPropertiesSet() throws Exception {
        // 获取一下nid变量
        channelMapping = OtterMigrateMap.makeComputingMap(new Function<Long, Long>() {

            public Long apply(Long pipelineId) {
                // 处理下pipline -> channel映射关系不存在的情况
                Channel channel = channelService.findByPipelineId(pipelineId);
                if (channel == null) {
                    throw new ConfigException("No Such Channel by pipelineId[" + pipelineId + "]");
                }

                updateMapping(channel, pipelineId);// 排除下自己
                channelCache.put(channel.getId(), channel);// 更新下channelCache
                return channel.getId();

            }
        });

        channelCache = new RefreshMemoryMirror<Long, Channel>(timeout, new RefreshMemoryMirror.ComputeFunction<Long, Channel>() {

            public Channel apply(Long key, Channel oldValue) {
                Channel channel = channelService.findById(key);
                if (channel == null) {
                    // 其他情况直接返回内存中的旧值
                    return oldValue;
                } else {
                    updateMapping(channel, null);// 排除下自己
                    return channel;
                }
            }
        });

        nodeCache = new RefreshMemoryMirror<Long, Node>(timeout, new RefreshMemoryMirror.ComputeFunction<Long, Node>() {

            public Node apply(Long key, Node oldValue) {
                Node node = nodeService.findById(key);
                if (node == null) {
                    return oldValue;
                } else {
                    return node;
                }
            }
        });
    }

    private void updateMapping(Channel channel, Long excludeId) {
        Long channelId = channel.getId();
        List<Pipeline> pipelines = channel.getPipelines();
        for (Pipeline pipeline : pipelines) {
            if (excludeId == null || !pipeline.getId().equals(excludeId)) {
                channelMapping.put(pipeline.getId(), channelId);
            }
        }
    }

    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }
}
