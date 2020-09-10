package com.monitor.argus.service.system;

import com.monitor.argus.bean.ParentAuthBean;
import com.monitor.argus.bean.system.AuthBean;
import com.monitor.argus.bean.system.UserBean;

import java.util.List;

/**
 * @Description:用户service接口
 * @Author: wangfeng
 */
public interface ISystemUserService {

    /**
     * 增加：用户信息
     *
     * @param userBean
     */
    public boolean addUserBean(UserBean userBean);

    /**
     * 修改：用户信息
     *
     * @param userBean 用户信息
     */
    public boolean updateUserBean(UserBean userBean);

    /**
     * 查找一条：用户信息
     *
     * @param userBean 用户信息
     */
    public UserBean getUserBean(UserBean userBean);

    /**
     * 查找列表：用户信息
     *
     * @param userBean 用户信息
     */
    public List<UserBean> getUserBeanList(UserBean userBean);

    /**
     * 修改密码
     *
     * @param oldPass
     * @param newPass
     * @param userId
     * @return
     */
    public boolean editPassword(String oldPass, String newPass, String userId);

    /**
     * 获取用户所有权限
     *
     * @param userId
     * @return
     */
    public List<AuthBean> getUserAuthList(String userId);

    /**
     * 编辑用户权限
     *
     * @param userId
     * @param authIds
     * @return
     */
    public Boolean editUserAuth(String userId, List<Integer> authIds);

    /**
     * 获得用户所有权限Json
     */
    public List<ParentAuthBean> getAuthMenus(String userId);
}
