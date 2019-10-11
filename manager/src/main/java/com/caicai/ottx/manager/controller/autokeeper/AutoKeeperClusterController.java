package com.caicai.ottx.manager.controller.autokeeper;

import com.alibaba.otter.shared.arbitrate.ArbitrateManageService;
import com.alibaba.otter.shared.arbitrate.exception.ArbitrateException;
import com.alibaba.otter.shared.common.model.autokeeper.AutoKeeperCluster;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.manager.controller.autokeeper.form.ZookeeperForm;
import com.caicai.ottx.service.autokeeper.impl.AutoKeeperCollector;
import com.caicai.ottx.service.config.autokeeper.AutoKeeperClusterService;
import com.caicai.ottx.service.config.channel.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by huaseng on 2019/8/28.
 */
@RestController
@RequestMapping("/zookeeper")
@Slf4j
public class AutoKeeperClusterController {

    @Autowired
    private AutoKeeperClusterService autoKeeperClusterService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ArbitrateManageService arbitrateManageService;

    @Autowired
    private AutoKeeperCollector autoKeeperCollector;

    @RequestMapping(value = "/reduction",method = RequestMethod.POST)
    public ApiResult<String> reduction(){
        String resultStr = "";
        List<Channel> channels = channelService.listAll();
        try{
            arbitrateManageService.systemEvent().init();
            // 遍历所有的Channel节点
            for (Channel channel : channels) {
                // 在ZK中初始化每个channel节点
                arbitrateManageService.channelEvent().init(channel.getId());

                // 在ZK中初始化该channel下的pipeline节点
                List<Pipeline> pipelines = channel.getPipelines();
                //
                for (Pipeline pipeline : pipelines) {
                    arbitrateManageService.pipelineEvent().init(pipeline.getChannelId(), pipeline.getId());
                }
            }
            resultStr = "恭喜！Zookeeper节点数据已经补全";
        }catch (ArbitrateException e){
            log.error("ERROR ## init zookeeper has a problem ", e);
            resultStr = "出错了！回复zookeeper的时候遇到问题！";
        }catch (Exception e) {
            log.error("ERROR ## init zookeeper has a problem ", e);
            resultStr = "出错了！回复zookeeper的时候遇到问题！";
        }
        return ApiResult.success(resultStr);
    }

    @RequestMapping(value = "/getZookeepers",method = RequestMethod.POST)
    public ApiResult<List<AutoKeeperCluster>> getZookeepers(){
        List<AutoKeeperCluster> autoKeeperClusters =  autoKeeperClusterService.listAutoKeeperClusters();
       return ApiResult.success(autoKeeperClusters);
    }

    @RequestMapping(value = "/getZookeeper",method = RequestMethod.POST)
    public ApiResult<AutoKeeperCluster> getZookeeper(@RequestBody ZookeeperForm zookeeperForm){
        AutoKeeperCluster autoKeeperCluster =  autoKeeperClusterService.findAutoKeeperClusterById(zookeeperForm.getId());
        return ApiResult.success(autoKeeperCluster);
    }

    @RequestMapping(value ="/add",method = RequestMethod.POST)
    public ApiResult<String> add(@RequestBody ZookeeperForm zookeeperForm){
        try{
            AutoKeeperCluster autoKeeperCluster = new AutoKeeperCluster();
            BeanUtils.copyProperties(zookeeperForm,autoKeeperCluster);
            autoKeeperClusterService.createAutoKeeperCluster(autoKeeperCluster);
            return ApiResult.success("插入成功");
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }

    @RequestMapping(value ="/remove",method = RequestMethod.POST)
    public ApiResult<String> remove(@RequestBody ZookeeperForm zookeeperForm){
        try{
            autoKeeperClusterService.removeAutoKeeperCluster(zookeeperForm.getId());
            return ApiResult.success("插入成功");
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }
    @RequestMapping(value ="/update",method = RequestMethod.POST)
    public ApiResult<String> update(@RequestBody ZookeeperForm zookeeperForm){
        try{
            AutoKeeperCluster autoKeeperCluster = new AutoKeeperCluster();
            BeanUtils.copyProperties(zookeeperForm,autoKeeperCluster);
            autoKeeperClusterService.modifyAutoKeeperCluster(autoKeeperCluster);
            return ApiResult.success("更新成功");
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }

    @RequestMapping(value ="/refresh",method = RequestMethod.POST)
    public ApiResult<String> refresh(@RequestBody ZookeeperForm zookeeperForm){
        try{
            AutoKeeperCluster autoKeeperCluster = autoKeeperClusterService.findAutoKeeperClusterById(zookeeperForm.getId());
            for (String address : autoKeeperCluster.getServerList()) {
                autoKeeperCollector.collectorServerStat(address);
                autoKeeperCollector.collectorConnectionStat(address);
                autoKeeperCollector.collectorWatchStat(address);
                autoKeeperCollector.collectorEphemeralStat(address);
            }
            return ApiResult.success("更新成功");
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }





}
