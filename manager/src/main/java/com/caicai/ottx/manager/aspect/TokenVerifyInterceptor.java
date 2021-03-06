package com.caicai.ottx.manager.aspect;

import com.caicai.ottx.common.ErrorCode;
import com.caicai.ottx.common.exception.BizException;
import com.caicai.ottx.common.utils.SessionThreadLocal;
import com.caicai.ottx.dal.entity.UserDO;
import com.caicai.ottx.manager.config.ApplicationContextProvider;
import com.caicai.ottx.service.config.sys.UserService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TokenVerifyInterceptor extends HandlerInterceptorAdapter {

    private UserService userService;

    private Cache<String, UserDO> cache = CacheBuilder.newBuilder()
            .maximumSize(100) // 设置缓存的最大容量
            .expireAfterWrite(10, TimeUnit.MINUTES) // 设置缓存在写入一分钟后失效
            .concurrencyLevel(10) // 设置并发级别为10
            .recordStats()  // 开启缓存统计
            .build();

    private static final String KEY_TOKEN = "TOKEN_KEY_";

    private static List<String> filterUris       = new ArrayList<>();
    private static List<String> filterUriOfStart = new ArrayList<>();

    static {
        filterUris.add("/");
        filterUris.add("/index.html");
        filterUris.add("/index.htm");
        filterUris.add("/user/login");
        filterUris.add("/user/reg");
        filterUris.add("/board/curDate");
        filterUris.add("/anchor/upload"); // 鲁班系统同步H5活动埋点

        // 过滤以xx开始的url
        filterUriOfStart.add("/activate/");
        filterUriOfStart.add("/datav/");
    }

    private static boolean exInclude(String uri) {
        if (filterUriOfStart.size() > 0) {
            for (String tmp : filterUriOfStart) {
                if (StringUtils.startsWith(uri, tmp)) return true;
            }
        }
        if (filterUris.size() > 0) {
            for (String tmp : filterUris) {
                if (StringUtils.endsWith(uri, tmp)) return true;
            }
        }
        return false;
    }


    /**
     * 检测token合法性
     *
     * @param token String 唯一token
     * @return boolean true-成功; false-失败
     */
    private UserDO getUserInfoByToken(String token) {
        if (userService == null) {
            synchronized (this) {
                if (userService == null) {
                    synchronized (this) {
                        userService = ApplicationContextProvider.getBean(UserService.class);
                    }
                }
            }
        }
        return userService.getUserInfoByToken(token);
    }

    /**
     * 获取客户端传入的token
     *
     * @param req HttpServletRequest
     * @return String
     */
    private static String getToken(HttpServletRequest req) {
        String token = req.getParameter("token");
        if (StringUtils.isBlank(token)) {
            token = req.getHeader("token");
        } else {
            //url参数中带过来，token要url编码
            try {
                token = URLEncoder.encode(token, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return token;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否有不需要验证的url
        String uri = request.getRequestURI();
        if (exInclude(uri)) {
            return true;
        }
        //OPTIONS请求不作处理
        if (StringUtils.equalsIgnoreCase("OPTIONS", request.getMethod())) {
            return true;
        }

        String token = getToken(request);
        if (StringUtils.isBlank(token)) {
            throw new BizException(ErrorCode.EC_401, ErrorCode.EM_401);
        }

        //检测token的合法性
        UserDO userDO = cache.getIfPresent(KEY_TOKEN + token);
        if (userDO == null) {
            userDO = getUserInfoByToken(token);
        }
        if (userDO == null) {
            log.info("token验证失败! token:{}", token);
            throw new BizException(ErrorCode.EC_401, ErrorCode.EM_401);
        }

        cache.put(KEY_TOKEN + token, userDO);
        SessionThreadLocal.setSession(userDO);

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,  ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,  Exception ex) throws Exception {
        SessionThreadLocal.removeSession(); // 删除
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
