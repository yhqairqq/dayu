package com.caicai.ottx.manager.controller.analysis.model;

import com.alibaba.otter.shared.common.model.statistics.throughput.ThroughputStat;
import com.caicai.ottx.service.statistics.table.param.AnalysisType;
import com.caicai.ottx.service.statistics.table.param.ThroughputInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/9/26.
 */
@Data
public class AnalysisResult<T> {

    Map<Long, ThroughputInfo> throughputInfoMap;
    Map<String,Object>  otherResult  = new HashMap<>();
    Map<AnalysisType, ThroughputInfo> throughputInfos2;
    ThroughputStat throughputStat2;
    List<T> plotCells = new ArrayList<>();

}
