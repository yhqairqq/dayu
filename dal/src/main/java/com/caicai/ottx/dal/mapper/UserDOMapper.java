/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * UserDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.UserDO;
import com.caicai.ottx.dal.entity.UserDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : user的 mapper 类
 * 
 * @author 	$author$
 */
public interface UserDOMapper {
    long countByExample(UserDOExample example);

    int deleteByExample(UserDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    List<UserDO> selectByExample(UserDOExample example);

    UserDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserDO record, @Param("example") UserDOExample example);

    int updateByExample(@Param("record") UserDO record, @Param("example") UserDOExample example);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);
}