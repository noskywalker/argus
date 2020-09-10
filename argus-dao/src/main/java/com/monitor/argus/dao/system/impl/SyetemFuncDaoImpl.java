package com.monitor.argus.dao.system.impl;

import com.monitor.argus.bean.system.AuthBean;
import com.monitor.argus.bean.system.FuncBean;
import com.monitor.argus.dao.mybatis.IBaseDao;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.dao.system.ISystemFuncDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangfeng on 16/8/18.
 */
@Repository("funcDao")
public class SyetemFuncDaoImpl implements ISystemFuncDao {


    @Autowired
    private IBaseDao baseDao;
    private final Logger logger = LoggerFactory.getLogger(SyetemFuncDaoImpl.class);

    @Override
    public boolean addFuncBean(FuncBean funcBean) {
        logger.info("Dao 新增功能 param:" + JsonUtil.beanToJson(funcBean));
        boolean flag = baseDao.insert("funcBeanMapper.insertFuncSelective", funcBean);
        logger.info("result:" + flag);
        return flag;
    }

    @Override
    public FuncBean getFuncBean(String funcId) {
        logger.info("Dao 获取功能详情 param:" + funcId);
        FuncBean func = baseDao.get("funcBeanMapper.selectFuncByPrimaryKey", funcId);
        logger.info("reuslt:" + JsonUtil.beanToJson(func));
        return func;
    }

    @Override
    public List<FuncBean> getFuncBeanList(FuncBean funcBean) {
        // logger.info("Dao 获取功能列表param:" + JsonUtil.beanToJson(funcBean));
        List<FuncBean> list = baseDao.getList("funcBeanMapper.getFuncBeanList", funcBean);
        return list;
    }

    @Override
    public boolean updateFuncBean(FuncBean funcBean) {
        logger.info("Dao 更新功能详情 param:" + JsonUtil.beanToJson(funcBean));
        boolean flag = baseDao.update("funcBeanMapper.updateFuncByPrimaryKeySelective", funcBean);
        logger.info("result:" + flag);
        return flag;
    }

    @Override
    public int countFuncBeanList(FuncBean funcBean) {
        logger.info("Dao 统计功能总数 param:" + JsonUtil.beanToJson(funcBean));
        int i = baseDao.get("funcBeanMapper.getFuncBeanListCount", funcBean);
        logger.info("result:" + i);
        return i;
    }

    @Override
    public boolean deleteFuncBean(String funcId) {
        logger.info("Dao 删除功能 params:" + funcId);
        boolean flag = baseDao.delete("funcBeanMapper.deleteFuncBean", funcId);
        logger.info("result:" + flag);
        return flag;
    }

    @Override
    public List<FuncBean> getListByAuth(List<Integer> authIds) {
        // logger.info("Dao 通过权限ID获取功能列表 param:" + JsonUtil.beanToJson(authIds));
        List<FuncBean> list = baseDao.getList("funcBeanMapper.getListByAuth", authIds);
        return list;

    }

    @Override
    public boolean updateAuthInfo(AuthBean authBean) {
        logger.info("Dao 更新权限信息。param:" + JsonUtil.beanToJson(authBean));
        boolean flag = baseDao.update("funcBeanMapper.updateAuthInfo", authBean);
        logger.info("result:" + flag);
        return flag;
    }


}
