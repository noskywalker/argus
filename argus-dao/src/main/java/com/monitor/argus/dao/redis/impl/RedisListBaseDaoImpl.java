/** */
package com.monitor.argus.dao.redis.impl;

import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.dao.redis.IRedisBaseDao;
import com.monitor.argus.dao.redis.IRedisListBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisListCommands.Position;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: redisSetBaseDao 实现类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-27 下午05:49:13
 * @Version: V1.0
 * 
 */
@Repository("redisListBaseDao")
public class RedisListBaseDaoImpl implements IRedisListBaseDao {

    private static final Logger logger = LoggerFactory.getLogger(RedisListBaseDaoImpl.class);
    @Autowired
    private IRedisBaseDao redisBaseDao;

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param timeout
     * @param keys
     * @return
     * 
     */
    @Override
    public List<String> bLPop(int timeout, List<String> keys) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param timeout
     * @param keys
     * @return
     * 
     */
    @Override
    public List<String> bRPop(int timeout, List<String> keys) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param timeout
     * @param srcKey
     * @param dstKey
     * @return
     * 
     */
    @Override
    public String bRPopLPush(int timeout, String srcKey, String dstKey) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param key
     * @param index
     * @return
     * 
     */
    @Override
    public String lIndex(String key, long index) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param key
     * @param where
     * @param pivot
     * @param value
     * @return
     * 
     */
    @Override
    public Long lInsert(String key, Position where, String pivot, String value) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:根据key，获取list的长度
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    @Override
    public Long lLen(final String key) {
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
            result = shardedJedis.llen(key);
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
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    @Override
    public String lPop(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:添加（头部） {@code values} 到 {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param key
     * @param values
     * @return
     * 
     */
    @Override
    public <T> Long lPush(String key, List<T> values) {
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
            for (T value : values) {
                result = shardedJedis.lpush(key, JsonUtil.beanToJson(value));
            }
        } catch (Exception e) {
            shardedJedis.del(key);
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
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param key
     * @param value
     * @return
     * 
     */
    @Override
    public Long lPushX(String key, String value) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param key
     * @param begin
     * @param end
     * @return
     * 
     */
    @Override
    public List<String> lRange(final String key, long begin, final long end) {
        List<String> result = null;
        if (!(key != null && !key.isEmpty())) {
            return null;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lrange(key, begin, end);
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
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param key
     * @param count
     * @param value
     * @return
     * 
     */
    @Override
    public Long lRem(String key, long count, String value) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param key
     * @param index
     * @param value
     * 
     */
    @Override
    public void lSet(String key, long index, String value) {
        // TODO Auto-generated method stub

    }

    /**
     * @Description:
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param key
     * @param begin
     * @param end
     * 
     */
    @Override
    public void lTrim(String key, long begin, long end) {
        // TODO Auto-generated method stub

    }

    /**
     * @Description:Removes and returns last element in list stored at
     *                      {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param key
     * @return
     * 
     */
    @Override
    public String rPop(final String key) {
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
            result = shardedJedis.rpop(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

    /**
     * @Description:Remove the last element from list at {@code srcKey}, 追加（尾部）
     *                     it 到 {@code dstKey} and return its value.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param srcKey
     * @param dstKey
     * @return
     * 
     */
    @Override
    public String rPopLPush(final String srcKey, final String dstKey) {
        // TODO
        return null;
    }

    /**
     * @Description:追加（尾部） {@code values} 到 {@code key}.
     * 
     * @Author: alex zhang
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param key
     * @param values
     * @return
     * 
     */
    @Override
    public <T> Long rPush(final String key, List<T> values) {
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
            for (T value : values) {
                result = shardedJedis.rpush(key, JsonUtil.beanToJson(value));
            }
        } catch (Exception e) {
            shardedJedis.del(key);
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
     * @CreateDate: 2015-4-28 下午03:09:43
     * @Version: V1.0
     * 
     * @param key
     * @param value
     * @return
     * 
     */
    @Override
    public Long rPushX(String key, String value) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 插入LIST 有失效时间
     * 
     * @param key
     * @param values
     * @param seconds
     * @return
     */
    @Override
    public <T> Long rPushEx(final String key, List<T> values, int seconds) {
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
            for (T value : values) {
                result = shardedJedis.rpush(key, JsonUtil.beanToJson(value));
                shardedJedis.expire(key, seconds);
            }
        } catch (Exception e) {
            shardedJedis.del(key);
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        return result;
    }

    @Override
    public <T> List<T> getList(Class<T> clazz, String key) {
        List<String> result = null;
        if (!(key != null && !key.isEmpty())) {
            return null;
        }
        ShardedJedis shardedJedis = redisBaseDao.getResource();
        if (shardedJedis == null) {
            return null;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lrange(key, 0, -1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            broken = true;
        } finally {
            redisBaseDao.returnResource(shardedJedis, broken);
        }
        List<T> list = new ArrayList<T>();
        if (result != null && result.size() > 0) {
            for (String s : result) {
                T t = (T) JsonUtil.jsonToBean(s, clazz);
                list.add(t);
            }
        }
        return list;
    }

}
