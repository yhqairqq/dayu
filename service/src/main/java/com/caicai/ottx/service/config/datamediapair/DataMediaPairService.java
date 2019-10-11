package com.caicai.ottx.service.config.datamediapair;

import com.alibaba.otter.manager.biz.common.baseservice.GenericService;
import com.alibaba.otter.shared.common.model.config.data.DataMediaPair;

import java.util.List;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface DataMediaPairService extends GenericService<DataMediaPair> {
     List<DataMediaPair> listByPipelineId(Long pipelineId);

     List<DataMediaPair> listByPipelineIdWithoutColumn(Long pipelineId);

     List<DataMediaPair> listByDataMediaId(Long dataMediaId);

     Long createAndReturnId(DataMediaPair dataMediaPair);

     boolean createIfNotExist(DataMediaPair dataMediaPair);
}
