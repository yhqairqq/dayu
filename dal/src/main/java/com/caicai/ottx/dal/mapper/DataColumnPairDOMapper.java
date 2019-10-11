/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * DataColumnPairDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.DataColumnPairDO;
import com.caicai.ottx.dal.entity.DataColumnPairDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : column_pair的 mapper 类
 * 
 * @author 	$author$
 */
public interface DataColumnPairDOMapper {
    long countByExample(DataColumnPairDOExample example);

    int deleteByExample(DataColumnPairDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DataColumnPairDO record);

    int insertSelective(DataColumnPairDO record);

    List<DataColumnPairDO> selectByExample(DataColumnPairDOExample example);

    DataColumnPairDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DataColumnPairDO record, @Param("example") DataColumnPairDOExample example);

    int updateByExample(@Param("record") DataColumnPairDO record, @Param("example") DataColumnPairDOExample example);

    int updateByPrimaryKeySelective(DataColumnPairDO record);

    int updateByPrimaryKey(DataColumnPairDO record);
}