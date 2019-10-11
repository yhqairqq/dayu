package com.caicai.ottx.manager;

import com.caicai.ottx.service.common.arbitrate.ArbitrateConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by huaseng on 2019/8/27.
 */
@Component
public class CustomerServletContainerInitializer  {
    @Autowired
    private ArbitrateConfigImpl arbitrateConfig;
}
