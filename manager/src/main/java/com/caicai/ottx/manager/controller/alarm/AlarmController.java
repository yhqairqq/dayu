package com.caicai.ottx.manager.controller.alarm;

import com.alibaba.otter.shared.common.model.config.alarm.AlarmRule;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.vo.PageResult;
import com.caicai.ottx.manager.controller.alarm.form.AlarmForm;
import com.caicai.ottx.service.config.alarm.AlarmRuleService;
import com.caicai.ottx.service.config.channel.ChannelService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
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

@RequestMapping("/kkkk")
@RestController
public class AlarmController {
    @Autowired
    private AlarmRuleService alarmRuleService;
    @Autowired
    private ChannelService channelService;

    @RequestMapping(value = "/getByPage",method = RequestMethod.POST)
    public ApiResult<PageResult> getByPage(@RequestBody AlarmForm alarmForm){
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("current", alarmForm.getCurrentPage());
        condition.put("pageSize", alarmForm.getPageSize());
        List<AlarmRule> alarmRules = alarmRuleService.listAllAlarmRules(condition);
        return ApiResult.success(new PageResult(alarmRules,(Page)alarmRules));
    }
    @RequestMapping(value = "/findByPipelineId",method = RequestMethod.POST)
    public ApiResult<List<AlarmRule>> findByPipelineId(@RequestBody AlarmForm alarmForm){
      try{
          List<AlarmRule> alarmRuleList =  alarmRuleService.getAlarmRules(alarmForm.getPipelineId());
          return ApiResult.success(alarmRuleList);
      }catch (Exception e){
          return ApiResult.failed(e.getMessage());
      }
    }


}
