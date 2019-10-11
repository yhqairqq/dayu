/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * SystemParameterDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.SystemParameterDO;
import com.caicai.ottx.dal.entity.SystemParameterDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : system_parameter的 mapper 类
 * 
 * @author 	$author$
 */
public interface SystemParameterDOMapper {
    long countByExample(SystemParameterDOExample example);

    int deleteByExample(SystemParameterDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SystemParameterDO record);

    int insertSelective(SystemParameterDO record);

    List<SystemParameterDO> selectByExampleWithBLOBs(SystemParameterDOExample example);

    List<SystemParameterDO> selectByExample(SystemParameterDOExample example);

    SystemParameterDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SystemParameterDO record, @Param("example") SystemParameterDOExample example);

    int updateByExampleWithBLOBs(@Param("record") SystemParameterDO record, @Param("example") SystemParameterDOExample example);

    int updateByExample(@Param("record") SystemParameterDO record, @Param("example") SystemParameterDOExample example);

    int updateByPrimaryKeySelective(SystemParameterDO record);

    int updateByPrimaryKeyWithBLOBs(SystemParameterDO record);

    int updateByPrimaryKey(SystemParameterDO record);
}