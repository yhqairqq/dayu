/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * DelayStatDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.DelayStatDO;
import com.caicai.ottx.dal.entity.DelayStatDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : delay_stat的 mapper 类
 * 
 * @author 	$author$
 */
public interface DelayStatDOMapper {
    long countByExample(DelayStatDOExample example);

    int deleteByExample(DelayStatDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DelayStatDO record);

    int insertSelective(DelayStatDO record);

    List<DelayStatDO> selectByExample(DelayStatDOExample example);

    DelayStatDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DelayStatDO record, @Param("example") DelayStatDOExample example);

    int updateByExample(@Param("record") DelayStatDO record, @Param("example") DelayStatDOExample example);

    int updateByPrimaryKeySelective(DelayStatDO record);

    int updateByPrimaryKey(DelayStatDO record);
}