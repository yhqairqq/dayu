package com.caicai.ottx.service.config.channel.impl;

import com.alibaba.otter.shared.arbitrate.ArbitrateManageService;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.channel.ChannelStatus;
import com.alibaba.otter.shared.common.model.config.parameter.SystemParameter;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.common.model.config.pipeline.PipelineParameter;
import com.alibaba.otter.shared.common.utils.Assert;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import com.caicai.ottx.dal.entity.ChannelDO;
import com.caicai.ottx.dal.mapper.ChannelDOMapperExt;
import com.caicai.ottx.service.common.exceptions.InvalidConfigureException;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.common.exceptions.RepeatConfigureException;
import com.caicai.ottx.service.config.channel.ChannelService;
import com.caicai.ottx.service.config.parameter.SystemParameterService;
import com.caicai.ottx.service.config.pipeline.PipelineService;
import com.caicai.ottx.service.remote.ConfigRemoteService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/23.
 */
@Service
@Slf4j
public class ChannelServiceImpl implements ChannelService{
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private SystemParameterService systemParameterService;
    @Autowired
    private ArbitrateManageService arbitrateManageService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private PipelineService pipelineService;
    @Autowired
    private ConfigRemoteService configRemoteService;

    @Autowired
    private ChannelDOMapperExt channelDOMapperExt;

    private boolean checkUnique(ChannelDO entityObj){
        Assert.assertNotNull(entityObj);
        int count = sqlSession.selectOne("checkChannelUnique",entityObj);
                //(Integer) getSqlMapClientTemplate().queryForObject("checkChannelUnique", entityObj);
        return count == 0 ? true : false;
    }


    public void create(Channel channel) {
        Assert.assertNotNull(channel);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {

                   ChannelDO channelDo = modelToDo(channel);
                    channelDo.setId(0L);

                    if (!checkUnique(channelDo)) {
                        String exceptionCause = "exist the same name channel in the database.";
                        log.warn("WARN ## " + exceptionCause);
                        throw new RepeatConfigureException(exceptionCause);
                    }
                    channelDOMapperExt.insertSelective(channelDo);
                    channel.setId(channelDo.getId());
//                    channelDao.insert(channelDo);
                    arbitrateManageService.channelEvent().init(channelDo.getId());

                } catch (RepeatConfigureException rce) {
                    throw rce;
                } catch (Exception e) {
                    log.error("ERROR ## create channel has an exception ", e);
                    throw new ManagerException(e);
                }
            }
        });
    }
    private ChannelDO modelToDo(Channel channel) {
      ChannelDO channelDO = new ChannelDO();
        try {
            channelDO.setId(channel.getId());
            channelDO.setName(channel.getName());
            channelDO.setDescription(channel.getDescription());
            channelDO.setStatus(channel.getStatus());
            channelDO.setParameters(channel.getParameters());
            channelDO.setGmtCreate(channel.getGmtCreate());
            channelDO.setGmtModified(channel.getGmtModified());
        } catch (Exception e) {
            log.error("ERROR ## change the channel Model to Do has an exception");
            throw new ManagerException(e);
        }
        return channelDO;
    }
    @Override
    public void remove(Long identity) {
        channelDOMapperExt.deleteByPrimaryKey(identity);
    }

    @Override
    public void modify(Channel channel) {
        Assert.assertNotNull(channel);

        try {
            ChannelDO channelDo = modelToDo(channel);
            if (checkUnique(channelDo)) {
                channelDOMapperExt.updateByPrimaryKeySelective(channelDo);
            } else {
                String exceptionCause = "exist the same name channel in the database.";
                log.warn("WARN ## " + exceptionCause);
                throw new RepeatConfigureException(exceptionCause);
            }

        } catch (RepeatConfigureException rce) {
            throw rce;
        } catch (Exception e) {
            log.error("ERROR ## modify channel has an exception ", e);
            throw new ManagerException(e);
        }
    }

    @Override
    public Channel findById(Long channelId) {
        Assert.assertNotNull(channelId);
        ChannelDO channelDO  = channelDOMapperExt.selectByPrimaryKey(channelId);
        if(channelDO != null){
            return   doToModel(channelDO);
        }
        return null;
    }

    @Override
    public List<Channel> listByIds(Long... identities) {
        List<Channel> channels = new ArrayList<>();
        try{
            List<ChannelDO> channelDos = null;
            if(identities.length < 1){
                channelDos = sqlSession.selectList("listChannels");
                if(CollectionUtils.isEmpty(channelDos)){
                    log.debug("DEBUG ## couldn't query any channel, maybe hasn't create any channel.");
                    return channels;
                }
            }else{
                channelDos = sqlSession.selectList("listChannelByIds",identities);
                if(CollectionUtils.isEmpty(channelDos)){
                    String exceptionCause = "couldn't query any channel by channelIds:" + Arrays.toString(identities);
                    log.error("ERROR ## " + exceptionCause);
                    throw new ManagerException(exceptionCause);
                }
            }
            channels = doToModel(channelDos);

        }catch (Exception e){
            log.error("ERROR ## query channels has an exception!");
            throw new ManagerException(e);
        }
        return channels;
    }
    private List<Channel> doToModel(List<ChannelDO> channelDos) {
        List<Channel> channels = new ArrayList<Channel>();
        try {
            // 1.将ChannelID单独拿出来
            List<Long> channelIds = new ArrayList<Long>();
            for (ChannelDO channelDo : channelDos) {
                channelIds.add(channelDo.getId());
            }
            Long[] idArray = new Long[channelIds.size()];

            // 拿到所有的Pipeline进行ChannelID过滤，避免重复查询。
            List<Pipeline> pipelines = pipelineService.listByChannelIds(channelIds.toArray(idArray));
            SystemParameter systemParameter = systemParameterService.find();
            for (ChannelDO channelDo : channelDos) {
                Channel channel = new Channel();
                channel.setId(channelDo.getId());
                channel.setName(channelDo.getName());
                channel.setDescription(channelDo.getDescription());
                ChannelStatus channelStatus = arbitrateManageService.channelEvent().status(channelDo.getId());
                channel.setStatus(null == channelStatus ? ChannelStatus.STOP : channelStatus);
                channel.setParameters(channelDo.getParameters());
                channel.setGmtCreate(channelDo.getGmtCreate());
                channel.setGmtModified(channelDo.getGmtModified());
                // 遍历，将该Channel节点下的Pipeline提取出来。
                List<Pipeline> subPipelines = new ArrayList<Pipeline>();
                for (Pipeline pipeline : pipelines) {
                    if (pipeline.getChannelId().equals(channelDo.getId())) {
                        // 合并PipelineParameter和ChannelParameter
                        PipelineParameter parameter = new PipelineParameter();
                        parameter.merge(systemParameter);
                        parameter.merge(channel.getParameters());
                        // 最后复制pipelineId参数
                        parameter.merge(pipeline.getParameters());
                        pipeline.setParameters(parameter);
                        subPipelines.add(pipeline);
                    }
                }

                channel.setPipelines(subPipelines);
                channels.add(channel);
            }
        } catch (Exception e) {
            log.error("ERROR ## change the channels DO to Model has an exception");
            throw new ManagerException(e);
        }

        return channels;
    }
    private Channel doToModel(ChannelDO channelDo) {
        Channel channel = new Channel();
        try {
            channel.setId(channelDo.getId());
            channel.setName(channelDo.getName());
            channel.setDescription(channelDo.getDescription());
            channel.setStatus(arbitrateManageService.channelEvent().status(channelDo.getId()));
            channel.setParameters(channelDo.getParameters());
            channel.setGmtCreate(channelDo.getGmtCreate());
            channel.setGmtModified(channelDo.getGmtModified());
            List<Pipeline> pipelines = pipelineService.listByChannelIds(channelDo.getId());
            // 合并PipelineParameter和ChannelParameter
            SystemParameter systemParameter = systemParameterService.find();
            for (Pipeline pipeline : pipelines) {
                PipelineParameter parameter = new PipelineParameter();
                parameter.merge(systemParameter);
                parameter.merge(channel.getParameters());
                // 最后复制pipelineId参数
                parameter.merge(pipeline.getParameters());
                pipeline.setParameters(parameter);
                // pipeline.getParameters().merge(channel.getParameters());
            }
            channel.setPipelines(pipelines);
        } catch (Exception e) {
            log.error("ERROR ## change the channel DO to Model has an exception");
            throw new ManagerException(e);
        }

        return channel;
    }
    @Override
    public List<Channel> listAll() {
        return listByIds();
    }

    @Override
    public List<Channel> listByCondition(Map condition) {
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
    public Channel findByPipelineId(Long pipelineId) {
        Pipeline pipeline = pipelineService.findById(pipelineId);
        Channel channel = findById(pipeline.getChannelId());
        return channel;
    }

    @Override
    public Channel findByIdWithoutColumn(Long pipelineId) {
        return null;
    }

    @Override
    public List<Channel> listByPipelineIds(Long... pipelineIds) {
        return null;
    }

    @Override
    public List<Channel> listByNodeId(Long nodeId) {
        return null;
    }

    @Override
    public List<Channel> listOnlyChannels(Long... identities) {
        return null;
    }

    @Override
    public List<Long> listAllChannelId() {
        List<ChannelDO> channelDos = sqlSession.selectList("listChannelPks");
                //channelDao.listChannelPks();
        List<Long> channelPks = new ArrayList<Long>();
        if (channelDos.isEmpty()) {
            log.debug("DEBUG ## couldn't query any channel");
        }
        for (ChannelDO channelDo : channelDos) {
            channelPks.add(channelDo.getId());
        }
        return channelPks;
    }

    @Override
    public List<Channel> listByNodeId(Long nodeId, ChannelStatus... status) {
        return null;
    }

    @Override
    public Page<Channel> listByConditionWithoutColumn(Map condition) {
        PageHelper.startPage((int)condition.get("currentPage"),(int)condition.get("pageSize"));
        List<ChannelDO> channelDOs = sqlSession.selectList("listChannelByCondtion",condition);
        if(CollectionUtils.isEmpty(channelDOs)){
            log.debug("DEBUG ## couldn't query any channel by the condition:" + JsonUtils.marshalToString(condition));
            return new Page<Channel>();
        }
        List<Channel> channels =  doToModelWithColumn(channelDOs);
        Page<Channel> page = new Page<Channel>();
        Page<Channel> pageTemp = (Page)channelDOs;
        page.addAll(channels);
        page.setPageSize(pageTemp.getPageSize());
        page.setPageNum(pageTemp.getPageNum());
        page.setTotal(pageTemp.getTotal());
        return page;
    }
    private List<Channel> doToModelWithColumn(List<ChannelDO> channelDos) {
        List<Channel> channels = new ArrayList<Channel>();
        try {
            // 1.将ChannelID单独拿出来
            List<Long> channelIds = new ArrayList<Long>();
            for (ChannelDO channelDo : channelDos) {
                channelIds.add(channelDo.getId());
            }
            Long[] idArray = new Long[channelIds.size()];

            // 拿到所有的Pipeline进行ChannelID过滤，避免重复查询。
            List<Pipeline> pipelines = pipelineService.listByChannelIdsWithoutColumn(channelIds.toArray(idArray));
            SystemParameter systemParameter = systemParameterService.find();
            for (ChannelDO channelDo : channelDos) {
                Channel channel = new Channel();
                channel.setId(channelDo.getId());
                channel.setName(channelDo.getName());
                channel.setDescription(channelDo.getDescription());
                ChannelStatus channelStatus = arbitrateManageService.channelEvent().status(channelDo.getId());
                channel.setStatus(null == channelStatus ? ChannelStatus.STOP : channelStatus);
                channel.setParameters(channelDo.getParameters());
                channel.setGmtCreate(channelDo.getGmtCreate());
                channel.setGmtModified(channelDo.getGmtModified());
                // 遍历，将该Channel节点下的Pipeline提取出来。
                List<Pipeline> subPipelines = new ArrayList<Pipeline>();
                for (Pipeline pipeline : pipelines) {
                    if (pipeline.getChannelId().equals(channelDo.getId())) {
                        // 合并PipelineParameter和ChannelParameter
                        PipelineParameter parameter = new PipelineParameter();
                        parameter.merge(systemParameter);
                        parameter.merge(channel.getParameters());
                        // 最后复制pipelineId参数
                        parameter.merge(pipeline.getParameters());
                        pipeline.setParameters(parameter);
                        subPipelines.add(pipeline);
                    }
                }

                channel.setPipelines(subPipelines);
                channels.add(channel);
            }
        } catch (Exception e) {
            log.error("ERROR ## change the channels DO to Model has an exception");
            throw new ManagerException(e);
        }

        return channels;
    }

    @Override
    public void stopChannel(Long channelId) {
        switchChannelStatus(channelId, ChannelStatus.STOP);
    }

    @Override
    public void notifyChannel(Long channelId) {
        switchChannelStatus(channelId, null);
    }

    @Override
    public void startChannel(Long channelId) {
        switchChannelStatus(channelId, ChannelStatus.START);
    }

    /**
     * <pre>
     * 切换Channel状态
     *      1.首先判断Channel是否为空或状态位是否正确
     *      2.通知总裁器，更新节点
     *      3.数据库数据库更新状态
     *      4.调用远程方法，推送Channel到node节点
     * </pre>
     */
    private void switchChannelStatus(final Long channelId, final ChannelStatus channelStatus) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    final ChannelDO channelDo =  channelDOMapperExt.selectByPrimaryKey(channelId);
//                            channelDao.findById(channelId);
                    if (null == channelDo) {
                        String exceptionCause = "query channelId:" + channelId + " return null.";
                        log.error("ERROR ## " + exceptionCause);
                        throw new ManagerException(exceptionCause);
                    }

                    ChannelStatus oldStatus = arbitrateManageService.channelEvent().status(channelDo.getId());
                    Channel channel = doToModel(channelDo);
                    // 检查下ddl/home配置
                    List<Pipeline> pipelines = channel.getPipelines();
                    if (pipelines.size() > 1) {
                        boolean ddlSync = true;
                        boolean homeSync = true;
                        for (Pipeline pipeline : pipelines) {
                            homeSync &= pipeline.getParameters().isHome();
                            ddlSync &= pipeline.getParameters().getDdlSync();
                        }

                        if (ddlSync) {
                            throw new InvalidConfigureException(InvalidConfigureException.INVALID_TYPE.DDL);
                        }

                        if (homeSync) {
                            throw new InvalidConfigureException(InvalidConfigureException.INVALID_TYPE.HOME);
                        }
                    }
                    channel.setStatus(oldStatus);
                    ChannelStatus newStatus = channelStatus;
                    if (newStatus != null) {
                        if (newStatus.equals(oldStatus)) {
                            // String exceptionCause = "switch the channel(" +
                            // channelId + ") status to " +
                            // channelStatus
                            // + " but it had the status:" + oldStatus;
                            // logger.error("ERROR ## " + exceptionCause);
                            // throw new ManagerException(exceptionCause);
                            // ignored
                            return;
                        } else {
                            channel.setStatus(newStatus);// 强制修改为当前变更状态
                        }
                    } else {
                        newStatus = oldStatus;
                    }

                    // 针对关闭操作，要优先更改对应的status，避免node工作线程继续往下跑
                    if (newStatus.isStop()) {
                        arbitrateManageService.channelEvent().stop(channelId);
                    } else if (newStatus.isPause()) {
                        arbitrateManageService.channelEvent().pause(channelId);
                    }

                    // 通知变更内容
                    boolean result = configRemoteService.notifyChannel(channel);// 客户端响应成功，才更改对应的状态

                    if (result) {
                        // 针对启动的话，需要先通知到客户端，客户端启动线程后，再更改channel状态
                        if (newStatus.isStart()) {
                            arbitrateManageService.channelEvent().start(channelId);
                        }
                    }

                } catch (Exception e) {
                    log.error("ERROR ## switch the channel(" + channelId + ") status has an exception.");
                    throw new ManagerException(e);
                }
            }
        });

    }
}
