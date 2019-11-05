package com.caicai.ottx.service.config.alarm.impl;

import com.alibaba.otter.shared.common.model.config.alarm.AlarmRule;
import com.alibaba.otter.shared.common.model.config.alarm.AlarmRuleStatus;
import com.alibaba.otter.shared.common.model.config.alarm.MonitorName;
import com.alibaba.otter.shared.common.model.config.parameter.SystemParameter;
import com.alibaba.otter.shared.common.utils.Assert;
import com.caicai.ottx.dal.entity.AlarmRuleDO;
import com.caicai.ottx.dal.entity.AlarmRuleParameter;
import com.caicai.ottx.dal.mapper.AlarmRuleDOMapperExt;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.config.alarm.AlarmRuleService;
import com.caicai.ottx.service.config.parameter.SystemParameterService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/9/2.
 */
@Service
@Slf4j
public class AlarmRuleServiceImpl implements AlarmRuleService {
    public static final String  TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private SystemParameterService systemParameterService;

    @Autowired
    private AlarmRuleDOMapperExt alarmRuleDOMapperExt;

    /**
     * 一键添加监控
     * @param alarmRule
     */

    public void doOnekeyAddMonitor(long pipelineId){
        SystemParameter systemParameter = systemParameterService.find();
        AlarmRule alarmRule = new AlarmRule();
        alarmRule.setPipelineId(pipelineId);
        alarmRule.setDescription("one key added!");
        alarmRule.setAutoRecovery(Boolean.FALSE);
        alarmRule.setReceiverKey(systemParameter.getDefaultAlarmReceiveKey());
        alarmRule.setStatus(AlarmRuleStatus.DISABLE);
        alarmRule.setRecoveryThresold(3);
        alarmRule.setIntervalTime(1800L);

        try {
            alarmRule.setMonitorName(MonitorName.EXCEPTION);
            alarmRule.setMatchValue("ERROR,EXCEPTION");
            alarmRule.setIntervalTime(1800L);
            alarmRule.setAutoRecovery(false);
            alarmRule.setRecoveryThresold(2);
            create(alarmRule);
            alarmRule.setMonitorName(MonitorName.POSITIONTIMEOUT);
            alarmRule.setMatchValue("600");
            alarmRule.setIntervalTime(1800L);
            alarmRule.setAutoRecovery(true);
            alarmRule.setRecoveryThresold(0);
            create(alarmRule);
            alarmRule.setMonitorName(MonitorName.DELAYTIME);
            alarmRule.setMatchValue("600");
            alarmRule.setIntervalTime(1800L);
            alarmRule.setAutoRecovery(false);
            alarmRule.setRecoveryThresold(2);
            create(alarmRule);
            alarmRule.setMonitorName(MonitorName.PROCESSTIMEOUT);
            alarmRule.setMatchValue("60");
            alarmRule.setIntervalTime(1800L);
            alarmRule.setAutoRecovery(true);
            alarmRule.setRecoveryThresold(2);
            create(alarmRule);
            // alarmRule.setMonitorName(MonitorName.PIPELINETIMEOUT);
            // alarmRule.setMatchValue("43200");
            // alarmRuleService.create(alarmRule);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }


    @Override
    public void create(AlarmRule alarmRule) {
        Assert.assertNotNull(alarmRule);
        alarmRuleDOMapperExt.insertSelective(modelToDo(alarmRule));
    }
    private AlarmRuleDO modelToDo(AlarmRule alarmRule) {
        AlarmRuleDO alarmRuleDo = new AlarmRuleDO();
        alarmRuleDo.setId(alarmRule.getId());
        alarmRuleDo.setMatchValue(alarmRule.getMatchValue());
        alarmRuleDo.setMonitorName(alarmRule.getMonitorName());
        alarmRuleDo.setReceiverKey(alarmRule.getReceiverKey());
        alarmRuleDo.setPipelineId(alarmRule.getPipelineId());
        alarmRuleDo.setStatus(alarmRule.getStatus());
        alarmRuleDo.setDescription(alarmRule.getDescription());
        alarmRuleDo.setGmtCreate(alarmRule.getGmtCreate());
        alarmRuleDo.setGmtModified(alarmRule.getGmtModified());
      AlarmRuleParameter alarmRuleParameter = new AlarmRuleParameter();
        alarmRuleParameter.setIntervalTime(alarmRule.getIntervalTime());
        if (alarmRule.getPauseTime() != null) {
            SimpleDateFormat format = new SimpleDateFormat(TIMESTAMP_FORMAT);
            alarmRuleParameter.setPauseTime(format.format(alarmRule.getPauseTime()));
        }
        alarmRuleParameter.setAutoRecovery(alarmRule.getAutoRecovery());
        alarmRuleParameter.setRecoveryThresold(alarmRule.getRecoveryThresold());
        alarmRuleDo.setAlarmRuleParameter(alarmRuleParameter);

        return alarmRuleDo;
    }

    @Override
    public void modify(AlarmRule alarmRule) {
        Assert.assertNotNull(alarmRule);
        alarmRuleDOMapperExt.updateByPrimaryKeySelective(modelToDo(alarmRule));
    }

    @Override
    public void remove(Long alarmRuleId) {
        Assert.assertNotNull(alarmRuleId);
         alarmRuleDOMapperExt.deleteByPrimaryKey(alarmRuleId);
    }

    @Override
    public void enableMonitor(Long alarmRuleId) {
        switchAlarmRuleStatus(alarmRuleId, AlarmRuleStatus.ENABLE, null);
    }

    @Override
    public void disableMonitor(Long alarmRuleId) {
        switchAlarmRuleStatus(alarmRuleId, AlarmRuleStatus.DISABLE, null);
    }

    @Override
    public void disableMonitor(Long alarmRuleId, String pauseTime) {
        switchAlarmRuleStatus(alarmRuleId, AlarmRuleStatus.ENABLE, pauseTime);
    }
    private void switchAlarmRuleStatus(Long alarmRuleId, AlarmRuleStatus alarmRuleStatus, String pauseTime) {
       AlarmRuleDO alarmRuleDo = alarmRuleDOMapperExt.selectByPrimaryKey(alarmRuleId);

        if (null == alarmRuleDo) {
            String exceptionCause = "query alarmRule:" + alarmRuleId + " return null.";
            log.error("ERROR ## " + exceptionCause);
            throw new ManagerException(exceptionCause);
        }

        alarmRuleDo.setStatus(alarmRuleStatus);
        if (alarmRuleDo.getAlarmRuleParameter() != null) {
            alarmRuleDo.getAlarmRuleParameter().setPauseTime(pauseTime);
        } else if (StringUtils.isNotEmpty(pauseTime)) {
            alarmRuleDo.setAlarmRuleParameter(new AlarmRuleParameter());
            alarmRuleDo.getAlarmRuleParameter().setPauseTime(pauseTime);
        }
        alarmRuleDOMapperExt.updateByPrimaryKeySelective(alarmRuleDo);
    }

    @Override
    public List<AlarmRule> getAllAlarmRules(AlarmRuleStatus status) {
        Assert.assertNotNull(status);
        List<AlarmRuleDO> alarmRuleDos = sqlSession.selectList("listAlarmByStatus",status);
                //alarmRuleDao.listByStatus(status);
        return doToModel(alarmRuleDos);
    }

    @Override
    public AlarmRule getAlarmRuleById(Long AlarmRuleId) {
        Assert.assertNotNull(AlarmRuleId);
        return doToModel(alarmRuleDOMapperExt.selectByPrimaryKey(AlarmRuleId));
    }

    @Override
    public Map<Long, List<AlarmRule>> getAlarmRules(AlarmRuleStatus status) {
        Assert.assertNotNull(status);
        List<AlarmRule> alarmRules = getAllAlarmRules(status);
        Map<Long, List<AlarmRule>> result = new HashMap<Long, List<AlarmRule>>();
        for (AlarmRule alarmRule : alarmRules) {
            List<AlarmRule> rules = result.get(alarmRule.getPipelineId());
            if (rules == null) {
                rules = new ArrayList<AlarmRule>();
            }
            if (!rules.contains(alarmRule)) {
                rules.add(alarmRule);
            }
            result.put(alarmRule.getPipelineId(), rules);
        }
        return result;
    }

    @Override
    public List<AlarmRule> getAlarmRules(Long pipelineId) {
        Assert.assertNotNull(pipelineId);
        List<AlarmRuleDO> alarmRuleDos = sqlSession.selectList("listAlarmByPipelineId",pipelineId);
                //alarmRuleDao.listByPipelineId(pipelineId);
        return doToModel(alarmRuleDos);
    }

    @Override
    public List<AlarmRule> getAlarmRules(Long pipelineId, AlarmRuleStatus status) {
        Assert.assertNotNull(pipelineId);
        Assert.assertNotNull(status);
        List<AlarmRuleDO> alarmRuleDos = sqlSession.selectList("listAlarmByPipelineId",pipelineId);
                //alarmRuleDao.listByPipelineId(pipelineId, status);
        List<AlarmRuleDO> result = new ArrayList<AlarmRuleDO>();
        for (AlarmRuleDO alarmRuleDo : alarmRuleDos) {
            if (alarmRuleDo.getStatus().equals(status)) {
                result.add(alarmRuleDo);
            }
        }
        return doToModel(result);
    }

    @Override
    public List<AlarmRule> listAllAlarmRules(Map condition) {
        PageHelper.startPage((int)condition.get("current"),(int)condition.get("pageSize"));
       List<AlarmRuleDO> alarmRuleDOS =  sqlSession.selectList("listAllAlarmOrderByPipeline",condition);
               //alarmRuleDao.listAllByPipeline(condition)
        List<AlarmRule> alarmRules = doToModel(alarmRuleDOS);
        Page<AlarmRule> page = new Page<AlarmRule>();
        Page<AlarmRule> pageTemp = (Page)alarmRuleDOS;
        page.addAll(alarmRules);
        page.setPageSize(pageTemp.getPageSize());
        page.setPageNum(pageTemp.getPageNum());
        page.setTotal(pageTemp.getTotal());
        return page;
    }
    private List<AlarmRule> doToModel(List<AlarmRuleDO> alarmRuleDos) {
        List<AlarmRule> alarmRules = new ArrayList<AlarmRule>();
        for (AlarmRuleDO alarmRuleDo : alarmRuleDos) {
            alarmRules.add(doToModel(alarmRuleDo));
        }
        return alarmRules;
    }

    private AlarmRule doToModel(AlarmRuleDO alarmRuleDo) {
        AlarmRule alarmRule = new AlarmRule();
        alarmRule.setId(alarmRuleDo.getId());
        alarmRule.setMatchValue(alarmRuleDo.getMatchValue());
        alarmRule.setMonitorName(alarmRuleDo.getMonitorName());
        alarmRule.setReceiverKey(alarmRuleDo.getReceiverKey());
        // 如果数据库里面的数据为空，则返回默认值
        alarmRule.setIntervalTime(alarmRuleDo.getAlarmRuleParameter() == null ? 1800L : alarmRuleDo.getAlarmRuleParameter().getIntervalTime());
        String pauseTime = alarmRuleDo.getAlarmRuleParameter() == null ? null : alarmRuleDo.getAlarmRuleParameter().getPauseTime();
        if (StringUtils.isNotEmpty(pauseTime)) {
            SimpleDateFormat format = new SimpleDateFormat(TIMESTAMP_FORMAT);
            try {
                alarmRule.setPauseTime(format.parse(pauseTime));
            } catch (ParseException e) {
                throw new ManagerException(e);
            }
        }

        alarmRule.setAutoRecovery(alarmRuleDo.getAlarmRuleParameter() == null ? false : alarmRuleDo.getAlarmRuleParameter().getAutoRecovery());
        alarmRule.setRecoveryThresold(alarmRuleDo.getAlarmRuleParameter() == null ? 3 : alarmRuleDo.getAlarmRuleParameter().getRecoveryThresold());
        alarmRule.setPipelineId(alarmRuleDo.getPipelineId());
        alarmRule.setStatus(alarmRuleDo.getStatus());
        alarmRule.setDescription(alarmRuleDo.getDescription());
        alarmRule.setGmtCreate(alarmRuleDo.getGmtCreate());
        alarmRule.setGmtModified(alarmRuleDo.getGmtModified());
        return alarmRule;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
