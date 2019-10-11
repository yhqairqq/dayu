package com.caicai.ottx.service.config.record.impl;

import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.common.model.config.record.LogRecord;
import com.alibaba.otter.shared.common.utils.Assert;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import com.alibaba.otter.shared.communication.core.model.Event;
import com.alibaba.otter.shared.communication.model.arbitrate.NodeAlarmEvent;
import com.caicai.ottx.dal.entity.LogRecordDO;
import com.caicai.ottx.dal.mapper.LogRecordDOMapperExt;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.config.channel.ChannelService;
import com.caicai.ottx.service.config.record.LogRecordService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/9/2.
 */
@Service
@Slf4j
public class LogRecordServiceImpl implements LogRecordService {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private LogRecordDOMapperExt logRecordDOMapperExt;

    @Override
    public void create(LogRecord entityObj) {
        Assert.assertNotNull(entityObj);
        logRecordDOMapperExt.insertSelective(modelToDo(entityObj));
    }
    /**
     * <pre>
     * 用于Model对象转化为DO对象
     * 优化：
     *      无SQL交互，只是简单进行字段组装，暂时无须优化
     * </pre>
     *
     * @param channel
     * @return ChannelDO
     */
    private LogRecordDO modelToDo(LogRecord entityObj) {

        LogRecordDO logRecordDo = new LogRecordDO();
        try {
            if (entityObj.getPipeline() != null && entityObj.getPipeline().getId() > 0) {
                Channel channel = channelService.findByPipelineId(entityObj.getPipeline().getId());
                logRecordDo.setChannelId(channel.getId());
                logRecordDo.setPipelineId(entityObj.getPipeline().getId());
            } else {
                logRecordDo.setChannelId(-1l);
                logRecordDo.setPipelineId(-1l);
            }
            logRecordDo.setNid(entityObj.getNid());
            logRecordDo.setTitle(entityObj.getTitle());
            String message = entityObj.getMessage();
            if (message != null && message.length() > 65535 / 3) {
                message = message.substring(0, 65535 / 3);
            }
            logRecordDo.setMessage(message);
            logRecordDo.setGmtCreate(entityObj.getGmtCreate());
            logRecordDo.setGmtModified(entityObj.getGmtModified());

        } catch (Exception e) {
            log.error("ERROR ## has an error where write log to db");
            throw new ManagerException(e);
        }
        return logRecordDo;
    }

    @Override
    public void remove(Long identity) {
        logRecordDOMapperExt.deleteByPrimaryKey(identity);
    }

    @Override
    public void modify(LogRecord entityObj) {
        logRecordDOMapperExt.updateByPrimaryKeySelective(modelToDo((entityObj)));
    }

    @Override
    public LogRecord findById(Long identity) {
        return doToModel(logRecordDOMapperExt.selectByPrimaryKey(identity));
    }

    @Override
    public List<LogRecord> listByIds(Long... identities) {
        return null;
    }

    @Override
    public List<LogRecord> listAll() {
        return null;
    }

    @Override
    public List<LogRecord> listByCondition(Map condition) {
        PageHelper.startPage((int)condition.get("current"),(int)condition.get("pageSize"));
        List<LogRecordDO> logRecordDos = sqlSession.selectList("listLogRecordsWithCondition",condition);
                //logRecordDao.listByCondition(condition);
        if (CollectionUtils.isEmpty(logRecordDos)) {
            log.debug("DEBUG ## couldn't query any log record by the condition:"
                    + JsonUtils.marshalToString(condition));
            return new Page<LogRecord>();
        }
        List<LogRecord> logRecordList =  doToModel(logRecordDos);
        Page<LogRecord> page = new Page<LogRecord>();
        Page<LogRecord> pageTemp = (Page)logRecordDos;
        page.addAll(logRecordList);
        page.setPageSize(pageTemp.getPageSize());
        page.setPageNum(pageTemp.getPageNum());
        page.setTotal(pageTemp.getTotal());
        return page;
    }
    private List<LogRecord> doToModel(List<LogRecordDO> logRecordDos) {
        List<LogRecord> logRecords = new ArrayList<LogRecord>();
        try {
            for (LogRecordDO logRecordDo : logRecordDos) {
                logRecords.add(doToModel(logRecordDo));
            }

        } catch (Exception e) {
            log.error("ERROR ##");
            throw new ManagerException(e);
        }

        return logRecords;
    }
    private LogRecord doToModel(LogRecordDO logRecordDo) {
        LogRecord logRecord = new LogRecord();
        try {

            logRecord.setId(logRecordDo.getId());
            if (logRecordDo.getPipelineId() > 0 && logRecordDo.getChannelId() > 0) {
                try {
                    Channel channel = channelService.findByPipelineId(logRecordDo.getPipelineId());
                    logRecord.setChannel(channel);
                    for (Pipeline pipeline : channel.getPipelines()) {
                        if (pipeline.getId().equals(logRecordDo.getPipelineId())) {
                            logRecord.setPipeline(pipeline);
                        }
                    }
                } catch (Exception e) {
                    // 可能历史的log记录对应的channel/pipeline已经被删除了，直接忽略吧
                    Channel channel = new Channel();
                    channel.setId(0l);
                    logRecord.setChannel(channel);
                    Pipeline pipeline = new Pipeline();
                    pipeline.setId(0l);
                    logRecord.setPipeline(pipeline);
                }
            } else {
                Channel channel = new Channel();
                channel.setId(-1l);
                logRecord.setChannel(channel);
                Pipeline pipeline = new Pipeline();
                pipeline.setId(-1l);
                logRecord.setPipeline(pipeline);
            }

            logRecord.setTitle(logRecordDo.getTitle());
            logRecord.setNid(logRecordDo.getNid());
            logRecord.setMessage(logRecordDo.getMessage());
            logRecord.setGmtCreate(logRecordDo.getGmtCreate());
            logRecord.setGmtModified(logRecordDo.getGmtModified());

        } catch (Exception e) {
            log.error("ERROR ## ");
            throw new ManagerException(e);
        }

        return logRecord;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public int getCount(Map condition) {
        return 0;
    }

    @Override
    public void create(Event event) {
        LogRecord logRecord = new LogRecord();
        if(event instanceof NodeAlarmEvent) {
            NodeAlarmEvent nodeAlarmEvent = (NodeAlarmEvent)event;
            Pipeline tempPipeline = new Pipeline();
            tempPipeline.setId(nodeAlarmEvent.getPipelineId());
            logRecord.setPipeline(tempPipeline);
            logRecord.setNid(nodeAlarmEvent.getNid());
            logRecord.setTitle(nodeAlarmEvent.getTitle());
            logRecord.setMessage(nodeAlarmEvent.getMessage());
        }

        this.create(logRecord);
    }

    @Override
    public List<LogRecord> listByPipelineId(Long pipelineId) {
        Assert.assertNotNull(pipelineId);
        List<LogRecordDO> logRecordDos = sqlSession.selectList("listLogRecordsByPipelineId",pipelineId);
                //logRecordDao.listByPipelineId(pipelineId);
        return doToModel(logRecordDos);
    }

    @Override
    public LogRecord listByPipelineIdTop(Long pipelineId) {
        Assert.assertNotNull(pipelineId);
        LogRecordDO logRecordDO = sqlSession.selectOne("logRecordByPipelineIdTop",pipelineId);
        if(logRecordDO == null){
            return null;
        }
        //logRecordDao.listByPipelineId(pipelineId);
        return doToModel(logRecordDO);
    }

    @Override
    public List<LogRecord> listByPipelineIdWithoutContent(Long pipelineId) {
        Assert.assertNotNull(pipelineId);
        List<LogRecordDO> logRecordDos = sqlSession.selectList("listLogRecordsByPipelineIdWithoutContent",pipelineId);
                //logRecordDao.listByPipelineIdWithoutContent(pipelineId);
        return doToModel(logRecordDos);
    }
}
