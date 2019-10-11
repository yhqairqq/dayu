package com.caicai.ottx.manager.controller.pipeline.form;

import com.alibaba.otter.shared.common.model.config.pipeline.PipelineParameter;
import com.caicai.ottx.manager.controller.BaseForm;
import lombok.Data;

/**
 * Created by huaseng on 2019/9/10.
 */
@Data
public class PipelineForm extends BaseForm {
    private long id; //pipelineId
    private Long channelId;
    private String              name;
    private String              description;                               // 描述信息
    private long[] selectNodeIds;
    private long[] loadNodeIds;
    private long[] extractNodeIds;
    private Long                  pipelineId;
    private Long                  parallelism                = 3L;                          // 并行度
    private PipelineParameter.LoadBanlanceAlgorithm lbAlgorithm                = PipelineParameter.LoadBanlanceAlgorithm.Random; // 负载均衡算法
    private Boolean               home                       = false;                       // 是否为主站点
    private PipelineParameter.SelectorMode selectorMode               = PipelineParameter.SelectorMode.Canal;          // 数据获取方式
    private String                destinationName;
    private Short                 mainstemClientId;                                         // mainstem订阅id
    private Integer               mainstemBatchsize          = 10000 * 10;                  // mainstem订阅批次大小
    private Integer               extractPoolSize            = 5;                           // extract模块载入线程数，针对单个载入通道
    private Integer               loadPoolSize               = 5;                           // load模块载入线程数，针对单个载入通道
    private Integer               fileLoadPoolSize           = 5;                           // 文件同步线程数

    private Boolean               dumpEvent                  = true;                        // 是否需要dumpevent对象
    private Boolean               dumpSelector               = true;                        // 是否需要dumpSelector信息
    private Boolean               dumpSelectorDetail         = true;                        // 是否需要dumpSelector的详细信息
    private PipelineParameter.PipeChooseMode pipeChooseType             = PipelineParameter.PipeChooseMode.AUTOMATIC;    // pipe传输模式
    private Boolean               useBatch                   = true;                        // 是否使用batch模式
    private Boolean               skipSelectException        = false;                       // 是否跳过select时的执行异常
    private Boolean               skipLoadException          = false;                       // 是否跳过load时的执行异常
    private PipelineParameter.ArbitrateMode arbitrateMode              = PipelineParameter.ArbitrateMode.ZOOKEEPER;     // 调度模式，默认进行自动选择
    private Long                  batchTimeout               = -1L;                         // 获取批量数据的超时时间,-1代表不进行超时控制，0代表永久，>0则表示按照指定的时间进行控制(单位毫秒)
    private Boolean               fileDetect                 = false;                       // 是否开启文件同步检测
    private Boolean               skipFreedom                = false;                       // 是否跳过自由门数据
    private Boolean               useLocalFileMutliThread    = false;                       // 是否启用对local
    // file同步启用多线程
    private Boolean               useFileEncrypt             = false;                       // 是否针对文件进行加密处理
    private Boolean               useExternalIp              = false;                       // 是否起用外部Ip
    private Boolean               useTableTransform          = false;                       // 是否启用转化机制，比如类型不同，默认为true，兼容老逻辑
    private Boolean               enableCompatibleMissColumn = true;                        // 是否启用兼容字段不匹配处理
    private Boolean               skipNoRow                  = false;                       // 跳过反查没记录的情况
    private String                channelInfo;                                              // 同步标记，设置该标记后会在retl_mark中记录，在messageParse时进行check，相同则忽略
    private Boolean               dryRun                     = false;                       // 是否启用dry
    // run模型，只记录load日志，不同步数据
    private Boolean               ddlSync                    = true;                        // 是否支持ddl同步
    private Boolean               dataSyncSkip               = false;                       //数据同步跳过
    private Boolean               skipDdlException           = false;
    private Boolean               passChange                 = true;     //传递变化
}
