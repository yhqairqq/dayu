/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * CanalDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.CanalDO;
import com.caicai.ottx.dal.entity.CanalDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : canal的 mapper 类
 * 
 * @author 	$author$
 */
public interface CanalDOMapper {
    long countByExample(CanalDOExample example);

    int deleteByExample(CanalDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CanalDO record);

    int insertSelective(CanalDO record);

    List<CanalDO> selectByExampleWithBLOBs(CanalDOExample example);

    List<CanalDO> selectByExample(CanalDOExample example);

    CanalDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CanalDO record, @Param("example") CanalDOExample example);

    int updateByExampleWithBLOBs(@Param("record") CanalDO record, @Param("example") CanalDOExample example);

    int updateByExample(@Param("record") CanalDO record, @Param("example") CanalDOExample example);

    int updateByPrimaryKeySelective(CanalDO record);

    int updateByPrimaryKeyWithBLOBs(CanalDO record);

    int updateByPrimaryKey(CanalDO record);
}