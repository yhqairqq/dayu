/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * ThroughputStatDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.ThroughputStatDO;
import com.caicai.ottx.dal.entity.ThroughputStatDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : throughput_stat的 mapper 类
 * 
 * @author 	$author$
 */
public interface ThroughputStatDOMapper {
    long countByExample(ThroughputStatDOExample example);

    int deleteByExample(ThroughputStatDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ThroughputStatDO record);

    int insertSelective(ThroughputStatDO record);

    List<ThroughputStatDO> selectByExample(ThroughputStatDOExample example);

    ThroughputStatDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ThroughputStatDO record, @Param("example") ThroughputStatDOExample example);

    int updateByExample(@Param("record") ThroughputStatDO record, @Param("example") ThroughputStatDOExample example);

    int updateByPrimaryKeySelective(ThroughputStatDO record);

    int updateByPrimaryKey(ThroughputStatDO record);
}