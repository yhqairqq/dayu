package com.caicai.ottx.service.config.autokeeper;

import com.alibaba.otter.shared.common.model.autokeeper.AutoKeeperCluster;

import java.util.List;

/**
 * Created by huaseng on 2019/8/23.
 */
public interface AutoKeeperClusterService {
     AutoKeeperCluster findAutoKeeperClusterById(Long id);

     List<AutoKeeperCluster> listAutoKeeperClusters();

     void modifyAutoKeeperCluster(AutoKeeperCluster autoKeeperCluster);

     void createAutoKeeperCluster(AutoKeeperCluster autoKeeperCluster);

     void removeAutoKeeperCluster(Long id);

     Integer getCount();
}
