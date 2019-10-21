package com.caicai.ottx.manager.controller.transpair;

import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.data.ColumnPairMode;
import com.alibaba.otter.shared.common.model.config.data.DataMedia;
import com.alibaba.otter.shared.common.model.config.data.DataMediaPair;
import com.alibaba.otter.shared.common.model.config.data.DataMediaSource;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.utils.BeanUtils;
import com.caicai.ottx.common.vo.PageResult;
import com.caicai.ottx.manager.controller.channel.form.ChannelForm;
import com.caicai.ottx.manager.controller.transpair.form.TransPairForm;
import com.caicai.ottx.manager.web.common.model.SeniorChannel;
import com.caicai.ottx.service.config.channel.model.Tag;
import com.caicai.ottx.service.config.datamedia.DataMediaService;
import com.caicai.ottx.service.config.datamediasource.DataMediaSourceService;
import com.caicai.ottx.service.trans.DataMediaPairTransService;
import com.caicai.ottx.service.trans.model.DataMediaPairTrans;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by huaseng on 2019/10/18.
 */
@RestController
@RequestMapping("/transpair")
@Slf4j
public class DataMediaPairTransController {
    @Autowired
    private DataMediaPairTransService dataMediaPairTransService;
    @Autowired
    private DataMediaSourceService dataMediaSourceService;
    @Autowired
    private DataMediaService dataMediaService;

    @RequestMapping(value = "/getByPage", method = RequestMethod.POST)
    public ApiResult<PageResult> getByPage(@RequestBody TransPairForm transPairForm) {
        try {
            Map<String, Object> condition = BeanUtils.objectToMap(transPairForm);
            List<DataMediaPairTrans> dataMediaPairTrans = dataMediaPairTransService.listByCondition(condition);
            if (CollectionUtils.isEmpty(dataMediaPairTrans)) {
                return ApiResult.success(new PageResult(null, new Page()));
            }
            return ApiResult.success(new PageResult(dataMediaPairTrans, (Page) dataMediaPairTrans));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResult.failed(e.getMessage());
        }
    }

    /**
     * 模板情况下添加
     *
     * @param transPairForm
     * @return
     */
    private ApiResult patternAdd(TransPairForm transPairForm) {
        try {
            DataMediaPairTrans dataMediaPairTrans = new DataMediaPairTrans();
            dataMediaPairTrans.setColumnPairMode(ColumnPairMode.INCLUDE);
            DataMedia sourceMedia = new DataMedia();
            sourceMedia.setId(transPairForm.getSourceMediaId());
            DataMedia targetMedia = new DataMedia();
            targetMedia.setId(transPairForm.getTargetMediaId());
            dataMediaPairTrans.setSource(sourceMedia);
            dataMediaPairTrans.setTarget(targetMedia);
            dataMediaPairTransService.create(dataMediaPairTrans);
            return ApiResult.success("添加成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }

    private ApiResult customizeAdd(TransPairForm transPairForm) {
        try{
            List<String> sourceDataMedia = transPairForm.getSourceDataMedia();
            List<String> targetDataMedia = transPairForm.getTargetDataMedia();
            sourceDataMedia = !CollectionUtils.isEmpty(sourceDataMedia) ?
                    sourceDataMedia.stream().filter(item -> !StringUtils.isEmpty(item)).collect(Collectors.toList())
                    : sourceDataMedia;
            String sourceSchema = sourceDataMedia.get(0).split("\\.")[0];
            targetDataMedia = !CollectionUtils.isEmpty(targetDataMedia) ?
                    targetDataMedia.stream().filter(item -> !StringUtils.isEmpty(item)).collect(Collectors.toList())
                    : targetDataMedia;
            String targetSchema = targetDataMedia.size() > 0 ? targetDataMedia.get(0).split("\\.")[0] : "";
            DataMediaSource source = dataMediaSourceService.findById(transPairForm.getSourceId());
            DataMediaSource target = dataMediaSourceService.findById(transPairForm.getTargetId());
            DataMedia sourceMedia = new DataMedia();
            if (sourceDataMedia.size() == 1) {
                String table = sourceDataMedia.get(0);
                String values[] = table.split("\\.");
                sourceMedia.setName(values[1]);
            } else {
                StringBuilder sourceNames = new StringBuilder();
                for (String table : sourceDataMedia) {
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
            targetMedia.setSource(target);
            targetMedia.setMode(DataMedia.Mode.SINGLE);
            targetMedia.setNamespace(targetSchema);

            for (String table : targetDataMedia) {
                String values[] = table.split("\\.");
                targetMedia.setName(values[1]);
                break;
            }
            long targetDataMediaId = dataMediaService.createReturnId(targetMedia);
            targetMedia.setId(targetDataMediaId);
            DataMediaPairTrans dataMediaPairTrans = new DataMediaPairTrans();
            dataMediaPairTrans.setColumnPairMode(ColumnPairMode.INCLUDE);
            dataMediaPairTrans.setSource(sourceMedia);
            dataMediaPairTrans.setTarget(targetMedia);
            dataMediaPairTransService.createAndReturnId(dataMediaPairTrans);
            return ApiResult.success("添加成功");
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return ApiResult.failed(e.getMessage());
        }

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ApiResult add(@RequestBody TransPairForm transPairForm) {
        if (transPairForm.getSourceMediaId() != 0 && transPairForm.getTargetMediaId() != 0) {
            return patternAdd(transPairForm);
        } else {
            return customizeAdd(transPairForm);
        }
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public ApiResult remove(@RequestBody TransPairForm transPairForm) {
        try {
            dataMediaPairTransService.remove(transPairForm.getId());
            return ApiResult.success("删除成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ApiResult update(@RequestBody TransPairForm transPairForm) {
        try {
            DataMediaPairTrans dataMediaPairTrans = new DataMediaPairTrans();
            dataMediaPairTrans.setId(transPairForm.getId());
            dataMediaPairTrans.setColumnPairMode(ColumnPairMode.INCLUDE);
            DataMedia sourceMedia = new DataMedia();
            dataMediaPairTrans.setId(transPairForm.getSourceMediaId());
            DataMedia targetMedia = new DataMedia();
            dataMediaPairTrans.setId(transPairForm.getTargetMediaId());
            dataMediaPairTrans.setSource(sourceMedia);
            dataMediaPairTrans.setTarget(targetMedia);
            dataMediaPairTransService.create(dataMediaPairTrans);
            return ApiResult.success("更新成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }
}
