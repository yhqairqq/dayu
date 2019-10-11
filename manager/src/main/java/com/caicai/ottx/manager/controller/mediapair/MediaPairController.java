package com.caicai.ottx.manager.controller.mediapair;

import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.data.*;
import com.alibaba.otter.shared.common.model.statistics.table.TableStat;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.manager.controller.mediapair.form.MediaPairForm;
import com.caicai.ottx.manager.web.common.model.SeniorDataMediaPair;
import com.caicai.ottx.service.config.channel.ChannelService;
import com.caicai.ottx.service.config.datamedia.DataMediaService;
import com.caicai.ottx.service.config.datamediapair.DataMediaPairService;
import com.caicai.ottx.service.config.datamediasource.DataMediaSourceService;
import com.caicai.ottx.service.statistics.table.TableStatService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by huaseng on 2019/9/10.
 */
@RequestMapping(value = "/mediapair")
@RestController
public class MediaPairController {
    @Autowired
    private DataMediaPairService dataMediaPairService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private DataMediaService dataMediaService;
    @Autowired
    private DataMediaSourceService dataMediaSourceService;

    @Autowired
    private TableStatService tableStatService;

    @RequestMapping(value = "/mediapairs",method = RequestMethod.POST)
    public ApiResult<List<SeniorDataMediaPair>> getMediaPairs(@RequestBody MediaPairForm mediaPairForm){
        try{
            Channel channel = channelService.findByPipelineId(mediaPairForm.getPipelineId());
            List<DataMediaPair> dataMediaPairs = dataMediaPairService.listByPipelineId(mediaPairForm.getPipelineId());

            List<TableStat> tableStats = tableStatService.listTableStat(mediaPairForm.getPipelineId());
            List<SeniorDataMediaPair> seniorDataMediaPairs = new ArrayList<>();
            for (DataMediaPair dataMediaPair : dataMediaPairs) {
                int flag = 0;
                TableStat tableStat1 = null;
                for (TableStat tableStat : tableStats) {
                    if (dataMediaPair.getId().equals(tableStat.getDataMediaPairId())) {
//                        tableStatMap.put(dataMediaPair.getId(), tableStat);
                        tableStat1 = tableStat;
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    tableStat1 = new TableStat();
                    tableStat1.setFileSize(0L);
                    tableStat1.setFileCount(0L);
                    tableStat1.setDeleteCount(0L);
                    tableStat1.setUpdateCount(0L);
                    tableStat1.setInsertCount(0L);
                    tableStat1.setGmtModified(dataMediaPair.getGmtModified());

                }
                SeniorDataMediaPair seniorDataMediaPair = new SeniorDataMediaPair();
                BeanUtils.copyProperties(dataMediaPair,seniorDataMediaPair);
                seniorDataMediaPair.setTableStat(tableStat1);
                seniorDataMediaPairs.add(seniorDataMediaPair);
                seniorDataMediaPair.setChannel(channel);
            }
            return ApiResult.success(seniorDataMediaPairs);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ApiResult<String> addMediaPair(@RequestBody MediaPairForm mediaPairForm){
        try{
            List<String> sourceDataMedia = mediaPairForm.getSourceDataMedia();
            List<String> targetDataMedia = mediaPairForm.getTargetDataMedia();
            sourceDataMedia= !CollectionUtils.isEmpty(sourceDataMedia)?
                    sourceDataMedia.stream().filter(item-> !StringUtils.isEmpty(item)).collect(Collectors.toList())
                    :sourceDataMedia;
            String sourceSchema = sourceDataMedia.get(0).split("\\.")[0];
            targetDataMedia= !CollectionUtils.isEmpty(targetDataMedia)?
                    targetDataMedia.stream().filter(item-> !StringUtils.isEmpty(item)).collect(Collectors.toList())
                    :targetDataMedia;
            String targetSchema = targetDataMedia.size()>0?targetDataMedia.get(0).split("\\.")[0]:"";
            DataMediaSource source = dataMediaSourceService.findById(mediaPairForm.getSourceId());
            DataMediaSource target = dataMediaSourceService.findById(mediaPairForm.getTargetId());
            DataMedia sourceMedia = new DataMedia();
            if(sourceDataMedia.size()  == 1){
                String table = sourceDataMedia.get(0);
                String values[] = table.split("\\.");
                sourceMedia.setName(values[1]);
            }else{
                StringBuilder sourceNames = new StringBuilder();
                for(String table:sourceDataMedia){
                    String values[] = table.split("\\.");
                    sourceNames.append(values[1]).append(";");
                }
                sourceMedia.setName(sourceNames.toString());
            }
            sourceMedia.setMode(DataMedia.Mode.SINGLE);
            sourceMedia.setNamespace(sourceSchema);

            sourceMedia.setSource(source);
            long sourceDataMediaId = dataMediaService.createReturnId(sourceMedia);
            sourceMedia.setId(sourceDataMediaId);
            DataMedia targetMedia = new DataMedia();
            //两种情况 1、mysql->mysql  mysql->mq
            //1
            targetMedia.setName(".*");
            targetMedia.setSource(target);
            targetMedia.setTopic(mediaPairForm.getTopic());
            targetMedia.setMode(DataMedia.Mode.SINGLE);
            if(target.getType().isMysql()){
                targetMedia.setNamespace(targetSchema);
               if(sourceDataMedia.size() == 1){
                   for(String table:targetDataMedia){
                       String values[] = table.split("\\.");
                       targetMedia.setName(values[1]);
                       break;
                   }
               }

            }else if(target.getType().isRocketMq() ||target.getType().isKafka() ){
                //如果目标为MQ  那么schema 和  source 一致
                targetMedia.setNamespace(sourceSchema);
            }
            long targetDataMediaId = dataMediaService.createReturnId(targetMedia);
            targetMedia.setId(targetDataMediaId);
            DataMediaPair mediaPair = wrapper(mediaPairForm);
            mediaPair.setPipelineId(mediaPairForm.getPipelineId());
            mediaPair.setPushWeight(mediaPairForm.getPushWeight());
            mediaPair.setColumnPairMode(ColumnPairMode.INCLUDE);
            mediaPair.setSource(sourceMedia);
            mediaPair.setTarget(targetMedia);
            dataMediaPairService.createAndReturnId(mediaPair);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
        return ApiResult.success("添加成功");
    }

    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    public ApiResult<String> remove(@RequestBody MediaPairForm mediaPairForm){
        try{
            dataMediaPairService.remove(mediaPairForm.getId());
            return ApiResult.success("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ApiResult<String> update(@RequestBody MediaPairForm mediaPairForm){
        try{
            DataMediaPair dataMediaPair =   wrapper(mediaPairForm);
            dataMediaPair.setId(mediaPairForm.getId());
            dataMediaPairService.modify(dataMediaPair);
            return ApiResult.success("更新成功");
        }catch (Exception e){
            return ApiResult.failed(e.getMessage());
        }

    }




    private DataMediaPair wrapper(MediaPairForm mediaPairForm){
        DataMediaPair dataMediaPair = new DataMediaPair();
        // filter解析
        ExtensionDataType filterType = ExtensionDataType.valueOf(mediaPairForm.getFilterType());
        ExtensionData filterData = new ExtensionData();
        filterData.setExtensionDataType(filterType);
        if (filterType.isClazz()) {
            filterData.setClazzPath(mediaPairForm.getFilterText());
        } else if (filterType.isSource()) {
            filterData.setSourceText(mediaPairForm.getFilterText());
        }
        dataMediaPair.setFilterData(filterData);

        ExtensionData resolverData = new ExtensionData();
        resolverData.setExtensionDataType(ExtensionDataType.CLAZZ);
        dataMediaPair.setResolverData(resolverData);
        return dataMediaPair;
    }
}
