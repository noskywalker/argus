package com.monitor.argus.service.system;

import com.monitor.argus.bean.system.FuncBean;

import java.util.List;

/**
 * Created by wangfeng on 16/8/23.
 */
public interface ISystemFuncService {
    /**
     * 添加功能
     *
     * @param funcBean
     * @return
     */
    public boolean addFuncBean(FuncBean funcBean);

    /**
     * 更新功能
     *
     * @param funcBean
     * @return
     */
    public boolean updateFuncBean(FuncBean funcBean);

    /**
     * 获取功能详情
     *
     * @param funcId
     * @return
     */
    public FuncBean getFuncBean(String funcId);

    /**
     * 获取功能列表
     *
     * @param funcBean
     * @return
     */
    public List<FuncBean> getFuncBeanList(FuncBean funcBean);

    /**
     * 删除功能
     */

    public boolean deleteFuncBean(String funcId);

}


