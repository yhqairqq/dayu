/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * DataMediaPairDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.DataMediaPairDO;
import com.caicai.ottx.dal.entity.DataMediaPairDOExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : data_media_pair的 mapper 类
 * 
 * @author 	$author$
 */
public interface DataMediaPairDOMapper {
    long countByExample(DataMediaPairDOExample example);

    int deleteByExample(DataMediaPairDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DataMediaPairDO record);

    int insertSelective(DataMediaPairDO record);

    List<DataMediaPairDO> selectByExampleWithBLOBs(DataMediaPairDOExample example);

    List<DataMediaPairDO> selectByExample(DataMediaPairDOExample example);

    DataMediaPairDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DataMediaPairDO record, @Param("example") DataMediaPairDOExample example);

    int updateByExampleWithBLOBs(@Param("record") DataMediaPairDO record, @Param("example") DataMediaPairDOExample example);

    int updateByExample(@Param("record") DataMediaPairDO record, @Param("example") DataMediaPairDOExample example);

    int updateByPrimaryKeySelective(DataMediaPairDO record);

    int updateByPrimaryKeyWithBLOBs(DataMediaPairDO record);

    int updateByPrimaryKey(DataMediaPairDO record);
}