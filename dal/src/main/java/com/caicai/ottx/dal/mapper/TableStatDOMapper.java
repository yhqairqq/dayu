/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * TableStatDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.TableStatDO;
import com.caicai.ottx.dal.entity.TableStatDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : table_stat的 mapper 类
 * 
 * @author 	$author$
 */
public interface TableStatDOMapper {
    long countByExample(TableStatDOExample example);

    int deleteByExample(TableStatDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TableStatDO record);

    int insertSelective(TableStatDO record);

    List<TableStatDO> selectByExample(TableStatDOExample example);

    TableStatDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TableStatDO record, @Param("example") TableStatDOExample example);

    int updateByExample(@Param("record") TableStatDO record, @Param("example") TableStatDOExample example);

    int updateByPrimaryKeySelective(TableStatDO record);

    int updateByPrimaryKey(TableStatDO record);
}