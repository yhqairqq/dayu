package com.caicai.ottx.manager.controller;

import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.service.utils.DataSourceChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by huaseng on 2019/9/23.
 */
@RestController
@RequestMapping("/valid")
public class ValidController {

    @Autowired
    private DataSourceChecker dataSourceChecker;

    @RequestMapping(value = "/binlogList",method = RequestMethod.POST)
    public ApiResult<String> binlogList(@RequestBody ValidForm validForm){
        try{
            String result =  dataSourceChecker.listBinlog(validForm.getUrl(),validForm.getUsername(),validForm.getPassword());
            return ApiResult.success(result);
        }catch (Exception e){
            return ApiResult.failed(e.getMessage());
        }

    }
}
