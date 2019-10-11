package com.caicai.ottx.manager.controller.node;

import com.alibaba.otter.manager.web.common.model.SeniorNode;
import com.alibaba.otter.shared.common.model.autokeeper.AutoKeeperCluster;
import com.alibaba.otter.shared.common.model.config.node.Node;
import com.alibaba.otter.shared.common.model.config.node.NodeParameter;
import com.caicai.ottx.common.ApiResult;
import com.caicai.ottx.common.utils.EnumBeanUtils;
import com.caicai.ottx.common.vo.PageResult;
import com.caicai.ottx.manager.controller.node.form.NodeForm;
import com.caicai.ottx.service.config.autokeeper.AutoKeeperClusterService;
import com.caicai.ottx.service.config.node.NodeService;
import com.caicai.ottx.service.config.pipeline.PipelineService;
import com.github.pagehelper.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/30.
 */
@RestController
@RequestMapping("/node")
public class NodeController {
    @Autowired
    private NodeService nodeService;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private AutoKeeperClusterService autoKeeperClusterService;

    @RequestMapping(value = "/getByPage",method = RequestMethod.POST)
    public ApiResult<PageResult> getByPage(@RequestBody NodeForm nodeForm){
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("current", nodeForm.getCurrentPage());
        condition.put("pageSize", nodeForm.getPageSize());
        List<Node> nodes = nodeService.listByCondition(condition);
        List<SeniorNode> seniorNodes = new ArrayList<SeniorNode>();
        for(Node node:nodes){
            SeniorNode seniorNode = new SeniorNode();
            seniorNode.setId(node.getId());
            seniorNode.setIp(node.getIp());
            seniorNode.setName(node.getName());
            seniorNode.setPort(node.getPort());
            seniorNode.setDescription(node.getDescription());
            seniorNode.setStatus(node.getStatus());
            seniorNode.setParameters(node.getParameters());
            seniorNode.setGmtCreate(node.getGmtCreate());
            seniorNode.setGmtModified(node.getGmtModified());
            seniorNode.setUsed(pipelineService.hasRelation(node.getId()));
            seniorNodes.add(seniorNode);
        }
        return ApiResult.success(new PageResult(seniorNodes,(Page)nodes));
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ApiResult<String> add(@RequestBody NodeForm nodeForm){
        try{
            Node node = new Node();
            NodeParameter parameter = new NodeParameter();

            BeanUtils.copyProperties(nodeForm,node);
            BeanUtils.copyProperties(nodeForm,parameter);

            if (parameter.getDownloadPort() == null || parameter.getDownloadPort() == 0) {
                parameter.setDownloadPort(node.getPort().intValue() + 1);
            }

            if (parameter.getMbeanPort() == null || parameter.getMbeanPort() == 0) {
                parameter.setMbeanPort(node.getPort().intValue() + 2);
            }

            Long autoKeeperclusterId = nodeForm.getAutoKeeperClusterId();
            if (autoKeeperclusterId != null && autoKeeperclusterId > 0) {
                AutoKeeperCluster autoKeeperCluster = autoKeeperClusterService.findAutoKeeperClusterById(autoKeeperclusterId);
                parameter.setZkCluster(autoKeeperCluster);
            }
            node.setParameters(parameter);
            nodeService.create(node);
            return ApiResult.success("插入成功");
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ApiResult<String> update(@RequestBody NodeForm nodeForm){
        try{
            Node node = new Node();
            NodeParameter parameter = new NodeParameter();

            BeanUtils.copyProperties(nodeForm,node);
            BeanUtils.copyProperties(nodeForm,parameter);

            if (parameter.getDownloadPort() == null || parameter.getDownloadPort() == 0) {
                parameter.setDownloadPort(node.getPort().intValue() + 1);
            }

            if (parameter.getMbeanPort() == null || parameter.getMbeanPort() == 0) {
                parameter.setMbeanPort(node.getPort().intValue() + 2);
            }

            Long autoKeeperclusterId = nodeForm.getAutoKeeperClusterId();
            if (autoKeeperclusterId != null && autoKeeperclusterId > 0) {
                AutoKeeperCluster autoKeeperCluster = autoKeeperClusterService.findAutoKeeperClusterById(autoKeeperclusterId);
                parameter.setZkCluster(autoKeeperCluster);
            }
            node.setParameters(parameter);
            nodeService.modify(node);
            return ApiResult.success("更新成功");
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }
    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    public ApiResult<String> remove(@RequestBody NodeForm nodeForm){
        try{
            nodeService.remove(nodeForm.getId());
            return ApiResult.success("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return ApiResult.failed(e.getMessage());
        }
    }
}
