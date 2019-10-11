/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * DataMediaDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.DataMediaDO;
import com.caicai.ottx.dal.entity.DataMediaDOExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : data_media的 mapper 类
 * 
 * @author 	$author$
 */
public interface DataMediaDOMapper {
    long countByExample(DataMediaDOExample example);

    int deleteByExample(DataMediaDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DataMediaDO record);

    int insertSelective(DataMediaDO record);

    List<DataMediaDO> selectByExampleWithBLOBs(DataMediaDOExample example);

    List<DataMediaDO> selectByExample(DataMediaDOExample example);

    DataMediaDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DataMediaDO record, @Param("example") DataMediaDOExample example);

    int updateByExampleWithBLOBs(@Param("record") DataMediaDO record, @Param("example") DataMediaDOExample example);

    int updateByExample(@Param("record") DataMediaDO record, @Param("example") DataMediaDOExample example);

    int updateByPrimaryKeySelective(DataMediaDO record);

    int updateByPrimaryKeyWithBLOBs(DataMediaDO record);

    int updateByPrimaryKey(DataMediaDO record);
}