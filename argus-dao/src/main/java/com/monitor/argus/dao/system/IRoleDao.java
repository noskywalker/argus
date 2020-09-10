package com.monitor.argus.dao.system;


import com.monitor.argus.bean.system.RoleBean;

import java.util.List;

/**
 * @Description:角色Dao接口
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-3-18 下午09:17:12
 * @Version: V1.0
 */
public interface IRoleDao {

    /**
     * 增加：角色信息
     * 
     * @param roleBean
     * @return
     * @Author null
     * @Date 2014-5-4 下午02:15:24
     * @Version V1.0
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
     * 获取角色总数
     * 
     * @author null
     * @date 2016年5月6日 下午4:00:57
     * 
     * @param roleBean
     * @return
     */
    public long getRoleBeanListCount(RoleBean roleBean);
}
