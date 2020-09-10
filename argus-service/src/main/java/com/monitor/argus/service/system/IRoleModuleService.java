package com.monitor.argus.service.system;

import com.monitor.argus.common.model.DataGrid;
import com.monitor.argus.bean.system.RoleModuleBean;

import java.util.List;

/**
 * @Description:角色的菜单模块service接口
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午09:17:12
 * @Version: V1.0
 */
public interface IRoleModuleService {

    /**
     * 增加：角色的菜单模块信息
     * 
     * @param roleModuleBean
     * @return
     * @Author null
     * @Date 2014-5-4 下午02:15:24
     * @Version V1.0
     * 
     */
    public boolean addRoleModuleBean(RoleModuleBean roleModuleBean);

    /**
     * 删除：角色的菜单模块信息
     * 
     * @param roleModuleBean
     * @return
     * @Author null
     * @Date 2014-5-4 下午02:15:24
     * @Version V1.0
     * 
     */
    public boolean deleteRoleModuleBean(RoleModuleBean roleModuleBean);

    /**
     * 修改：角色的菜单模块信息
     * 
     * @param roleModuleBean 角色的菜单模块信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:00
     * @Version V1.0
     * 
     */
    public boolean updateRoleModuleBean(RoleModuleBean roleModuleBean);

    /**
     * 查找一条：角色的菜单模块信息
     * 
     * @param roleModuleBean 角色的菜单模块信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:07
     * @Version V1.0
     * 
     */
    public RoleModuleBean getRoleModuleBean(RoleModuleBean roleModuleBean);

    /**
     * 查找列表：角色的菜单模块信息
     * 
     * @param roleModuleBean 角色的菜单模块信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:20
     * @Version V1.0
     * 
     */
    public List<RoleModuleBean> getRoleModuleBeanList(RoleModuleBean roleModuleBean);

    /**
     * 获取角色的菜单模块DataGrid页面展示DataGrid
     * 
     * @param p2pservice
     * @return
     * @Author null
     * @Date 2014-5-19 下午05:44:09
     * @Version V1.0
     * 
     */
    public DataGrid roleModuleBeanDataGrid(RoleModuleBean roleModuleBean);
}
