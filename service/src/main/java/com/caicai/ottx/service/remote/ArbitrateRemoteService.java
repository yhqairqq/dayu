package com.caicai.ottx.service.remote;

import com.alibaba.otter.shared.communication.model.arbitrate.NodeAlarmEvent;
import com.alibaba.otter.shared.communication.model.arbitrate.StopChannelEvent;
import com.alibaba.otter.shared.communication.model.arbitrate.StopNodeEvent;

public interface ArbitrateRemoteService {
    /**
     * 处理node信息报警
     */
     void onNodeAlarm(NodeAlarmEvent event);

    /**
     * 处理客户端关闭channel的事件
     */
     void onStopNode(StopNodeEvent event);

    /**
     * 处理客户端关闭channel的事件
     */
     void onStopChannel(StopChannelEvent event);
}
