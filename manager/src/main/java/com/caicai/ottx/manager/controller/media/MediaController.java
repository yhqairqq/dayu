package com.caicai.ottx.manager.controller.media;

import com.alibaba.otter.shared.common.model.config.data.DataMedia;
import com.alibaba.otter.shared.common.model.config.data.DataMediaPair;
import com.alibaba.otter.shared.common.model.config.data.DataMediaSource;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.vo.PageResult;
import com.caicai.ottx.manager.controller.media.form.MediaForm;
import com.caicai.ottx.manager.controller.mediasource.form.MediaSourceForm;
import com.caicai.ottx.manager.web.common.model.SeniorDataMedia;
import com.caicai.ottx.manager.web.common.model.SeniorDataMediaSource;
import com.caicai.ottx.service.config.datamedia.DataMediaService;
import com.caicai.ottx.service.config.datamediapair.DataMediaPairService;
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

    @RequestMapping(value = "/getByPage", method = RequestMethod.POST)
    public ApiResult<PageResult>  getByPage(@RequestBody MediaForm mediaForm) {
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("current", mediaForm.getCurrentPage());
        condition.put("pageSize", mediaForm.getPageSize());
        List<DataMedia> dataMediaList = dataMediaService.listByCondition(condition);
        if (CollectionUtils.isEmpty(dataMediaList)) {
            return ApiResult.success(new PageResult(null, new Page()));
        }
        List<SeniorDataMedia> seniorDataMedias = new ArrayList<SeniorDataMedia>();
        for(DataMedia dataMedia:dataMediaList){
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

    }
}
