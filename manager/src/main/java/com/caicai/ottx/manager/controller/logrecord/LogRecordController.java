package com.caicai.ottx.manager.controller.logrecord;

import com.alibaba.otter.shared.common.model.config.record.LogRecord;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.vo.PageResult;
import com.caicai.ottx.manager.controller.logrecord.form.LogRecordForm;
import com.caicai.ottx.service.config.record.LogRecordService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/9/2.
 */
@RestController
@RequestMapping("/logrecord")
public class LogRecordController {
    @Autowired
    private LogRecordService logRecordService;


    @RequestMapping(value = "/getByPage",method = RequestMethod.POST)
    public ApiResult<PageResult> getByPage(@RequestBody LogRecordForm logRecordForm){
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("current", logRecordForm.getCurrentPage());
        condition.put("pageSize", logRecordForm.getPageSize());
        List<LogRecord> logRecords = logRecordService.listByCondition(condition);
        return  ApiResult.success(new PageResult(logRecords,(Page)logRecords));
    }

    @RequestMapping(value = "/fetchByPipelineIdTop",method = RequestMethod.POST)
    public ApiResult<LogRecord> fetchByPipelineIdTop(@RequestBody LogRecordForm logRecordForm){
      try{
          LogRecord logRecord = logRecordService.listByPipelineIdTop(logRecordForm.getPipelineId());
          return  ApiResult.success(logRecord);
      }catch (Exception e){
          return ApiResult.failed(e.getMessage());
      }
    }




}
