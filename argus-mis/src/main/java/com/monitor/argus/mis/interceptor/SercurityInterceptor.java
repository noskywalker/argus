package com.monitor.argus.mis.interceptor;


import com.monitor.argus.bean.system.FuncBean;
import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.service.system.impl.UserRedisServiceImpl;
import com.monitor.argus.common.annotation.Auth;
import com.monitor.argus.common.util.LocalSessionUtil;
import com.monitor.argus.mis.init.AuthCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


import static com.monitor.argus.mis.init.AuthCacheService.notURI;

public class SercurityInterceptor implements HandlerInterceptor {

    private final static Logger log = LoggerFactory.getLogger(SercurityInterceptor.class);

    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
    }

    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod method = (HandlerMethod) handler;
        Auth auth = method.getMethod().getAnnotation(Auth.class);
        // 需要验证
        if (auth == null || auth.verifyLogin()) {
            // 这里可以根据session的用户来判断角色的权限，根据权限来重定向不同的页面，简单起见，这里只是做了一个重定向
            UserBean userBean = (UserBean) request.getSession().getAttribute(LocalSessionUtil.SEESION_KEY_LOGIN_USER);
            if (userBean == null) {
                // 被拦截，重定向到login界面
                request.getRequestDispatcher("/system/user/login").forward(request, response);
                return false;
            } else {
                // 检查用户权限
                if (checkAuth(request)) {
                    if (isLogPrint(request.getRequestURI())) {
                        log.info("user:{},email:{},请求{},鉴权通过", userBean.getUserName(),
                                userBean.getEmail(), request.getRequestURI());
                    }
                    return true;
                } else {
                    // 被拦截，重定向到首页
                    log.info("user:{},email:{},请求{},鉴权未通过", userBean.getUserName(),
                            userBean.getEmail(), request.getRequestURI());
                    request.getRequestDispatcher("/dashboard/index").forward(request, response);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkAuth(HttpServletRequest request) {
        String reqUri = request.getRequestURI();
        // 权限uri列表
        List<FuncBean> allUris = AuthCacheService.allURI;
        // 用户uri列表
        List<FuncBean> userUris = (List) request.getSession().getAttribute(UserRedisServiceImpl.SEESION_KEY_LOGIN_USER_AUTH);

        // 模糊匹配权限uri列表
        boolean isContain = false;
        for (FuncBean funcb : allUris) {
            if (funcb != null && !StringUtils.isEmpty(funcb.getFuncUri())
                    && reqUri.contains(funcb.getFuncUri())) {
                isContain = true;
                break;
            }
        }
        if (isContain) {
            // uri在权限控制之内
            boolean isRight = false;
            for (FuncBean userUri : userUris) {
                if (userUri != null && !StringUtils.isEmpty(userUri.getFuncUri())
                        && reqUri.contains(userUri.getFuncUri())) {
                    isRight = true;
                    break;
                }
            }
            return isRight;
        } else {
            // uri不在权限控制之内
            return true;
        }
    }

    private boolean isLogPrint(String reqUri) {
        List<String> notURIs = notURI;
        if (notURIs != null) {
            for (String notURI : notURIs) {
                if (reqUri.contains(notURI)) {
                    return false;
                }
            }
        }
        return true;
    }

}
