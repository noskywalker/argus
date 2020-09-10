package com.monitor.argus.dao.system;

import com.monitor.argus.bean.system.UserBean;

import java.util.List;
import java.util.Map;


/**
 * @Description:系统用户Dao接口
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午09:17:12
 * @Version: V1.0
 */
public interface ISystemUserDao {

    /**
     * 增加：系统用户信息
     *
     * @param userBean
     */
    public boolean addUserBean(UserBean userBean);


    /**
     * 修改：系统用户信息
     *
     * @param userBean 系统用户信息
     */
    public boolean updateUserBean(UserBean userBean);

    /**
     * 查找一条：系统用户信息
     *
     * @param userBean 系统用户信息
     */
    public UserBean getUserBean(UserBean userBean);

    /**
     * 通过ID获取用户信息
     *
     * @param userId
     */
    public UserBean getUserBeanByPrimaryKey(String userId);

    /**
     * 查找列表：系统用户信息List
     *
     * @param userBean 系统用户信息
     */
    public List<UserBean> getUserBeanList(UserBean userBean);

    /**
     * 查找列表：系统用户信息count
     *
     * @param userBean 系统用户信息
     */
    public Integer getUserBeanListCount(UserBean userBean);

    /**
     * 用户关联权限
     */
    public boolean addUserAuth(Map<String, Object> map);

    /**
     * 删除用户关联的权限
     */
    public boolean deleteUserAuth(String userId);

    /**
     * 通过用户ID获取权限列表
     *
     * @param userId
     * @return
     */
    public List<Integer> getAuthListByUserId(String userId);
}
