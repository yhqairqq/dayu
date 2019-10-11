/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * AlarmRuleDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.AlarmRuleDO;
import com.caicai.ottx.dal.entity.AlarmRuleDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : alarm_rule的 mapper 类
 * 
 * @author 	$author$
 */
public interface AlarmRuleDOMapper {
    long countByExample(AlarmRuleDOExample example);

    int deleteByExample(AlarmRuleDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AlarmRuleDO record);

    int insertSelective(AlarmRuleDO record);

    List<AlarmRuleDO> selectByExampleWithBLOBs(AlarmRuleDOExample example);

    List<AlarmRuleDO> selectByExample(AlarmRuleDOExample example);

    AlarmRuleDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AlarmRuleDO record, @Param("example") AlarmRuleDOExample example);

    int updateByExampleWithBLOBs(@Param("record") AlarmRuleDO record, @Param("example") AlarmRuleDOExample example);

    int updateByExample(@Param("record") AlarmRuleDO record, @Param("example") AlarmRuleDOExample example);

    int updateByPrimaryKeySelective(AlarmRuleDO record);

    int updateByPrimaryKeyWithBLOBs(AlarmRuleDO record);

    int updateByPrimaryKey(AlarmRuleDO record);
}