package com.caicai.ottx.manager.controller.channel;

import com.alibaba.otter.canal.instance.manager.model.Canal;
import com.alibaba.otter.shared.arbitrate.ArbitrateManageService;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.channel.ChannelParameter;
import com.alibaba.otter.shared.common.model.config.channel.ChannelStatus;
import com.alibaba.otter.shared.common.model.config.data.DataMediaPair;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.common.utils.cmd.Exec;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.utils.BeanUtils;
import com.caicai.ottx.common.utils.EnumBeanUtils;
import com.caicai.ottx.common.vo.PageResult;
import com.caicai.ottx.manager.controller.channel.form.ChannelForm;
import com.caicai.ottx.manager.web.common.model.SeniorChannel;
import com.caicai.ottx.service.common.arbitrate.ArbitrateConfigImpl;
import com.caicai.ottx.service.common.exceptions.InvalidConfigureException;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.common.exceptions.RepeatConfigureException;
import com.caicai.ottx.service.config.canal.CanalService;
import com.caicai.ottx.service.config.channel.ChannelService;
import com.caicai.ottx.service.config.channel.TagChannelRelationService;
import com.caicai.ottx.service.config.channel.TagService;
import com.caicai.ottx.service.config.channel.model.Tag;
import com.caicai.ottx.service.position.PositionService;
import com.caicai.ottx.service.statistics.stage.ProcessStatService;
import com.caicai.ottx.service.utils.ChannelDataxJobGenerator;
import com.github.pagehelper.Page;
import com.ibatis.common.jdbc.SimpleDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huaseng on 2019/8/22.
 */

@RequestMapping("/channel")
@RestController
@Slf4j
public class ChannelController {

    @Autowired
    private ArbitrateConfigImpl arbitrateConfigImpl;

    @Autowired
    private ArbitrateManageService arbitrateManageService;
    @Autowired
    private ChannelService channelService;

    @Autowired
    private TagChannelRelationService tagChannelRelationService;

    @Autowired
    private ProcessStatService processStatService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ChannelDataxJobGenerator channelDataxJobGenerator;
    @Autowired
    private CanalService canalService;


    @RequestMapping(value = "/getByPage",method = RequestMethod.POST)
    public ApiResult<PageResult> getByPage(@RequestBody ChannelForm channelForm){
        try{
            Map<String, Object> condition = BeanUtils.objectToMap(channelForm);
            List<Channel>  channels = channelService.listByConditionWithoutColumn(condition);
            List<SeniorChannel> seniorChannels = new ArrayList<SeniorChannel>();
            if(CollectionUtils.isEmpty(channels)){
                return ApiResult.success(new PageResult(null,new Page()));
            }
            for (Channel channel : channels) {
                boolean processEmpty = false;
                List<Pipeline> pipelines = channel.getPipelines();
                for (Pipeline pipeline : pipelines) {
                    if (processStatService.listRealtimeProcessStat(channel.getId(), pipeline.getId()).isEmpty()) {
                        processEmpty = true;
                    }
                }
                SeniorChannel seniorChannel = new SeniorChannel();
                seniorChannel.setId(channel.getId());
                seniorChannel.setName(channel.getName());
                seniorChannel.setParameters(channel.getParameters());
                seniorChannel.setPipelines(channel.getPipelines());
                seniorChannel.setStatus(channel.getStatus());
                seniorChannel.setDescription(channel.getDescription());
                seniorChannel.setGmtCreate(channel.getGmtCreate());
                seniorChannel.setGmtModified(channel.getGmtModified());
                seniorChannel.setProcessEmpty(processEmpty);
                //获取tag
                Tag tag = tagChannelRelationService.findByChannelId(channel.getId());
                seniorChannel.setTag(tag);
                seniorChannel.setReady(canReady(channel));
                seniorChannels.add(seniorChannel);
            }
            return ApiResult.success(new PageResult(seniorChannels,(Page)channels));
        }catch (Exception e){
            log.error(e.getMessage());
            return ApiResult.failed(e.getMessage());
        }
    }

    private boolean canReady(Channel channel){
        boolean hasPiplines = false;
        boolean hasMediaPairs = false;
        boolean hasNodes = false;
        boolean nodeAlive = false;
        for(Pipeline pipeline:channel.getPipelines()){
            hasPiplines = true;
            for(DataMediaPair dataMediaPair:pipeline.getPairs()){
                hasMediaPairs = true;
                break;
            }
            for(com.alibaba.otter.shared.common.model.config.node.Node node : pipeline.getExtractNodes()){
                hasNodes = true;
                if(node.getStatus().isStart()){
                    nodeAlive  = true;
                }
                break;
            }
        }
        return hasPiplines&&hasMediaPairs&&hasNodes&&nodeAlive;
    }


    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ApiResult<String> add(@RequestBody ChannelForm channelForm){
        Channel channel = new Channel();
        ChannelParameter parameter = new ChannelParameter();
        try{
            EnumBeanUtils.copyProperties(channelForm,channel);
            EnumBeanUtils.copyProperties(channelForm,parameter);
        }catch (Exception e){

        }
        channel.setStatus(ChannelStatus.STOP);
        channel.setParameters(parameter);
      try{
          channelService.create(channel);
          Tag tag = channelForm.getTag();
          tag.setChannelId(channel.getId());
          tagService.create(tag);
      }catch (Exception e){
          e.printStackTrace();
          return ApiResult.failed(e.getMessage());
      }
      return ApiResult.success("添加成功");
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ApiResult<String> update(@RequestBody ChannelForm channelForm){
        Channel channel = new Channel();
        ChannelParameter parameter = new ChannelParameter();
        try{
            EnumBeanUtils.copyProperties(channelForm,channel);
            EnumBeanUtils.copyProperties(channelForm,parameter);
        }catch (Exception e){

        }
        channel.setStatus(channelService.findById(channel.getId()).getStatus());
        parameter.setChannelId(channel.getId());
        channel.setParameters(parameter);
        try {
            channelService.modify(channel);
            Tag tag = channelForm.getTag();
            tag.setChannelId(channel.getId());
            tagService.modify(tag);
        } catch (RepeatConfigureException rce) {
            return ApiResult.failed(rce.getMessage());
        }
        return ApiResult.success("更新成功");
    }


    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    public ApiResult remove(@RequestBody ChannelForm channelForm){
        try{
            channelService.remove(channelForm.getId());
        }catch (Exception e){
            return ApiResult.failed(e.getMessage());
        }
        return ApiResult.success("删除成功");
    }

    /**
     * 用于Channel运行状态的更新操作
     * @return
     */
    @RequestMapping(value ="/doStatus",method = RequestMethod.POST)
    public ApiResult<String> doStatus(@RequestBody ChannelForm channelForm){
        Channel channel =  channelService.findById(channelForm.getId());
        List<Exec.Result> resultList = null;
        //处理位点
        if("new".equalsIgnoreCase(channelForm.getStart())){
            if(!CollectionUtils.isEmpty(channel.getPipelines())){
                channel.getPipelines().stream().forEach(item->{
                    positionService.remove(item.getId());
                });
            }
            channel.getPipelines().stream().forEach(item->{
                Canal canal = canalService.findByName(item.getParameters().getDestinationName());
                List<String> positions = canal.getCanalParameter().getPositions();
                positions.clear();
                canalService.modify(canal);
            });
        }else if("history".equalsIgnoreCase(channelForm.getStart())){
          try{
              SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
              Date now = simpleDateFormat.parse(channelForm.getStartTime());
              if(!CollectionUtils.isEmpty(channel.getPipelines())){
                  channel.getPipelines().stream().forEach(item->{
                      positionService.remove(item.getId());
                  });
              }
              channel.getPipelines().stream().forEach(item->{
                  Canal canal = canalService.findByName(item.getParameters().getDestinationName());
                  List<String> positions = canal.getCanalParameter().getPositions();
                  positions.clear();
                  positions.add("{\"timestamp\":"+now.getTime()+"}");
                  canalService.modify(canal);
              });
          }catch (Exception e){
              e.printStackTrace();
              return ApiResult.failed(e.getMessage());
          }
        }else if ("full".equalsIgnoreCase(channelForm.getStart())){
            Date now = new Date();
            //历史记录同步
            resultList =  channelDataxJobGenerator.processTask(channel);
            //最新位点同步
            for(Exec.Result result : resultList){
                if(result.getExitCode() != 0){
                    ApiResult.failed(result.getStdout());
                }
            }
            /**
             *  1、删除zk位点信息
             *  2、修改cannal 位点信息 时间戳参考全量同步钱的时间戳  {"journalName":"","position":0,"timestamp":0};
             *  3、启动链路
             */
            if(!CollectionUtils.isEmpty(channel.getPipelines())){
                channel.getPipelines().stream().forEach(item->{
                    positionService.remove(item.getId());
                });
            }
            channel.getPipelines().stream().forEach(item->{
                Canal canal = canalService.findByName(item.getParameters().getDestinationName());
                List<String> positions = canal.getCanalParameter().getPositions();
                positions.clear();
                positions.add("{\"timestamp\":"+now.getTime()+"}");
                canalService.modify(canal);
            });
        }

        if("start".equals(channelForm.getStatus())){
            try{
                channelService.startChannel(channelForm.getId());
            }catch (ManagerException e){
                if (e.getCause() instanceof InvalidConfigureException) {
                   return ApiResult.failed(((InvalidConfigureException) e.getCause()).getType().name());
                }
            }catch (InvalidConfigureException rce){
                return ApiResult.failed(rce.getType().name());
            }
        }else if("stop".equals(channelForm.getStatus())){
            channelService.stopChannel(channelForm.getId());
        }
        return ApiResult.success(CollectionUtils.isEmpty(resultList)?"":resultList.get(0).getStdout());
    }
    /**
     * 用户channel的配置强制推送
     * @param channelForm
     * @return
     */
    @RequestMapping(value ="/doNotify",method = RequestMethod.POST)
    public ApiResult<String> doNotify(@RequestBody ChannelForm channelForm){
        channelService.notifyChannel(channelForm.getId());
        return ApiResult.success("操作成功");
    }
}
