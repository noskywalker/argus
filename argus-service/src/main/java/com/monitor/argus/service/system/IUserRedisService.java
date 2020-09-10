/** */
package com.monitor.argus.service.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.monitor.argus.bean.system.FuncBean;
import com.monitor.argus.bean.system.UserBean;

/**
 * @Description:对用户相关操作的redis service接口
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-27 下午06:15:04
 * @Version: V1.0
 * 
 */
public interface IUserRedisService {

    /**
     * @Description:根据sessionId增加 userBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:51:58
     * @Version: V1.0
     * 
     * @param UserBean
     * @return
     * 
     */
    public boolean addUserBeanSession(HttpServletRequest request, HttpServletResponse response, UserBean userBean);

    /**
     * @Description:根据sessionId查找userBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午08:24:46
     * @Version: V1.0
     * 
     * @param sessionId
     * @return
     * 
     */
    public UserBean getUserBeanSession(HttpServletRequest request);

    /**
     * @Description:根据request获取本地session查找userBean
     * @param request
     * @return
     */
    public UserBean getUserBeanSessionLocal(HttpServletRequest request);

    /**
     * @Description:本地session设置用户信息
     * @param request
     * @param resultUserbean
     */
    public void setUserBeanSessionLocal(HttpServletRequest request, UserBean resultUserbean);

    /**
     * @Description:本地session设置用户权限信息
     * @param request
     * @param userURI
     */
    public void setUserFuncSessionLocal(HttpServletRequest request, List<FuncBean> userURI);

    /**
     * 清空session
     * @param request
     */
    public void removeUserBeanSessionLocal(HttpServletRequest request);

    /**
     * @Description:根据sessionId更新userBean过期时间
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:51:58
     * @Version: V1.0
     * 
     * @param UserBean
     * @return
     * 
     */
    public boolean updateUserBeanSessionExpire(HttpServletRequest request);

    /**
     * @Description:根据sessionId删除userBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:51:58
     * @Version: V1.0
     * 
     * @param UserBean
     * @return
     * 
     */
    public void deleteUserBeanSession(HttpServletRequest request);

    /**
     * @Description:根据手机号 增加 验证码
     * @author null
     * @date 2016年4月8日 下午9:58:17
     * 
     * @param phoneNo
     * @param smsCheckCode
     * @return
     */
    public boolean addUserLoginSmsCheckCode(String phoneNo, String smsCheckCode);

    /**
     * @Description:根据手机号 删除 验证码
     * @author null
     * @date 2016年4月8日 下午9:58:17
     * 
     * @param phoneNo
     * @param smsCheckCode
     * @return
     */
    public boolean delUserLoginSmsCheckCode(String phoneNo);

    /**
     * @Description:根据手机号 获取 验证码
     * 
     * @author null
     * @date 2016年4月8日 下午10:03:26
     * 
     * @param phoneNo
     * @return
     */
    public String getUserLoginSmsCheckCode(String phoneNo);

    /**
     * @Description:增加 userBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:51:58
     * @Version: V1.0
     * 
     * @param UserBean
     * @return
     * 
     */
    public boolean addUserBean(UserBean userBean);

    /**
     * @Description:批量增加 userBean 使用pipeline方式
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:53:20
     * @Version: V1.0
     * 
     * @param list
     * @return
     * 
     */
    public boolean addUserBeanList(List<UserBean> list);

    /**
     * @Description:删除userBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:53:55
     * @Version: V1.0
     * 
     * @param key
     * 
     */
    public void deleteUserBean(String key);

    /**
     * @Description:删除多个userBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:54:34
     * @Version: V1.0
     * 
     * @param keys
     * 
     */
    public void deleteUserBeanList(List<String> keys);

    /**
     * @Description:修改userBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:54:58
     * @Version: V1.0
     * 
     * @param userBean
     * @return
     * 
     */
    public boolean updateUserBean(UserBean userBean);

    /**
     * @Description: 通过key获取userBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:55:16
     * @Version: V1.0
     * 
     * @param keyId
     * @return
     * 
     */
    public UserBean getUserBean(String keyId);
}
