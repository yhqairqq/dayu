/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * AutoKeeperClusterDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.AutoKeeperClusterDO;
import com.caicai.ottx.dal.entity.AutoKeeperClusterDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : autokeeper_cluster的 mapper 类
 * 
 * @author 	$author$
 */
public interface AutoKeeperClusterDOMapper {
    long countByExample(AutoKeeperClusterDOExample example);

    int deleteByExample(AutoKeeperClusterDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AutoKeeperClusterDO record);

    int insertSelective(AutoKeeperClusterDO record);

    List<AutoKeeperClusterDO> selectByExample(AutoKeeperClusterDOExample example);

    AutoKeeperClusterDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AutoKeeperClusterDO record, @Param("example") AutoKeeperClusterDOExample example);

    int updateByExample(@Param("record") AutoKeeperClusterDO record, @Param("example") AutoKeeperClusterDOExample example);

    int updateByPrimaryKeySelective(AutoKeeperClusterDO record);

    int updateByPrimaryKey(AutoKeeperClusterDO record);
}