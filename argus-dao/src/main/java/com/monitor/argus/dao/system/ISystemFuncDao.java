package com.monitor.argus.dao.system;

import com.monitor.argus.bean.system.AuthBean;
import com.monitor.argus.bean.system.FuncBean;

import java.util.List;

/**
 * Created by wangfeng on 16/8/18.
 */
public interface ISystemFuncDao {
    /**
     * 增加功能
     *
     * @param funcBean
     * @return
     */
    public boolean addFuncBean(FuncBean funcBean);

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
     * 更新功能
     *
     * @param funcBean
     * @return
     */
    public boolean updateFuncBean(FuncBean funcBean);

    /**
     * 统计功能总数
     *
     * @param funcBean
     * @return
     */
    public int countFuncBeanList(FuncBean funcBean);

    /**
     * 删除功能
     *
     * @param funcId
     * @return
     */
    public boolean deleteFuncBean(String funcId);

    /**
     * 根据权限IDS获取功能list
     */
    public List<FuncBean> getListByAuth(List<Integer> authIds);

    /**
     * 更新父类信息
     */
    public boolean updateAuthInfo(AuthBean authBean);

}
