package com.caicai.ottx.manager.controller.position;

import com.alibaba.otter.shared.arbitrate.ArbitrateViewService;
import com.alibaba.otter.shared.arbitrate.model.PositionEventData;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.manager.controller.position.form.PostionForm;
import com.caicai.ottx.service.config.pipeline.PipelineService;
import com.caicai.ottx.service.position.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huaseng on 2019/9/26.
 */
@RestController
@RequestMapping("/position")
@Slf4j
public class PositionController {

    @Autowired
    private ArbitrateViewService arbitrateViewService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private PositionService positionService;

    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    public ApiResult remove(@RequestBody PostionForm postionForm){
        try{
            Pipeline pipeline = pipelineService.findById(postionForm.getPipelineId());
            String destination = pipeline.getParameters().getDestinationName();
            short clientId = pipeline.getParameters().getMainstemClientId();
            PositionEventData position = arbitrateViewService.getCanalCursor(destination, clientId);
            log.warn("remove pipelineId[{}] position \n {}", postionForm.getPipelineId(), position); // 记录一下日志
            arbitrateViewService.removeCanalCursor(destination, clientId);
            return ApiResult.success("清空成功");
        }catch (Exception e){
            return ApiResult.failed(e.getMessage());
        }
    }

}
