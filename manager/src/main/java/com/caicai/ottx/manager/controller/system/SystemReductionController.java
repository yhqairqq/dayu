package com.caicai.ottx.manager.controller.system;

import com.alibaba.otter.shared.arbitrate.ArbitrateManageService;
import com.alibaba.otter.shared.arbitrate.exception.ArbitrateException;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.service.config.channel.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by huaseng on 2019/8/28.
 */
@RestController
@RequestMapping("/zookeeper")
@Slf4j
public class SystemReductionController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ArbitrateManageService arbitrateManageService;


}
