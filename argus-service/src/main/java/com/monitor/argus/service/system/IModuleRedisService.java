/** */
package com.monitor.argus.service.system;

import com.monitor.argus.bean.system.ModuleBean;

import java.util.List;


/**
 * @Description:对菜单模块相关操作的redis service接口
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-27 下午06:15:04
 * @Version: V1.0
 * 
 */
public interface IModuleRedisService {

    /**
     * @Description:增加 moduleBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:51:58
     * @Version: V1.0
     * 
     * @param ModuleBean
     * @return
     * 
     */
    public Long addModuleBean(String key, ModuleBean moduleBean);

    /**
     * @Description:批量增加 moduleBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:53:20
     * @Version: V1.0
     * 
     * @param list
     * @return
     * 
     */
    public Long addModuleBeanList(String key, List<ModuleBean> list);

    /**
     * @Description:删除moduleBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:53:55
     * @Version: V1.0
     * 
     * @param key
     * 
     */
    public void deleteModuleBean(String key);

    /**
     * @Description:删除多个moduleBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:54:34
     * @Version: V1.0
     * 
     * @param keys
     * 
     */
    public void deleteModuleBeanList(List<String> keys);

    /**
     * @Description:修改moduleBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:54:58
     * @Version: V1.0
     * 
     * @param moduleBean
     * @return
     * 
     */
    public boolean updateModuleBean(ModuleBean moduleBean);

    /**
     * @Description: 通过key获取moduleBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:55:16
     * @Version: V1.0
     * 
     * @param keyId
     * @return
     * 
     */
    public ModuleBean getModuleBean(String keyId);

    /**
     * @Description: 通过key获取moduleBeanList
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:55:16
     * @Version: V1.0
     * 
     * @param keyId
     * @return
     * 
     */
    public List<ModuleBean> getModuleBeanList(String key);
}
