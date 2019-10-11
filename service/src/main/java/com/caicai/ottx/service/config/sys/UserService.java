package com.caicai.ottx.service.config.sys;

import com.caicai.ottx.common.system.UserVo;
import com.caicai.ottx.dal.entity.UserDO;

/**
 * Created by huaseng on 2019/8/20.
 */
public interface UserService {
    static final String TOKEN_INFO_SPLIT = "<@>";
    static final int    TOKEN_TIMEOUT    = 60;
    static final long   ONE_MINUTE       = 60 * 1000;
    UserVo login(String username, String password);

    UserDO getCurrentUser();

    UserDO getUserInfoByToken(String token);
}
