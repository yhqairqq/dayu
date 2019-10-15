package com.caicai.ottx.service.config.datamediapair.impl;

import com.alibaba.otter.manager.biz.common.exceptions.RepeatConfigureException;
import com.alibaba.otter.shared.common.model.config.data.*;
import com.alibaba.otter.shared.common.utils.Assert;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import com.caicai.ottx.dal.entity.DataMediaPairDO;
import com.caicai.ottx.dal.mapper.DataMediaPairDOMapperExt;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.config.datacolumnpair.DataColumnPairGroupService;
import com.caicai.ottx.service.config.datacolumnpair.DataColumnPairService;
import com.caicai.ottx.service.config.datamedia.DataMediaService;
import com.caicai.ottx.service.config.datamediapair.DataMediaPairService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/28.
 */
@Service
@Slf4j
public class DataMediaPairServiceImpl implements DataMediaPairService{

    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private DataMediaService dataMediaService;
    @Autowired
    private DataColumnPairService dataColumnPairService;
    @Autowired
    private DataColumnPairGroupService dataColumnPairGroupService;
    @Autowired
    private DataMediaPairDOMapperExt dataMediaPairDOMapperExt ;


    /**
     * 根据pipeline iD找到该树干下的所有的DataMediaPairs
     * @param pipelineId
     * @return
     */
    @Override
    public List<DataMediaPair> listByPipelineId(Long pipelineId) {
        Assert.assertNotNull(pipelineId);
        List<DataMediaPair> dataMediaPairs = new ArrayList<>();
        try{
            List<DataMediaPairDO> dataMediaPairDOS = sqlSession.selectList("listDataMediaPairsByPipelineId",pipelineId
            );
            if(CollectionUtils.isEmpty(dataMediaPairDOS)){
                log.debug("DEBUG ##couldn't query any dataMediaPair");
                return dataMediaPairs;
            }
            dataMediaPairs = doToModel(dataMediaPairDOS);
        }catch (Exception e){
            log.error("ERROR ## query dataMediaPairs by pipelineId:" + pipelineId + " has an exception!", e);
            throw new ManagerException(e);
        }
        return dataMediaPairs;
    }
    private List<DataMediaPair> doToModel(List<DataMediaPairDO> dataMediaPairDos) {
        List<Long> dataMediaPairIds = new ArrayList<Long>();
        for (DataMediaPairDO dataMediaPairDo : dataMediaPairDos) {
            dataMediaPairIds.add(dataMediaPairDo.getId());
        }
        Map<Long, List<ColumnPair>> columnPairMap = dataColumnPairService.listByDataMediaPairIds(dataMediaPairIds.toArray(new Long[dataMediaPairIds.size()]));
        Map<Long, List<ColumnGroup>> columnPairGroupMap = dataColumnPairGroupService.listByDataMediaPairIds(dataMediaPairIds.toArray(new Long[dataMediaPairIds.size()]));
        List<DataMediaPair> dataMediaPairs = new ArrayList<DataMediaPair>();
        for (DataMediaPairDO dataMediaPairDo : dataMediaPairDos) {
            List<ColumnPair> columnPairs = columnPairMap.get(dataMediaPairDo.getId()) == null ? new ArrayList<ColumnPair>() : columnPairMap.get(dataMediaPairDo.getId());
            List<ColumnGroup> columnGroups = columnPairGroupMap.get(dataMediaPairDo.getId()) == null ? new ArrayList<ColumnGroup>() : columnPairGroupMap.get(dataMediaPairDo.getId());
            dataMediaPairs.add(doToModel(dataMediaPairDo, columnPairs, columnGroups));
        }

        return dataMediaPairs;
    }


    private DataMediaPair doToModel(DataMediaPairDO dataMediaPairDo, List<ColumnPair> columnPairs,
                                    List<ColumnGroup> columnGroups) {
        DataMediaPair dataMediaPair = new DataMediaPair();
        try {
            dataMediaPair.setId(dataMediaPairDo.getId());
            dataMediaPair.setPipelineId(dataMediaPairDo.getPipelineId());
            dataMediaPair.setPullWeight(dataMediaPairDo.getPullWeight());
            dataMediaPair.setPushWeight(dataMediaPairDo.getPushWeight());
            if (org.apache.commons.lang.StringUtils.isNotBlank(dataMediaPairDo.getFilter())) {
                dataMediaPair.setFilterData(JsonUtils.unmarshalFromString(dataMediaPairDo.getFilter(),
                        ExtensionData.class));
            }

            if (org.apache.commons.lang.StringUtils.isNotBlank(dataMediaPairDo.getResolver())) {
                dataMediaPair.setResolverData(JsonUtils.unmarshalFromString(dataMediaPairDo.getResolver(),
                        ExtensionData.class));
            }
            dataMediaPair.setColumnPairs(columnPairs);
            dataMediaPair.setColumnGroups(columnGroups);
            dataMediaPair.setColumnPairMode(dataMediaPairDo.getColumnPairMode());
            dataMediaPair.setGmtCreate(dataMediaPairDo.getGmtCreate());
            dataMediaPair.setGmtModified(dataMediaPairDo.getGmtModified());

            // 组装DataMedia
            List<DataMedia> dataMedias = dataMediaService.listByIds(dataMediaPairDo.getSourceDataMediaId(),
                    dataMediaPairDo.getTargetDataMediaId());
            if (null == dataMedias || dataMedias.size() != 2) {
                // 抛出异常
                return dataMediaPair;
            }

            for (DataMedia dataMedia : dataMedias) {
                if (dataMedia.getId().equals(dataMediaPairDo.getSourceDataMediaId())) {
                    dataMediaPair.setSource(dataMedia);
                } else if (dataMedia.getId().equals(dataMediaPairDo.getTargetDataMediaId())) {
                    dataMediaPair.setTarget(dataMedia);
                }
            }
        } catch (Exception e) {
            log.error("ERROR ## change the dataMediaPair Do to Model has an exception", e);
            throw new ManagerException(e);
        }

        return dataMediaPair;
    }

    @Override
    public List<DataMediaPair> listByPipelineIdWithoutColumn(Long pipelineId) {
        Assert.assertNotNull(pipelineId);
        List<DataMediaPair> dataMediaPairs = new ArrayList<>();
        try{
            List<DataMediaPairDO> dataMediaPairDos = sqlSession.selectList("listDataMediaPairsByPipelineId",pipelineId);
            if(CollectionUtils.isEmpty(dataMediaPairDos)){
                log.debug("DEBUG ## couldn't query any dataMediaPair, maybe hasn't create any dataMediaPair.");
                return dataMediaPairs;
            }
            dataMediaPairs = doToModelWithoutOther(dataMediaPairDos);
        }catch (Exception e){
            log.error("ERROR ## query dataMediaPairs by pipelineId:" + pipelineId + " has an exception!", e);
            throw new ManagerException(e);
        }
        return dataMediaPairs;
    }
    private List<DataMediaPair> doToModelWithoutOther(List<DataMediaPairDO> dataMediaPairDos) {
        List<DataMediaPair> dataMediaPairs = new ArrayList<DataMediaPair>();
        for (DataMediaPairDO dataMediaPairDo : dataMediaPairDos) {
            dataMediaPairs.add(doToModel(dataMediaPairDo, null, null));
        }

        return dataMediaPairs;
    }

    @Override
    public List<DataMediaPair> listByDataMediaId(Long dataMediaId) {
        Assert.assertNotNull(dataMediaId);
        List<DataMediaPair> dataMediaPairs = new ArrayList<DataMediaPair>();
        try {
            List<DataMediaPairDO> dataMediaPairDos = sqlSession.selectList("listDataMediaPairsByDataMediaId",dataMediaId);
                    //dataMediaPairDao.listByDataMediaId(dataMediaId);
            if (dataMediaPairDos.isEmpty()) {
                log.debug("DEBUG ## couldn't query any dataMediaPair, maybe hasn't create any dataMediaPair.");
                return dataMediaPairs;
            }
            dataMediaPairs = doToModel(dataMediaPairDos);
        } catch (Exception e) {
            log.error("ERROR ## query dataMediaPairs by dataMediaId:" + dataMediaId + " has an exception!", e);
            throw new ManagerException(e);
        }

        return dataMediaPairs;
    }

    private boolean checkUnique(DataMediaPairDO dataMediaPairDo){
        int count = sqlSession.selectOne("checkDataMediaPairUnique",dataMediaPairDo);
//                (Integer) getSqlMapClientTemplate().queryForObject("checkDataMediaPairUnique", dataMediaPair);
        return count == 0 ? true : false;
    }

    @Override
    public Long createAndReturnId(DataMediaPair dataMediaPair) {
        Assert.assertNotNull(dataMediaPair);
        try {
            DataMediaPairDO dataMediaPairDo = modelToDo(dataMediaPair);
            dataMediaPairDo.setId(0L);
            if (!checkUnique(dataMediaPairDo)) {
                String exceptionCause = "exist the same pair in the database.";
                log.warn("WARN ## " + exceptionCause);
                throw new RepeatConfigureException(exceptionCause);
            }
            dataMediaPairDOMapperExt.insertSelective(dataMediaPairDo);
//            dataMediaPairDao.insert(dataMediaPairDo);
            return dataMediaPairDo.getId();
        } catch (RepeatConfigureException rcf) {
            throw rcf;
        } catch (Exception e) {
            log.error("ERROR ## create dataMediaPair has an exception!", e);
            throw new ManagerException(e);
        }
    }
    private DataMediaPairDO modelToDo(DataMediaPair dataMediaPair) {
        DataMediaPairDO dataMediaPairDo = new DataMediaPairDO();
        try {
            dataMediaPairDo.setId(dataMediaPair.getId());
            dataMediaPairDo.setPipelineId(dataMediaPair.getPipelineId());
            dataMediaPairDo.setSourceDataMediaId(dataMediaPair.getSource().getId());
            dataMediaPairDo.setTargetDataMediaId(dataMediaPair.getTarget().getId());
            dataMediaPairDo.setFilter(JsonUtils.marshalToString(dataMediaPair.getFilterData()));
            dataMediaPairDo.setResolver(JsonUtils.marshalToString(dataMediaPair.getResolverData()));
            dataMediaPairDo.setPullWeight(dataMediaPair.getPullWeight());
            dataMediaPairDo.setPushWeight(dataMediaPair.getPushWeight());
            dataMediaPairDo.setColumnPairMode(dataMediaPair.getColumnPairMode());
            dataMediaPairDo.setGmtCreate(dataMediaPair.getGmtCreate());
            dataMediaPairDo.setGmtModified(dataMediaPair.getGmtModified());
        } catch (Exception e) {
            log.error("ERROR ## change the dataMediaPair Model to Do has an exception", e);
            throw new ManagerException(e);
        }

        return dataMediaPairDo;
    }

    @Override
    public boolean createIfNotExist(DataMediaPair dataMediaPair) {
        return false;
    }

    @Override
    public void create(DataMediaPair entityObj) {

    }

    @Override
    public void remove(Long identity) {
        dataMediaPairDOMapperExt.deleteByPrimaryKey(identity);
    }

    @Override
    public void modify(DataMediaPair dataMediaPair) {
        DataMediaPairDO dataMediaPairDo =   modelToDo(dataMediaPair);
        dataMediaPairDOMapperExt.updateByPrimaryKeySelective(dataMediaPairDo);
    }

    @Override
    public DataMediaPair findById(Long dataMediaPairId) {
        Assert.assertNotNull(dataMediaPairId);
        DataMediaPairDO dataMediaPairDO = dataMediaPairDOMapperExt.selectByPrimaryKey(dataMediaPairId);
     return doToModel(dataMediaPairDO,null,null);
    }




    @Override
    public List<DataMediaPair> listByIds(Long... identities) {
        return null;
    }

    @Override
    public List<DataMediaPair> listAll() {
        return null;
    }

    @Override
    public List<DataMediaPair> listByCondition(Map condition) {
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
}
