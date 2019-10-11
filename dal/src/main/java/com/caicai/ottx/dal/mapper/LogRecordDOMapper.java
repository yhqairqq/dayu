/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * LogRecordDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.LogRecordDO;
import com.caicai.ottx.dal.entity.LogRecordDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : log_record的 mapper 类
 * 
 * @author 	$author$
 */
public interface LogRecordDOMapper {
    long countByExample(LogRecordDOExample example);

    int deleteByExample(LogRecordDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(LogRecordDO record);

    int insertSelective(LogRecordDO record);

    List<LogRecordDO> selectByExampleWithBLOBs(LogRecordDOExample example);

    List<LogRecordDO> selectByExample(LogRecordDOExample example);

    LogRecordDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") LogRecordDO record, @Param("example") LogRecordDOExample example);

    int updateByExampleWithBLOBs(@Param("record") LogRecordDO record, @Param("example") LogRecordDOExample example);

    int updateByExample(@Param("record") LogRecordDO record, @Param("example") LogRecordDOExample example);

    int updateByPrimaryKeySelective(LogRecordDO record);

    int updateByPrimaryKeyWithBLOBs(LogRecordDO record);

    int updateByPrimaryKey(LogRecordDO record);
}