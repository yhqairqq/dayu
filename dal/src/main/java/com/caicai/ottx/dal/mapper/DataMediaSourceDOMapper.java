/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * DataMediaSourceDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.DataMediaSourceDO;
import com.caicai.ottx.dal.entity.DataMediaSourceDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : data_media_source的 mapper 类
 * 
 * @author 	$author$
 */
public interface DataMediaSourceDOMapper {
    long countByExample(DataMediaSourceDOExample example);

    int deleteByExample(DataMediaSourceDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DataMediaSourceDO record);

    int insertSelective(DataMediaSourceDO record);

    List<DataMediaSourceDO> selectByExample(DataMediaSourceDOExample example);

    DataMediaSourceDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DataMediaSourceDO record, @Param("example") DataMediaSourceDOExample example);

    int updateByExample(@Param("record") DataMediaSourceDO record, @Param("example") DataMediaSourceDOExample example);

    int updateByPrimaryKeySelective(DataMediaSourceDO record);

    int updateByPrimaryKey(DataMediaSourceDO record);
}