package com.caicai.ottx.service.statistics.throughput.impl;

import com.alibaba.otter.shared.common.model.statistics.throughput.ThroughputStat;
import com.alibaba.otter.shared.common.utils.Assert;
import com.caicai.ottx.dal.entity.ThroughputStatDO;
import com.caicai.ottx.dal.mapper.ThroughputStatDOMapperExt;
import com.caicai.ottx.service.statistics.table.param.*;
import com.caicai.ottx.service.statistics.throughput.ThroughputStatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by huaseng on 2019/9/2.
 */
@Service
@Slf4j
public class ThroughputStatServiceImpl implements ThroughputStatService {

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private ThroughputStatDOMapperExt throughputStatDOMapperExt;


    @Override
    public Map<AnalysisType, ThroughputInfo> listRealtimeThroughput(RealtimeThroughputCondition condition) {
        Assert.assertNotNull(condition);
        Map<AnalysisType, ThroughputInfo> throughputInfos = new HashMap<AnalysisType, ThroughputInfo>();
    TimelineThroughputCondition timelineCondition = new TimelineThroughputCondition();
        Date realtime = new Date(System.currentTimeMillis());
        timelineCondition.setPipelineId(condition.getPipelineId());
        timelineCondition.setType(condition.getType());
        timelineCondition.setStart(new Date(realtime.getTime() - condition.getMax() * 60 * 1000));
        timelineCondition.setEnd(realtime);
        List< ThroughputStatDO> throughputStatDOs = sqlSession.selectList("listTimelineThroughputStat", condition);
                //throughputDao.listTimelineThroughputStat(timelineCondition);
        for (AnalysisType analysisType : condition.getAnalysisType()) {
           ThroughputInfo throughputInfo = new ThroughputInfo();
            List<ThroughputStat> throughputStat = new ArrayList<ThroughputStat>();
            for ( ThroughputStatDO throughputStatDO : throughputStatDOs) {
                if (realtime.getTime() - throughputStatDO.getEndTime().getTime() <= analysisType.getValue() * 60 * 1000) {
                    throughputStat.add(throughputStatDOToModel(throughputStatDO));
                }
            }
            throughputInfo.setItems(throughputStat);
            throughputInfo.setSeconds(analysisType.getValue() * 60L);
            throughputInfos.put(analysisType, throughputInfo);
        }
        return throughputInfos;
    }

    @Override
    public Map<Long, ThroughputInfo> listTimelineThroughput(TimelineThroughputCondition condition) {
        Assert.assertNotNull(condition);
        Map<Long, ThroughputInfo> throughputInfos = new LinkedHashMap<Long, ThroughputInfo>();
        List<ThroughputStatDO> throughputStatDOs = sqlSession.selectList("listTimelineThroughputStat",condition);
//                throughputDao.listTimelineThroughputStat(condition);
        int size = throughputStatDOs.size();
        int k = size - 1;
        for (Long i = condition.getStart().getTime(); i <= condition.getEnd().getTime(); i += 60 * 1000) {
          ThroughputInfo throughputInfo = new ThroughputInfo();
            List<ThroughputStat> throughputStat = new ArrayList<ThroughputStat>();
            // 取出每个时间点i以内的数据，k是一个游标，每次遍历时前面已经取过了的数据就不用再遍历了
            for (int j = k; j >= 0; --j) {
                if ((i - throughputStatDOs.get(j).getEndTime().getTime() <= 60 * 1000)
                        && (i - throughputStatDOs.get(j).getEndTime().getTime() >= 0)) {
                    throughputStat.add(throughputStatDOToModel(throughputStatDOs.get(j)));
                    k = j - 1;
                }// 如果不满足if条件，则后面的数据也不用再遍历
                else {
                    break;
                }
            }
            if (throughputStat.size() > 0) {
                throughputInfo.setItems(throughputStat);
                throughputInfo.setSeconds(1 * 60L);
                throughputInfos.put(i, throughputInfo);
            }

        }
        return throughputInfos;
    }

    @Override
    public List<ThroughputStat> listRealtimeThroughputByPipelineIds(List<Long> pipelineIds, int minute) {
        Assert.assertNotNull(pipelineIds);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("pipelineIds", pipelineIds);
        param.put("minute", minute);
        List<ThroughputStatDO> throughputStatDOs = sqlSession.selectList("listRealtimeThroughputStatByPipelineIds",param
        );

//                throughputDao.listRealTimeThroughputStatByPipelineIds(pipelineIds,
//                minute);

        List<ThroughputStat> infos = new ArrayList<ThroughputStat>();
        for (ThroughputStatDO throughputStatDO : throughputStatDOs) {
            infos.add(throughputStatDOToModel(throughputStatDO));
        }

        return infos;
    }

    private ThroughputStat throughputStatDOToModel(ThroughputStatDO throughputStatDO) {
        ThroughputStat throughputStat = new ThroughputStat();
        throughputStat.setId(throughputStatDO.getId());
        throughputStat.setPipelineId(throughputStatDO.getPipelineId());
        throughputStat.setStartTime(throughputStatDO.getStartTime());
        throughputStat.setEndTime(throughputStatDO.getEndTime());
        throughputStat.setType(throughputStatDO.getType());
        throughputStat.setNumber(throughputStatDO.getNumber());
        throughputStat.setSize(throughputStatDO.getSize());
        throughputStat.setGmtCreate(throughputStatDO.getGmtCreate());
        throughputStat.setGmtModified(throughputStatDO.getGmtModified());
        return throughputStat;
    }

    @Override
    public ThroughputStat findThroughputStatByPipelineId(ThroughputCondition condition) {
        Assert.assertNotNull(condition);
      ThroughputStatDO throughputStatDO = sqlSession.selectOne("findRealtimeThroughputStat",condition);
              //throughputDao.findRealtimeThroughputStat(condition);
        ThroughputStat throughputStat = new ThroughputStat();
        if (throughputStatDO != null) {
            throughputStat = throughputStatDOToModel(throughputStatDO);
        }
        return throughputStat;
    }

    @Override
    public void createOrUpdateThroughput(ThroughputStat item) {
        Assert.assertNotNull(item);
        throughputStatDOMapperExt.insertSelective(throughputStatModelToDo(item));
//        throughputDao.insertThroughputStat(throughputStatModelToDo(item));
    }

    /**
     * 用于Model对象转化为DO对象
     *
     * @param throughputStat
     * @return throughputStatDO
     */
    private   ThroughputStatDO throughputStatModelToDo(ThroughputStat throughputStat) {
          ThroughputStatDO throughputStatDO = new   ThroughputStatDO();
        throughputStatDO.setId(throughputStat.getId());
        throughputStatDO.setPipelineId(throughputStat.getPipelineId());
        throughputStatDO.setStartTime(throughputStat.getStartTime());
        throughputStatDO.setEndTime(throughputStat.getEndTime());
        throughputStatDO.setType(throughputStat.getType());
        throughputStatDO.setNumber(throughputStat.getNumber());
        throughputStatDO.setSize(throughputStat.getSize());
        throughputStatDO.setGmtCreate(throughputStat.getGmtCreate());
        throughputStatDO.setGmtModified(throughputStat.getGmtModified());
        return throughputStatDO;
    }
}
