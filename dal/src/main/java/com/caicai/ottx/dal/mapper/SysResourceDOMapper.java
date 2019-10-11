/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * SysResourceDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.SysResourceDO;
import com.caicai.ottx.dal.entity.SysResourceDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : sys_resource的 mapper 类
 * 
 * @author 	$author$
 */
public interface SysResourceDOMapper {
    long countByExample(SysResourceDOExample example);

    int deleteByExample(SysResourceDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysResourceDO record);

    int insertSelective(SysResourceDO record);

    List<SysResourceDO> selectByExample(SysResourceDOExample example);

    SysResourceDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysResourceDO record, @Param("example") SysResourceDOExample example);

    int updateByExample(@Param("record") SysResourceDO record, @Param("example") SysResourceDOExample example);

    int updateByPrimaryKeySelective(SysResourceDO record);

    int updateByPrimaryKey(SysResourceDO record);
}