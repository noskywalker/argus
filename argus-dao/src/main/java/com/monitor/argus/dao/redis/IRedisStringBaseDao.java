package com.monitor.argus.dao.redis;

/**
 * @Description:Redis String BaseDao接口类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-27 下午10:31:56
 * @Version: V1.0
 */
public interface IRedisStringBaseDao {

    /**
     * @Description: 通过key获取
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-27 下午05:55:16
     * @Version: V1.0
     * 
     * @param keyId
     * @return
     * 
     */
    public String get(String key);

    public byte[] getRedisBytes(String key);

    /**
     * @Description: 为key设置value
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 上午10:34:54
     * @Version: V1.0
     * 
     * @param key
     * @param value
     * 
     */
    public String set(String key, String value);

    public String set(String key, byte[] value);

    public String setEx(String key, int seconds, byte[] value);

    public Long del(String key);

    /**
     * @Description:为key设置value, 当且仅当key不存在.
     * 
     *                           only if key does not exist.
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 上午10:36:21
     * @Version: V1.0
     * 
     * @param key
     * @param value
     * @return
     * 
     */
    public Long setNX(String key, String value);

    /**
     * @Description:Set the {@code value} and expiration in {@code seconds} for
     *                  {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 上午10:39:29
     * @Version: V1.0
     * 
     * @param key
     * @param seconds
     * @param value
     * 
     */
    public String setEx(String key, int seconds, String value);

    /**
     * @Description:Set the {@code value} and expiration in {@code milliseconds}
     *                  for {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 上午10:39:42
     * @Version: V1.0
     * 
     * @param key
     * @param milliseconds
     * @param value
     * 
     */
    public void pSetEx(String key, long milliseconds, String value);

    /**
     * @Description:Get the length of the value stored at {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 上午10:41:25
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    public Long strLen(String key);

    /**
     * 获取指定key的到期剩余时间，间接实现update并且不更改ttl的功能
     * 
     * @param key
     * @return
     */
    public Long ttl(String key);
}
