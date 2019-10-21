package com.caicai.ottx.service.trans.impl;

import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.data.*;
import com.alibaba.otter.shared.common.utils.Assert;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import com.caicai.ottx.dal.entity.DataMediaPairDO;
import com.caicai.ottx.dal.entity.DataMediaPairTransDO;
import com.caicai.ottx.dal.mapper.DataMediaPairTransDOMapperExt;
import com.caicai.ottx.service.common.exceptions.ManagerException;
import com.caicai.ottx.service.config.datacolumnpair.DataColumnPairGroupService;
import com.caicai.ottx.service.config.datacolumnpair.DataColumnPairService;
import com.caicai.ottx.service.config.datamedia.DataMediaService;
import com.caicai.ottx.service.trans.DataMediaPairTransService;
import com.caicai.ottx.service.trans.model.DataMediaPairTrans;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huaseng on 2019/10/18.
 */
@Service
@Slf4j
public class DataMediaPairTransServiceImpl implements DataMediaPairTransService {

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private DataMediaPairTransDOMapperExt dataMediaPairTransDOMapperExt;

    @Autowired
    private DataMediaService dataMediaService;
    @Autowired
    private DataColumnPairService dataColumnPairService;
    @Autowired
    private DataColumnPairGroupService dataColumnPairGroupService;

    @Override
    public void create(DataMediaPairTrans dataMediaPairTrans) {
        DataMediaPairTransDO dataMediaPairTransDO = modelToDo(dataMediaPairTrans);
        dataMediaPairTransDOMapperExt.insertSelective(dataMediaPairTransDO);
    }

    @Override
    public void remove(Long id) {
        dataMediaPairTransDOMapperExt.deleteByPrimaryKey(id);
    }

    @Override
    public void modify(DataMediaPairTrans dataMediaPairTrans) {
        DataMediaPairTransDO dataMediaPairTransDO = modelToDo(dataMediaPairTrans);
        dataMediaPairTransDOMapperExt.updateByPrimaryKeySelective(dataMediaPairTransDO);
    }

    @Override
    public List<DataMediaPairTrans> listByIds(Long... identities) {
        return null;
    }

    @Override
    public List<DataMediaPairTrans> listAll() {
        return null;
    }

    @Override
    public DataMediaPairTrans findById(Long id) {
        Assert.assertNotNull(id);
        DataMediaPairTransDO dataMediaPairTransDO = dataMediaPairTransDOMapperExt.selectByPrimaryKey(id);
        return doToModel(dataMediaPairTransDO,null,null);
    }

    @Override
    public DataMediaPairTrans findByName(String name) {
        return null;
    }

    @Override
    public int getCount(Map condition) {
        return 0;
    }

    @Override
    public List<DataMediaPairTrans> listByCondition(Map condition) {
        PageHelper.startPage((int)condition.get("currentPage"),(int)condition.get("pageSize"));

        List<DataMediaPairTransDO> dataMediaPairTransDOS = sqlSession.selectList("dataMediaPairTransList",condition);
        if(CollectionUtils.isEmpty(dataMediaPairTransDOS)){
            log.debug("DEBUG ## couldn't query any channel by the condition:" + JsonUtils.marshalToString(condition));
            return new Page<DataMediaPairTrans>();
        }
        List<DataMediaPairTrans> dataMediaPairTrans = doToModelWithoutOther(dataMediaPairTransDOS);
        Page<DataMediaPairTrans> page = new Page<DataMediaPairTrans>();
        Page<DataMediaPairTrans> pageTemp = (Page)dataMediaPairTransDOS;
        page.addAll(dataMediaPairTrans);
        page.setPageSize(pageTemp.getPageSize());
        page.setPageNum(pageTemp.getPageNum());
        page.setTotal(pageTemp.getTotal());
        return page;
    }

    @Override
    public long createAndReturnId(DataMediaPairTrans dataMediaPairTrans) {
        DataMediaPairTransDO dataMediaPairTransDO = modelToDo(dataMediaPairTrans) ;
        dataMediaPairTransDOMapperExt.insertSelective(dataMediaPairTransDO);
        return dataMediaPairTransDO.getId();
    }


    private List<DataMediaPairTrans> doToModel(List<DataMediaPairTransDO> dataMediaPairTransDos) {
        List<Long> dataMediaPairTransIds = new ArrayList<Long>();
        for (DataMediaPairTransDO dataMediaPairTransDO : dataMediaPairTransDos) {
            dataMediaPairTransIds.add(dataMediaPairTransDO.getId());
        }
        Map<Long, List<ColumnPair>> columnPairMap = dataColumnPairService.listByDataMediaPairIds(dataMediaPairTransIds.toArray(new Long[dataMediaPairTransIds.size()]));
        Map<Long, List<ColumnGroup>> columnPairGroupMap = dataColumnPairGroupService.listByDataMediaPairIds(dataMediaPairTransIds.toArray(new Long[dataMediaPairTransIds.size()]));
        List<DataMediaPairTrans> dataMediaPairTranss = new ArrayList<DataMediaPairTrans>();
        for (DataMediaPairTransDO dataMediaPairTransDO : dataMediaPairTransDos) {
            List<ColumnPair> columnPairs = columnPairMap.get(dataMediaPairTransDO.getId()) == null ? new ArrayList<ColumnPair>() : columnPairMap.get(dataMediaPairTransDO.getId());
            List<ColumnGroup> columnGroups = columnPairGroupMap.get(dataMediaPairTransDO.getId()) == null ? new ArrayList<ColumnGroup>() : columnPairGroupMap.get(dataMediaPairTransDO.getId());
            dataMediaPairTranss.add(doToModel(dataMediaPairTransDO, columnPairs, columnGroups));
        }
        return dataMediaPairTranss;
    }


    private DataMediaPairTrans doToModel(DataMediaPairTransDO dataMediaPairTransDO, List<ColumnPair> columnPairs,
                                         List<ColumnGroup> columnGroups) {
        DataMediaPairTrans dataMediaPairTrans = new DataMediaPairTrans();
        try {
            dataMediaPairTrans.setId(dataMediaPairTransDO.getId());

            dataMediaPairTrans.setColumnPairs(columnPairs);
            dataMediaPairTrans.setColumnGroups(columnGroups);
            dataMediaPairTrans.setColumnPairMode(dataMediaPairTransDO.getColumnPairMode());
            dataMediaPairTrans.setGmtCreate(dataMediaPairTransDO.getGmtCreate());
            dataMediaPairTrans.setGmtModified(dataMediaPairTransDO.getGmtModified());

            // 组装DataMedia
            List<DataMedia> dataMedias = dataMediaService.listByIds(dataMediaPairTransDO.getSourceDataMediaId(),
                    dataMediaPairTransDO.getTargetDataMediaId());
            if (null == dataMedias || dataMedias.size() != 2) {
                // 抛出异常
                return dataMediaPairTrans;
            }

            for (DataMedia dataMedia : dataMedias) {
                if (dataMedia.getId().equals(dataMediaPairTransDO.getSourceDataMediaId())) {
                    dataMediaPairTrans.setSource(dataMedia);
                } else if (dataMedia.getId().equals(dataMediaPairTransDO.getTargetDataMediaId())) {
                    dataMediaPairTrans.setTarget(dataMedia);
                }
            }
        } catch (Exception e) {
            log.error("ERROR ## change the dataMediaPair Do to Model has an exception", e);
            throw new ManagerException(e);
        }

        return dataMediaPairTrans;
    }

    private DataMediaPairTransDO modelToDo(DataMediaPairTrans dataMediaPairTrans) {
        DataMediaPairTransDO dataMediaPairTransDO = new DataMediaPairTransDO();
        try {
            dataMediaPairTransDO.setId(dataMediaPairTrans.getId());
            dataMediaPairTransDO.setSourceDataMediaId(dataMediaPairTrans.getSource().getId());
            dataMediaPairTransDO.setTargetDataMediaId(dataMediaPairTrans.getTarget().getId());
            dataMediaPairTransDO.setColumnPairMode(dataMediaPairTrans.getColumnPairMode());
            dataMediaPairTransDO.setGmtCreate(dataMediaPairTrans.getGmtCreate());
            dataMediaPairTransDO.setGmtModified(dataMediaPairTrans.getGmtModified());
        } catch (Exception e) {
            log.error("ERROR ## change the dataMediaPair Model to Do has an exception", e);
            throw new ManagerException(e);
        }
        return dataMediaPairTransDO;
    }

    private List<DataMediaPairTrans> doToModelWithoutOther(List<DataMediaPairTransDO> dataMediaPairTransDos) {
        List<DataMediaPairTrans> dataMediaPairTrans = new ArrayList<DataMediaPairTrans>();
        for (DataMediaPairTransDO dataMediaPairTransDo : dataMediaPairTransDos) {
            dataMediaPairTrans.add(doToModel(dataMediaPairTransDo, null, null));
        }

        return dataMediaPairTrans;
    }



}
