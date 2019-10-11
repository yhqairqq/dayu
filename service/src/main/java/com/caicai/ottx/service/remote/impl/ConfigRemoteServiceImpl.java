package com.caicai.ottx.service.remote.impl;

import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.channel.ChannelStatus;
import com.alibaba.otter.shared.common.model.config.data.DataMatrix;
import com.alibaba.otter.shared.common.model.config.node.Node;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import com.alibaba.otter.shared.communication.core.CommunicationClient;
import com.alibaba.otter.shared.communication.core.CommunicationRegistry;
import com.alibaba.otter.shared.communication.model.config.*;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.config.channel.ChannelService;
import com.caicai.ottx.service.config.datamatrix.DataMatrixService;
import com.caicai.ottx.service.config.node.NodeService;
import com.caicai.ottx.service.remote.ConfigRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by huaseng on 2019/9/21.
 */
@Slf4j
public class ConfigRemoteServiceImpl implements ConfigRemoteService{

    @PostConstruct
    public void init(){
        // 注册一下事件处理
        CommunicationRegistry.regist(ConfigEventType.findChannel, this);
        CommunicationRegistry.regist(ConfigEventType.findNode, this);
        CommunicationRegistry.regist(ConfigEventType.findTask, this);
        CommunicationRegistry.regist(ConfigEventType.findMedia, this);
    }

    @Autowired
    private CommunicationClient communicationClient;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private DataMatrixService dataMatrixService;


    @Override
    public boolean notifyChannel(Channel channel) {
        Assert.notNull(channel);
        // 获取所有的Node节点
        NotifyChannelEvent event = new NotifyChannelEvent();
        event.setChannel(channel);

        Set<String> addrsSet = new HashSet<String>();

        // 组装当前otter所有的存活的node节点
        // List<Node> nodes = nodeService.listAll();
        // for (Node node : nodes) {
        // if (node.getStatus().isStart() &&
        // StringUtils.isNotEmpty(node.getIp()) && node.getPort() != 0) {
        // final String addr = node.getIp() + ":" + node.getPort();
        // addrsList.add(addr);
        // }
        // }

        // 组装当前pipeline下的存活的node节点
        for (Pipeline pipeline : channel.getPipelines()) {
            List<Node> nodes = new ArrayList<Node>();
            nodes.addAll(pipeline.getSelectNodes());
            nodes.addAll(pipeline.getExtractNodes());
            nodes.addAll(pipeline.getLoadNodes());
            for (Node node : nodes) {
                if (node.getStatus().isStart() && StringUtils.isNotEmpty(node.getIp()) && node.getPort() != 0) {
                    String addr = node.getIp() + ":" + node.getPort();
                    if (node.getParameters().getUseExternalIp()) {
                        addr = node.getParameters().getExternalIp() + ":" + node.getPort();
                    }
                    addrsSet.add(addr);
                }
            }
        }

        List<String> addrsList = new ArrayList<String>(addrsSet);
        if (CollectionUtils.isEmpty(addrsList) && channel.getStatus().isStart()) {
            throw new ManagerException("no live node for notifyChannel");
        } else if (CollectionUtils.isEmpty(addrsList)) {
            // 针对关闭操作，可直接处理
            return true;
        } else {
            Collections.shuffle(addrsList);// 做一下随机，避免每次选择的机器都是同一台
            try {
                String[] addrs = addrsList.toArray(new String[addrsList.size()]);
                List<Boolean> result = (List<Boolean>) communicationClient.call(addrs, event); // 推送配置
                log.info("## notifyChannel to [{}] channel[{}] result[{}]",
                        new Object[] { ArrayUtils.toString(addrs), channel.toString(), result });

                boolean flag = true;
                for (Boolean f : result) {
                    flag &= f;
                }

                return flag;
            } catch (Exception e) {
                log.error("## notifyChannel error!", e);
                throw new ManagerException(e);
            }
        }
    }

    @Override
    public Channel onFindChannel(FindChannelEvent event) {
        Assert.notNull(event);
        Long channelId = event.getChannelId();
        Long pipelineId = event.getPipelineId();
        Channel channel = null;
        if (channelId != null) {
            channel = channelService.findById(channelId);
        } else {
            Assert.notNull(pipelineId);
            channel = channelService.findByPipelineId(pipelineId);
        }

        return channel;
    }

    @Override
    public Node onFindNode(FindNodeEvent event) {
        Assert.notNull(event);
        Assert.notNull(event.getNid());
        return nodeService.findById(event.getNid());
    }

    @Override
    public List<Channel> onFindTask(FindTaskEvent event) {
        Assert.notNull(event);
        Assert.notNull(event.getNid());
        // 同时查询start/pause状态的同步任务，因为在发布时重启jvm刚好在执行stopNode的restart指令，此时channel处于pause状态，丢失了任务命令
        return channelService.listByNodeId(event.getNid(), ChannelStatus.START, ChannelStatus.PAUSE);
    }

    @Override
    public String onFindMedia(FindMediaEvent event) {
        Assert.notNull(event);
        Assert.notNull(event.getDataId());
        DataMatrix matrix = dataMatrixService.findByGroupKey(event.getDataId());
        return JsonUtils.marshalToString(matrix);
    }


    public CommunicationClient getCommunicationClient() {
        return communicationClient;
    }

    public void setCommunicationClient(CommunicationClient communicationClient) {
        this.communicationClient = communicationClient;
    }

    public ChannelService getChannelService() {
        return channelService;
    }

    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }

    public NodeService getNodeService() {
        return nodeService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public DataMatrixService getDataMatrixService() {
        return dataMatrixService;
    }

    public void setDataMatrixService(DataMatrixService dataMatrixService) {
        this.dataMatrixService = dataMatrixService;
    }
}
