package com.monitor.argus.dao.system.impl;

import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.dao.mybatis.IBaseDao;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.dao.system.ISystemUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * @Description:系统用户Dao接口实现类
 * @Author: wangfeng
 */
@Repository("userDao")
public class SystemUserDaoImpl implements ISystemUserDao {

    @Autowired
    private IBaseDao baseDao;
    private final Logger logger = LoggerFactory.getLogger(SystemUserDaoImpl.class);

    @Override
    public boolean addUserBean(UserBean userBean) {
        logger.info("Dao 插入系统用户信息.param:" + JsonUtil.beanToJson(userBean));
        boolean flag = baseDao.insert("userBeanMapper.insertSelective", userBean);
        logger.info("result:" + flag);
        return flag;
    }


    @Override
    public boolean updateUserBean(UserBean userBean) {
        logger.info("Dao 更新系统用户信息.param:" + JsonUtil.beanToJson(userBean));
        boolean flag = baseDao.update("userBeanMapper.updateByPrimaryKeySelective", userBean);
        logger.info("result:" + flag);
        return flag;
    }

    @Override
    public UserBean getUserBean(UserBean userBean) {
        logger.info("Dao 获取系统用户信息.param:" + JsonUtil.beanToJson(userBean));
        UserBean user = baseDao.get("userBeanMapper.getUserBean", userBean);
        logger.info("result:" + JsonUtil.beanToJson(user));
        return user;

    }

    @Override
    public UserBean getUserBeanByPrimaryKey(String userId) {
        logger.info("DAO 根据ID获取用户信息.param:" + userId);
        UserBean user = baseDao.get("userBeanMapper.selectByPrimaryKey", userId);
        logger.info("result:" + JsonUtil.beanToJson(user));
        return user;
    }

    @Override
    public List<UserBean> getUserBeanList(UserBean userBean) {
        logger.info("Dao 获取系统用户信息List.param:" + JsonUtil.beanToJson(userBean));
        List<UserBean> list = baseDao.getList("userBeanMapper.getUserBeanList", userBean);
        logger.info("result:" + JsonUtil.beanToJson(list));
        return list;
    }

    @Override
    public Integer getUserBeanListCount(UserBean userBean) {
        logger.info("Dao 获取系统用户信息count.param:" + JsonUtil.beanToJson(userBean));
        Integer i = baseDao.get("userBeanMapper.getUserBeanListCount", userBean);
        logger.info("result:" + i);
        return i;
    }


    @Override
    public boolean addUserAuth(Map<String, Object> map) {
        logger.info("Dao 用户关联权限.param:" + JsonUtil.beanToJson(map));
        boolean flag = baseDao.insert("userBeanMapper.insertUserAuth", map);
        logger.info("result:" + flag);
        return flag;
    }

    @Override
    public boolean deleteUserAuth(String userId) {
        logger.info("Dao 删除用户关联的权限.param:" + userId);
        boolean flag = baseDao.delete("userBeanMapper.deleteUserAuth", userId);
        logger.info("result:" + flag);
        return flag;
    }

    @Override
    public List<Integer> getAuthListByUserId(String userId) {
        logger.info("DAO 通过用户ID获取用户关联的权限.param:" + userId);
        List<Integer> list = baseDao.getList("userBeanMapper.getAuthListByUserId", userId);
        // logger.info("result:{}", JsonUtil.beanToJson(list));
        return list;
    }
}
