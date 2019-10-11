package com.caicai.ottx.service.statistics.table;

import com.alibaba.otter.shared.common.model.statistics.table.TableStat;
import com.caicai.ottx.service.statistics.table.param.BehaviorHistoryInfo;
import com.caicai.ottx.service.statistics.table.param.TimelineBehaviorHistoryCondition;

import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface TableStatService {
    /**
     * 增量更新对应的Table统计状态
     */
     void updateTableStat(TableStat stat);

    /**
     * 查询对应同步任务下的统计信息
     */
     List<TableStat> listTableStat(Long pipelineId);

    /**
     * 更新对应的history信息，做报表统计用
     */
     void insertBehaviorHistory(TableStat stat);

    /**
     * 查询对应的报表数据
     */
     Map<Long, BehaviorHistoryInfo> listTimelineBehaviorHistory(TimelineBehaviorHistoryCondition condition);
}
