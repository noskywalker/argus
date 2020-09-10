/** */
package com.monitor.argus.service.system.impl;

import com.monitor.argus.common.util.*;
import com.monitor.argus.bean.system.FuncBean;
import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.dao.redis.IRedisKeyBaseDao;
import com.monitor.argus.dao.redis.IRedisStringBaseDao;
import com.monitor.argus.service.system.IUserRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @Description:对用户相关操作的redis service实现类
 * @Author: alex zhang
 * @CreateDate: 2015-4-27 下午06:16:11
 * @Version: V1.0
 */
@Repository("userRedisService")
public class UserRedisServiceImpl implements IUserRedisService {

    @Autowired
    IRedisStringBaseDao redisStringBaseDao;

    @Autowired
    IRedisKeyBaseDao redisKeyBaseDao;

    public final static String SEESION_KEY_LOGIN_USER = "session_user";
    public final static String SEESION_KEY_LOGIN_USER_AUTH = "session_user_auth";

    /**
     * @param sessionId
     * @param userBean
     * @return
     * @Description:根据sessionId增加 userBean
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 上午11:13:58
     * @Version: V1.0
     */
    public boolean addUserBeanSession(HttpServletRequest request, HttpServletResponse response, UserBean userBean) {
        if (userBean == null) {
            return false;
        }
        String sessionId = YmallSessionUtil.getSessionID(request, response);
        String value = JsonUtil.beanToJson(userBean);
        redisStringBaseDao.setEx(ConstantsForRedis.MGR_LOGIN_USER_PRE + sessionId,
                (int) ConstantsForRedis.MGR_LOGIN_USER_OVERDATETIME, value);
        return true;
    }

    /**
     * @param phoneNo
     * @param smsCheckCode
     * @return
     * @Description:根据手机号 增加 验证码
     * @author null
     * @date 2016年4月8日 下午9:58:17
     */
    public boolean addUserLoginSmsCheckCode(String phoneNo, String smsCheckCode) {
        if (StringUtil.isEmpty(phoneNo) || StringUtil.isEmpty(smsCheckCode)) {
            return false;
        }
        redisStringBaseDao.del(ConstantsForRedis.MGR_LOGIN_SMSCHECKCODE_PRE + phoneNo);
        redisStringBaseDao.setEx(ConstantsForRedis.MGR_LOGIN_SMSCHECKCODE_PRE + phoneNo,
                (int) ConstantsForRedis.MGR_LOGIN_SMSCHECKCODE_OVERDATETIME, smsCheckCode);
        return true;
    }

    /**
     * @param phoneNo
     * @param smsCheckCode
     * @return
     * @Description:根据手机号 删除 验证码
     * @author null
     * @date 2016年4月8日 下午9:58:17
     */
    public boolean delUserLoginSmsCheckCode(String phoneNo) {
        if (StringUtil.isEmpty(phoneNo)) {
            return false;
        }
        redisStringBaseDao.del(ConstantsForRedis.MGR_LOGIN_SMSCHECKCODE_PRE + phoneNo);
        return true;
    }

    /**
     * @param phoneNo
     * @return
     * @Description:根据手机号 获取 验证码
     * @author null
     * @date 2016年4月8日 下午10:03:26
     */
    public String getUserLoginSmsCheckCode(String phoneNo) {
        String jsonStr = redisStringBaseDao.get(ConstantsForRedis.MGR_LOGIN_SMSCHECKCODE_PRE + phoneNo);
        return jsonStr;
    }

    /**
     * @param sessionId
     * @return
     * @Description:根据sessionId查找userBean
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 上午11:16:36
     * @Version: V1.0
     */
    public UserBean getUserBeanSession(HttpServletRequest request) {
        String sessionId = YmallSessionUtil.getSessionID(request);
        String jsonStr = redisStringBaseDao.get(ConstantsForRedis.MGR_LOGIN_USER_PRE + sessionId);
        UserBean userBean = (UserBean) JsonUtil.jsonToBean(jsonStr, UserBean.class);
        if (userBean == null) {
            userBean = new UserBean();
        }
        return userBean;
    }

    /**
     *
     * @param request
     * @return
     */
    public UserBean  getUserBeanSessionLocal(HttpServletRequest request) {
        UserBean userBean = (UserBean) request.getSession().getAttribute(LocalSessionUtil.SEESION_KEY_LOGIN_USER);
        if (userBean == null) {
            userBean = new UserBean();
        }
        return userBean;
    }

    public void setUserBeanSessionLocal(HttpServletRequest request, UserBean resultUserbean) {
        request.getSession().setAttribute(SEESION_KEY_LOGIN_USER, resultUserbean);
    }

    public void setUserFuncSessionLocal(HttpServletRequest request, List<FuncBean> userURI) {
        request.getSession().setAttribute(SEESION_KEY_LOGIN_USER_AUTH, userURI);
    }

    public void removeUserBeanSessionLocal(HttpServletRequest request) {
        request.getSession().removeAttribute(SEESION_KEY_LOGIN_USER);
        request.getSession().removeAttribute(SEESION_KEY_LOGIN_USER_AUTH);
    }

    /**
     * @param UserBean
     * @return
     * @Description:根据sessionId更新userBean过期时间
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:51:58
     * @Version: V1.0
     */
    public boolean updateUserBeanSessionExpire(HttpServletRequest request) {
        String sessionId = YmallSessionUtil.getSessionID(request);
        String key = ConstantsForRedis.MGR_LOGIN_USER_PRE + sessionId;
        return redisKeyBaseDao.expire(key, ConstantsForRedis.MGR_LOGIN_USER_OVERDATETIME);
    }

    /**
     * @param UserBean
     * @return
     * @Description:根据sessionId删除userBean
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:51:58
     * @Version: V1.0
     */
    public void deleteUserBeanSession(HttpServletRequest request) {
        String sessionId = YmallSessionUtil.getSessionID(request);
        redisKeyBaseDao.del(ConstantsForRedis.MGR_LOGIN_USER_PRE + sessionId);
    }

    /**
     * @param userBean
     * @return
     * @Description:
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午06:16:11
     * @Version: V1.0
     */
    @Override
    public boolean addUserBean(UserBean userBean) {
        if (userBean == null) {
            return false;
        }
        String value = JsonUtil.beanToJson(userBean);
        redisStringBaseDao.set(userBean.getId(), value);
        return true;
    }

    /**
     * @param list
     * @return
     * @Description:
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午06:16:11
     * @Version: V1.0
     */
    @Override
    public boolean addUserBeanList(List<UserBean> userBeanList) {
        if (!(userBeanList != null && userBeanList.size() > 0)) {
            return false;
        }
        for (UserBean userBean : userBeanList) {
            String value = JsonUtil.beanToJson(userBean);
            redisStringBaseDao.set(userBean.getId(), value);
        }
        // TODO
        return true;
    }

    /**
     * @param key
     * @Description:
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午06:16:11
     * @Version: V1.0
     */
    @Override
    public void deleteUserBean(String key) {
        redisKeyBaseDao.del(key);
    }

    /**
     * @param keys
     * @Description:
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午06:16:11
     * @Version: V1.0
     */
    @Override
    public void deleteUserBeanList(List<String> keys) {
        redisKeyBaseDao.del(keys);
    }

    /**
     * @param key
     * @return
     * @Description:
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午06:16:11
     * @Version: V1.0
     */
    @Override
    public UserBean getUserBean(String key) {
        if (!(key != null && !key.isEmpty())) {
            return null;
        }
        String jsonStr = redisStringBaseDao.get(key);
        UserBean userBean = (UserBean) JsonUtil.jsonToBean(jsonStr, UserBean.class);
        return userBean;
    }

    /**
     * @param userBean
     * @return
     * @Description:
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午06:16:11
     * @Version: V1.0
     */
    @Override
    public boolean updateUserBean(UserBean userBean) {
        // TODO
        return false;
    }

}
