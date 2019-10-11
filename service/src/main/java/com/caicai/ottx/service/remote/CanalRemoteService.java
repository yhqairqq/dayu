package com.caicai.ottx.service.remote;

import com.alibaba.otter.canal.instance.manager.model.Canal;
import com.alibaba.otter.shared.communication.model.canal.FindCanalEvent;
import com.alibaba.otter.shared.communication.model.canal.FindFilterEvent;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface CanalRemoteService {

    /**
     * 接收客户端的查询Canal请求
     */
     Canal onFindCanal(FindCanalEvent event);

    /**
     * 接收客户端的查询filter请求
     */
     String onFindFilter(FindFilterEvent event);
}
