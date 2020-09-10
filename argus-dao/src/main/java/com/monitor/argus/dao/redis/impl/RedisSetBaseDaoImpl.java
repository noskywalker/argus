/** */
package com.monitor.argus.dao.redis.impl;

import com.monitor.argus.dao.redis.IRedisBaseDao;
import com.monitor.argus.dao.redis.IRedisSetBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;

import java.util.Map;
import java.util.Set;

/**
 * @Description: redisSetBaseDao 实现类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-27 下午05:49:13
 * @Version: V1.0
 * 
 */
@Repository("redisSetBaseDao")
public class RedisSetBaseDaoImpl implements IRedisSetBaseDao {

    private static final Logger logger = LoggerFactory.getLogger(RedisSetBaseDaoImpl.class);
    @Autowired
    private IRedisBaseDao redisBaseDao;

    @Override
    public Long hset(String key, String field, String value) {
        Long result = null;
        if (!(key != null && !key.isEmpty())) {
            return null;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hset(key, field, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public Long hdel(String key, String... fields) {
        Long result = null;
        if (!(key != null && !key.isEmpty())) {
            return null;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hdel(key, fields);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        Map<String, String> result = null;
        if (!(key != null && !key.isEmpty())) {
            return null;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hgetAll(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public Set<String> hkeys(String key) {
        Set<String> result = null;
        if (!(key != null && !key.isEmpty())) {
            return null;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hkeys(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }
}
