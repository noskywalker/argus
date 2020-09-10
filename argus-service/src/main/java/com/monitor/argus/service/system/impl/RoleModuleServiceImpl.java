package com.monitor.argus.service.system.impl;

import com.monitor.argus.common.model.DataGrid;
import com.monitor.argus.bean.system.RoleModuleBean;
import com.monitor.argus.dao.system.IRoleModuleDao;
import com.monitor.argus.service.system.IRoleModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @Description:角色Service接口实现类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午09:16:54
 * @Version: V1.0
 */
@Repository("roleModuleService")
public class RoleModuleServiceImpl implements IRoleModuleService {

    private final Logger logger = LoggerFactory.getLogger(RoleModuleServiceImpl.class);
    @Autowired
    private IRoleModuleDao roleModuleDao;

    @Override
    public boolean addRoleModuleBean(RoleModuleBean roleModuleBean) {
        logger.info("Service 插入角色信息");
        return roleModuleDao.addRoleModuleBean(roleModuleBean);
    }

    @Override
    public boolean deleteRoleModuleBean(RoleModuleBean roleModuleBean) {
        logger.info("Service 删除角色信息");
        return roleModuleDao.deleteRoleModuleBean(roleModuleBean);
    }

    @Override
    public boolean updateRoleModuleBean(RoleModuleBean roleModuleBean) {
        logger.info("Service 修改角色信息");
        return roleModuleDao.updateRoleModuleBean(roleModuleBean);
    }

    @Override
    public RoleModuleBean getRoleModuleBean(RoleModuleBean roleModuleBean) {
        logger.info("Service 获取角色信息");
        return roleModuleDao.getRoleModuleBean(roleModuleBean);
    }

    @Override
    public List<RoleModuleBean> getRoleModuleBeanList(RoleModuleBean roleModuleBean) {
        logger.info("Service 获取角色信息List");
        return roleModuleDao.getRoleModuleBeanList(roleModuleBean);
    }

    @Override
    public DataGrid<RoleModuleBean> roleModuleBeanDataGrid(RoleModuleBean roleModuleBean) {
        DataGrid<RoleModuleBean> dg = new DataGrid<RoleModuleBean>();
        List<RoleModuleBean> roleModuleBeanList = roleModuleDao.getRoleModuleBeanList(roleModuleBean);
        Long count = roleModuleDao.getRoleModuleBeanListCount(roleModuleBean);
        dg.setRows(roleModuleBeanList);
        dg.setTotal(count);
        logger.info("Service 查询角色列表信息进行页面展示 - E");
        return dg;
    }
}
