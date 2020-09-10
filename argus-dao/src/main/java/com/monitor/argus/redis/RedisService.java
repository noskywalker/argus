package com.monitor.argus.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RedisService {

	@Autowired
	ShardedJedisPool shardedJedisPool;

	public Map<String, String> hgetAll(String key) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			Map<String, String> value = jedis.hgetAll(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return null;
	}

	public String get(String key) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			String value = jedis.get(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return null;
	}

	public void incrby(String key,long count) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			jedis.incrBy(key,count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}


	public void set(String key,String value) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			jedis.set(key,value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	public Long sadd(String key, String... members) {
		Long resultl = 0L;
		ShardedJedis jedis = jedisConnection(key);
		try {
			resultl = jedis.sadd(key, members);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return resultl;
	}

	public Long scard(String key) {
		Long resultl = 0L;
		ShardedJedis jedis = jedisConnection(key);
		try {
			resultl = jedis.scard(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return resultl;
	}

	public void hdel(String name,String key) {
		ShardedJedis jedis = jedisConnection(name);
		try {
			jedis.hdel(name,key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	public void expire(String key,int seconds) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			jedis.expire(key,seconds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	public boolean setNX(String key,String value) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			boolean success = jedis.setnx(key,value)==1;
			return success;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return false;
	}

	public Long hlength(String key) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			Long length = jedis.hlen(key);
			return length;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return 0L;
	}

	public Set<String> hkeys(String key) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			return jedis.hkeys(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return null;
	}
	public void hset(String name,String key,String value) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			jedis.hset(name,key,value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	public String getFromlist(String key,int index) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			return jedis.lindex(key,index);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return null;
	}

	public Long lpush(String key, String value) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			return jedis.lpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return null;
	}

	public Long lpushs(String key, String... value) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			return jedis.lpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return null;
	}

	public Long size(String key){
		ShardedJedis jedis = jedisConnection(key);
		try {
			return jedis.llen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return null;
	}

	public String lpop(String key) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			return jedis.lpop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return null;
	}

	public Long rpush(String key, String value) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			return jedis.rpush(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return null;

	}

	public String rpop(String key) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			return jedis.rpop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return null;
	}


	public boolean redisAvailable(String key){
		ShardedJedis jedis = shardedJedisPool.getResource();
		return WatchDog.isAvailable(jedis.getShard(key));
	}
	
	private ShardedJedis jedisConnection(String key) {
		return shardedJedisPool.getResource();
	}

	public boolean exists(String mergeKey) {
		ShardedJedis jedis = jedisConnection(mergeKey);
		try {
			return jedis.exists(mergeKey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return false;
	}

	public void delete(String mergeKey) {
		ShardedJedis jedis = jedisConnection(mergeKey);
		try {
			jedis.del(mergeKey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}

	public void hincr(String mergeKey,String field,long value) {
		ShardedJedis jedis = jedisConnection(mergeKey);
		try {
			jedis.hincrBy(mergeKey,field,value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
	}
	public List<String> hmget(String key,String... fields) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			return jedis.hmget(key,fields);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return Collections.EMPTY_LIST;
	}

	public List<String> listAll(String key) {
		ShardedJedis jedis = jedisConnection(key);
		try {
			return jedis.lrange(key,0,-1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			shardedJedisPool.returnResource(jedis);
		}
		return Collections.EMPTY_LIST;
	}
}
