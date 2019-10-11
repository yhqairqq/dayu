package com.caicai.ottx.service.config.pipeline.impl;

import com.alibaba.otter.shared.arbitrate.ArbitrateManageService;
import com.alibaba.otter.shared.arbitrate.ArbitrateViewService;
import com.alibaba.otter.shared.common.model.config.data.DataMediaPair;
import com.alibaba.otter.shared.common.model.config.data.DataMediaPairComparable;
import com.alibaba.otter.shared.common.model.config.node.Node;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.common.utils.Assert;
import com.caicai.ottx.dal.entity.PipelineDO;
import com.caicai.ottx.dal.entity.PipelineNodeRelationDO;
import com.caicai.ottx.dal.entity.PipelineNodeRelationDOExample;
import com.caicai.ottx.dal.mapper.PipelineDOMapperExt;
import com.caicai.ottx.dal.mapper.PipelineNodeRelationDOMapperExt;
import com.caicai.ottx.service.common.alarm.AlarmService;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.common.exceptions.RepeatConfigureException;
import com.caicai.ottx.service.config.alarm.AlarmRuleService;
import com.caicai.ottx.service.config.datamediapair.DataMediaPairService;
import com.caicai.ottx.service.config.node.NodeService;
import com.caicai.ottx.service.config.pipeline.PipelineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by huaseng on 2019/8/26.
 */
@Service
@Slf4j
public class PipelineServiceImpl implements PipelineService{


    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private PipelineDOMapperExt pipelineDOMapperExt;
    @Autowired
    private PipelineNodeRelationDOMapperExt pipelineNodeRelationDOMapperExt;

    @Autowired
    private DataMediaPairService dataMediaPairService;
    @Autowired
    private ArbitrateViewService arbitrateViewService;

    @Autowired
    private ArbitrateManageService arbitrateManageService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private NodeService nodeService;
    @Autowired
    private AlarmRuleService alarmRuleService;

    @Override
    public void create(Pipeline pipeline) {
        Assert.assertNotNull(pipeline);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {

                try {
                   PipelineDO pipelineDo = modelToDo(pipeline);
                    if (!checkUnique(pipelineDo)) {
                        String exceptionCause = "exist the same name pipeline under the channel("
                                + pipelineDo.getChannelId() + ") in the database.";
                        log.warn("WARN ## " + exceptionCause);
                        throw new RepeatConfigureException(exceptionCause);
                    }
                    pipelineDOMapperExt.insertSelective(pipelineDo);

                    //默认插入监控
                    alarmRuleService.doOnekeyAddMonitor(pipelineDo.getId());

                    List<PipelineNodeRelationDO> pipelineNodeRelationDos = new ArrayList<PipelineNodeRelationDO>();

                    for (Node node : pipeline.getSelectNodes()) {
                      PipelineNodeRelationDO pipelineNodeRelationDo = new PipelineNodeRelationDO();
                        pipelineNodeRelationDo.setPipelineId(pipelineDo.getId());
                        pipelineNodeRelationDo.setNodeId(node.getId());
                        pipelineNodeRelationDo.setLocation(PipelineNodeRelationDO.Location.SELECT);
                        pipelineNodeRelationDos.add(pipelineNodeRelationDo);
                    }

                    for (Node node : pipeline.getExtractNodes()) {
                      PipelineNodeRelationDO pipelineNodeRelationDo = new PipelineNodeRelationDO();
                        pipelineNodeRelationDo.setPipelineId(pipelineDo.getId());
                        pipelineNodeRelationDo.setNodeId(node.getId());
                        pipelineNodeRelationDo.setLocation(PipelineNodeRelationDO.Location.EXTRACT);
                        pipelineNodeRelationDos.add(pipelineNodeRelationDo);
                    }

                    for (Node node : pipeline.getLoadNodes()) {
                      PipelineNodeRelationDO pipelineNodeRelationDo = new PipelineNodeRelationDO();
                        pipelineNodeRelationDo.setPipelineId(pipelineDo.getId());
                        pipelineNodeRelationDo.setNodeId(node.getId());
                        pipelineNodeRelationDo.setLocation(PipelineNodeRelationDO.Location.LOAD);
                        pipelineNodeRelationDos.add(pipelineNodeRelationDo);
                    }

//                    pipelineNodeRelationDao.insertBatch(pipelineNodeRelationDos);
                    for(PipelineNodeRelationDO pipelineNodeRelationDO:pipelineNodeRelationDos){
                        pipelineNodeRelationDOMapperExt.insertSelective(pipelineNodeRelationDO);
                    }
                    arbitrateManageService.pipelineEvent().init(pipelineDo.getChannelId(), pipelineDo.getId());
                } catch (RepeatConfigureException rce) {
                    throw rce;
                } catch (Exception e) {
                    log.error("ERROR ## create pipeline has an exception!");
                    throw new ManagerException(e);
                }
            }
        });
    }

    @Override
    public void remove(Long identity) {
        pipelineDOMapperExt.deleteByPrimaryKey(identity);
    }
    private boolean checkUnique(PipelineDO pipelineDo){
        int count = sqlSession.selectOne("checkPipelineUnique",pipelineDo);
                //(Integer) getSqlMapClientTemplate().queryForObject("checkPipelineUnique", pipelineDO);
        return count == 0 ? true : false;
    }

    @Override
    public void modify(Pipeline pipeline) {
        Assert.assertNotNull(pipeline);
        try {

           PipelineDO pipelineDo = modelToDo(pipeline);

            if (!checkUnique(pipelineDo)) {
                String exceptionCause = "exist the same name pipeline under the channel(" + pipelineDo.getChannelId()
                        + ") in the database.";
                log.warn("WARN ## " + exceptionCause);
                throw new RepeatConfigureException(exceptionCause);
            }
            PipelineNodeRelationDOExample example = new PipelineNodeRelationDOExample();
            example.createCriteria().andPipelineIdEqualTo(pipelineDo.getId());
            pipelineNodeRelationDOMapperExt.deleteByExample(example);
            pipelineDOMapperExt.updateByPrimaryKeySelective(pipelineDo);

            List<PipelineNodeRelationDO> pipelineNodeRelationDos = new ArrayList<PipelineNodeRelationDO>();

            for (Node node : pipeline.getSelectNodes()) {
                PipelineNodeRelationDO pipelineNodeRelationDo = new PipelineNodeRelationDO();
                pipelineNodeRelationDo.setPipelineId(pipelineDo.getId());
                pipelineNodeRelationDo.setNodeId(node.getId());
                pipelineNodeRelationDo.setLocation(PipelineNodeRelationDO.Location.SELECT);
                pipelineNodeRelationDos.add(pipelineNodeRelationDo);
            }

            for (Node node : pipeline.getExtractNodes()) {
                PipelineNodeRelationDO pipelineNodeRelationDo = new PipelineNodeRelationDO();
                pipelineNodeRelationDo.setPipelineId(pipelineDo.getId());
                pipelineNodeRelationDo.setNodeId(node.getId());
                pipelineNodeRelationDo.setLocation(PipelineNodeRelationDO.Location.EXTRACT);
                pipelineNodeRelationDos.add(pipelineNodeRelationDo);
            }

            for (Node node : pipeline.getLoadNodes()) {
                PipelineNodeRelationDO pipelineNodeRelationDo = new PipelineNodeRelationDO();
                pipelineNodeRelationDo.setPipelineId(pipelineDo.getId());
                pipelineNodeRelationDo.setNodeId(node.getId());
                pipelineNodeRelationDo.setLocation(PipelineNodeRelationDO.Location.LOAD);
                pipelineNodeRelationDos.add(pipelineNodeRelationDo);
            }

//            pipelineNodeRelationDao.insertBatch(pipelineNodeRelationDos);
            for(PipelineNodeRelationDO pipelineNodeRelationDO:pipelineNodeRelationDos){
                pipelineNodeRelationDOMapperExt.insertSelective(pipelineNodeRelationDO);
            }
        } catch (Exception e) {
            log.error("ERROR ## modify the pipeline(" + pipeline.getId() + ") has an exception!");
            throw new ManagerException(e);
        }
    }
    /**
     * 根据pipelineId找到pipeline
     */
    @Override
    public Pipeline findById(Long pipelineId) {
        Assert.assertNotNull(pipelineId);
        List<Pipeline> pipeline = listByIds(pipelineId);
        if (pipeline.size() != 1) {
            String exceptionCause = "query pipeline by pipelineId:" + pipelineId + " but return " + pipeline.size()
                    + " pipeline!";
            log.error("ERROR ## " + exceptionCause);
            throw new ManagerException(exceptionCause);
        }
        return pipeline.get(0);
    }

    @Override
    public List<Pipeline> listByIds(Long... identities) {
        List<Pipeline> pipelines = new ArrayList<Pipeline>();
        try {
            List<PipelineDO> pipelineDos = new ArrayList<PipelineDO>();
            if (identities.length < 1) {
                pipelineDos = sqlSession.selectList("listPipelines");
//                        pipelineDao.listAll();
//                if (pipelineDos.isEmpty()) {
                    log.debug("DEBUG ## couldn't query any pipeline, maybe hasn't create any pipeline.");
                    return pipelines;
                }
             else {
                pipelineDos = sqlSession.selectList("listPipelineByIds",identities);
//                        pipelineDao.listByMultiId(identities);
                if (CollectionUtils.isEmpty(pipelineDos)) {
                    String exceptionCause = "couldn't query any pipeline by pipelineIds:" + Arrays.toString(identities);
                    log.error("ERROR ## " + exceptionCause);
                    throw new ManagerException(exceptionCause);
                }
            }
            pipelines = doToModel(pipelineDos);
        } catch (Exception e) {
            log.error("ERROR ## query pipelines has an exception!");
            throw new ManagerException(e);
        }
        return pipelines;
    }
    private List<Pipeline> doToModel(List<PipelineDO> pipelineDos) {
        List<Pipeline> pipelines = new ArrayList<Pipeline>();
        for (PipelineDO pipelineDo : pipelineDos) {
            pipelines.add(doToModel(pipelineDo));
        }
        return pipelines;
    }
    /**
     * 用于Model对象转化为DO对象
     *
     * @param pipeline
     * @return PipelineDO
     */
    private PipelineDO modelToDo(Pipeline pipeline) {
     PipelineDO pipelineDO = new PipelineDO();
        try {
            pipelineDO.setId(pipeline.getId());
            pipelineDO.setName(pipeline.getName());
            pipelineDO.setParameters(pipeline.getParameters());
            pipelineDO.setDescription(pipeline.getDescription());
            pipelineDO.setChannelId(pipeline.getChannelId());
            pipelineDO.setGmtCreate(pipeline.getGmtCreate());
            pipelineDO.setGmtModified(pipeline.getGmtModified());

        } catch (Exception e) {
            log.error("ERROR ## change the pipeline Model to Do has an exception");
            throw new ManagerException(e);
        }

        return pipelineDO;
    }

    /**
     * 用于DO对象转化为Model对象
     *
     * @param
     * @return Pipeline
     */
    private Pipeline doToModel(PipelineDO pipelineDo) {
        Pipeline pipeline = new Pipeline();
        try {
            pipeline.setId(pipelineDo.getId());
            pipeline.setName(pipelineDo.getName());
            pipeline.setParameters(pipelineDo.getParameters());
            pipeline.setDescription(pipelineDo.getDescription());
            pipeline.setGmtCreate(pipelineDo.getGmtCreate());
            pipeline.setGmtModified(pipelineDo.getGmtModified());
            pipeline.setChannelId(pipelineDo.getChannelId());
            pipeline.getParameters().setMainstemClientId(pipeline.getId().shortValue());

            // 组装DatamediaPair
            List<DataMediaPair> pairs = dataMediaPairService.listByPipelineId(pipelineDo.getId());
            Collections.sort(pairs, new DataMediaPairComparable());
            pipeline.setPairs(pairs);

            // 组装Node
            List<PipelineNodeRelationDO> relations = sqlSession.selectList("listRelationsByPipelineIds",new Long[]{pipelineDo.getId()});
//                    pipelineNodeRelationDao.listByPipelineIds(pipelineDo.getId());

            List<Long> totalNodeIds = new ArrayList<Long>();

            for (PipelineNodeRelationDO relation : relations) {
                Long nodeId = relation.getNodeId();
                if (!totalNodeIds.contains(nodeId)) {
                    totalNodeIds.add(nodeId);
                }
            }

            List<Node> totalNodes = nodeService.listByIds(totalNodeIds.toArray(new Long[totalNodeIds.size()]));
            List<Node> selectNodes = new ArrayList<Node>();
            List<Node> extractNodes = new ArrayList<Node>();
            List<Node> loadNodes = new ArrayList<Node>();

            for (Node node : totalNodes) {
                for (PipelineNodeRelationDO relation : relations) {
                    if (node.getId().equals(relation.getNodeId())) {
                        if (relation.getLocation().isSelect()) {
                            selectNodes.add(node);
                        } else if (relation.getLocation().isExtract()) {
                            extractNodes.add(node);
                        } else if (relation.getLocation().isLoad()) {
                            loadNodes.add(node);
                        }
                    }
                }
            }

            pipeline.setSelectNodes(selectNodes);
            pipeline.setExtractNodes(extractNodes);
            pipeline.setLoadNodes(loadNodes);

        } catch (Exception e) {
            log.error("ERROR ## change the pipeline Do to Model has an exception");
            throw new ManagerException(e);
        }

        return pipeline;
    }

    @Override
    public List<Pipeline> listAll() {

       List<PipelineDO> pipelineDOS = sqlSession.selectList("listPipelines");
       return doToModel(pipelineDOS);
    }

    @Override
    public List<Pipeline> listByCondition(Map condition) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public int getCount(Map condition) {
        return 0;
    }

    @Override
    public List<Pipeline> listByChannelIds(Long... channelIds) {
        Assert.assertNotNull(channelIds);
        List<Pipeline> pipelines = new ArrayList<Pipeline>();
        try{
            List<PipelineDO> pipelineDos = sqlSession.selectList("listPipelinesByChannelIds",channelIds);
            if(CollectionUtils.isEmpty(pipelineDos)){
                log.debug("DEBUG ## query pipeline by channelId:" + channelIds + " return null.");
                return pipelines;
            }
            pipelines = doToModel(pipelineDos);
        }catch (Exception e){
            log.error("ERROR ## query pipelines by channelIds:" + channelIds.toString() + " has an exception!");
            throw new ManagerException(e);
        }
        return pipelines;
    }

    @Override
    public List<Pipeline> listByChannelIdsWithoutOther(Long... channelIds) {
        return null;
    }

    @Override
    public List<Pipeline> listByChannelIdsWithoutColumn(Long... channelIds) {
        Assert.assertNotNull(channelIds);
        List<Pipeline> pipelines = new ArrayList<>();
        try{
            List<PipelineDO> pipelineDos = sqlSession.selectList("listPipelinesByChannelIds",channelIds);
            if(CollectionUtils.isEmpty(pipelineDos)){
                log.debug("DEBUG ## query pipeline by channelId:" + channelIds + " return null.");
                return pipelines;
            }
            pipelines = doToModelWithoutColumn(pipelineDos);

        }catch (Exception e){
            log.error("ERROR ## query pipelines by channelIds:" + channelIds.toString() + " has an exception!");
            throw new ManagerException(e);
        }
        return pipelines;
    }
    private List<Pipeline> doToModelWithoutColumn(List<PipelineDO> pipelineDos) {
        List<Pipeline> pipelines = new ArrayList<Pipeline>();
        for (PipelineDO pipelineDo : pipelineDos) {
            pipelines.add(doToModelWithoutColumn(pipelineDo));
        }
        return pipelines;
    }
    private Pipeline doToModelWithoutColumn(PipelineDO pipelineDo) {
        Pipeline pipeline = new Pipeline();
        try {
            pipeline.setId(pipelineDo.getId());
            pipeline.setName(pipelineDo.getName());
            pipeline.setParameters(pipelineDo.getParameters());
            pipeline.setDescription(pipelineDo.getDescription());
            pipeline.setGmtCreate(pipelineDo.getGmtCreate());
            pipeline.setGmtModified(pipelineDo.getGmtModified());
            pipeline.setChannelId(pipelineDo.getChannelId());
            pipeline.getParameters().setMainstemClientId(pipeline.getId().shortValue());

            // 组装DatamediaPair
            List<DataMediaPair> pairs = dataMediaPairService.listByPipelineIdWithoutColumn(pipelineDo.getId());
            Collections.sort(pairs, new DataMediaPairComparable());
            pipeline.setPairs(pairs);

            // 组装Node
            List<PipelineNodeRelationDO> relations = sqlSession.selectList("listRelationsByPipelineIds",new long[]{pipelineDo.getId()});

            List<Long> totalNodeIds = new ArrayList<Long>();

            for (PipelineNodeRelationDO relation : relations) {
                Long nodeId = relation.getNodeId();
                if (!totalNodeIds.contains(nodeId)) {
                    totalNodeIds.add(nodeId);
                }
            }

            List<Node> totalNodes = nodeService.listByIds(totalNodeIds.toArray(new Long[totalNodeIds.size()]));
            List<Node> selectNodes = new ArrayList<Node>();
            List<Node> extractNodes = new ArrayList<Node>();
            List<Node> loadNodes = new ArrayList<Node>();

            for (Node node : totalNodes) {
                for (PipelineNodeRelationDO relation : relations) {
                    if (node.getId().equals(relation.getNodeId())) {
                        if (relation.getLocation().isSelect()) {
                            selectNodes.add(node);
                        } else if (relation.getLocation().isExtract()) {
                            extractNodes.add(node);
                        } else if (relation.getLocation().isLoad()) {
                            loadNodes.add(node);
                        }
                    }
                }
            }

            pipeline.setSelectNodes(selectNodes);
            pipeline.setExtractNodes(extractNodes);
            pipeline.setLoadNodes(loadNodes);

        } catch (Exception e) {
            log.error("ERROR ## change the pipeline Do to Model has an exception");
            throw new ManagerException(e);
        }

        return pipeline;
    }

    @Override
    public List<Pipeline> listByNodeId(Long nodeId) {
        return null;
    }

    @Override
    public boolean hasRelation(Long nodeId) {
        List<PipelineNodeRelationDO> relations = sqlSession.selectList("listRelationsByNodeId",nodeId);
//                pipelineNodeRelationDao.listByNodeId(nodeId);
        if (CollectionUtils.isEmpty(relations)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<Pipeline> listByDestinationWithoutOther(String destination) {
        Assert.assertNotNull(destination);
        Map<String,Object> map = new HashMap<>();
        map.put("destination",destination);
        List<Pipeline> pipelines = new ArrayList<Pipeline>();
        try {

            List<PipelineDO> pipelineDos = sqlSession.selectList("listByDestinationCondition",map);
                    //pipelineDao.listByDestinationCondition(destination);
            if (CollectionUtils.isEmpty(pipelineDos)) {
                log.debug("DEBUG ## query pipeline by destination:" + destination + " return null.");
                return pipelines;
            }
            pipelines = doToModelWithoutOther(pipelineDos);
        } catch (Exception e) {
            log.error("ERROR ## query pipelines by destination:" + destination + " has an exception!");
            throw new ManagerException(e);
        }
        return pipelines;
    }



    private List<Pipeline> doToModelWithoutOther(List<PipelineDO> pipelineDos) {
        List<Pipeline> pipelines = new ArrayList<Pipeline>();
        for (PipelineDO pipelineDo : pipelineDos) {
            pipelines.add(doToModelWithoutOther(pipelineDo));
        }
        return pipelines;
    }
    private Pipeline doToModelWithoutOther(PipelineDO pipelineDo) {
        Pipeline pipeline = new Pipeline();
        try {
            pipeline.setId(pipelineDo.getId());
            pipeline.setName(pipelineDo.getName());
            pipeline.setParameters(pipelineDo.getParameters());
            pipeline.setDescription(pipelineDo.getDescription());
            pipeline.setGmtCreate(pipelineDo.getGmtCreate());
            pipeline.setGmtModified(pipelineDo.getGmtModified());
            pipeline.setChannelId(pipelineDo.getChannelId());
            pipeline.getParameters().setMainstemClientId(pipeline.getId().shortValue());

        } catch (Exception e) {
            log.error("ERROR ## change the pipeline Do to Model has an exception");
            throw new ManagerException(e);
        }

        return pipeline;
    }
}
