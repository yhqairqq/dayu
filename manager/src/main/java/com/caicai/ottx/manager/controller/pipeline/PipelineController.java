package com.caicai.ottx.manager.controller.pipeline;

import com.alibaba.otter.canal.parse.index.CanalLogPositionManager;
import com.alibaba.otter.canal.protocol.position.LogPosition;
import com.alibaba.otter.shared.arbitrate.ArbitrateViewService;
import com.alibaba.otter.shared.arbitrate.model.MainStemEventData;
import com.alibaba.otter.shared.arbitrate.model.PositionEventData;
import com.alibaba.otter.shared.common.model.config.alarm.AlarmRule;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.node.Node;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.common.model.config.pipeline.PipelineParameter;
import com.alibaba.otter.shared.common.model.statistics.delay.DelayStat;
import com.alibaba.otter.shared.common.model.statistics.throughput.ThroughputStat;
import com.alibaba.otter.shared.common.model.statistics.throughput.ThroughputType;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.utils.EnumBeanUtils;
import com.caicai.ottx.manager.controller.alarm.form.AlarmForm;
import com.caicai.ottx.manager.controller.pipeline.form.PipelineForm;
import com.caicai.ottx.manager.web.common.model.SeniorPipeline;
import com.caicai.ottx.service.config.alarm.AlarmRuleService;
import com.caicai.ottx.service.config.channel.ChannelService;
import com.caicai.ottx.service.config.pipeline.PipelineService;
import com.caicai.ottx.service.statistics.delay.DelayStatService;
import com.caicai.ottx.service.statistics.table.param.ThroughputCondition;
import com.caicai.ottx.service.statistics.throughput.ThroughputStatService;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by huaseng on 2019/9/10.
 */
@RequestMapping("/pipeline")
@RestController
public class PipelineController {

    @Autowired
    private PipelineService pipelineService;

    @Autowired
    private DelayStatService delayStatService;
    @Autowired
    private ArbitrateViewService arbitrateViewService;

    @Autowired
    private ThroughputStatService throughputStatService;
    @Autowired
    private AlarmRuleService alarmRuleService;

    @Autowired
    private ChannelService channelService;


    @RequestMapping(value = "/getPipelines",method = RequestMethod.POST)
    public ApiResult<List<SeniorPipeline>> getPipelines(@RequestBody PipelineForm pipelineForm){
        try{
            Channel channel = channelService.findById(pipelineForm.getChannelId());
            List<Pipeline> pipelines =  pipelineService.listByChannelIds(pipelineForm.getChannelId());
            List<SeniorPipeline> seniorPipelines = new ArrayList<>();
            for (Pipeline pipeline : pipelines) {
                DelayStat delayStat = delayStatService.findRealtimeDelayStat(pipeline.getId());
                if (delayStat.getDelayNumber() == null) {
                    delayStat.setDelayNumber(0L);
                    delayStat.setDelayTime(0L);
                    delayStat.setGmtModified(pipeline.getGmtModified());
                }
//                delayStats.put(pipeline.getId(), delayStat);
                MainStemEventData mainStemEventData =  arbitrateViewService.mainstemData(pipelineForm.getChannelId(), pipeline.getId());
//                mainstemDatas.put(pipeline.getId(), arbitrateViewService.mainstemData(pipelineForm.getChannelId(), pipeline.getId()));
                ThroughputCondition condition = new ThroughputCondition();
                condition.setPipelineId(pipeline.getId());
                condition.setType(ThroughputType.ROW);
                ThroughputStat throughputStat = throughputStatService.findThroughputStatByPipelineId(condition);
//                throughputStats.put(pipeline.getId(), throughputStat);
                List<AlarmRule> alarmRules = alarmRuleService.getAlarmRules(pipeline.getId());
//                alarmRuleStats.put(pipeline.getId(), alarmRules);
                PositionEventData positionData = arbitrateViewService.getCanalCursor(pipeline.getParameters().getDestinationName(),
                        pipeline.getParameters().getMainstemClientId());



//                positionDatas.put(pipeline.getId(), positionData);

                SeniorPipeline seniorPipeline = new SeniorPipeline();
                try{
                    BeanUtils.copyProperties(pipeline,seniorPipeline);
                }catch (Exception e){}
                seniorPipeline.setDelayStat(delayStat);
                seniorPipeline.setMainStemEventData(mainStemEventData);
                seniorPipeline.setThroughputStat(throughputStat);
                seniorPipeline.setPositionEventData(positionData);
                seniorPipeline.setAlarmRules(alarmRules);
                seniorPipeline.setChannel(channel);
                seniorPipelines.add(seniorPipeline);
            }
            return ApiResult.success(seniorPipelines);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ApiResult<String> add(@RequestBody PipelineForm pipelineForm){
        try{
            Pipeline pipeline = new Pipeline();
            PipelineParameter parameters = new PipelineParameter();
            try{
                EnumBeanUtils.copyProperties(pipelineForm,pipeline);
                EnumBeanUtils.copyProperties(pipelineForm,parameters);
            }catch (Exception e){
            }
            List<Long> selectNodeIds = Arrays.asList(ArrayUtils.toObject(pipelineForm.getSelectNodeIds())
                  );
            List<Node> selectNodes = new ArrayList<Node>();
            for (Long selectNodeId : selectNodeIds) {
                Node node = new Node();
                node.setId(selectNodeId);
                selectNodes.add(node);
            }

            // select/extract节点普遍配置为同一个节点
            List<Long> extractNodeIds = Arrays.asList(ArrayUtils.toObject(pipelineForm.getSelectNodeIds()));
            // List<Long> extractNodeIds =
            // Arrays.asList(ArrayUtils.toObject(pipelineInfo.getField("extractNodeIds").getLongValues()));
            List<Node> extractNodes = new ArrayList<Node>();
            for (Long extractNodeId : extractNodeIds) {
                Node node = new Node();
                node.setId(extractNodeId);
                extractNodes.add(node);
            }

            List<Long> loadNodeIds = Arrays.asList(ArrayUtils.toObject(pipelineForm.getLoadNodeIds()));
            List<Node> loadNodes = new ArrayList<Node>();
            for (Long loadNodeId : loadNodeIds) {
                Node node = new Node();
                node.setId(loadNodeId);
                loadNodes.add(node);
            }

            pipeline.setSelectNodes(selectNodes);
            pipeline.setExtractNodes(extractNodes);
            pipeline.setLoadNodes(loadNodes);
            pipeline.setParameters(parameters);

            pipelineService.create(pipeline);

            return ApiResult.success("添加成功");
        }catch (Exception e){
            return ApiResult.failed(e.getMessage());
        }
    }


    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ApiResult<String> update(@RequestBody PipelineForm pipelineForm){
        try{
            Pipeline pipeline = new Pipeline();
            PipelineParameter parameters = new PipelineParameter();
            try{
                EnumBeanUtils.copyProperties(pipelineForm,pipeline);
                EnumBeanUtils.copyProperties(pipelineForm,parameters);
            }catch (Exception e){
            }
            parameters.setPipelineId(pipeline.getId());
            List<Long> selectNodeIds = Arrays.asList(ArrayUtils.toObject(pipelineForm.getSelectNodeIds()));
            List<Node> selectNodes = new ArrayList<Node>();
            for (Long selectNodeId : selectNodeIds) {
                Node node = new Node();
                node.setId(selectNodeId);
                selectNodes.add(node);
            }

            // select/extract节点普遍配置为同一个节点
            List<Long> extractNodeIds = Arrays.asList(ArrayUtils.toObject(pipelineForm.getSelectNodeIds()));
            // List<Long> extractNodeIds =
            // Arrays.asList(ArrayUtils.toObject(pipelineInfo.getField("extractNodeIds").getLongValues()));
            List<Node> extractNodes = new ArrayList<Node>();
            for (Long extractNodeId : extractNodeIds) {
                Node node = new Node();
                node.setId(extractNodeId);
                extractNodes.add(node);
            }

            List<Long> loadNodeIds = Arrays.asList(ArrayUtils.toObject(pipelineForm.getLoadNodeIds()));
            List<Node> loadNodes = new ArrayList<Node>();
            for (Long loadNodeId : loadNodeIds) {
                Node node = new Node();
                node.setId(loadNodeId);
                loadNodes.add(node);
            }

            pipeline.setSelectNodes(selectNodes);
            pipeline.setExtractNodes(extractNodes);
            pipeline.setLoadNodes(loadNodes);
            pipeline.setParameters(parameters);

            List<Pipeline> values = pipelineService.listByDestinationWithoutOther(pipeline.getParameters()
                    .getDestinationName());

            if (!values.isEmpty()) {
                if (values.size() > 1 || !values.get(0).getId().equals(pipeline.getId())) {
                    return ApiResult.failed("更新错误");
                }
            }
            pipelineService.modify(pipeline);
             return ApiResult.success("更新成功");
        }catch (Exception e){
            return ApiResult.failed(e.getMessage());
        }
    }
    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    public ApiResult<String> remove(@RequestBody PipelineForm pipelineForm){
        try{
            pipelineService.remove(pipelineForm.getId());
            return ApiResult.success("删除成功");
        }catch (Exception e){
            return ApiResult.failed(e.getMessage());
        }
    }
}
