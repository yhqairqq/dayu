package com.caicai.ottx.service.config.record;

import com.alibaba.otter.shared.common.model.config.record.LogRecord;
import com.alibaba.otter.shared.communication.core.model.Event;
import com.caicai.ottx.service.common.baseservice.GenericService;

import java.util.List;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface LogRecordService extends GenericService<LogRecord> {
     void create(Event event);

     List<LogRecord> listByPipelineId(Long pipelineId);

     LogRecord listByPipelineIdTop(Long pipelineId);


     List<LogRecord> listByPipelineIdWithoutContent(Long pipelineId);
}
