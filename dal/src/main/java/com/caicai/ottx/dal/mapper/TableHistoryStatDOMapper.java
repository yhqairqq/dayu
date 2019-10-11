/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * TableHistoryStatDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.TableHistoryStatDO;
import com.caicai.ottx.dal.entity.TableHistoryStatDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : table_history_stat的 mapper 类
 * 
 * @author 	$author$
 */
public interface TableHistoryStatDOMapper {
    long countByExample(TableHistoryStatDOExample example);

    int deleteByExample(TableHistoryStatDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TableHistoryStatDO record);

    int insertSelective(TableHistoryStatDO record);

    List<TableHistoryStatDO> selectByExample(TableHistoryStatDOExample example);

    TableHistoryStatDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TableHistoryStatDO record, @Param("example") TableHistoryStatDOExample example);

    int updateByExample(@Param("record") TableHistoryStatDO record, @Param("example") TableHistoryStatDOExample example);

    int updateByPrimaryKeySelective(TableHistoryStatDO record);

    int updateByPrimaryKey(TableHistoryStatDO record);
}