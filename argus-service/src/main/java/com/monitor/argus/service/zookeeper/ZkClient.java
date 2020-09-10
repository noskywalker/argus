package com.monitor.argus.service.zookeeper;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.imps.CuratorFrameworkState;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

public class ZkClient {

	private CuratorFramework cf;

	private void beforeCheck() {
		Assert.notNull(zkServer);
		Assert.notNull(zkPort);
		Assert.isTrue(zkTimeout != 0);
		Assert.isTrue(conTimeout != 0);
	}

	private void afterInit() {
		CuratorFramework cf = getCf();
		try {
			Stat st = cf.checkExists().forPath(ns);
			if (st == null) {
				cf.create().forPath(ns);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostConstruct
	public void init() {
		beforeCheck();
		if ((cf == null) || (cf.getState() != CuratorFrameworkState.STARTED)) {
			CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
			cf = builder.connectString(this.zkServer).sessionTimeoutMs(this.zkTimeout).connectionTimeoutMs(conTimeout)
					.canBeReadOnly(false).namespace(ns).retryPolicy(new ExponentialBackoffRetry(1000, 5))
					.defaultData(null).build();

			if (cf.getState() == CuratorFrameworkState.LATENT) {
				cf.start();
			}
		}
		afterInit();
	}

	public CuratorFramework getCf() {
		return cf;
	}

	public int getConTimeout() {
		return conTimeout;
	}

	public void setConTimeout(int conTimeout) {
		this.conTimeout = conTimeout;
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	@Value("${zk.server}")
	private String zkServer;
	@Value("${zk.connect.timeout}")
	private int conTimeout;
	@Value("${zk.port}")
	private String zkPort;
	@Value("${zk.ns}")
	private String ns;

	public String getZkServer() {
		return zkServer;
	}

	public void setZkServer(String zkServer) {
		this.zkServer = zkServer;
	}

	public String getZkPort() {
		return zkPort;
	}

	public int getZkTimeout() {
		return zkTimeout;
	}

	public void setZkTimeout(int zkTimeout) {
		this.zkTimeout = zkTimeout;
	}

	public void setZkPort(String zkPort) {
		this.zkPort = zkPort;
	}

	@Value("${zk.timeout}")
	private int zkTimeout;
}
