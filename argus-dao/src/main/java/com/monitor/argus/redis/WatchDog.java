package com.monitor.argus.redis;

import com.monitor.argus.common.node.redis.RedisHostNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Deprecated
public class WatchDog {

	Logger logger=LoggerFactory.getLogger(getClass());
	boolean FAIR = false;

	/**
	 * use the DEFAULT_CONCURRENCY_LEVEL=16----------------------------
	 */
	private static ConcurrentHashMap<String, RedisHostNode> redisNodes = new ConcurrentHashMap<String, RedisHostNode>();

	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(FAIR);
	@Autowired
	ShardedJedisPool shardedJedisPool;

	@PostConstruct
	public void watch() throws Exception {
		logger.info("redis healthy check beginning,please wait..");
		Collection<Jedis> jedisList =  shardedJedisPool.getResource().getAllShards();
		for (Jedis jedis : jedisList) {
			String host = jedis.getClient().getHost();
			String port = jedis.getClient().getPort() + "";
			String nodeKey = RedisHostNode.nodeKey(host, port);
			RedisHostNode nodeExists = redisNodes.get(RedisHostNode.nodeKey(host, port));
			String statisticInfo = jedis.info();
			String maxMemory = jedis.configGet("maxmemory").get(1);
			if (nodeExists == null) {
				RedisHostNode redisNode = new RedisHostNode(statisticInfo);
				redisNode.setHostIP(host);
				redisNode.setPort(port);
				redisNodes.putIfAbsent(nodeKey, redisNode);
			} else {
				nodeExists.updateState(statisticInfo,maxMemory);
			}
			
			Thread.sleep(1000*3);
		}
	}

	public static boolean isAvailable(Jedis jedis) {
		RedisHostNode redisNode = redisNodes
				.get(RedisHostNode.nodeKey(jedis.getClient().getHost(), jedis.getClient().getPort() + ""));
		if(redisNode==null){
			return true;
		}
		return redisNode.isAvailable();
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		JedisShardInfo i = new JedisShardInfo("10.141.4.56", 6379);
		i.setPassword("test123");
		Jedis jedis = new Jedis(i);
		String statisticInfo=jedis.info();
		
		System.out.println(statisticInfo.substring(statisticInfo.indexOf("used_memory")+12, statisticInfo.indexOf("used_memory_human")));
		System.out.println(jedis.configGet("maxmemory").get(1));
	}
}
