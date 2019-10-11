/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * PipelineNodeRelationDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.PipelineNodeRelationDO;
import com.caicai.ottx.dal.entity.PipelineNodeRelationDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : pipeline_node_relation的 mapper 类
 * 
 * @author 	$author$
 */
public interface PipelineNodeRelationDOMapper {
    long countByExample(PipelineNodeRelationDOExample example);

    int deleteByExample(PipelineNodeRelationDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PipelineNodeRelationDO record);

    int insertSelective(PipelineNodeRelationDO record);

    List<PipelineNodeRelationDO> selectByExample(PipelineNodeRelationDOExample example);

    PipelineNodeRelationDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PipelineNodeRelationDO record, @Param("example") PipelineNodeRelationDOExample example);

    int updateByExample(@Param("record") PipelineNodeRelationDO record, @Param("example") PipelineNodeRelationDOExample example);

    int updateByPrimaryKeySelective(PipelineNodeRelationDO record);

    int updateByPrimaryKey(PipelineNodeRelationDO record);
}