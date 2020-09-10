package com.monitor.argus.service.system.impl;

import com.monitor.argus.bean.system.FuncBean;
import com.monitor.argus.dao.system.ISystemFuncDao;
import com.monitor.argus.service.system.ISystemFuncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangfeng on 16/8/23.
 */

@Service("systemFuncService")
public class SystemFuncServiceImpl implements ISystemFuncService {

    @Autowired
    private ISystemFuncDao funcDao;

    private final Logger logger = LoggerFactory.getLogger(SystemFuncServiceImpl.class);

    @Override
    public boolean addFuncBean(FuncBean funcBean) {
        logger.info("Service 添加功能");
        if (null != isAuthHaveFunc(funcBean)) {
            throw new RuntimeException("权限ID已存在对应功能");
        }
        return funcDao.addFuncBean(funcBean);
    }

    @Override
    public boolean updateFuncBean(FuncBean funcBean) {
        logger.info("Service 更新功能");
        FuncBean func = isAuthHaveFunc(funcBean);
        if (null != func && !func.getId().equals(funcBean.getId())) {
            throw new RuntimeException("权限ID已存在对应功能");
        }
        return funcDao.updateFuncBean(funcBean);
    }

    @Override
    public FuncBean getFuncBean(String funcId) {
        logger.info("Service 获取功能详情");
        return funcDao.getFuncBean(funcId);
    }

    @Override
    public List<FuncBean> getFuncBeanList(FuncBean funcBean) {
        // logger.info("Service 根据条件获取功能列表");
        return funcDao.getFuncBeanList(funcBean);
    }

    @Override
    public boolean deleteFuncBean(String funcId) {
        logger.info("Service 删除功能");
        FuncBean func = funcDao.getFuncBean(funcId);
        if (func == null || func.getId() == 0) {
            throw new RuntimeException("funcId无对应功能对象funcId = " + funcId);
        }
        return funcDao.deleteFuncBean(funcId);
    }

    private FuncBean isAuthHaveFunc(FuncBean funcBean) {
        FuncBean func = new FuncBean();
        func.setAuthId(funcBean.getAuthId());
        List<FuncBean> list = funcDao.getFuncBeanList(func);
        if (null != list && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
