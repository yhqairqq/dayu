package com.caicai.ottx.service.config.datacolumnpair.impl;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.otter.shared.common.model.config.data.ColumnGroup;
import com.alibaba.otter.shared.common.model.config.data.ColumnPair;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import com.caicai.ottx.dal.entity.DataColumnPairGroupDO;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.config.datacolumnpair.DataColumnPairGroupService;
import com.caicai.ottx.service.config.datacolumnpair.DataColumnPairService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
public class DataColumnPairGroupServiceImpl implements DataColumnPairGroupService{

    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private DataColumnPairService dataColumnPairService;

    @Override
    public void removeByDataMediaPairId(Long dataMediaPairId) {

    }

    @Override
    public List<ColumnGroup> listByDataMediaPairId(Long dataMediaPairId) {
        return null;
    }

    @Override
    public Map<Long, List<ColumnGroup>> listByDataMediaPairIds(Long... dataMediaPairIds) {
        Assert.assertNotNull(dataMediaPairIds);
        Map<Long,List<ColumnGroup>> dataColumnGroups = new HashMap<>();
        try{
            List<DataColumnPairGroupDO> dataColumnPairGroupDOs = sqlSession.selectList("listDataColumnPairGroupByDataMediaPairIds",dataMediaPairIds);
            if(CollectionUtils.isEmpty(dataColumnPairGroupDOs)){
                log.debug("DEBUG ## couldn't query any dataColumnPairGroup, maybe hasn't create any dataColumnPairGroup.");
                return dataColumnGroups;
            }
            for(DataColumnPairGroupDO dataColumnPairGroupDO:dataColumnPairGroupDOs){
                List<ColumnGroup> columnGroups = dataColumnGroups.get(dataColumnPairGroupDO.getDataMediaPairId());
                if(!CollectionUtils.isEmpty(columnGroups)){
                    ColumnGroup columnGroup =  doToModel(dataColumnPairGroupDO);
                    if(!columnGroups.contains(columnGroup)){
                        columnGroups.add(columnGroup);
                    }
                }else{
                    columnGroups = new ArrayList<ColumnGroup>();
                    columnGroups.add(doToModel(dataColumnPairGroupDO));
                    dataColumnGroups.put(dataColumnPairGroupDO.getDataMediaPairId(), columnGroups);
                }
            }
        }catch (Exception e){
            log.error("ERROR ## query dataColumnPairGroup by dataMediaId:" + dataMediaPairIds + " has an exception!");
            throw new ManagerException(e);
        }
        return dataColumnGroups;
    }

    /**
     * 用于DO对象转化为Model对象
     */
    private ColumnGroup doToModel(DataColumnPairGroupDO dataColumnPairGroupDo) {
        ColumnGroup columnGroup = new ColumnGroup();
        columnGroup.setId(dataColumnPairGroupDo.getId());
        List<ColumnPair> columnPairs = new ArrayList<ColumnPair>();
        if (StringUtils.isNotBlank(dataColumnPairGroupDo.getColumnPairContent())) {
            columnPairs = JsonUtils.unmarshalFromString(dataColumnPairGroupDo.getColumnPairContent(),
                    new TypeReference<ArrayList<ColumnPair>>() {
                    });
        }

        columnGroup.setColumnPairs(columnPairs);
        columnGroup.setDataMediaPairId(dataColumnPairGroupDo.getDataMediaPairId());
        columnGroup.setGmtCreate(dataColumnPairGroupDo.getGmtCreate());
        columnGroup.setGmtModified(dataColumnPairGroupDo.getGmtModified());

        return columnGroup;
    }

    private List<ColumnGroup> doToModel(List<DataColumnPairGroupDO> dataColumnPairGroupDos) {
        List<ColumnGroup> columnGroups = new ArrayList<ColumnGroup>();
        for (DataColumnPairGroupDO dataColumnPairGroupDO : dataColumnPairGroupDos) {
            columnGroups.add(doToModel(dataColumnPairGroupDO));
        }

        return columnGroups;
    }

    /**
     * 用于Model对象转化为DO对象
     *
     * @param dataColumnPair
     * @return DataMediaPairDO
     */
    private DataColumnPairGroupDO modelToDo(ColumnGroup columnGroup) {
        DataColumnPairGroupDO dataColumnPairGroupDo = new DataColumnPairGroupDO();
        dataColumnPairGroupDo.setId(columnGroup.getId());
        dataColumnPairGroupDo.setColumnPairContent(JsonUtils.marshalToString(columnGroup.getColumnPairs()));
        dataColumnPairGroupDo.setDataMediaPairId(columnGroup.getDataMediaPairId());
        dataColumnPairGroupDo.setGmtCreate(columnGroup.getGmtCreate());
        dataColumnPairGroupDo.setGmtModified(columnGroup.getGmtModified());

        return dataColumnPairGroupDo;
    }





    @Override
    public void create(ColumnGroup entityObj) {

    }

    @Override
    public void remove(Long identity) {

    }

    @Override
    public void modify(ColumnGroup entityObj) {

    }

    @Override
    public ColumnGroup findById(Long identity) {
        return null;
    }

    @Override
    public List<ColumnGroup> listByIds(Long... identities) {
        return null;
    }

    @Override
    public List<ColumnGroup> listAll() {
        return null;
    }

    @Override
    public List<ColumnGroup> listByCondition(Map condition) {
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
