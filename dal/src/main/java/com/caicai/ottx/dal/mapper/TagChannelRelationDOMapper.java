/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * TagChannelRelationDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.TagChannelRelationDO;
import com.caicai.ottx.dal.entity.TagChannelRelationDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : tag_channel_relation的 mapper 类
 * 
 * @author 	$author$
 */
public interface TagChannelRelationDOMapper {
    long countByExample(TagChannelRelationDOExample example);

    int deleteByExample(TagChannelRelationDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TagChannelRelationDO record);

    int insertSelective(TagChannelRelationDO record);

    List<TagChannelRelationDO> selectByExample(TagChannelRelationDOExample example);

    TagChannelRelationDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TagChannelRelationDO record, @Param("example") TagChannelRelationDOExample example);

    int updateByExample(@Param("record") TagChannelRelationDO record, @Param("example") TagChannelRelationDOExample example);

    int updateByPrimaryKeySelective(TagChannelRelationDO record);

    int updateByPrimaryKey(TagChannelRelationDO record);
}