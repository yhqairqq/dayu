/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * DataMatrixDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.DataMatrixDO;
import com.caicai.ottx.dal.entity.DataMatrixDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : data_matrix的 mapper 类
 * 
 * @author 	$author$
 */
public interface DataMatrixDOMapper {
    long countByExample(DataMatrixDOExample example);

    int deleteByExample(DataMatrixDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DataMatrixDO record);

    int insertSelective(DataMatrixDO record);

    List<DataMatrixDO> selectByExample(DataMatrixDOExample example);

    DataMatrixDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DataMatrixDO record, @Param("example") DataMatrixDOExample example);

    int updateByExample(@Param("record") DataMatrixDO record, @Param("example") DataMatrixDOExample example);

    int updateByPrimaryKeySelective(DataMatrixDO record);

    int updateByPrimaryKey(DataMatrixDO record);
}