package com.monitor.argus.dao.system;

import com.monitor.argus.bean.system.AuthBean;

import java.util.List;

/**
 * Created by wangfeng on 16/8/17.
 */
public interface ISystemAuthDao {
    /**
     * 增加权限
     *
     * @param authBean
     * @return
     */
    public boolean addAuthBean(AuthBean authBean);


    /**
     * 获取权限详情
     *
     * @param authId
     * @return
     */
    public AuthBean getAuthBean(String authId);


    /**
     * 获取权限列表
     *
     * @param authBean
     * @return
     */
    public List<AuthBean> getAuthBeanList(AuthBean authBean);


    /**
     * 更新权限
     *
     * @param authBean
     * @return
     */
    public boolean updateAuthBean(AuthBean authBean);


    /**
     * 统计权限总数
     *
     * @param authBean
     * @return
     */
    public int countAuthBeanList(AuthBean authBean);

    /**
     * 通过IDs获取权限列表
     */
    public List<AuthBean> getAuthListByIds(List<Integer> authIds);

    /**
     * 根据父类ID更新父类名称
     */
    public boolean updateParentInfo(AuthBean authBean);


}
