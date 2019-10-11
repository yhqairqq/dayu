/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * ChannelDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.ChannelDO;
import com.caicai.ottx.dal.entity.ChannelDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : channel的 mapper 类
 * 
 * @author 	$author$
 */
public interface ChannelDOMapper {
    long countByExample(ChannelDOExample example);

    int deleteByExample(ChannelDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ChannelDO record);

    int insertSelective(ChannelDO record);

    List<ChannelDO> selectByExampleWithBLOBs(ChannelDOExample example);

    List<ChannelDO> selectByExample(ChannelDOExample example);

    ChannelDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ChannelDO record, @Param("example") ChannelDOExample example);

    int updateByExampleWithBLOBs(@Param("record") ChannelDO record, @Param("example") ChannelDOExample example);

    int updateByExample(@Param("record") ChannelDO record, @Param("example") ChannelDOExample example);

    int updateByPrimaryKeySelective(ChannelDO record);

    int updateByPrimaryKeyWithBLOBs(ChannelDO record);

    int updateByPrimaryKey(ChannelDO record);
}