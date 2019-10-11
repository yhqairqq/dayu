/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * NodeDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.NodeDO;
import com.caicai.ottx.dal.entity.NodeDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : node的 mapper 类
 * 
 * @author 	$author$
 */
public interface NodeDOMapper {
    long countByExample(NodeDOExample example);

    int deleteByExample(NodeDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(NodeDO record);

    int insertSelective(NodeDO record);

    List<NodeDO> selectByExampleWithBLOBs(NodeDOExample example);

    List<NodeDO> selectByExample(NodeDOExample example);

    NodeDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") NodeDO record, @Param("example") NodeDOExample example);

    int updateByExampleWithBLOBs(@Param("record") NodeDO record, @Param("example") NodeDOExample example);

    int updateByExample(@Param("record") NodeDO record, @Param("example") NodeDOExample example);

    int updateByPrimaryKeySelective(NodeDO record);

    int updateByPrimaryKeyWithBLOBs(NodeDO record);

    int updateByPrimaryKey(NodeDO record);
}