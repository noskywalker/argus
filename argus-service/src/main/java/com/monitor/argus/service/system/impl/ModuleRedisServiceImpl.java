/** */
package com.monitor.argus.service.system.impl;

import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.service.system.IModuleRedisService;
import com.monitor.argus.dao.redis.IRedisKeyBaseDao;
import com.monitor.argus.dao.redis.IRedisListBaseDao;
import com.monitor.argus.dao.redis.IRedisStringBaseDao;
import com.monitor.argus.bean.system.ModuleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description:对用户相关操作的redis service实现类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-27 下午06:16:11
 * @Version: V1.0
 * 
 */
@Repository("moduleRedisService")
public class ModuleRedisServiceImpl implements IModuleRedisService {

    @Autowired
    IRedisStringBaseDao redisStringBaseDao;

    @Autowired
    IRedisListBaseDao redisListBaseDao;

    @Autowired
    IRedisKeyBaseDao redisKeyBaseDao;

    /**
     * @Description:增加 moduleBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午06:16:11
     * @Version: V1.0
     * 
     * @param moduleBean
     * @return
     * 
     */
    @Override
    public Long addModuleBean(String key, ModuleBean moduleBean) {
        if (!(key != null && !key.isEmpty())) {
            return null;
        }
        List<ModuleBean> moduleBeanList = new ArrayList<ModuleBean>();
        moduleBeanList.add(moduleBean);
        return redisListBaseDao.rPush(key, moduleBeanList);
    }

    /**
     * @Description:批量增加 moduleBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午06:16:11
     * @Version: V1.0
     * 
     * @param list
     * @return
     * 
     */
    @Override
    public Long addModuleBeanList(String key, List<ModuleBean> moduleBeanList) {
        if (!(key != null && !key.isEmpty())) {
            return null;
        }
        return redisListBaseDao.rPush(key, moduleBeanList);
    }

    /**
     * @Description:删除moduleBean
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午06:16:11
     * @Version: V1.0
     * 
     * @param key
     * 
     */
    @Override
    public void deleteModuleBean(String key) {
        redisKeyBaseDao.del(key);
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午06:16:11
     * @Version: V1.0
     * 
     * @param keys
     * 
     */
    @Override
    public void deleteModuleBeanList(List<String> keys) {
        redisKeyBaseDao.del(keys);
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午06:16:11
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    @Override
    public ModuleBean getModuleBean(String key) {
        if (!(key != null && !key.isEmpty())) {
            return null;
        }
        String jsonStr = redisStringBaseDao.get(key);
        ModuleBean moduleBean = (ModuleBean) JsonUtil.jsonToBean(jsonStr, ModuleBean.class);
        return moduleBean;
    }

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
    public List<ModuleBean> getModuleBeanList(String key) {
        List<ModuleBean> moduleBeanList = new ArrayList<ModuleBean>();
        List<String> jsonList = redisListBaseDao.lRange(key, 0, -1);
        for (String jsonStr : jsonList) {
            ModuleBean moduleBean = (ModuleBean) JsonUtil.jsonToBean(jsonStr, ModuleBean.class);
            moduleBeanList.add(moduleBean);
        }
        return moduleBeanList;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午06:16:11
     * @Version: V1.0
     * 
     * @param moduleBean
     * @return
     * 
     */
    @Override
    public boolean updateModuleBean(ModuleBean moduleBean) {
        // TODO
        return false;
    }

}
