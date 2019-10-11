package com.caicai.ottx.manager.controller.full;

import com.alibaba.otter.shared.common.utils.cmd.Exec;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.Constants;
import com.caicai.ottx.manager.controller.full.form.SyncForm;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by huaseng on 2019/9/9.
 */
@RequestMapping("/full")
@RestController
public class FullSyncController {

    private static final String BASE_DIR =   System.getProperty("user.dir");

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
}
