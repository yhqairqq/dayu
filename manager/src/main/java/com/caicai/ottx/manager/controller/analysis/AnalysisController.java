package com.caicai.ottx.manager.controller.analysis;

import com.alibaba.otter.shared.arbitrate.ArbitrateManageService;
import com.alibaba.otter.shared.arbitrate.ArbitrateViewService;
import com.alibaba.otter.shared.arbitrate.model.MainStemEventData;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.channel.ChannelStatus;
import com.alibaba.otter.shared.common.model.config.data.DataMediaPair;
import com.alibaba.otter.shared.common.model.config.node.Node;
import com.alibaba.otter.shared.common.model.statistics.throughput.ThroughputStat;
import com.alibaba.otter.shared.common.model.statistics.throughput.ThroughputType;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.vo.PageResult;
import com.caicai.ottx.manager.controller.analysis.form.AnalysisForm;
import com.caicai.ottx.manager.controller.analysis.model.AnalysisResult;
import com.caicai.ottx.manager.controller.analysis.model.PlotCell;
import com.caicai.ottx.manager.controller.analysis.model.PlotCellBehavior;
import com.caicai.ottx.service.config.channel.ChannelService;
import com.caicai.ottx.service.config.datamediapair.DataMediaPairService;
import com.caicai.ottx.service.config.node.NodeService;
import com.caicai.ottx.service.remote.NodeRemoteService;
import com.caicai.ottx.service.statistics.delay.DelayStatService;
import com.caicai.ottx.service.statistics.delay.param.DelayStatInfo;
import com.caicai.ottx.service.statistics.delay.param.TopDelayStat;
import com.caicai.ottx.service.statistics.table.TableStatService;
import com.caicai.ottx.service.statistics.table.param.*;
import com.caicai.ottx.service.statistics.throughput.ThroughputStatService;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huaseng on 2019/9/2.
 */
@RequestMapping("/analysis")
@RestController
@Slf4j
public class AnalysisController {


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
    @Autowired
    private AsynRequestCache asynRequestCache;
    @Autowired
    private NodeService nodeService;
    @Autowired
    private NodeRemoteService nodeRemoteService;

    @RequestMapping(value = "/topstat/getByPage",method = RequestMethod.POST)
    public ApiResult<PageResult> getByPage(@RequestBody AnalysisForm analysisForm){
        int topN = analysisForm.getTopN();
        int minute = analysisForm.getMinute();
        List<TopDelayStat> tops = delayStatService.listTopDelayStat("", topN);
        List<Long> pipelineIds = new ArrayList<Long>();
        for (TopDelayStat top : tops) {
            top.setStatTime(Long.valueOf(minute));
            pipelineIds.add(top.getPipelineId());
        }
        Map<Long, ChannelStatus> channelStatuses = new HashMap<Long, ChannelStatus>(tops.size(), 1f);
        Map<Long, MainStemEventData> mainstemStatuses = new HashMap<Long, MainStemEventData>(tops.size(), 1f);
        if (pipelineIds.size() > 0) {
            List<ThroughputStat> stats = throughputStatService.listRealtimeThroughputByPipelineIds(pipelineIds, minute);
            for (ThroughputStat stat : stats) {
                for (TopDelayStat top : tops) {
                    if (stat.getPipelineId().equals(top.getPipelineId())) {
                        TopDelayStat.DataStat s = new TopDelayStat.DataStat(stat.getNumber(), stat.getSize());
                        if (ThroughputType.FILE == stat.getType()) {
                            top.setFileStat(s);
                        } else if (ThroughputType.ROW == stat.getType()) {
                            top.setDbStat(s);
                        }
                        break;
                    }
                }
            }

            for (TopDelayStat top : tops) {
                if (!channelStatuses.containsKey(top.getChannelId())) {
                    channelStatuses.put(top.getChannelId(),
                            arbitrateManageService.channelEvent().status(top.getChannelId()));
                }

                if (!mainstemStatuses.containsKey(top.getPipelineId())) {
                    mainstemStatuses.put(top.getPipelineId(),
                            arbitrateViewService.mainstemData(top.getChannelId(), top.getPipelineId()));
                }
            }
        }
        return ApiResult.success(new PageResult(tops,new Page(1,topN)));
    }

    /**
     * 分析 单位时间内操作数据的记录条数和流量大小 根据管道
     * @param analysisForm
     * @return
     */
    @RequestMapping(value = "/fetchAnalysisThroughputHistory",method = RequestMethod.POST)
    public ApiResult<AnalysisResult> fetchAnalysisThroughputHistory(@RequestBody AnalysisForm analysisForm){
        try{
            long pipelineId = analysisForm.getPipelineId();
            Date end = null;
            Date start = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date currentDate = new Date();
            Calendar calendar =  Calendar.getInstance();
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

            Channel channel = channelService.findByPipelineId(pipelineId);
            Map<Long, ThroughputInfo> throughputInfos1 = new LinkedHashMap<Long, ThroughputInfo>();
            Map<Long, ThroughputInfo> throughputInfos2 = new LinkedHashMap<Long, ThroughputInfo>();
            TimelineThroughputCondition condition1 = new TimelineThroughputCondition();
            TimelineThroughputCondition condition2 = new TimelineThroughputCondition();
            if (null != start && null != end) {
                condition1.setStart(start);
                condition1.setEnd(end);
                condition1.setType(ThroughputType.ROW);
                condition1.setPipelineId(pipelineId);
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
            for(long key:throughputInfos1.keySet()){
                PlotCell plotCell  =  new PlotCell();
                ThroughputInfo throughputInfo = throughputInfos1.get(key);
                plotCell.setNum(throughputInfo.getNumber());
                plotCell.setQuantity(throughputInfo.getQuantity());
                plotCell.setSize(throughputInfo.getSize());
                plotCell.setTime(key);
                plotCells.add(plotCell);
            }
            analysisResult.setPlotCells(plotCells);


            Map<String,Object> otherResult = new HashMap<>();
            otherResult.put("totalRecord1",totalRecord1);
            otherResult.put("avgRecordPerS",totalRecord1/24*3600);
            otherResult.put("totalSize1",totalSize1);
            otherResult.put("avgSizePerS",totalRecord1/24*3600);
            otherResult.put("start", sdf.format(start));
            otherResult.put("end", sdf.format(end));
            analysisResult.setOtherResult(otherResult);
//            for (ThroughputInfo info : throughputInfos2.values()) {
//                totalRecord2 += info.getNumber();
//                totalSize2 += info.getSize();
//            }
            return ApiResult.success(analysisResult);
        }catch (Exception e){
            return ApiResult.failed(e.getMessage());
        }

    }


    @RequestMapping(value = "/fetchAnalysisDelayStat",method = RequestMethod.POST)
    public ApiResult<AnalysisResult> fetchAnalysisDelayStat(@RequestBody AnalysisForm analysisForm){
        try{
            Date end = null;
            Date start = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date currentDate = new Date();
            Calendar calendar =  Calendar.getInstance();
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
            long pipelineId = analysisForm.getPipelineId();
            Channel channel = channelService.findByPipelineId(pipelineId);
            Map<Long, DelayStatInfo> delayStatInfos = new HashMap<Long, DelayStatInfo>();
            if (null != start && null != end) {
                delayStatInfos = delayStatService.listTimelineDelayStat(pipelineId, start, end);
            }
            Double delayAvg = 0.0;
            for (DelayStatInfo info : delayStatInfos.values()) {
                delayAvg += info.getAvgDelayTime();
            }
            if (delayStatInfos.size() != 0) {
                delayAvg = delayAvg / (1.0 * delayStatInfos.size());
            }
            AnalysisResult analysisResult = new AnalysisResult();
            List<PlotCell> plotCells = new ArrayList<>();
            for(long key:delayStatInfos.keySet()){
                DelayStatInfo delayStatInfo =  delayStatInfos.get(key);
                PlotCell plotCell = new PlotCell();
                plotCell.setAvgDelayNumber(delayStatInfo.getAvgDelayTime());
                plotCell.setAvgDelayTime(delayStatInfo.getAvgDelayTime());
                plotCell.setTime(key);
                plotCells.add(plotCell);
            }
            analysisResult.setPlotCells(plotCells);
            Map<String,Object> otherResult = new HashMap<>();
            otherResult.put("delayAvg", delayAvg);
            otherResult.put("channel", channel);
            otherResult.put("pipelineId", pipelineId);
            otherResult.put("start", sdf.format(start));
            otherResult.put("end", sdf.format(end));
            analysisResult.setOtherResult(otherResult);
            return ApiResult.success(analysisResult);
        }catch (Exception e){
            return ApiResult.failed(e.getMessage());
        }
    }

    @RequestMapping(value = "/fetchAnalysisThroughputStat",method = RequestMethod.POST)
    public ApiResult<AnalysisResult>  fetchAnalysisThroughputStat(@RequestBody AnalysisForm analysisForm){
       try{
           long pipelineId = analysisForm.getPipelineId();
           Channel channel = channelService.findByPipelineId(analysisForm.getPipelineId());
//        RealtimeThroughputCondition condition1 = new RealtimeThroughputCondition();
           RealtimeThroughputCondition condition2 = new RealtimeThroughputCondition();
//        ThroughputCondition condition11 = new ThroughputCondition();
           ThroughputCondition condition22 = new ThroughputCondition();
           List<AnalysisType> analysisType = new ArrayList<AnalysisType>();
           analysisType.add(AnalysisType.ONE_MINUTE);
           analysisType.add(AnalysisType.FIVE_MINUTE);
           analysisType.add(AnalysisType.FIFTEEN_MINUTE);
//        condition1.setPipelineId(pipelineId);
//        condition1.setAnalysisType(analysisType);
//        condition1.setType(ThroughputType.FILE);
           condition2.setPipelineId(pipelineId);
           condition2.setAnalysisType(analysisType);
           condition2.setType(ThroughputType.ROW);
//        condition11.setPipelineId(pipelineId);
//        condition11.setType(ThroughputType.FILE);
           condition22.setPipelineId(pipelineId);
           condition22.setType(ThroughputType.ROW);
//        Map<AnalysisType, ThroughputInfo> throughputInfos1 = throughputStatService.listRealtimeThroughput(condition1);
           Map<AnalysisType, ThroughputInfo> throughputInfos2 = throughputStatService.listRealtimeThroughput(condition2);
//        ThroughputStat throughputStat1 = throughputStatService.findThroughputStatByPipelineId(condition11);
           ThroughputStat throughputStat2 = throughputStatService.findThroughputStatByPipelineId(condition22);


           AnalysisResult analysisResult = new AnalysisResult();
           analysisResult.setThroughputInfos2(throughputInfos2);
           analysisResult.setThroughputStat2(throughputStat2);
           Map<String,Object> otherResult = new HashMap<>();
           otherResult.put("one", AnalysisType.ONE_MINUTE);
           otherResult.put("five", AnalysisType.FIVE_MINUTE);
           otherResult.put("fifteen", AnalysisType.FIFTEEN_MINUTE);
           analysisResult.setOtherResult(otherResult);
           return ApiResult.success(analysisResult);
       }catch (Exception e){
           return ApiResult.failed(e.getMessage());
       }
    }

    /**
     * 分析根据数据对查询增 删 改 和总和的记录条数
     * @param analysisForm
     * @return
     */
    @RequestMapping(value = "/fetchBehaviorHistoryCurve",method = RequestMethod.POST)
    public ApiResult<AnalysisResult>  fetchBehaviorHistoryCurve(@RequestBody AnalysisForm analysisForm){
        try {
            long dataMediaPairId = analysisForm.getDataMediaPairId();
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

            DataMediaPair dataMediaPair = dataMediaPairService.findById(dataMediaPairId);
            Channel channel = channelService.findByPipelineId(dataMediaPair.getPipelineId());

            Map<Long, BehaviorHistoryInfo> behaviourHistoryInfos = new LinkedHashMap<Long, BehaviorHistoryInfo>();

           TimelineBehaviorHistoryCondition condition = new TimelineBehaviorHistoryCondition();

            if (null != start && null != end) {
                condition.setStart(start);
                condition.setEnd(end);
                condition.setPairId(dataMediaPairId);
                behaviourHistoryInfos = tableStatService.listTimelineBehaviorHistory(condition);
            }

            Long totalInsert = 0L;
            Long totalUpdate = 0L;
            Long totalDelete = 0L;
            Long totalFileCount = 0L;
            Long totalFileSize = 0L;
            for (BehaviorHistoryInfo info : behaviourHistoryInfos.values()) {
                totalInsert += info.getInsertNumber();
                totalUpdate += info.getUpdateNumber();
                totalDelete += info.getDeleteNumber();
                totalFileCount += info.getFileNumber();
                totalFileSize += info.getFileSize();
            }
            AnalysisResult<PlotCellBehavior> analysisResult = new AnalysisResult();
            Map<String,Object> otherResult = new HashMap<>();

            otherResult.put("totalInsert", totalInsert);
            otherResult.put("totalUpdate", totalUpdate);
            otherResult.put("totalDelete", totalDelete);

//            context.put("totalFileCount", totalFileCount);
//            context.put("totalFileSize", totalFileSize);
//            context.put("behaviourHistoryInfos", behaviourHistoryInfos);
            List<PlotCellBehavior> plotCellBehaviors = new ArrayList<>();
            for(long key:behaviourHistoryInfos.keySet()){
                BehaviorHistoryInfo behaviorHistoryInfo = behaviourHistoryInfos.get(key);
                PlotCellBehavior plotCellBehavior = new PlotCellBehavior();
                if(behaviorHistoryInfo.getInsertNumber() > 0){
                    plotCellBehavior.setTime(key);
                    plotCellBehavior.setType("insert");
                    plotCellBehavior.setNum(behaviorHistoryInfo.getInsertNumber());
                }else if(behaviorHistoryInfo.getUpdateNumber() > 0){
                    plotCellBehavior.setTime(key);
                    plotCellBehavior.setType("update");
                    plotCellBehavior.setNum(behaviorHistoryInfo.getUpdateNumber());
                }else if(behaviorHistoryInfo.getDeleteNumber() > 0){
                    plotCellBehavior.setTime(key);
                    plotCellBehavior.setType("delete");
                    plotCellBehavior.setNum(behaviorHistoryInfo.getDeleteNumber());
                }
                plotCellBehaviors.add(plotCellBehavior);
            }
            analysisResult.setPlotCells(plotCellBehaviors);
            otherResult.put("start", sdf.format(start));
            otherResult.put("end", sdf.format(end));
            otherResult.put("dataMediaPair", dataMediaPair);
            otherResult.put("channel", channel);
            analysisResult.setOtherResult(otherResult);
            return ApiResult.success(analysisResult);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }


    @RequestMapping(value = "/fetchAllBehaviorHistory",method = RequestMethod.POST)
    public ApiResult<List> fetchAllBehaviorHistory(){
        return ApiResult.success(asynRequestCache.getLastData());
    }

    @RequestMapping(value = "/fetchAllNodeInfo",method = RequestMethod.POST)
    public ApiResult<List> fetchAllNodeInfo(){
       try{
           List result = new ArrayList();
           List<Node> nodeList =  nodeService.listAll();
           for(Node node:nodeList){
               AnalysisResult analysisResult = new AnalysisResult();
               Map<String, Object> otherResult = new HashMap<>();
               List<Channel> channels = channelService.listByNodeId(node.getId());
               if (node.getStatus().isStart()) {
                   otherResult.put("heapMemoryUsage", nodeRemoteService.getHeapMemoryUsage(node.getId()));
                   otherResult.put("versionInfo", nodeRemoteService.getNodeVersionInfo(node.getId()));
                   otherResult.put("systemInfo", nodeRemoteService.getNodeSystemInfo(node.getId()));
                   otherResult.put("threadActiveSize", nodeRemoteService.getThreadActiveSize(node.getId()));
                   otherResult.put("threadPoolSize", nodeRemoteService.getThreadPoolSize(node.getId()));
                   otherResult.put("runningPipelines", nodeRemoteService.getRunningPipelines(node.getId()));
               } else {
                   otherResult.put("heapMemoryUsage", 0);
                   otherResult.put("threadActiveSize", 0);
                   otherResult.put("threadPoolSize", 0);
                   otherResult.put("runningPipelines", 0);
                   otherResult.put("versionInfo", "");
                   otherResult.put("systemInfo", "");
               }
               otherResult.put("node", node);
               otherResult.put("channels", channels);
               analysisResult.setOtherResult(otherResult);
               result.add(analysisResult);
           }
           return ApiResult.success(result);
       }catch (Exception e){
           log.error(e.getMessage());
           e.printStackTrace();
           return ApiResult.failed(e.getMessage());
       }
    }
}
