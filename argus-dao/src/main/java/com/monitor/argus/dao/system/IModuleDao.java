package com.monitor.argus.dao.system;

import com.monitor.argus.bean.system.ModuleBean;
import com.monitor.argus.bean.system.RoleModuleBean;

import java.util.List;


/**
 * @Description:菜单模块Dao接口
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午09:17:12
 * @Version: V1.0
 */
public interface IModuleDao {

    /**
     * 增加：菜单模块信息
     * 
     * @param moduleBean
     * @return
     * @Author null
     * @Date 2014-5-4 下午02:15:24
     * @Version V1.0
     * 
     */
    public boolean addModuleBean(ModuleBean moduleBean);

    /**
     * 删除：菜单模块信息
     * 
     * @param moduleBean
     * @return
     * @Author null
     * @Date 2014-5-4 下午02:15:24
     * @Version V1.0
     * 
     */
    public boolean deleteModuleBean(ModuleBean moduleBean);

    /**
     * 修改：菜单模块信息
     * 
     * @param moduleBean 菜单模块信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:00
     * @Version V1.0
     * 
     */
    public boolean updateModuleBean(ModuleBean moduleBean);

    /**
     * 查找一条：菜单模块信息
     * 
     * @param moduleBean 菜单模块信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:07
     * @Version V1.0
     * 
     */
    public ModuleBean getModuleBean(ModuleBean moduleBean);

    /**
     * 查找列表：菜单模块信息
     * 
     * @param moduleBean 菜单模块信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:20
     * @Version V1.0
     * 
     */
    public List<ModuleBean> getModuleBeanList(ModuleBean moduleBean);

    /**
     * 查找列表：菜单模块信息 count
     * 
     * @param moduleBean 菜单模块信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:20
     * @Version V1.0
     * 
     */
    public long getModuleBeanListCount(ModuleBean moduleBean);

    /**
     * 查找列表：根据角色，获取菜单模块信息
     * 
     * @param roleModuleBean 菜单模块信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:20
     * @Version V1.0
     * 
     */
    public List<ModuleBean> getRoleSubModuleBeanList(RoleModuleBean roleModuleBean);

}
