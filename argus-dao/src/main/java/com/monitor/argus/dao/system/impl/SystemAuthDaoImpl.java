package com.monitor.argus.dao.system.impl;

import com.monitor.argus.bean.system.AuthBean;
import com.monitor.argus.dao.mybatis.IBaseDao;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.dao.system.ISystemAuthDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangfeng on 16/8/17.
 */
@Repository("authDao")
public class SystemAuthDaoImpl implements ISystemAuthDao {

    @Autowired
    private IBaseDao baseDao;
    private final Logger logger = LoggerFactory.getLogger(SystemAuthDaoImpl.class);

    @Override
    public boolean addAuthBean(AuthBean authBean) {
        logger.info("Dao 插入权限信息 param:" + JsonUtil.beanToJson(authBean));
        boolean flag = baseDao.insert("authBeanMapper.insertAuthSelective", authBean);
        logger.info("result:" + flag);
        return flag;
    }


    @Override
    public AuthBean getAuthBean(String authId) {
        logger.info("Dao 获取权限详情 param:" + authId);
        AuthBean auth = baseDao.get("authBeanMapper.selectAuthByPrimaryKey", authId);
        logger.info("result:" + JsonUtil.beanToJson(auth));
        return auth;

    }


    @Override
    public List<AuthBean> getAuthBeanList(AuthBean authBean) {
        // logger.info("Dao 获取权限列表.param:" + JsonUtil.beanToJson(authBean));
        List<AuthBean> list = baseDao.getList("authBeanMapper.getAuthBeanList", authBean);
        return list;
    }


    @Override
    public boolean updateAuthBean(AuthBean authBean) {
        logger.info("Dao 更新权限详情.param:" + JsonUtil.beanToJson(authBean));
        boolean flag = baseDao.update("authBeanMapper.updateAuthByPrimaryKeySelective", authBean);
        logger.info("result:" + flag);
        return flag;
    }


    @Override
    public int countAuthBeanList(AuthBean authBean) {
        logger.info("Dao 统计权限总数.param:" + JsonUtil.beanToJson(authBean));
        int i = baseDao.get("authBeanMapper.getAuthBeanListCount", authBean);
        logger.info("result:" + i);
        return i;
    }

    @Override
    public List<AuthBean> getAuthListByIds(List<Integer> authIds) {
        // logger.info("Dao 通过权限ID list获取权限列表.params:" + JsonUtil.beanToJson(authIds));
        List<AuthBean> list = baseDao.getList("authBeanMapper.getAuthListByIds", authIds);
        return list;
    }

    @Override
    public boolean updateParentInfo(AuthBean authBean) {
        logger.info("Dao 更新父类权限名称.param:" + authBean);
        boolean flag = baseDao.update("authBeanMapper.updateParentInfo", authBean);
        logger.info("result:" + flag);
        return flag;
    }


}
