/**
 * 
 * Copyright From 2015, 微贷（杭州）金融信息服务有限公司. All rights reserved.
 * 
 * UserSessionDOMapper.java
 * 
 */
package com.caicai.ottx.dal.mapper;

import com.caicai.ottx.dal.entity.UserSessionDO;
import com.caicai.ottx.dal.entity.UserSessionDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 表 : sys_user_session的 mapper 类
 * 
 * @author 	$author$
 */
public interface UserSessionDOMapper {
    long countByExample(UserSessionDOExample example);

    int deleteByExample(UserSessionDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserSessionDO record);

    int insertSelective(UserSessionDO record);

    List<UserSessionDO> selectByExample(UserSessionDOExample example);

    UserSessionDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserSessionDO record, @Param("example") UserSessionDOExample example);

    int updateByExample(@Param("record") UserSessionDO record, @Param("example") UserSessionDOExample example);

    int updateByPrimaryKeySelective(UserSessionDO record);

    int updateByPrimaryKey(UserSessionDO record);
}