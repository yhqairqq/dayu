package com.caicai.ottx.manager.controller.transpair;

import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.data.ColumnPairMode;
import com.alibaba.otter.shared.common.model.config.data.DataMedia;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.utils.BeanUtils;
import com.caicai.ottx.common.vo.PageResult;
import com.caicai.ottx.manager.controller.channel.form.ChannelForm;
import com.caicai.ottx.manager.controller.transpair.form.TransPairForm;
import com.caicai.ottx.manager.web.common.model.SeniorChannel;
import com.caicai.ottx.service.config.channel.model.Tag;
import com.caicai.ottx.service.trans.DataMediaPairTransService;
import com.caicai.ottx.service.trans.model.DataMediaPairTrans;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/10/18.
 */
@RestController
@RequestMapping("/transpair")
@Slf4j
public class DataMediaPairTransController {

    @Autowired
    private DataMediaPairTransService dataMediaPairTransService;
    @RequestMapping(value = "/getByPage",method = RequestMethod.POST)
    public ApiResult<PageResult> getByPage(@RequestBody TransPairForm transPairForm){
        try{
            Map<String, Object> condition = BeanUtils.objectToMap(transPairForm);
          List<DataMediaPairTrans> dataMediaPairTrans =  dataMediaPairTransService.listByCondition(condition);
            if(CollectionUtils.isEmpty(dataMediaPairTrans)){
                return ApiResult.success(new PageResult(null,new Page()));
            }
            return ApiResult.success(new PageResult(dataMediaPairTrans,(Page)dataMediaPairTrans));
        }catch (Exception e){
            log.error(e.getMessage());
            return ApiResult.failed(e.getMessage());
        }
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ApiResult add(@RequestBody TransPairForm transPairForm){
        try{
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
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }

    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    public ApiResult remove(@RequestBody TransPairForm transPairForm){
       try{
           dataMediaPairTransService.remove(transPairForm.getId());
           return ApiResult.success("删除成功");
       }catch (Exception e){
           log.error(e.getMessage());
           e.printStackTrace();
           return ApiResult.failed(e.getMessage());
       }
    }
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ApiResult update(@RequestBody TransPairForm transPairForm){
        try{
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
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }
}
