package com.monitor.argus.common.node.redis;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import com.monitor.argus.common.node.HostNode;

public class RedisHostNode extends HostNode {

	/**
	 * 
	 */
	private static final int REDIS_MAX_MEMORY_USAGE = 90;
	private volatile boolean available = true;
	private String port;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8905748836786940777L;

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Map<String, String> getStatistic() {
		return statistic;
	}

	public void setStatistic(Map<String, String> statistic) {
		this.statistic = statistic;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	private Map<String, String> statistic = new TreeMap<String, String>();

	public RedisHostNode(String statisticInfo) {
		// init statistic info
	}

	public String nodeKey() {
		return nodeKey(getHostIP(), port);
	}

	public boolean isAvailable() {
		return available;
	}

	public static String nodeKey(String host, String port) {
		return host + ":" + port;
	}

	public void updateState(String statisticInfo, String maxMemory) {
		try {
			double currentMemoryUsage = 0;
			double maxMemoryUsage = 1;
			double usageRate = 0;
			if (StringUtils.isEmpty(statisticInfo) || StringUtils.isEmpty(maxMemory)) {
				String str_CurrentMemoryUsage = statisticInfo.substring(statisticInfo.indexOf("used_memory") + 12,
						statisticInfo.indexOf("used_memory_human"));
				currentMemoryUsage = Double.parseDouble(str_CurrentMemoryUsage);
				maxMemoryUsage = Double.parseDouble(maxMemory);
				usageRate = (currentMemoryUsage / maxMemoryUsage) * 100;
			}
			if (usageRate > REDIS_MAX_MEMORY_USAGE) {
				available = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			available = true;
		}
	}

	public static void main(String[] args) {
		double a = 1234;
		double b = 12345;
		System.out.println(a / b);
	}
}
