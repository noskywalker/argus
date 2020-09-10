/** */
package com.monitor.argus.dao.redis.impl;

import com.monitor.argus.dao.redis.IRedisBaseDao;
import com.monitor.argus.dao.redis.IRedisStringBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;

/**
 * @Description: P2puserRedisDao 实现类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-27 下午05:49:13
 * @Version: V1.0
 * 
 */
@Repository("redisStringBaseDao")
public class RedisStringBaseDaoImpl implements IRedisStringBaseDao {

    private static final Logger logger = LoggerFactory.getLogger(RedisStringBaseDaoImpl.class);
    @Autowired
    private IRedisBaseDao redisBaseDao;

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 上午10:42:21
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    @Override
    public String get(String key) {
        String result = null;
        if (!(key != null && !key.isEmpty())) {
            return null;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.get(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public byte[] getRedisBytes(String key) {
        byte[] result = null;
        if (!(key != null && !key.isEmpty())) {
            return null;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.get(key.getBytes());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 上午10:42:21
     * @Version: V1.0
     * 
     * @param key
     * @param milliseconds
     * @param value
     * 
     */
    @Override
    public void pSetEx(final String key, final long milliseconds, final String value) {
        // TODO
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 上午10:42:21
     * @Version: V1.0
     * 
     * @param key
     * @param value
     * 
     */
    @Override
    public String set(final String key, final String value) {
        String result = null;
        if (!(key != null && !key.isEmpty())) {
            return result;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.set(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public String set(String key, byte[] value) {
        String result = null;
        if (!(key != null && !key.isEmpty())) {
            return result;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.set(key.getBytes(), value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;

    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 上午10:42:21
     * @Version: V1.0
     * 
     * @param key
     * @param seconds
     * @param value
     * 
     */
    @Override
    public String setEx(final String key, int seconds, final String value) {
        String result = null;
        if (!(key != null && !key.isEmpty())) {
            return result;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setex(key, seconds, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public String setEx(String key, int seconds, byte[] value) {
        String result = null;
        if (!(key != null && !key.isEmpty())) {
            return result;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setex(key.getBytes(), seconds, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 上午10:42:21
     * @Version: V1.0
     * 
     * @param key
     * @param value
     * @return
     * 
     */
    @Override
    public Long setNX(final String key, final String value) {
        Long result = null;
        if (!(key != null && !key.isEmpty())) {
            return result;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setnx(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 上午10:42:21
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    @Override
    public Long strLen(String key) {
        Long result = null;
        if (!(key != null && !key.isEmpty())) {
            return result;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.strlen(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public Long ttl(String key) {
        Long result = null;
        if (!(key != null && !key.isEmpty())) {
            return result;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.ttl(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public Long del(final String key) {
        Long result = null;
        if (!(key != null && !key.isEmpty())) {
            return result;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.del(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

}
