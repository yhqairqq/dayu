package com.caicai.ottx.service.config.autokeeper.impl;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.otter.shared.common.model.autokeeper.AutoKeeperCluster;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.utils.JsonUtils;
import com.caicai.ottx.dal.entity.AutoKeeperClusterDO;
import com.caicai.ottx.dal.mapper.AutoKeeperClusterDOMapperExt;
import com.caicai.ottx.service.config.autokeeper.AutoKeeperClusterService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huaseng on 2019/8/23.
 */
@Service
@Slf4j
public class AutoKeeperClusterServiceImpl implements AutoKeeperClusterService{

    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private AutoKeeperClusterDOMapperExt autoKeeperClusterDOMapperExt;

    @Override
    public AutoKeeperCluster findAutoKeeperClusterById(Long id) {
        AutoKeeperClusterDO autoKeeperClusterDO = autoKeeperClusterDOMapperExt.selectByPrimaryKey(id);
        return autoKeeperClusterDO == null ? null:doToModel(autoKeeperClusterDO);
    }

    private List<AutoKeeperCluster> doToModel(List<AutoKeeperClusterDO> autoKeeperClusterDos) {
        List<AutoKeeperCluster> autoKeeperClusters = new ArrayList<AutoKeeperCluster>();
        for (AutoKeeperClusterDO autoKeeperClusterDo : autoKeeperClusterDos) {
            autoKeeperClusters.add(doToModel(autoKeeperClusterDo));
        }
        return autoKeeperClusters;
    }

    private AutoKeeperCluster doToModel(AutoKeeperClusterDO autoKeeperClusterDo) {
        AutoKeeperCluster autoKeeperCluster = new AutoKeeperCluster();
        autoKeeperCluster.setId(autoKeeperClusterDo.getId());
        autoKeeperCluster.setClusterName(autoKeeperClusterDo.getClusterName());
        autoKeeperCluster.setDescription(autoKeeperClusterDo.getDescription());
        autoKeeperCluster.setServerList(JsonUtils.unmarshalFromString(autoKeeperClusterDo.getServerList(),
                new TypeReference<List<String>>() {
                }));
        autoKeeperCluster.setGmtCreate(autoKeeperClusterDo.getGmtCreate());
        autoKeeperCluster.setGmtModified(autoKeeperClusterDo.getGmtModified());
        return autoKeeperCluster;
    }

    @Override
    public List<AutoKeeperCluster> listAutoKeeperClusters() {
        List<AutoKeeperClusterDO> autoKeeperClusterDOS =  sqlSession.selectList("listAutoKeeperClusters");
        List<AutoKeeperCluster> zookeepers =  doToModel(autoKeeperClusterDOS);
        return zookeepers;
    }

    @Override
    public void modifyAutoKeeperCluster(AutoKeeperCluster autoKeeperCluster) {
        autoKeeperClusterDOMapperExt.updateByPrimaryKeySelective(modelToDo(autoKeeperCluster));
    }

    @Override
    public void createAutoKeeperCluster(AutoKeeperCluster autoKeeperCluster) {
        autoKeeperClusterDOMapperExt.insertSelective(modelToDo(autoKeeperCluster));
    }

    private AutoKeeperClusterDO modelToDo(AutoKeeperCluster autoKeeperCluster) {
        AutoKeeperClusterDO autokeeperClusterDo = new AutoKeeperClusterDO();
        autokeeperClusterDo.setId(autoKeeperCluster.getId());
        autokeeperClusterDo.setClusterName(autoKeeperCluster.getClusterName());
        autokeeperClusterDo.setDescription(autoKeeperCluster.getDescription());
        autokeeperClusterDo.setServerList(JsonUtils.marshalToString(autoKeeperCluster.getServerList()));
        autokeeperClusterDo.setGmtCreate(autoKeeperCluster.getGmtCreate());
        autokeeperClusterDo.setGmtModified(autoKeeperCluster.getGmtModified());
        return autokeeperClusterDo;
    }

    @Override
    public void removeAutoKeeperCluster(Long id) {
        autoKeeperClusterDOMapperExt.deleteByPrimaryKey(id);
    }

    @Override
    public Integer getCount() {
        return null;
    }
}
