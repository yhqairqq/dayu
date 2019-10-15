package com.caicai.ottx.manager.controller.mediasource;

import com.alibaba.otter.canal.instance.manager.model.Canal;
import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.alibaba.otter.shared.common.model.config.data.DataMedia;
import com.alibaba.otter.shared.common.model.config.data.DataMediaSource;
import com.alibaba.otter.shared.common.model.config.data.DataMediaType;
import com.alibaba.otter.shared.common.model.config.data.db.DbMediaSource;
import com.alibaba.otter.shared.common.model.config.data.mq.MqMediaSource;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.utils.BeanUtils;
import com.caicai.ottx.common.vo.PageResult;
import com.caicai.ottx.manager.controller.mediasource.form.MediaSourceForm;
import com.caicai.ottx.manager.web.common.model.SeniorDataMediaSource;
import com.caicai.ottx.service.config.canal.CanalService;
import com.caicai.ottx.service.config.datamedia.DataMediaService;
import com.caicai.ottx.service.config.datamediasource.DataMediaSourceService;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by huaseng on 2019/9/2.
 */
@RestController
@RequestMapping("/mediasource")
@Slf4j
public class MediaSourceController {

    @Autowired
    private DataMediaSourceService dataMediaSourceService;
    @Autowired
    private DataMediaService dataMediaService;

    @Autowired
    private CanalService canalService;

    @RequestMapping(value = "/getByPage", method = RequestMethod.POST)
    public ApiResult<PageResult> getByPage(@RequestBody MediaSourceForm mediaSourceForm) {
        try{
            Map<String, Object> condition = BeanUtils.objectToMap(mediaSourceForm);
            List<DataMediaSource> dataMediaSources = dataMediaSourceService.listByCondition(condition);
            if (CollectionUtils.isEmpty(dataMediaSources)) {
                return ApiResult.success(new PageResult(null, new Page()));
            }
            List<SeniorDataMediaSource> seniorDataMediaSources = new ArrayList<SeniorDataMediaSource>();
            for (DataMediaSource dataMediaSource : dataMediaSources) {

                SeniorDataMediaSource seniorDataMediaSource = new SeniorDataMediaSource();
                seniorDataMediaSource.setEncode(dataMediaSource.getEncode());
                seniorDataMediaSource.setGmtCreate(dataMediaSource.getGmtCreate());
                seniorDataMediaSource.setGmtModified(dataMediaSource.getGmtModified());
                seniorDataMediaSource.setId(dataMediaSource.getId());
                seniorDataMediaSource.setName(dataMediaSource.getName());
                seniorDataMediaSource.setType(dataMediaSource.getType());
                if (dataMediaSource instanceof DbMediaSource) {
                    seniorDataMediaSource.setDriver(((DbMediaSource) dataMediaSource).getDriver());
                    seniorDataMediaSource.setUrl(((DbMediaSource) dataMediaSource).getUrl());
                    seniorDataMediaSource.setUsername(((DbMediaSource) dataMediaSource).getUsername());
                } else if (dataMediaSource instanceof MqMediaSource) {
                    seniorDataMediaSource.setUrl(((MqMediaSource) dataMediaSource).getUrl());
//                seniorDataMediaSource.setStorePath(((MqMediaSource) dataMediaSource).getStorePath());
                }
                List<DataMedia> dataMedia = dataMediaService.listByDataMediaSourceId(dataMediaSource.getId());
                seniorDataMediaSource.setDataMedias(dataMedia);
                if (dataMedia.size() < 1) {
                    seniorDataMediaSource.setUsed(false);
                } else {
                    seniorDataMediaSource.setUsed(true);
                }
                seniorDataMediaSources.add(seniorDataMediaSource);
            }
            return ApiResult.success(new PageResult(seniorDataMediaSources, (Page) dataMediaSources));
        }catch (Exception e){
            log.error(e.getMessage());
            return ApiResult.failed(e.getMessage());
        }
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ApiResult<String> add(@RequestBody MediaSourceForm mediaSourceForm){
        try{
            DataMediaSource mediaSource = null;
            if(StringUtils.equalsIgnoreCase(mediaSourceForm.getType(),"MYSQL")){
                mediaSource  = new DbMediaSource();
                mediaSource.setType(DataMediaType.MYSQL);
                ((DbMediaSource)mediaSource).setDriver("com.mysql.jdbc.Driver");
            }else if(StringUtils.equalsIgnoreCase(mediaSourceForm.getType(),"ROCKETMQ")){
                mediaSource = new MqMediaSource();
                mediaSource.setType(DataMediaType.ROCKETMQ);
            }else if(StringUtils.equalsIgnoreCase(mediaSourceForm.getType(),"KAFKA")){
                mediaSource = new MqMediaSource();
                mediaSource.setType(DataMediaType.KAFKA);
            }
            org.springframework.beans.BeanUtils.copyProperties(mediaSourceForm,mediaSource);
            dataMediaSourceService.create(mediaSource);

        }catch (Exception e){
            ApiResult.failed(e.getMessage());
        }
        return ApiResult.success("添加成功");
    }

    @RequestMapping(value = "/getDetailById",method = RequestMethod.POST)
    public ApiResult<DataMediaSource> getDetailById(@RequestBody MediaSourceForm mediaSourceForm){
       try{
           DataMediaSource dataMediaSource =  dataMediaSourceService.findById(mediaSourceForm.getId());
           return ApiResult.success(dataMediaSource);
       }catch (Exception e){
           e.printStackTrace();
           ApiResult.failed(e.getMessage());
       }
       return ApiResult.failed("内部错误");
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ApiResult<String> update(@RequestBody MediaSourceForm mediaSourceForm){
        try{
            DataMediaSource mediaSource = null;
            if(StringUtils.equalsIgnoreCase(mediaSourceForm.getType(),"MYSQL")){
                mediaSource  = new DbMediaSource();
                mediaSource.setType(DataMediaType.MYSQL);
                ((DbMediaSource)mediaSource).setDriver("com.mysql.jdbc.Driver");
            }else if(StringUtils.equalsIgnoreCase(mediaSourceForm.getType(),"ROCKETMQ")){
                mediaSource = new MqMediaSource();
                mediaSource.setType(DataMediaType.ROCKETMQ);
            }else if(StringUtils.equalsIgnoreCase(mediaSourceForm.getType(),"KAFKA")){
                mediaSource = new MqMediaSource();
                mediaSource.setType(DataMediaType.KAFKA);
            }
            org.springframework.beans.BeanUtils.copyProperties(mediaSourceForm,mediaSource);
            dataMediaSourceService.modify(mediaSource);

        }catch (Exception e){
            ApiResult.failed(e.getMessage());
        }
        return ApiResult.success("添加成功");
    }
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public ApiResult<String> delete(@RequestBody MediaSourceForm mediaSourceForm){
        dataMediaSourceService.remove(mediaSourceForm.getId());
        return ApiResult.success("删除成功");
    }

    @RequestMapping(value = "/listAll",method = RequestMethod.POST)
    public ApiResult<List<SeniorDataMediaSource>> listAll(@RequestBody MediaSourceForm mediaSourceForm){
        List<String> canalUrls = new ArrayList<>();
       if(StringUtils.isNotBlank(mediaSourceForm.getDestinationName())){
           Canal canal = canalService.findByName(mediaSourceForm.getDestinationName());
           List<List<CanalParameter.DataSourcing>> groupDbAddresses =  canal.getCanalParameter().getGroupDbAddresses();
           for(List<CanalParameter.DataSourcing> groupDbAddress:groupDbAddresses){
               for(CanalParameter.DataSourcing dbAddress:groupDbAddress){
                   canalUrls.add(dbAddress.getDbAddress().getHostString());
               }
           }
       }

        List<SeniorDataMediaSource> seniorDataMediaSources = new ArrayList<>();
        List<DataMediaSource> dataMediaSources = dataMediaSourceService.listAll();
        if (CollectionUtils.isEmpty(dataMediaSources)) {
            return ApiResult.success(seniorDataMediaSources);
        }

        if("source".equals(mediaSourceForm.getType())&&canalUrls.size() > 0){
            dataMediaSources = dataMediaSources.stream().filter(dataMediaSource -> {
                if(dataMediaSource.getType().isMysql()){
                    for(String canalUrl:canalUrls){
                        if(((DbMediaSource) dataMediaSource).getUrl().contains(canalUrl)){
                            return true;
                        }
                    }
                }
                return false;
            }).collect(Collectors.toList());
        }
        for (DataMediaSource dataMediaSource : dataMediaSources) {
            SeniorDataMediaSource seniorDataMediaSource = new SeniorDataMediaSource();
            seniorDataMediaSource.setEncode(dataMediaSource.getEncode());
            seniorDataMediaSource.setGmtCreate(dataMediaSource.getGmtCreate());
            seniorDataMediaSource.setGmtModified(dataMediaSource.getGmtModified());
            seniorDataMediaSource.setId(dataMediaSource.getId());
            seniorDataMediaSource.setName(dataMediaSource.getName());
            seniorDataMediaSource.setType(dataMediaSource.getType());
            if (dataMediaSource instanceof DbMediaSource) {
                seniorDataMediaSource.setDriver(((DbMediaSource) dataMediaSource).getDriver());
                seniorDataMediaSource.setUrl(((DbMediaSource) dataMediaSource).getUrl());
                seniorDataMediaSource.setUsername(((DbMediaSource) dataMediaSource).getUsername());
                seniorDataMediaSource.setPassword(((DbMediaSource) dataMediaSource).getPassword());
            } else if (dataMediaSource instanceof MqMediaSource) {
                seniorDataMediaSource.setUrl(((MqMediaSource) dataMediaSource).getUrl());
//                seniorDataMediaSource.setStorePath(((MqMediaSource) dataMediaSource).getStorePath());
            }
            List<DataMedia> dataMedia = dataMediaService.listByDataMediaSourceId(dataMediaSource.getId());
            seniorDataMediaSource.setDataMedias(dataMedia);
            if (dataMedia.size() < 1) {
                seniorDataMediaSource.setUsed(false);
            } else {
                seniorDataMediaSource.setUsed(true);
            }
            seniorDataMediaSources.add(seniorDataMediaSource);
        }
        return ApiResult.success(seniorDataMediaSources);
    }
}
