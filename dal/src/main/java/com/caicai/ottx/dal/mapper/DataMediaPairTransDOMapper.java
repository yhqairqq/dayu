/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * DataMediaPairTransDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.DataMediaPairTransDO;
import com.caicai.ottx.dal.entity.DataMediaPairTransDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : data_media_pair_trans的 mapper 类
 * 
 * @author 	$author$
 */
public interface DataMediaPairTransDOMapper {
    long countByExample(DataMediaPairTransDOExample example);

    int deleteByExample(DataMediaPairTransDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DataMediaPairTransDO record);

    int insertSelective(DataMediaPairTransDO record);

    List<DataMediaPairTransDO> selectByExample(DataMediaPairTransDOExample example);

    DataMediaPairTransDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DataMediaPairTransDO record, @Param("example") DataMediaPairTransDOExample example);

    int updateByExample(@Param("record") DataMediaPairTransDO record, @Param("example") DataMediaPairTransDOExample example);

    int updateByPrimaryKeySelective(DataMediaPairTransDO record);

    int updateByPrimaryKey(DataMediaPairTransDO record);
}