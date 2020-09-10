package com.monitor.argus.service.system;

import com.monitor.argus.common.model.DataGrid;
import com.monitor.argus.bean.system.ModuleBean;
import com.monitor.argus.bean.system.RoleModuleBean;

import java.util.List;


/**
 * @Description:用户service接口
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午09:17:12
 * @Version: V1.0
 */
public interface IModuleService {

    /**
     * 增加：用户信息
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
     * 删除：用户信息
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
     * 修改：用户信息
     * 
     * @param moduleBean 用户信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:00
     * @Version V1.0
     * 
     */
    public boolean updateModuleBean(ModuleBean moduleBean);

    /**
     * 查找一条：用户信息
     * 
     * @param moduleBean 用户信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:07
     * @Version V1.0
     * 
     */
    public ModuleBean getModuleBean(ModuleBean moduleBean);

    /**
     * 查找列表：用户信息
     * 
     * @param moduleBean 用户信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:20
     * @Version V1.0
     * 
     */
    public List<ModuleBean> getModuleBeanList(ModuleBean moduleBean);

    /**
     * 获取用户DataGrid页面展示DataGrid
     * 
     * @param p2pservice
     * @return
     * @Author null
     * @Date 2014-5-19 下午05:44:09
     * @Version V1.0
     * 
     */
    public DataGrid moduleBeanDataGrid(ModuleBean moduleBean);

    /**
     * @Description: TODO 根据角色ID和父模块ID查询模块信息
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-3-18 下午11:01:59
     * @Version: V1.0
     * 
     * @param roleId
     * @return
     * @throws Exception
     * 
     */
    public List<ModuleBean> getRoleModules(String roleId) throws Exception;

    /**
     * @Description: TODO 根据角色ID和父模块ID查询模块信息
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-3-19 上午11:51:44
     * @Version: V1.0
     * 
     * @param roleModuleBean
     * @return
     * 
     */
    public List<ModuleBean> getRoleSubModuleBeanList(RoleModuleBean roleModuleBean);

    /**
     * @Description:为分配角色：根据角色ID和父模块ID查询模块信息
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-3-18 下午11:01:59
     * @Version: V1.0
     * 
     * @param roleId
     * @return
     * @throws Exception
     * 
     */
    public List<ModuleBean> getRoleModulesForSet(String roleId) throws Exception;

    /**
     * @Description: 根据角色获取模块
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-3-19 下午12:00:05
     * @Version: V1.0
     * 
     * @param roleId
     * @return
     * @throws Exception
     * 
     */
    public List<ModuleBean> getModulesByRoleId(String roleId) throws Exception;
}
