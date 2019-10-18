package com.caicai.ottx.manager.controller.full;

import com.alibaba.otter.shared.common.model.config.data.DataMediaPair;
import com.alibaba.otter.shared.common.utils.cmd.Exec;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.Constants;
import com.caicai.ottx.manager.controller.full.form.SyncForm;
import com.caicai.ottx.service.config.datamediapair.DataMediaPairService;
import com.caicai.ottx.service.trans.DataMediaPairTransService;
import com.caicai.ottx.service.trans.model.DataMediaPairTrans;
import com.caicai.ottx.service.utils.ChannelDataxJobGenerator;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by huaseng on 2019/9/9.
 */
@RequestMapping("/full")
@RestController
public class FullSyncController {

    private static final String BASE_DIR =   System.getProperty("user.dir");

    @Autowired
    private ChannelDataxJobGenerator channelDataxJobGenerator;

    @Autowired
    private DataMediaPairTransService dataMediaPairTransService;

    @RequestMapping(value = "/sync",method = RequestMethod.POST)
    public ApiResult sync(@RequestBody SyncForm syncForm){
        String cmd = BASE_DIR+"/datax/bin/datax.py "+BASE_DIR+"/datax/job/"+syncForm.getName();
        try{
            File file = new File(BASE_DIR+"/datax/job/"+syncForm.getName());
            FileUtils.write(file,
                    syncForm.getJson(),
                    "UTF-8");
            Exec.Result result = Exec.execute(cmd);
            return ApiResult.success(result.getStdout());
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }

    }

    @RequestMapping(value = "/pairSync",method = RequestMethod.POST)
    public ApiResult fullSync(@RequestBody SyncForm syncForm){
        if(syncForm.getMediaPairid() == 0){
            return ApiResult.failed("data media id not 0");
        }
        DataMediaPairTrans dataMediaPairTrans =   dataMediaPairTransService.findById(syncForm.getMediaPairid());
        if(dataMediaPairTrans == null){
            return ApiResult.failed("数据对为空");
        }
        DataMediaPair dataMediaPair = new DataMediaPair();
        dataMediaPair.setId(dataMediaPairTrans.getId());
        dataMediaPair.setSource(dataMediaPairTrans.getSource());
        dataMediaPair.setTarget(dataMediaPairTrans.getTarget());
        List<Exec.Result> resultList =  channelDataxJobGenerator.processTask(dataMediaPair);
       return  ApiResult.success(resultList);
    }

}
