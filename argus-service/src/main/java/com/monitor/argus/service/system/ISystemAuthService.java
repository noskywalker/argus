package com.monitor.argus.service.system;

import com.monitor.argus.bean.system.AuthBean;

/**
 * @Description:权限管理接口 Created by wangfeng on 16/8/22.
 */
public interface ISystemAuthService {
    /**
     * 添加权限
     *
     * @return
     */
    public boolean addAuthBean(AuthBean authBean);

    /**
     * 更新权限
     *
     * @return
     */
    public boolean updateAuthBean(AuthBean authBean);

    /**
     * 获得权限详情
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
    public Object getAuthBeanList(Integer userId, AuthBean authBean, Integer flag);

}
