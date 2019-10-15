package com.caicai.ottx.manager.controller.media;

import com.alibaba.otter.shared.common.model.config.data.DataMedia;
import com.alibaba.otter.shared.common.model.config.data.DataMediaPair;
import com.alibaba.otter.shared.common.model.config.data.DataMediaSource;
import com.alibaba.otter.shared.common.model.config.data.db.DbMediaSource;
import com.alibaba.otter.shared.common.model.config.data.mq.MqMediaSource;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.utils.BeanUtils;
import com.caicai.ottx.common.vo.PageResult;
import com.caicai.ottx.manager.controller.media.form.MediaForm;
import com.caicai.ottx.manager.controller.mediasource.form.MediaSourceForm;
import com.caicai.ottx.manager.web.common.model.SeniorDataMedia;
import com.caicai.ottx.manager.web.common.model.SeniorDataMediaSource;
import com.caicai.ottx.service.config.datamedia.DataMediaService;
import com.caicai.ottx.service.config.datamediapair.DataMediaPairService;
import com.caicai.ottx.service.config.datamediasource.DataMediaSourceService;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/10/8.
 */
@RestController
@Slf4j
@RequestMapping("/media")
public class MediaController {
    @Autowired
    private DataMediaService dataMediaService;
    @Autowired
    private DataMediaPairService dataMediaPairService;
    @Autowired
    private DataMediaSourceService dataMediaSourceService;

    @RequestMapping(value = "/getByPage", method = RequestMethod.POST)
    public ApiResult<PageResult> getByPage(@RequestBody MediaForm mediaForm) {
        Map<String, Object> condition = null;
        try {
            condition = BeanUtils.objectToMap(mediaForm);
            List<DataMedia> dataMediaList = dataMediaService.listByCondition(condition);
            if (CollectionUtils.isEmpty(dataMediaList)) {
                return ApiResult.success(new PageResult(null, new Page()));
            }
            List<SeniorDataMedia> seniorDataMedias = new ArrayList<SeniorDataMedia>();
            for (DataMedia dataMedia : dataMediaList) {
                SeniorDataMedia seniorDataMedia = new SeniorDataMedia();
                seniorDataMedia.setId(dataMedia.getId());
                seniorDataMedia.setEncode(dataMedia.getEncode());
                seniorDataMedia.setGmtCreate(dataMedia.getGmtCreate());
                seniorDataMedia.setGmtModified(dataMedia.getGmtModified());
                seniorDataMedia.setName(dataMedia.getName());
                seniorDataMedia.setNamespace(dataMedia.getNamespace());
                seniorDataMedia.setSource(dataMedia.getSource());
                seniorDataMedia.setTopic(dataMedia.getTopic());
                List<DataMediaPair> pairs = dataMediaPairService.listByDataMediaId(dataMedia.getId());
                seniorDataMedia.setPairs(pairs);
                if (pairs.size() < 1) {
                    seniorDataMedia.setUsed(false);
                } else {
                    seniorDataMedia.setUsed(true);
                }
                seniorDataMedias.add(seniorDataMedia);
            }
            return ApiResult.success(new PageResult(seniorDataMedias, (Page) dataMediaList));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ApiResult.failed(e.getMessage());
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ApiResult<String> addMedia(@RequestBody MediaForm mediaForm) {
        try {
            DataMedia dataMedia = new DataMedia();
            // reduce
            String name = mediaForm.getNames().stream().reduce( (a, b) ->  a + ";" + b
            ).get();
            dataMedia.setName(name);
            dataMedia.setNamespace(mediaForm.getNamespace());
            dataMedia.setTopic(mediaForm.getTopic());
            DataMediaSource dataMediaSource = dataMediaSourceService.findById(mediaForm.getSourceId());
            if (dataMediaSource.getType().isMysql() || dataMediaSource.getType().isOracle()) {
                dataMedia.setSource((DbMediaSource) dataMediaSource);
            } else if (dataMediaSource.getType().isNapoli() || dataMediaSource.getType().isKafka() || dataMediaSource.getType().isRocketMq()) {
                dataMedia.setSource((MqMediaSource) dataMediaSource);
            }
            dataMediaService.create(dataMedia);
            return ApiResult.success("插入成功");
        } catch (Exception e) {
            e.getMessage();
            log.error(e.getMessage());
            return ApiResult.failed(e.getMessage());
        }
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ApiResult<String> update(@RequestBody MediaForm mediaForm) {
        try {
            DataMedia dataMedia = new DataMedia();
            dataMedia.setId(mediaForm.getId());
            // reduce
            String name =  mediaForm.getNames().stream().reduce( (a, b) ->  a + ";" + b + ";"
            ).get();
            dataMedia.setTopic(mediaForm.getTopic());
            dataMedia.setName(name);
            dataMedia.setNamespace(mediaForm.getNamespace());
            DataMediaSource dataMediaSource = dataMediaSourceService.findById(mediaForm.getSourceId());
            if (dataMediaSource.getType().isMysql() || dataMediaSource.getType().isOracle()) {
                dataMedia.setSource((DbMediaSource) dataMediaSource);
            } else if (dataMediaSource.getType().isNapoli() || dataMediaSource.getType().isKafka() || dataMediaSource.getType().isRocketMq()) {
                dataMedia.setSource((MqMediaSource) dataMediaSource);
            }
            dataMediaService.modify(dataMedia);
            return ApiResult.success("更新成功");
        } catch (Exception e) {
            e.getMessage();
            log.error(e.getMessage());
            return ApiResult.failed(e.getMessage());
        }
    }


}
