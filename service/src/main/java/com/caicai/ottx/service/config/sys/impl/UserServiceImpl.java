package com.caicai.ottx.service.config.sys.impl;

import com.caicai.ottx.common.ErrorCode;
import com.caicai.ottx.common.exception.BizException;
import com.caicai.ottx.common.system.UserVo;
import com.caicai.ottx.common.utils.MD5Util;
import com.caicai.ottx.common.utils.SessionThreadLocal;
import com.caicai.ottx.dal.entity.UserDO;
import com.caicai.ottx.dal.entity.UserSessionDO;
import com.caicai.ottx.dal.mapper.UserSessionDOMapperExt;
import com.caicai.ottx.service.config.sys.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by huaseng on 2019/8/20.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private UserSessionDOMapperExt userSessionDOMapperExt;

    @Override
    public UserVo login(String username, String password) {
        UserDO userDO = findByUsername(username);
        if(userDO == null && !StringUtils.equals(userDO.getPassword(),password)){
            throw new BizException(10002,"用户名或密码错误!");
        }
        //生成登录成功token
        String token = createUserSessionToken(userDO.getId());
        if(StringUtils.isNotBlank(token)){
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(userDO,userVo);
            userVo.setToken(token);
            return userVo;
        }
        return null;
    }

    @Override
    public UserDO getCurrentUser() {
        return SessionThreadLocal.getSession();
    }

    @Override
    public UserDO getUserInfoByToken(String token) {
        UserSessionDO userSessionDO = sqlSession.selectOne("findByToken",token);
        if(userSessionDO == null){
            throw new BizException(ErrorCode.EC_401, "无效的Token");
        }
        Long loginTime = userSessionDO.getLoginTime();

        int timeout = userSessionDO.getTimeout();
        if (timeout <= 0) timeout = TOKEN_TIMEOUT; // 默认60分钟失效
        if (loginTime == null || loginTime + timeout * ONE_MINUTE < System.currentTimeMillis()) {
            throw new BizException(ErrorCode.EC_401, "登录令牌已失效, 请重新登录");

        }
        return sqlSession.selectOne("queryUserByUid",userSessionDO.getUserId());
    }

    private UserDO findByUsername(String username){
       return sqlSession.selectOne("queryUserByUsername",username);
    }


    private synchronized String createUserSessionToken(Long userId) {
        String randomStr = UUID.randomUUID().toString().replaceAll("-", "");

        String rawStr = randomStr + TOKEN_INFO_SPLIT + userId;
        String token = "";
        try {
            token = MD5Util.getMD5(rawStr);
        } catch (Exception e) {
            //
        }
        UserSessionDO us = sqlSession.selectOne("findUserSessionByUid",userId);
        if (us != null) {
            us.setToken(token);
            us.setLoginTime(System.currentTimeMillis());
            userSessionDOMapperExt.updateByPrimaryKeySelective(us);
        } else {
            us = new UserSessionDO(userId, token);
            us.setLoginTime(System.currentTimeMillis());
            userSessionDOMapperExt.insertSelective(us);
        }
        return token;
    }
}
