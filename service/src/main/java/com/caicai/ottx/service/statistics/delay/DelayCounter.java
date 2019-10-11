package com.caicai.ottx.service.statistics.delay;

/**
 * Created by huaseng on 2019/9/2.
 */
public interface DelayCounter {
     Long incAndGet(Long pipelineId, Long number);

     Long decAndGet(Long pipelineId, Long number);

     Long setAndGet(Long pipelineId, Long number);
}
