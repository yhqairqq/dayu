/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * DataColumnPairGroupDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.DataColumnPairGroupDO;
import com.caicai.ottx.dal.entity.DataColumnPairGroupDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : column_pair_group的 mapper 类
 * 
 * @author 	$author$
 */
public interface DataColumnPairGroupDOMapper {
    long countByExample(DataColumnPairGroupDOExample example);

    int deleteByExample(DataColumnPairGroupDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DataColumnPairGroupDO record);

    int insertSelective(DataColumnPairGroupDO record);

    List<DataColumnPairGroupDO> selectByExampleWithBLOBs(DataColumnPairGroupDOExample example);

    List<DataColumnPairGroupDO> selectByExample(DataColumnPairGroupDOExample example);

    DataColumnPairGroupDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DataColumnPairGroupDO record, @Param("example") DataColumnPairGroupDOExample example);

    int updateByExampleWithBLOBs(@Param("record") DataColumnPairGroupDO record, @Param("example") DataColumnPairGroupDOExample example);

    int updateByExample(@Param("record") DataColumnPairGroupDO record, @Param("example") DataColumnPairGroupDOExample example);

    int updateByPrimaryKeySelective(DataColumnPairGroupDO record);

    int updateByPrimaryKeyWithBLOBs(DataColumnPairGroupDO record);

    int updateByPrimaryKey(DataColumnPairGroupDO record);
}