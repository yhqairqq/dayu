package com.caicai.ottx.service.config.datacolumnpair.impl;

import com.alibaba.otter.shared.common.model.config.data.Column;
import com.alibaba.otter.shared.common.model.config.data.ColumnPair;
import com.caicai.ottx.dal.entity.DataColumnPairDO;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.config.datacolumnpair.DataColumnPairService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/8/28.
 */
@Service
@Slf4j
public class DataColumnPairServiceImpl implements DataColumnPairService{
    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<ColumnPair> listByDataMediaPairId(Long dataMediaPairId) {
        return null;
    }

    @Override
    public Map<Long, List<ColumnPair>> listByDataMediaPairIds(Long... dataMediaPairIds) {
        Assert.assertNotNull(dataMediaPairIds);
        Map<Long,List<ColumnPair>> dataColumnPairs = new HashMap<>();
        try{

            List<DataColumnPairDO> dataColumnPairDOs = sqlSession.selectList("listDataColumnPairByDataMediaPairIds",dataMediaPairIds);
            if(CollectionUtils.isEmpty(dataColumnPairDOs)){
                log.debug("DEBUG ## couldn't query any dataColumnPair, maybe hasn't create any dataColumnPair.");
                return dataColumnPairs;
            }
            for(DataColumnPairDO dataColumnPairDO:dataColumnPairDOs){
                List<ColumnPair> columnPairs = dataColumnPairs.get(dataColumnPairDO.getDataMediaPairId());
                if(!CollectionUtils.isEmpty(columnPairs)){
                    ColumnPair columnPair =  doToModel(dataColumnPairDO);
                    if(!columnPairs.contains(columnPair)){
                        columnPairs.add(columnPair);
                    }
                }else{
                    columnPairs = new ArrayList<ColumnPair>();
                    columnPairs.add(doToModel(dataColumnPairDO));
                    dataColumnPairs.put(dataColumnPairDO.getDataMediaPairId(),columnPairs);
                }
            }

        }catch (Exception e){
            log.error("ERROR ## query dataColumnPair by dataMediaId:" + dataMediaPairIds + " has an exception!");
            throw new ManagerException(e);
        }
        return dataColumnPairs;
    }
    /**
     * 用于DO对象转化为Model对象
     */
    private ColumnPair doToModel(DataColumnPairDO dataColumnPairDo) {

        Column sourceColumn = dataColumnPairDo.getSourceColumnName() == null ? null : new Column(
                dataColumnPairDo.getSourceColumnName());
        Column targetColumn = dataColumnPairDo.getTargetColumnName() == null ? null : new Column(
                dataColumnPairDo.getTargetColumnName());
        ColumnPair columnPair = new ColumnPair(sourceColumn, targetColumn);
        columnPair.setId(dataColumnPairDo.getId());
        columnPair.setDataMediaPairId(dataColumnPairDo.getDataMediaPairId());
        columnPair.setGmtCreate(dataColumnPairDo.getGmtCreate());
        columnPair.setGmtModified(dataColumnPairDo.getGmtModified());

        return columnPair;
    }

    private List<ColumnPair> doToModel(List<DataColumnPairDO> dataColumnPairDos) {

        List<ColumnPair> columnPairs = new ArrayList<ColumnPair>();
        for (DataColumnPairDO dataColumnPairDo : dataColumnPairDos) {
            columnPairs.add(doToModel(dataColumnPairDo));
        }

        return columnPairs;
    }

    /**
     * 用于Model对象转化为DO对象
     *
     * @param dataColumnPair
     * @return DataMediaPairDO
     */
    private DataColumnPairDO modelToDo(ColumnPair dataColumnPair) {
        DataColumnPairDO dataColumnPairDo = new DataColumnPairDO();
        dataColumnPairDo.setId(dataColumnPair.getId());
        dataColumnPairDo.setSourceColumnName(dataColumnPair.getSourceColumn() == null ? null : dataColumnPair.getSourceColumn().getName());
        dataColumnPairDo.setTargetColumnName(dataColumnPair.getTargetColumn() == null ? null : dataColumnPair.getTargetColumn().getName());
        dataColumnPairDo.setDataMediaPairId(dataColumnPair.getDataMediaPairId());
        dataColumnPairDo.setGmtCreate(dataColumnPair.getGmtCreate());
        dataColumnPairDo.setGmtModified(dataColumnPair.getGmtModified());

        return dataColumnPairDo;
    }
    @Override
    public void createBatch(List<ColumnPair> dataColumnPairs) {

    }

    @Override
    public void removeByDataMediaPairId(Long dataMediaPairId) {

    }

    @Override
    public void create(ColumnPair entityObj) {

    }

    @Override
    public void remove(Long identity) {

    }

    @Override
    public void modify(ColumnPair entityObj) {

    }

    @Override
    public ColumnPair findById(Long identity) {
        return null;
    }

    @Override
    public List<ColumnPair> listByIds(Long... identities) {
        return null;
    }

    @Override
    public List<ColumnPair> listAll() {
        return null;
    }

    @Override
    public List<ColumnPair> listByCondition(Map condition) {
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
