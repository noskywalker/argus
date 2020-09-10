package com.monitor.argus.dao.system.impl;

import com.monitor.argus.bean.system.ModuleBean;
import com.monitor.argus.bean.system.RoleModuleBean;
import com.monitor.argus.dao.mybatis.IBaseDao;
import com.monitor.argus.dao.system.IModuleDao;
import com.monitor.argus.dao.system.IRoleModuleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * @Description:菜单模块Dao接口实现类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午09:16:54
 * @Version: V1.0
 */
@Transactional(rollbackFor = Exception.class)
@Repository("moduleDao")
public class ModuleDaoImpl implements IModuleDao {

    @Autowired
    private IBaseDao baseDao;
    private final Logger logger = LoggerFactory.getLogger(ModuleDaoImpl.class);

    @Autowired
    private IRoleModuleDao roleModuleDao;

    @Override
    public boolean addModuleBean(ModuleBean moduleBean) {
        logger.info("Dao 插入菜单模块信息");
        moduleBean.setStatus(1);
        moduleBean.setCreateTime(new Date());
        return baseDao.insert("moduleBeanMapper.insert", moduleBean);
    }

    @Override
    public boolean deleteModuleBean(ModuleBean moduleBean) {
        logger.info("Dao 删除菜单模块信息");
        moduleBean.setStatus(0);
        moduleBean.setUpdateTime(new Date());
        return baseDao.update("moduleBeanMapper.updateByPrimaryKeySelective", moduleBean);
    }

    @Override
    public boolean updateModuleBean(ModuleBean moduleBean) {
        logger.info("Dao 修改菜单模块信息");
        moduleBean.setUpdateTime(new Date());
        return baseDao.update("moduleBeanMapper.updateByPrimaryKeySelective", moduleBean);
    }

    @Override
    public ModuleBean getModuleBean(ModuleBean moduleBean) {
        logger.info("Dao 获取菜单模块信息");
        return baseDao.get("moduleBeanMapper.getModuleBean", moduleBean);
    }

    @Override
    public List<ModuleBean> getModuleBeanList(ModuleBean moduleBean) {
        logger.info("Dao 获取菜单模块信息List");
        return baseDao.getList("moduleBeanMapper.getModuleBeanList", moduleBean);
    }

    @Override
    public long getModuleBeanListCount(ModuleBean moduleBean) {
        logger.info("Dao 获取菜单模块信息List");
        return baseDao.get("moduleBeanMapper.getModuleBeanListCount", moduleBean);
    }

    @Override
    public List<ModuleBean> getRoleSubModuleBeanList(RoleModuleBean roleModuleBean) {
        logger.info("Dao 根据角色菜单 获取菜单模块信息List");
        return baseDao.getList("moduleBeanMapper.getRoleSubModuleBeanList", roleModuleBean);
    }

}
