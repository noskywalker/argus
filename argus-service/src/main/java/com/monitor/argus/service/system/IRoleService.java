package com.monitor.argus.service.system;

import com.monitor.argus.common.model.DataGrid;
import com.monitor.argus.bean.system.RoleBean;

import java.util.List;


/**
 * @Description:角色service接口
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午09:17:12
 * @Version: V1.0
 */
public interface IRoleService {

    /**
     * 增加：角色信息
     * 
     * @param roleBean
     * @return
     * @Author null
     * @Version V1.0
     * @Date 2014-5-4 下午02:15:24
     * 
     */
    public boolean addRoleBean(RoleBean roleBean);

    /**
     * 删除：角色信息
     * 
     * @param roleBean
     * @return
     * @Author null
     * @Date 2014-5-4 下午02:15:24
     * @Version V1.0
     * 
     */
    public boolean deleteRoleBean(RoleBean roleBean);

    /**
     * 修改：角色信息
     * 
     * @param roleBean 角色信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:00
     * @Version V1.0
     * 
     */
    public boolean updateRoleBean(RoleBean roleBean);

    /**
     * 查找一条：角色信息
     * 
     * @param roleBean 角色信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:07
     * @Version V1.0
     * 
     */
    public RoleBean getRoleBean(RoleBean roleBean);

    /**
     * 查找列表：角色信息
     * 
     * @param roleBean 角色信息
     * 
     * @return
     * @Author null
     * @Date 2014-4-28 下午02:58:20
     * @Version V1.0
     * 
     */
    public List<RoleBean> getRoleBeanList(RoleBean roleBean);

    /**
     * 获取角色DataGrid页面展示DataGrid
     * 
     * @param p2pservice
     * @return
     * @Author null
     * @Date 2014-5-19 下午05:44:09
     * @Version V1.0
     * 
     */
    public DataGrid<RoleBean> roleBeanDataGrid(RoleBean roleBean);
}
