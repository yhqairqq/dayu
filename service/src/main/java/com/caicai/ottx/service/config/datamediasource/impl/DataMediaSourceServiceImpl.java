package com.caicai.ottx.service.config.datamediasource.impl;

import com.alibaba.otter.shared.common.model.config.data.DataMediaSource;
import com.alibaba.otter.shared.common.model.config.data.db.DbMediaSource;
import com.alibaba.otter.shared.common.model.config.data.mq.MqMediaSource;
import com.alibaba.otter.shared.common.utils.Assert;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import com.caicai.ottx.dal.entity.DataMediaSourceDO;
import com.caicai.ottx.dal.mapper.DataMediaSourceDOMapperExt;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.common.exceptions.RepeatConfigureException;
import com.caicai.ottx.service.config.datamediasource.DataMediaSourceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/28.
 */
@Service
@Slf4j
public class DataMediaSourceServiceImpl implements DataMediaSourceService{
    @Autowired
    private SqlSession sqlSession;


    @Autowired
    private DataMediaSourceDOMapperExt dataMediaSourceDOMapperExt;


    private boolean checkUnique(DataMediaSourceDO dataMediaSourceDo){
        int count =  sqlSession.selectOne("checkDataMediaSourceUnique",dataMediaSourceDo);
        if(count > 0){
            String exceptionCause = "exist the same name source in the database.";
            log.warn("WARN ## " + exceptionCause);
            throw new RepeatConfigureException(exceptionCause);
        }
        return true;
    }

    @Override
    public void create(DataMediaSource entityObj) {
        Assert.assertNotNull(entityObj);
        try{
            DataMediaSourceDO dataMediaSourceDo = modelToDo(entityObj);
            checkUnique(dataMediaSourceDo);
            dataMediaSourceDOMapperExt.insertSelective(dataMediaSourceDo);

        } catch (RepeatConfigureException rce) {
            throw rce;
        }catch (Exception e){
            e.printStackTrace();
            log.error("ERROR ## create dataMediaSource has an exception!");
            throw new ManagerException(e);
        }
    }
    private DataMediaSourceDO modelToDo(DataMediaSource dataMediaSource) {
        DataMediaSourceDO dataMediaSourceDo = new DataMediaSourceDO();
        try {
            dataMediaSourceDo.setId(dataMediaSource.getId());
            dataMediaSourceDo.setName(dataMediaSource.getName());
            dataMediaSourceDo.setType(dataMediaSource.getType());
            if (dataMediaSource instanceof DbMediaSource) {
                dataMediaSourceDo.setProperties(JsonUtils.marshalToString((DbMediaSource) dataMediaSource));
            } else if (dataMediaSource instanceof MqMediaSource) {
                dataMediaSourceDo.setProperties(JsonUtils.marshalToString((MqMediaSource) dataMediaSource));
            }

            dataMediaSourceDo.setGmtCreate(dataMediaSource.getGmtCreate());
            dataMediaSourceDo.setGmtModified(dataMediaSource.getGmtModified());
        } catch (Exception e) {
            log.error("ERROR ## change the dataMediaSource Model to Do has an exception");
            throw new ManagerException(e);
        }

        return dataMediaSourceDo;
    }

    @Override
    public void remove(Long dataMediaSourceId) {
        Assert.assertNotNull(dataMediaSourceId);
        try {
            dataMediaSourceDOMapperExt.deleteByPrimaryKey(dataMediaSourceId);
        } catch (Exception e) {
            log.error("ERROR ## remove dataMediaSource has an exception!");
            throw new ManagerException(e);
        }

    }

    @Override
    public void modify(DataMediaSource dataMediaSource) {
        Assert.assertNotNull(dataMediaSource);
        try{
           DataMediaSourceDO dataMediaSourceDo = modelToDo(dataMediaSource);
            if(checkUnique(dataMediaSourceDo)){
                dataMediaSourceDOMapperExt.updateByPrimaryKeySelective(dataMediaSourceDo);
            }
        }catch (RepeatConfigureException rce) {
            throw rce;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ERROR ## modify dataMediaSource has an exception!");
            throw new ManagerException(e);
        }
    }

    @Override
    public DataMediaSource findById(Long dataMediaSourceId) {
        Assert.assertNotNull(dataMediaSourceId);
        List<DataMediaSource> dataMediaSources = listByIds(dataMediaSourceId);
        if(!CollectionUtils.isEmpty(dataMediaSources)){
            if(dataMediaSources.size() != 1){
                String exceptionCause = "query dataMediaSourceId:" + dataMediaSourceId + " but return "
                        + dataMediaSources.size() + " dataMediaSource.";
                log.error("ERROR ## " + exceptionCause);
                throw new ManagerException(exceptionCause);
            }
        }
        return dataMediaSources.get(0);
    }

    @Override
    public List<DataMediaSource> listByIds(Long... identities) {
        List<DataMediaSourceDO> dataMediaSourceDos = null;
        if (identities.length < 1) {
            dataMediaSourceDos =  sqlSession.selectList("listDataMediaSources");
            if (CollectionUtils.isEmpty(dataMediaSourceDos)) {
                log.debug("DEBUG ## couldn't query any dataMediaSource, maybe hasn't create any dataMediaSource.");
                return new ArrayList<DataMediaSource>();
            }
        } else {
            dataMediaSourceDos = sqlSession.selectList("listSourceByIds",identities);
            if (CollectionUtils.isEmpty(dataMediaSourceDos)) {
                String exceptionCause = "couldn't query any dataMediaSource by dataMediaSourceIds:"
                        + Arrays.toString(identities);
                log.error("ERROR ## " + exceptionCause);
                throw new ManagerException(exceptionCause);
            }
        }

        return doToModel(dataMediaSourceDos);
    }
    private List<DataMediaSource> doToModel(List<DataMediaSourceDO> dataMediaSourceDos) {
        List<DataMediaSource> dataMediaSources = new ArrayList<DataMediaSource>();
        for (DataMediaSourceDO dataMediaSourceDo : dataMediaSourceDos) {
            dataMediaSources.add(doToModel(dataMediaSourceDo));
        }
        return dataMediaSources;
    }
    private DataMediaSource doToModel(DataMediaSourceDO dataMediaSourceDo) {

        DataMediaSource dataMediaSource = new DbMediaSource();
        try {
            if (dataMediaSourceDo.getType().isMysql() || dataMediaSourceDo.getType().isOracle()) {
                dataMediaSource = JsonUtils.unmarshalFromString(dataMediaSourceDo.getProperties(), DbMediaSource.class);
            } else if (dataMediaSourceDo.getType().isNapoli() || dataMediaSourceDo.getType().isKafka() || dataMediaSourceDo.getType().isRocketMq()) {
                dataMediaSource = JsonUtils.unmarshalFromString(dataMediaSourceDo.getProperties(), MqMediaSource.class);
            }
            dataMediaSource.setId(dataMediaSourceDo.getId());
            dataMediaSource.setGmtCreate(dataMediaSourceDo.getGmtCreate());
            dataMediaSource.setGmtModified(dataMediaSourceDo.getGmtModified());
        } catch (Exception e) {
            log.error("ERROR ## change the dataMediaSource Do to Model has an exception");
            throw new ManagerException(e);
        }
        return dataMediaSource;
    }

    @Override
    public List<DataMediaSource> listAll() {
        return listByIds();
    }

    @Override
    public List<DataMediaSource> listByCondition(Map condition) {
        PageHelper.startPage((int)condition.get("current"),(int)condition.get("pageSize"));
        List<DataMediaSource> dataMediaSources = new ArrayList<DataMediaSource>();
        try {
            List<DataMediaSourceDO> dataMediaSourceDos = sqlSession.selectList("listDataMediaSources",condition);
                    //dataMediaSourceDao.listByCondition(condition);
            if (CollectionUtils.isEmpty(dataMediaSourceDos)) {
                log.debug("DEBUG ## couldn't query any DataMediaSources by the condition:"
                        + JsonUtils.marshalToString(condition));
                return new Page<DataMediaSource>();
            }
            dataMediaSources = doToModel(dataMediaSourceDos);
            Page<DataMediaSource> page = new Page<DataMediaSource>();
            Page<DataMediaSource> pageTemp = (Page)dataMediaSourceDos;
            page.addAll(dataMediaSources);
            page.setPageSize(pageTemp.getPageSize());
            page.setPageNum(pageTemp.getPageNum());
            page.setTotal(pageTemp.getTotal());
            return page;
        } catch (Exception e) {
            log.error("ERROR ## query DataMediaSources by condition has an exception!");
            throw new ManagerException(e);
        }
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
