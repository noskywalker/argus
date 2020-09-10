package com.monitor.argus.dao.system.impl;

import com.monitor.argus.bean.system.RoleBean;
import com.monitor.argus.common.util.UuidUtil;
import com.monitor.argus.dao.mybatis.IBaseDao;
import com.monitor.argus.dao.system.IRoleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


/**
 * @Description:角色Dao接口实现类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午09:16:54
 * @Version: V1.0
 */
@Repository("roleDao")
public class RoleDaoImpl implements IRoleDao {

    @Autowired
    private IBaseDao baseDao;
    private final Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);

    @Override
    public boolean addRoleBean(RoleBean roleBean) {
        logger.info("Dao 插入角色信息");
        roleBean.setId(UuidUtil.getUUID());
        roleBean.setCreateTime(new Date());
        return baseDao.insert("roleBeanMapper.insert", roleBean);
    }

    @Override
    public boolean deleteRoleBean(RoleBean roleBean) {
        logger.info("Dao 删除角色信息");
        roleBean.setStatus(0);
        roleBean.setUpdateTime(new Date());
        return baseDao.update("roleBeanMapper.updateByPrimaryKeySelective", roleBean);
    }

    @Override
    public boolean updateRoleBean(RoleBean roleBean) {
        logger.info("Dao 修改角色信息");
        roleBean.setUpdateTime(new Date());
        return baseDao.update("roleBeanMapper.updateByPrimaryKeySelective", roleBean);
    }

    @Override
    public RoleBean getRoleBean(RoleBean roleBean) {
        logger.info("Dao 获取角色信息");
        return baseDao.get("roleBeanMapper.getRoleBean", roleBean);
    }

    @Override
    public List<RoleBean> getRoleBeanList(RoleBean roleBean) {
        logger.info("Dao 获取角色信息List");
        return baseDao.getList("roleBeanMapper.getRoleBeanList", roleBean);
    }

    @Override
    public long getRoleBeanListCount(RoleBean roleBean) {
        logger.info("Dao 获取角色信息List");
        return baseDao.get("roleBeanMapper.getRoleBeanListCount", roleBean);
    }

}
