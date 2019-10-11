package com.caicai.ottx.service.config.parameter;

import com.alibaba.otter.shared.common.model.config.parameter.SystemParameter;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface SystemParameterService {

     void createOrUpdate(SystemParameter systemParameter);

     SystemParameter find();
}
