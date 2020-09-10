/** */
package com.monitor.argus.dao.redis.impl;

import com.monitor.argus.dao.redis.IRedisBaseDao;
import com.monitor.argus.dao.redis.IRedisKeyBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Description: redisKeyBaseDao 实现类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-27 下午05:49:13
 * @Version: V1.0
 * 
 */
@Repository("redisKeyBaseDao")
public class RedisKeyBaseDaoImpl implements IRedisKeyBaseDao {

    private static final Logger logger = LoggerFactory.getLogger(RedisKeyBaseDaoImpl.class);
    @Autowired
    private IRedisBaseDao redisBaseDao;

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    @Override
    public void del(String key) {
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        boolean broken = false;
        try {
            shardedJedis.del(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param keys
     * @return
     * 
     */
    @Override
    public void del(List<String> keys) {
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        boolean broken = false;
        try {
            for (String key : keys) {
                shardedJedis.del(key);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param keys
     * @return
     * 
     */
    @Override
    public Long del(final String... keys) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    @Override
    public String dump(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    @Override
    public Boolean exists(String key) {
        Boolean result = false;
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        boolean broken = false;
        try {
            result = shardedJedis.exists(key);
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
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @param seconds
     * @return
     * 
     */
    @Override
    public Boolean expire(String key, int seconds) {
        boolean result = false;
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        boolean broken = false;
        try {
            shardedJedis.expire(key, seconds);
            result = true;
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
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @param unixTime
     * @return
     * 
     */
    @Override
    public Boolean expireAt(String key, Date date) {
        Boolean result = null;
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        boolean broken = false;
        try {
            shardedJedis.expireAt(key, date.getTime());
            result = true;
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
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param pattern
     * @return
     * 
     */
    @Override
    public Set<String> keys(String pattern) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @param dbIndex
     * @return
     * 
     */
    @Override
    public Boolean move(String key, int dbIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @param millis
     * @return
     * 
     */
    @Override
    public Boolean pExpire(String key, long millis) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @param unixTimeInMillis
     * @return
     * 
     */
    @Override
    public Boolean pExpireAt(String key, long unixTimeInMillis) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    @Override
    public Long pTtl(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    @Override
    public Boolean persist(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @return
     * 
     */
    @Override
    public String randomKey() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param oldName
     * @param newName
     * 
     */
    @Override
    public void rename(String oldName, String newName) {
        // TODO Auto-generated method stub

    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param oldName
     * @param newName
     * @return
     * 
     */
    @Override
    public Boolean renameNX(String oldName, String newName) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @param ttlInMillis
     * @param serializedValue
     * 
     */
    @Override
    public void restore(String key, long ttlInMillis, String serializedValue) {
        // TODO Auto-generated method stub

    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param options
     * @return
     * 
     */
    @Override
    public Cursor<String> scan(ScanOptions options) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @param params
     * @return
     * 
     */
    @Override
    public List<String> sort(String key, SortParameters params) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @param params
     * @param storeKey
     * @return
     * 
     */
    @Override
    public Long sort(String key, SortParameters params, String storeKey) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    @Override
    public Long ttl(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午01:08:21
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    @Override
    public DataType type(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    // TODO
}
