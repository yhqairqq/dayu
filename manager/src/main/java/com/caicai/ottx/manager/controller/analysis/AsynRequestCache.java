package com.caicai.ottx.manager.controller.analysis;

import com.alibaba.otter.shared.arbitrate.ArbitrateManageService;
import com.alibaba.otter.shared.arbitrate.ArbitrateViewService;
import com.alibaba.otter.shared.arbitrate.model.MainStemEventData;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.common.model.statistics.throughput.ThroughputType;
import com.caicai.ottx.manager.controller.analysis.model.AnalysisResult;
import com.caicai.ottx.manager.controller.analysis.model.PlotCell;
import com.caicai.ottx.service.config.channel.ChannelService;
import com.caicai.ottx.service.config.datamediapair.DataMediaPairService;
import com.caicai.ottx.service.config.pipeline.PipelineService;
import com.caicai.ottx.service.statistics.delay.DelayStatService;
import com.caicai.ottx.service.statistics.table.TableStatService;
import com.caicai.ottx.service.statistics.table.param.ThroughputInfo;
import com.caicai.ottx.service.statistics.table.param.TimelineThroughputCondition;
import com.caicai.ottx.service.statistics.throughput.ThroughputStatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by huaseng on 2019/10/10.
 */
@Slf4j
@Component
public class AsynRequestCache {
    private List data = new ArrayList();
    private long period = 10000;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private ThroughputStatService throughputStatService;
    @Autowired
    private DelayStatService delayStatService;
    @Autowired
    private ArbitrateManageService arbitrateManageService;
    @Autowired
    private ArbitrateViewService arbitrateViewService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private DataMediaPairService dataMediaPairService;
    @Autowired
    private TableStatService tableStatService;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public List getLastData() {
        return this.data;
    }

    @PostConstruct
    public void start() {
        executorService.scheduleAtFixedRate(new Runnable() {
            public void run() {
                data.clear();
                for (Pipeline pipeline : getActivePipline()) {
                    data.add(fetchAnalysisThroughputHistory(pipeline));
                }
            }
        }, period, period, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }

    private AnalysisResult fetchAnalysisThroughputHistory(Pipeline pipeline) {
        try {
            Date end = null;
            Date start = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_WEEK, -1);
            String startTime = sdf.format(calendar.getTime());
            String endTime = sdf.format(currentDate);
            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
                start = new Date(System.currentTimeMillis() / 60000 * 60000 - 24 * 60 * 60 * 1000);
                end = new Date(System.currentTimeMillis() / 60000 * 60000);
            } else {// 当前24小时，时间取整分钟
                sdf.setLenient(false);
                if (null != startTime && null != endTime) {
                    start = sdf.parse(startTime);
                    end = sdf.parse(endTime);
                }
            }
            Map<Long, ThroughputInfo> throughputInfos1 = new LinkedHashMap<Long, ThroughputInfo>();
            Map<Long, ThroughputInfo> throughputInfos2 = new LinkedHashMap<Long, ThroughputInfo>();
            TimelineThroughputCondition condition1 = new TimelineThroughputCondition();
            TimelineThroughputCondition condition2 = new TimelineThroughputCondition();
            if (null != start && null != end) {
                condition1.setStart(start);
                condition1.setEnd(end);
                condition1.setType(ThroughputType.ROW);
                condition1.setPipelineId(pipeline.getId());
//                condition2.setStart(start);
//                condition2.setEnd(end);
//                condition2.setType(ThroughputType.FILE);
//                condition2.setPipelineId(pipelineId);
                throughputInfos1 = throughputStatService.listTimelineThroughput(condition1);
//                throughputInfos2 = throughputStatService.listTimelineThroughput(condition2);
            }
            Long totalRecord1 = 0L;
            Long totalRecord2 = 0L;
            Long totalSize1 = 0L;
            Long totalSize2 = 0L;
            for (ThroughputInfo info : throughputInfos1.values()) {
                totalRecord1 += info.getNumber();
                totalSize1 += info.getSize();
            }
            AnalysisResult analysisResult = new AnalysisResult();
            List<PlotCell> plotCells = new ArrayList<>();
            for (long key : throughputInfos1.keySet()) {
                PlotCell plotCell = new PlotCell();
                ThroughputInfo throughputInfo = throughputInfos1.get(key);
                plotCell.setNum(throughputInfo.getNumber());
                plotCell.setQuantity(throughputInfo.getQuantity());
                plotCell.setSize(throughputInfo.getSize());
                plotCell.setTime(key);
                plotCells.add(plotCell);
            }
            analysisResult.setPlotCells(plotCells);
            Map<String, Object> otherResult = new HashMap<>();
            otherResult.put("totalRecord1", totalRecord1);
            otherResult.put("totalSize1", totalSize1);
            otherResult.put("start", sdf.format(start));
            otherResult.put("end", sdf.format(end));
            otherResult.put("pipeline",pipeline);
            analysisResult.setOtherResult(otherResult);
            return analysisResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private List<Pipeline> getActivePipline() {
        List<Pipeline> pipelines = pipelineService.listAll();
        return pipelines.stream().filter(item -> {
            MainStemEventData mainStemEventData = arbitrateViewService.mainstemData(item.getChannelId(), item.getId());
            if (mainStemEventData != null && mainStemEventData.isActive()) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());

    }

}
