/** */
package com.monitor.argus.dao.redis;

import redis.clients.jedis.ShardedJedis;

/**
 * @Description:
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-5-26 上午11:27:26
 * @Version: V1.0
 */
public interface IRedisBaseDao {

    /**
     * @Description:取得redis的客户端，可以执行命令了。
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-5-26 上午11:28:18
     * @Version: V1.0
     * 
     * @return
     * 
     */
    public abstract ShardedJedis getResource();

    /**
     * @Description:将资源返还给pool
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-5-26 上午11:31:36
     * @Version: V1.0
     * 
     * @param shardedJedis
     * 
     */
    public void returnResource(ShardedJedis shardedJedis);

    /**
     * @Description:出现异常后，将资源返还给pool
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-5-26 上午11:31:40
     * @Version: V1.0
     * 
     * @param shardedJedis
     * @param broken
     * 
     */
    public void returnResource(ShardedJedis shardedJedis, boolean broken);

}
