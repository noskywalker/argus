package com.monitor.argus.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:Ymall session工具类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-28 下午01:50:36
 * @Version: V1.0
 * 
 * 
 */
public class YmallSessionUtil {

    protected static String cookieName = "YingSessionCookie";

    /**
     * @Description:根据request，response获取SessionID
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午09:55:31
     * @Version: V1.0
     * 
     * @param request
     * @param response
     * @return
     * 
     */
    public static String getSessionID(HttpServletRequest request, HttpServletResponse response) {
        String sessionID = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; sessionID == null && i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(cookieName) && !"".equals(cookie.getValue())) {
                    sessionID = cookie.getValue();
                    return sessionID;
                }
            }
        }

        String domain = RegexUtil.getDomainNameFromUrl(request.getRequestURL().toString());

        if (sessionID == null) {
            sessionID = request.getSession().getId();
            Cookie MMCookie = new Cookie(cookieName, sessionID);
            MMCookie.setPath(ConstantsForCommon.URL_SLASH);
            MMCookie.setDomain(ConstantsForCommon.URL_DOT + domain);
            response.addCookie(MMCookie);
        }
        return sessionID;
    }

    /**
     * @Description:根据request获取SessionID
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:59:24
     * @Version: V1.0
     * 
     * @param request
     * @return
     * 
     */
    public static String getSessionID(HttpServletRequest request) {
        String sessionID = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; sessionID == null && i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(cookieName) && !"".equals(cookie.getValue())) {
                    sessionID = cookie.getValue();
                    return sessionID;
                }
            }
        }
        if (sessionID == null) {
            sessionID = request.getSession().getId();
        }
        return sessionID;
    }
}
