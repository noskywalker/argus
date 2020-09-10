package com.monitor.argus.dao.redis;

import java.util.Map;
import java.util.Set;

/**
 * @Description:Redis Set BaseDao接口类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-28 上午11:03:17
 * @Version: V1.0
 * 
 * 
 */
public interface IRedisSetBaseDao {

    /**
     * 存入set
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hset(String key, String field, String value);

    /**
     * 删除set中的某几项
     * 
     * @param key
     * @param fields
     * @return
     */
    public Long hdel(String key, String... fields);

    /**
     * 查找set中所有值
     * 
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key);

    /**
     * 查找set
     * 
     * @param key
     * @return
     */
    public Set<String> hkeys(String key);
}
