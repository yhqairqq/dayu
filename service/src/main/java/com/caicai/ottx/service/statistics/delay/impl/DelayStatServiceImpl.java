package com.caicai.ottx.service.statistics.delay.impl;

import com.alibaba.otter.manager.biz.statistics.delay.param.TimelineDelayCondition;
import com.alibaba.otter.shared.common.model.statistics.delay.DelayStat;
import com.alibaba.otter.shared.common.utils.Assert;
import com.caicai.ottx.dal.entity.DelayStatDO;
import com.caicai.ottx.dal.mapper.DelayStatDOMapperExt;
import com.caicai.ottx.service.statistics.delay.DelayStatService;
import com.caicai.ottx.service.statistics.delay.param.DelayStatInfo;
import com.caicai.ottx.service.statistics.delay.param.TopDelayStat;
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
public class DelayStatServiceImpl implements DelayStatService {

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private DelayStatDOMapperExt delayStatDOMapperExt;

    @Override
    public void createDelayStat(DelayStat stat) {
        Assert.assertNotNull(stat);
        delayStatDOMapperExt.insertSelective(delayStatModelToDo(stat));
    }

    /**
     * 用于Model对象转化为DO对象
     *
     * @param delayStat
     * @return DelayStatDO
     */
    private DelayStatDO delayStatModelToDo(DelayStat delayStat) {
        DelayStatDO delayStatDO = new DelayStatDO();
        delayStatDO.setId(delayStat.getId());
        delayStatDO.setDelayTime(delayStat.getDelayTime());
        delayStatDO.setDelayNumber(delayStat.getDelayNumber());
        delayStatDO.setPipelineId(delayStat.getPipelineId());
        delayStatDO.setGmtCreate(delayStat.getGmtCreate());
        delayStatDO.setGmtModified(delayStat.getGmtModified());
        return delayStatDO;

    }

    @Override
    public DelayStat findRealtimeDelayStat(Long pipelineId) {
        Assert.assertNotNull(pipelineId);
        DelayStatDO delayStatDO = sqlSession.selectOne("findRealtimeDelayStat",pipelineId);
                //delayStatDao.findRealtimeDelayStat(pipelineId);
        DelayStat delayStat = new DelayStat();
        if (delayStatDO != null) {
            delayStat = delayStatDOToModel(delayStatDO);
        }
        return delayStat;
    }
    private DelayStat delayStatDOToModel(DelayStatDO delayStatDO) {
        DelayStat delayStat = new DelayStat();
        delayStat.setId(delayStatDO.getId());
        delayStat.setDelayTime(delayStatDO.getDelayTime());
        delayStat.setDelayNumber(delayStatDO.getDelayNumber());
        delayStat.setPipelineId(delayStatDO.getPipelineId());
        delayStat.setGmtCreate(delayStatDO.getGmtCreate());
        delayStat.setGmtModified(delayStatDO.getGmtModified());
        return delayStat;

    }

    @Override
    public Map<Long, DelayStatInfo> listTimelineDelayStat(Long pipelineId, Date start, Date end) {

        Map<Long, DelayStatInfo> delayStatInfos = new LinkedHashMap<Long,DelayStatInfo>();
        TimelineDelayCondition tdc = new TimelineDelayCondition();
        tdc.setPipelineId(pipelineId);
        tdc.setStart(start);
        tdc.setEnd(end);
        Assert.assertNotNull(tdc);
        List<DelayStatDO> delayStatDOs = sqlSession.selectList("listTimelineDelayStatsByPipelineId",tdc);
//                delayStatDao.listTimelineDelayStatsByPipelineId(pipelineId, start, end);
        int size = delayStatDOs.size();
        int k = size - 1;
        for (Long i = start.getTime(); i <= end.getTime(); i += 60 * 1000) {
          DelayStatInfo delayStatInfo = new DelayStatInfo();
            List<DelayStat> delayStats = new ArrayList<DelayStat>();
            // 取出每个时间点i以内的数据，k是一个游标，每次遍历时前面已经取过了的数据就不用再遍历了
            for (int j = k; j >= 0; --j) {
                if ((i - delayStatDOs.get(j).getGmtModified().getTime() <= 60 * 1000)
                        && (i - delayStatDOs.get(j).getGmtModified().getTime() >= 0)) {
                    delayStats.add(delayStatDOToModel(delayStatDOs.get(j)));
                    k = j - 1;
                }// 如果不满足if条件，则后面的数据也不用再遍历
                else {
                    break;
                }
            }
            if (delayStats.size() > 0) {
                delayStatInfo.setItems(delayStats);
                delayStatInfos.put(i, delayStatInfo);
            }

        }
        return delayStatInfos;
    }

    @Override
    public List<TopDelayStat> listTopDelayStat(String searchKey, int topN) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("searchKey", searchKey);
        param.put("topN", topN);
        List<TopDelayStat> topDelayStats =   sqlSession.selectList("listTopByName",param);
        return topDelayStats;
    }
}
