/** */
package com.monitor.argus.dao.redis.impl;

import com.monitor.argus.dao.redis.IRedisBaseDao;
import com.monitor.argus.dao.redis.IRedisZSetBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Description: redisSetBaseDao 实现类
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-27 下午05:49:13
 * @Version: V1.0
 * 
 */
@Repository("redisZSetBaseDao")
public class RedisZSetBaseDaoImpl implements IRedisZSetBaseDao {

    private static final Logger logger = LoggerFactory.getLogger(RedisZSetBaseDaoImpl.class);
    @Autowired
    private IRedisBaseDao redisBaseDao;
}
