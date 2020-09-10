package com.monitor.argus.service.system.impl;

import com.monitor.argus.common.model.DataGrid;
import com.monitor.argus.bean.system.ModuleBean;
import com.monitor.argus.bean.system.RoleModuleBean;
import com.monitor.argus.dao.system.IModuleDao;
import com.monitor.argus.dao.system.IRoleModuleDao;
import com.monitor.argus.service.system.IModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * @Description:菜单模块Service接口实现类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午09:16:54
 * @Version: V1.0
 */
@Transactional(rollbackFor = Exception.class)
@Repository("moduleService")
public class ModuleServiceImpl implements IModuleService {

    private final Logger logger = LoggerFactory.getLogger(ModuleServiceImpl.class);

    @Autowired
    private IRoleModuleDao roleModuleService;
    @Autowired
    private IModuleDao moduleDao;

    @Override
    public boolean addModuleBean(ModuleBean moduleBean) {
        logger.info("Service 插入菜单模块信息");
        return moduleDao.addModuleBean(moduleBean);
    }

    @Override
    public boolean deleteModuleBean(ModuleBean moduleBean) {
        logger.info("Service 删除菜单模块信息");
        return moduleDao.deleteModuleBean(moduleBean);
    }

    @Override
    public boolean updateModuleBean(ModuleBean moduleBean) {
        logger.info("Service 修改菜单模块信息");
        return moduleDao.updateModuleBean(moduleBean);
    }

    @Override
    public ModuleBean getModuleBean(ModuleBean moduleBean) {
        logger.info("Service 获取菜单模块信息");
        return moduleDao.getModuleBean(moduleBean);
    }

    @Override
    public List<ModuleBean> getModuleBeanList(ModuleBean moduleBean) {
        logger.info("Service 获取菜单模块信息List");
        return moduleDao.getModuleBeanList(moduleBean);
    }

    @Override
    public DataGrid<ModuleBean> moduleBeanDataGrid(ModuleBean moduleBean) {
        DataGrid<ModuleBean> dg = new DataGrid<ModuleBean>();
        List<ModuleBean> moduleBeanList = moduleDao.getModuleBeanList(moduleBean);
        Long count = moduleDao.getModuleBeanListCount(moduleBean);
        dg.setRows(moduleBeanList);
        dg.setTotal(count);
        logger.info("Service 查询菜单模块列表信息进行页面展示 - E");
        return dg;
    }

    @Override
    public List<ModuleBean> getModulesByRoleId(String roleId) throws Exception {
        RoleModuleBean roleModuleBean = new RoleModuleBean();
        roleModuleBean.setRoleId(roleId);
        List<RoleModuleBean> roleModuleList = roleModuleService.getRoleModuleBeanList(roleModuleBean);
        if (roleModuleList != null && roleModuleList.size() > 0) {
            List<ModuleBean> modules = new ArrayList<ModuleBean>();
            for (RoleModuleBean prive : roleModuleList) {
                ModuleBean m = new ModuleBean();
                m.setId(prive.getModuleId());
                m = this.getModuleBean(m);
                modules.add(m);
            }
            return modules;
        }
        return null;
    }

    @Override
    public List<ModuleBean> getRoleModules(String roleId) throws Exception {
        // 1.查询根菜单模块列表
        RoleModuleBean rootModuleBeanCon = new RoleModuleBean();
        rootModuleBeanCon.setRoleId(roleId);
        /** 查询条件-父模块ID：-1-非跟模块的所有模块... */
        rootModuleBeanCon.setParentId(-1);
        /** 查询条件-是够被设置为某roleId的模块:1-是 */
        rootModuleBeanCon.setSeted(1);

        List<ModuleBean> subSetedModuleList = this.getRoleSubModuleBeanList(rootModuleBeanCon);
        // 父模块id 集合
        Set<Integer> parentIdSet = new TreeSet<Integer>();
        if (subSetedModuleList != null && subSetedModuleList.size() > 0) {
            for (ModuleBean subSetedModule : subSetedModuleList) {
                parentIdSet.add(subSetedModule.getParentId());
            }
        }
        // 根模块list
        List<ModuleBean> rootModuleList = new ArrayList<ModuleBean>();
        for (Integer parentId : parentIdSet) {
            ModuleBean moduleBea = new ModuleBean();
            moduleBea.setId(parentId);
            moduleBea = this.getModuleBean(moduleBea);
            if (moduleBea != null) {
                rootModuleList.add(moduleBea);
            }
        }

        if (rootModuleList != null && rootModuleList.size() > 0) {
            for (ModuleBean rootModule : rootModuleList) {
                // 2.根据根菜单模块查询其子菜单模块列表
                RoleModuleBean subModuleBeanCon = new RoleModuleBean();
                subModuleBeanCon.setRoleId(roleId);
                subModuleBeanCon.setParentId(rootModule.getId());
                /** 查询条件-是够被设置为某roleId的模块:1-是 */
                subModuleBeanCon.setSeted(1);
                List<ModuleBean> subModuleList = this.getRoleSubModuleBeanList(subModuleBeanCon);
                if (subModuleList != null && subModuleList.size() > 0) {
                    rootModule.setSubModules(subModuleList);
                }
            }
            return rootModuleList;
        }
        return null;
    }

    @Override
    public List<ModuleBean> getRoleSubModuleBeanList(RoleModuleBean roleModuleBean) {
        logger.info("Service 获取角色信息List");
        return moduleDao.getRoleSubModuleBeanList(roleModuleBean);
    }

    @Override
    public List<ModuleBean> getRoleModulesForSet(String roleId) throws Exception {
        // 1.查询根菜单模块列表
        RoleModuleBean rootModuleBeanCon = new RoleModuleBean();
        rootModuleBeanCon.setRoleId(roleId);
        /** 查询条件-父模块ID：-1-非跟模块的所有模块... */
        rootModuleBeanCon.setParentId(0);
        /** 查询条件-是够被设置为某roleId的模块:1-是 */
        rootModuleBeanCon.setSeted(null);

        List<ModuleBean> rootModuleList = this.getRoleSubModuleBeanList(rootModuleBeanCon);

        if (rootModuleList != null && rootModuleList.size() > 0) {
            for (ModuleBean rootModule : rootModuleList) {
                // 2.根据根菜单模块查询其子菜单模块列表
                RoleModuleBean subModuleBeanCon = new RoleModuleBean();
                subModuleBeanCon.setRoleId(roleId);
                subModuleBeanCon.setParentId(rootModule.getId());
                /** 查询条件-是够被设置为某roleId的模块:1-是 */
                rootModuleBeanCon.setSeted(null);
                List<ModuleBean> subModuleList = this.getRoleSubModuleBeanList(subModuleBeanCon);
                if (subModuleList != null && subModuleList.size() > 0) {
                    rootModule.setSubModules(subModuleList);
                }
            }
            return rootModuleList;
        }
        return null;
    }

}
