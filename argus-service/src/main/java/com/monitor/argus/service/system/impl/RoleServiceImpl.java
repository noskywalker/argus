package com.monitor.argus.service.system.impl;

import com.monitor.argus.common.model.DataGrid;
import com.monitor.argus.bean.system.RoleBean;
import com.monitor.argus.dao.system.IRoleDao;
import com.monitor.argus.service.system.IRoleService;
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
@Repository("roleService")
public class RoleServiceImpl implements IRoleService {

    private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Autowired
    private IRoleDao roleDao;

    @Override
    public boolean addRoleBean(RoleBean roleBean) {
        logger.info("Service 插入角色信息");
        return roleDao.addRoleBean(roleBean);
    }

    @Override
    public boolean deleteRoleBean(RoleBean roleBean) {
        logger.info("Service 删除角色信息");
        boolean boo = roleDao.deleteRoleBean(roleBean);
        return boo;
    }

    @Override
    public boolean updateRoleBean(RoleBean roleBean) {
        logger.info("Service 修改角色信息");
        boolean boo = roleDao.updateRoleBean(roleBean);
        return boo;
    }

    @Override
    public RoleBean getRoleBean(RoleBean roleBean) {
        logger.info("Service 获取角色信息");
        return roleDao.getRoleBean(roleBean);
    }

    @Override
    public List<RoleBean> getRoleBeanList(RoleBean roleBean) {
        logger.info("Service 获取角色信息List");
        return roleDao.getRoleBeanList(roleBean);
    }

    @Override
    public DataGrid<RoleBean> roleBeanDataGrid(RoleBean roleBean) {
        logger.info("Service 查询角色列表信息进行页面展示 - S");
        DataGrid<RoleBean> dg = new DataGrid<RoleBean>();
        List<RoleBean> roleBeanList = roleDao.getRoleBeanList(roleBean);
        Long count = roleDao.getRoleBeanListCount(roleBean);
        dg.setRows(roleBeanList);
        dg.setTotal(count);
        logger.info("Service 查询角色列表信息进行页面展示 - E");
        return dg;
    }
}
