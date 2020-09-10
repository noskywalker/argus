package com.monitor.argus.dao.system.impl;

import com.monitor.argus.bean.system.RoleModuleBean;
import com.monitor.argus.common.util.UuidUtil;
import com.monitor.argus.dao.mybatis.IBaseDao;
import com.monitor.argus.dao.system.IRoleModuleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Description:角色菜单模块Dao接口实现类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午09:16:54
 * @Version: V1.0
 */
@Repository("roleModuleDao")
public class RoleModuleDaoImpl implements IRoleModuleDao {

    @Autowired
    private IBaseDao baseDao;
    private final Logger logger = LoggerFactory.getLogger(RoleModuleDaoImpl.class);

    @Override
    public boolean addRoleModuleBean(RoleModuleBean roleModuleBean) {
        logger.info("Dao 插入角色菜单模块信息");
        roleModuleBean.setId(UuidUtil.getUUID());
        roleModuleBean.setCreateTime(new Date());
        return baseDao.insert("roleModuleBeanMapper.insert", roleModuleBean);
    }

    @Override
    public boolean deleteRoleModuleBean(RoleModuleBean roleModuleBean) {
        logger.info("Dao 删除角色菜单模块信息");
        roleModuleBean.setStatus(0);
        roleModuleBean.setUpdateTime(new Date());
        return baseDao.update("roleModuleBeanMapper.updateByPrimaryKeySelective", roleModuleBean);
    }

    @Override
    public boolean updateRoleModuleBean(RoleModuleBean roleModuleBean) {
        logger.info("Dao 修改角色菜单模块信息");
        roleModuleBean.setUpdateTime(new Date());
        return baseDao.update("roleModuleBeanMapper.updateByPrimaryKeySelective", roleModuleBean);
    }

    @Override
    public RoleModuleBean getRoleModuleBean(RoleModuleBean roleModuleBean) {
        logger.info("Dao 获取角色菜单模块信息");
        return baseDao.get("roleModuleBeanMapper.getRoleModuleBean", roleModuleBean);
    }

    @Override
    public List<RoleModuleBean> getRoleModuleBeanList(RoleModuleBean roleModuleBean) {
        logger.info("Dao 获取角色菜单模块信息List");
        return baseDao.getList("roleModuleBeanMapper.getRoleModuleBeanList", roleModuleBean);
    }

    public long getRoleModuleBeanListCount(RoleModuleBean roleModuleBean) {
        logger.info("Dao 获取角色菜单模块信息count");
        return baseDao.get("roleModuleBeanMapper.getRoleModuleBeanListCount", roleModuleBean);
    }
}
