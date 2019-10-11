/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * PipelineDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.PipelineDO;
import com.caicai.ottx.dal.entity.PipelineDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : pipeline的 mapper 类
 * 
 * @author 	$author$
 */
public interface PipelineDOMapper {
    long countByExample(PipelineDOExample example);

    int deleteByExample(PipelineDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PipelineDO record);

    int insertSelective(PipelineDO record);

    List<PipelineDO> selectByExampleWithBLOBs(PipelineDOExample example);

    List<PipelineDO> selectByExample(PipelineDOExample example);

    PipelineDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PipelineDO record, @Param("example") PipelineDOExample example);

    int updateByExampleWithBLOBs(@Param("record") PipelineDO record, @Param("example") PipelineDOExample example);

    int updateByExample(@Param("record") PipelineDO record, @Param("example") PipelineDOExample example);

    int updateByPrimaryKeySelective(PipelineDO record);

    int updateByPrimaryKeyWithBLOBs(PipelineDO record);

    int updateByPrimaryKey(PipelineDO record);
}