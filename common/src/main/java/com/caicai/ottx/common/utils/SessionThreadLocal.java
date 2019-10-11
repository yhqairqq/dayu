package com.caicai.ottx.common.utils;

import com.caicai.ottx.dal.entity.UserDO;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by oneape<oneape15@163.com>
 * Created 2019-06-07 22:09.
 * Modify:
 */
@Slf4j
public final class SessionThreadLocal {
    private static ThreadLocal<UserDO> local = new ThreadLocal<>();


    /**
     * 设置用户Session
     *
     * @param session User
     */
    public static void setSession(UserDO session) {
        local.set(session);
    }

    /**
     * 获取用户Session
     *
     * @return User
     */
    public static UserDO getSession() {
        return local.get();
    }

    /**
     * 获取用户id
     *
     * @return Long
     */
    public static Long getUserId() {
        UserDO user = local.get();
        return user == null ? null : user.getId();
    }

    /**
     * 删除Session
     * PS: 业务层面不要单独调用该方法
     */
    public static void removeSession() {
        local.remove();
    }
}
