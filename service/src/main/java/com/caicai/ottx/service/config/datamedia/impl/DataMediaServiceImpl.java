package com.caicai.ottx.service.config.datamedia.impl;

import com.alibaba.otter.manager.biz.common.exceptions.RepeatConfigureException;
import com.alibaba.otter.shared.common.model.config.data.DataMedia;
import com.alibaba.otter.shared.common.model.config.data.DataMediaSource;
import com.alibaba.otter.shared.common.model.config.data.db.DbDataMedia;
import com.alibaba.otter.shared.common.model.config.data.mq.MqDataMedia;
import com.alibaba.otter.shared.common.utils.Assert;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import com.caicai.ottx.dal.entity.DataMediaDO;
import com.caicai.ottx.dal.mapper.DataMediaDOMapperExt;
import com.caicai.ottx.service.common.DataSourceCreator;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.config.datamedia.DataMediaService;
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
public class DataMediaServiceImpl implements DataMediaService {

    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private DataMediaSourceService dataMediaSourceService;
    @Autowired
    private DataSourceCreator dataSourceCreator;

    @Autowired
    private DataMediaDOMapperExt dataMediaDOMapperExt;


    @Override
    public List<DataMedia> listByDataMediaSourceId(Long dataMediaSourceId) {
        Assert.assertNotNull(dataMediaSourceId);
        List<DataMediaDO> dataMediaDos = null;
        try {
            dataMediaDos = sqlSession.selectList("listDataMediasByDataMediaSourceId",dataMediaSourceId);
                    //dataMediaDao.listByDataMediaSourceId(dataMediaSourceId);
            if (CollectionUtils.isEmpty(dataMediaDos)) {
                log.debug("DEBUG ## couldn't query any dataMedia, maybe hasn't create any dataMedia.");
                return new ArrayList<DataMedia>();
            }
        } catch (Exception e) {
            log.error("ERROR ## query dataMedias by sourceId:" + dataMediaSourceId + " has an exception!");
            throw new ManagerException(e);
        }
        return doToModel(dataMediaDos);

    }

    @Override
    public Long createReturnId(DataMedia dataMedia) {
        Assert.assertNotNull(dataMedia);
        try {
            DataMediaDO dataMediaDo = modelToDo(dataMedia);
//            if (!dataMediaDao.checkUnique(dataMediaDo)) {
//                String exceptionCause = "exist the same name dataMedia in the database.";
//                logger.warn("WARN ## " + exceptionCause);
//                throw new RepeatConfigureException(exceptionCause);
//            }
            dataMediaDOMapperExt.insertSelective(dataMediaDo);
            return dataMediaDo.getId();
//            dataMediaDao.insert(dataMediaDo);
        } catch (RepeatConfigureException rce) {
            throw rce;
        } catch (Exception e) {
            log.error("ERROR ## create dataMedia has an exception!");
            throw new ManagerException(e);
        }
    }

    @Override
    public List<String> queryColumnByMedia(DataMedia dataMedia) {
        return null;
    }

    @Override
    public List<String> queryColumnByMediaId(Long dataMediaId) {
        return null;
    }



    @Override
    public void create(DataMedia dataMedia) {
        Assert.assertNotNull(dataMedia);
        try {
          DataMediaDO dataMediaDo = modelToDo(dataMedia);
            dataMediaDo.setId(0L);
//            if (!dataMediaDao.checkUnique(dataMediaDo)) {
//                String exceptionCause = "exist the same name dataMedia in the database.";
//                logger.warn("WARN ## " + exceptionCause);
//                throw new RepeatConfigureException(exceptionCause);
//            }
            dataMediaDOMapperExt.insertSelective(dataMediaDo);
//            dataMediaDao.insert(dataMediaDo);
        } catch (RepeatConfigureException rce) {
            throw rce;
        } catch (Exception e) {
            log.error("ERROR ## create dataMedia has an exception!");
            throw new ManagerException(e);
        }
    }

    private DataMediaDO modelToDo(DataMedia dataMedia) {

        DataMediaDO dataMediaDo = new DataMediaDO();

        try {

            dataMediaDo.setId(dataMedia.getId());
            dataMediaDo.setName(dataMedia.getName());
            dataMediaDo.setNamespace(dataMedia.getNamespace());
            dataMediaDo.setDataMediaSourceId(dataMedia.getSource().getId());
            // if (dataMedia instanceof DbDataMedia) {
            // dataMediaDo.setProperties(JsonUtils.marshalToString((DbDataMedia) dataMedia));
            // }
            dataMediaDo.setTopic(dataMedia.getTopic());
            dataMediaDo.setProperties(JsonUtils.marshalToString(dataMedia));
            dataMediaDo.setGmtCreate(dataMedia.getGmtCreate());
            dataMediaDo.setGmtModified(dataMedia.getGmtModified());
        } catch (Exception e) {
            log.error("ERROR ## change the dataMedia Model to Do has an exception");
            throw new ManagerException(e);
        }

        return dataMediaDo;
    }

    @Override
    public void remove(Long identity) {
        dataMediaDOMapperExt.deleteByPrimaryKey(identity);
    }

    @Override
    public void modify(DataMedia dataMedia) {
        Assert.assertNotNull(dataMedia);
        try {
           DataMediaDO dataMediaDo = modelToDo(dataMedia);
//            if (dataMediaDao.checkUnique(dataMediaDo)) {
//                dataMediaDao.update(dataMediaDo);
//            } else {
//                String exceptionCause = "exist the same name dataMedia in the database.";
//                logger.warn("WARN ## " + exceptionCause);
//                throw new RepeatConfigureException(exceptionCause);
//            }
            dataMediaDOMapperExt.updateByPrimaryKeySelective(dataMediaDo);
        } catch (RepeatConfigureException rce) {
            throw rce;
        } catch (Exception e) {
            log.error("ERROR ## modify dataMedia has an exception!");
            throw new ManagerException(e);
        }
    }

    @Override
    public DataMedia findById(Long identity) {
        return doToModel(dataMediaDOMapperExt.selectByPrimaryKey(identity));
    }

    @Override
    public List<DataMedia> listByIds(Long... identities) {

        List<DataMedia> dataMedias = new ArrayList<>();
        try {
            List<DataMediaDO> dataMediaDOs = null;
            if(identities.length < 1){
                dataMediaDOs = sqlSession.selectList("listDataMedias");
                if(CollectionUtils.isEmpty(dataMediaDOs)){
                    log.debug("DEBUG ## couldn't query any dataMedia, maybe hasn't create any dataMedia.");
                    return dataMedias;
                }
            }else{
                dataMediaDOs = sqlSession.selectList("listDataMediaByIds",identities);
                if(CollectionUtils.isEmpty(dataMediaDOs)) {
                    String exceptionCause = "couldn't query any dataMedia by dataMediaIds:" + Arrays.toString(identities);
                    log.error("ERROR ## " + exceptionCause);
                    throw new ManagerException(exceptionCause);
                }
            }
            dataMedias = doToModel(dataMediaDOs);
        } catch (Exception e) {

        }
        return dataMedias;
    }
    private List<DataMedia> doToModel(List<DataMediaDO> dataMediaDos) {
        List<DataMedia> dataMedias = new ArrayList<DataMedia>();
        for (DataMediaDO dataMediaDo : dataMediaDos) {
            dataMedias.add(doToModel(dataMediaDo));
        }

        return dataMedias;
    }

    /**
     * 用于DO对象转化为Model对象
     *
     * @param dataMediaDo
     * @return DataMedia
     */
    private DataMedia doToModel(DataMediaDO dataMediaDo) {
        DataMedia dataMedia = null;
        try {
            DataMediaSource dataMediaSource = dataMediaSourceService.findById(dataMediaDo.getDataMediaSourceId());
            if (dataMediaSource.getType().isMysql() || dataMediaSource.getType().isOracle()) {
                dataMedia = JsonUtils.unmarshalFromString(dataMediaDo.getProperties(), DbDataMedia.class);
                dataMedia.setSource(dataMediaSource);
            } else if (dataMediaSource.getType().isNapoli() || dataMediaSource.getType().isKafka() || dataMediaSource.getType().isRocketMq()) {
                dataMedia = JsonUtils.unmarshalFromString(dataMediaDo.getProperties(), MqDataMedia.class);
                dataMedia.setSource(dataMediaSource);
            }

            dataMedia.setId(dataMediaDo.getId());
            dataMedia.setGmtCreate(dataMediaDo.getGmtCreate());
            dataMedia.setGmtModified(dataMediaDo.getGmtModified());

        } catch (Exception e) {
            log.error("ERROR ## change the dataMedia Do to Model has an exception");
            throw new ManagerException(e);
        }

        return dataMedia;
    }

    @Override
    public List<DataMedia> listAll() {
        return listByIds();
    }

    @Override
    public List<DataMedia> listByCondition(Map condition) {
        PageHelper.startPage((int)condition.get("currentPage"),(int)condition.get("pageSize"));
        List<DataMedia> dataMedias = new ArrayList<DataMedia>();
        try {
            List<DataMediaDO> dataMediaDos = sqlSession.selectList("listDataMedias",condition);
//                    dataMediaDao.listByCondition(condition);
            if (CollectionUtils.isEmpty(dataMediaDos)) {
                log.debug("DEBUG ## couldn't query any dataMedias by the condition:"
                        + JsonUtils.marshalToString(condition));
                return new Page<DataMedia>();
            }
            dataMedias = doToModel(dataMediaDos);
            Page<DataMedia> page = new Page<DataMedia>();
            Page<DataMedia> pageTemp = (Page)dataMediaDos;
            page.addAll(dataMedias);
            page.setPageSize(pageTemp.getPageSize());
            page.setPageNum(pageTemp.getPageNum());
            page.setTotal(pageTemp.getTotal());
            return page;
        } catch (Exception e) {
            log.error("ERROR ## query dataMedias by condition has an exception!");
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
