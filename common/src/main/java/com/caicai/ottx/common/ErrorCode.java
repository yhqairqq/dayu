package com.caicai.ottx.common;

/**
 * Created by huaseng on 2019/8/20.
 */
public interface ErrorCode {
    int EC_200 = 200;
    String EM_200 = "操作成功"; //

    int EC_400 = 400;
    String EM_400 = "错误的请求"; //

    int EC_401 = 401;
    String EM_401 = "未授权的操作,新重新登录"; //

    int EC_405 = 405;
    String EM_405 = "请求Method方法不支持"; //

    int EC_500 = 500;
    String EM_500 = "服务器内部发生错误"; //

    int EC_1000 = 1000;
    String EM_1000 = "查询数据超时"; //

    int EC_1001 = 1001;
    String EM_1001 = "请求数据不存在"; //
}
