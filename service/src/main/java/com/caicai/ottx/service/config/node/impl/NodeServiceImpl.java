package com.caicai.ottx.service.config.node.impl;

import com.alibaba.otter.shared.arbitrate.ArbitrateManageService;
import com.alibaba.otter.shared.common.model.autokeeper.AutoKeeperCluster;
import com.alibaba.otter.shared.common.model.config.node.Node;
import com.alibaba.otter.shared.common.model.config.node.NodeParameter;
import com.alibaba.otter.shared.common.model.config.node.NodeStatus;
import com.alibaba.otter.shared.common.utils.Assert;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import com.caicai.ottx.dal.entity.NodeDO;
import com.caicai.ottx.dal.mapper.NodeDOMapperExt;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.common.exceptions.RepeatConfigureException;
import com.caicai.ottx.service.config.autokeeper.AutoKeeperClusterService;
import com.caicai.ottx.service.config.node.NodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/26.
 */
@Service
@Slf4j
public class NodeServiceImpl  implements NodeService {
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private ArbitrateManageService arbitrateManageService;
    @Autowired
    private AutoKeeperClusterService autoKeeperClusterService;
    @Autowired
    private NodeDOMapperExt nodeDOMapperExt;

    @Override
    public void create(Node node) {
        Assert.assertNotNull(node);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                   NodeDO nodeDo = modelToDo(node);
                    nodeDo.setId(0L);
                    if (!checkUnique(nodeDo)) {
                        String exceptionCause = "exist the same repeat node in the database.";
                        log.warn("WARN ## " + exceptionCause);
                        throw new com.alibaba.otter.manager.biz.common.exceptions.RepeatConfigureException(exceptionCause);
                    }
                    nodeDOMapperExt.insertSelective(nodeDo);
                } catch (RepeatConfigureException rce) {
                    throw rce;
                } catch (Exception e) {
                    log.error("ERROR ## create node has an exception!");
                    throw new ManagerException(e);
                }
            }
        });
    }

    @Override
    public void remove(Long nodeId) {
        Assert.assertNotNull(nodeId);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    nodeDOMapperExt.deleteByPrimaryKey(nodeId);
                } catch (Exception e) {
                    log.error("ERROR ## remove node(" + nodeId + ") has an exception!");
                    throw new ManagerException(e);
                }
            }
        });
    }

    private boolean checkUnique( NodeDO nodeDo){
        int count  = sqlSession.selectOne("checkNodeUnique",nodeDo);
        return count == 0 ? true : false;
    }

    @Override
    public void modify(Node node) {
        Assert.assertNotNull(node);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                  NodeDO nodeDo = modelToDo(node);
                    if (checkUnique(nodeDo)) {
                        nodeDOMapperExt.updateByPrimaryKeySelective(nodeDo);
                    } else {
                        String exceptionCause = "exist the same repeat node in the database.";
                        log.warn("WARN ## " + exceptionCause);
                        throw new RepeatConfigureException(exceptionCause);
                    }
                } catch (RepeatConfigureException rce) {
                    throw rce;
                } catch (Exception e) {
                    log.error("ERROR ## modify node(" + node.getId() + ") has an exception!");
                    throw new ManagerException(e);
                }
            }
        });
    }
    private NodeDO modelToDo(Node node) {
      NodeDO nodeDo = new NodeDO();
        try {
            nodeDo.setId(node.getId());
            nodeDo.setIp(node.getIp());
            nodeDo.setName(node.getName());
            nodeDo.setPort(node.getPort());
            nodeDo.setDescription(node.getDescription());
            nodeDo.setStatus(node.getStatus());
            nodeDo.setParameters(node.getParameters());
            nodeDo.setGmtCreate(node.getGmtCreate());
            nodeDo.setGmtModified(node.getGmtModified());
        } catch (Exception e) {
            log.error("ERROR ## change the node Model to Do has an exception");
            throw new ManagerException(e);
        }
        return nodeDo;
    }
    @Override
    public Node findById(Long nodeId) {
        Assert.assertNotNull(nodeId);
        List<Node> nodes = listByIds(nodeId);
        if (nodes.size() != 1) {
            String exceptionCause = "query nodeId:" + nodeId + " return null.";
            log.error("ERROR ## " + exceptionCause);
            throw new ManagerException(exceptionCause);
        }

        return nodes.get(0);
    }

    @Override
    public List<Node> listByIds(Long... identities) {
        List<Node> nodes = new ArrayList<>();
        try{
            List<NodeDO> nodeDos = null;
            if(identities.length == 0){
                nodeDos = sqlSession.selectList("listNodes");
            }else{
                nodeDos =  sqlSession.selectList("listNodeByIds",identities);
            }

            // 验证zk的node信息
            List<Long> nodeIds = arbitrateManageService.nodeEvent().liveNodes();
            for (NodeDO nodeDo : nodeDos) {
                if (nodeIds.contains(nodeDo.getId())) {
                    nodeDo.setStatus(NodeStatus.START);
                } else {
                    nodeDo.setStatus(NodeStatus.STOP);
                }
            }
            nodes = doToModel(nodeDos);
        }catch (Exception e){
            log.error("ERROR ## query nodes has an exception!");
            throw new ManagerException(e);
        }
        return nodes;
    }
    private List<Node> doToModel(List<NodeDO> nodeDos) {
        List<Node> nodes = new ArrayList<Node>();
        for (NodeDO nodeDo : nodeDos) {
            nodes.add(doToModel(nodeDo));
        }
        return nodes;
    }
    /**
     * 用于DO对象转化为Model对象
     *
     * @param nodeDo
     * @return Node
     */
    private Node doToModel(NodeDO nodeDo) {
        Node node = new Node();
        try {
            node.setId(nodeDo.getId());
            node.setIp(nodeDo.getIp());
            node.setName(nodeDo.getName());
            node.setPort(nodeDo.getPort());
            node.setDescription(nodeDo.getDescription());
            node.setStatus(nodeDo.getStatus());
            // 处理下zk集群
            NodeParameter parameter = nodeDo.getParameters();
            if (parameter.getZkCluster() != null) {
                AutoKeeperCluster zkCluster = autoKeeperClusterService.findAutoKeeperClusterById(parameter.getZkCluster().getId());
                parameter.setZkCluster(zkCluster);
            }

            node.setParameters(parameter);
            node.setGmtCreate(nodeDo.getGmtCreate());
            node.setGmtModified(nodeDo.getGmtModified());
        } catch (Exception e) {
            log.error("ERROR ## change the node Do to Model has an exception");
            throw new ManagerException(e);
        }

        return node;
    }
    @Override
    public List<Node> listAll() {
        return listByIds();
    }

    @Override
    public List<Node> listByCondition(Map condition) {
        PageHelper.startPage((int)condition.get("current"),(int)condition.get("pageSize"));
        List<NodeDO> nodeDos = sqlSession.selectList("listNodes",condition);
        if(CollectionUtils.isEmpty(nodeDos)){
            log.debug("DEBUG ## couldn't query any channel by the condition:" + JsonUtils.marshalToString(condition));
            return new Page<Node>();
        }
        //验证zk的node信息
        List<Long> nodeIds = arbitrateManageService.nodeEvent().liveNodes();
        for(NodeDO nodeDO:nodeDos){
            if(!CollectionUtils.isEmpty(nodeIds) && nodeIds.contains(nodeDO.getId())){
                nodeDO.setStatus(NodeStatus.START);
            }else{
                nodeDO.setStatus(NodeStatus.STOP);
            }
        }
        List<Node> nodes =  doToModel(nodeDos);
        Page<Node> page = new Page<Node>();
        Page<Node> pageTemp = (Page)nodeDos;
        page.addAll(nodes);
        page.setPageSize(pageTemp.getPageSize());
        page.setPageNum(pageTemp.getPageNum());
        page.setTotal(pageTemp.getTotal());
        return page;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public int getCount(Map condition) {
        return 0;
    }
}
