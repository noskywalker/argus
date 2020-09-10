/** */
package com.monitor.argus.dao.redis.impl;

import com.monitor.argus.dao.redis.IRedisBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @Description:
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-5-26 上午11:32:20
 * @Version: V1.0
 * 
 */
@Repository("redisBaseDao")
public class RedisBaseDaoImpl implements IRedisBaseDao {

    private static final Logger logger = LoggerFactory.getLogger(RedisBaseDaoImpl.class);
    @Autowired
    private ShardedJedisPool shardedJedisPool;

    /**
     * @Description:取得redis的客户端资源
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-5-26 上午11:32:20
     * @Version: V1.0
     * 
     * @return
     * 
     */
    @Override
    public ShardedJedis getResource() {
        try {
            ShardedJedis shardedJedis = shardedJedisPool.getResource();
            String host = shardedJedis.getShardInfo("").getHost();

            logger.debug("Get a resource from the sharded jedis pool - success{}", host);
            return shardedJedis;
        } catch (Exception e) {
            logger.error("Get a resource from the sharded jedis pool - error{}", e);
        }
        return null;
    }

    /**
     * @Description:将资源返还给pool
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-5-26 上午11:32:20
     * @Version: V1.0
     * 
     * @param shardedJedis
     * 
     */
    @Override
    public void returnResource(ShardedJedis shardedJedis) {
        shardedJedisPool.returnResource(shardedJedis);
        logger.info("Return the resource to the sharded jedis pool - end");
    }

    /**
     * @Description:出现异常后，将资源返还给pool
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-5-26 上午11:32:20
     * @Version: V1.0
     * 
     * @param shardedJedis
     * @param broken
     * 
     */
    @Override
    public void returnResource(ShardedJedis shardedJedis, boolean broken) {
        if (broken) {
            shardedJedisPool.returnBrokenResource(shardedJedis);
        } else {
            shardedJedisPool.returnResource(shardedJedis);
        }
        logger.info("Return the resource to the sharded jedis pool - end");
    }
}
