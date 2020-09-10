package com.monitor.argus.common.node;

import java.io.Serializable;

public class HostNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7382687689154595036L;

	public String getHostIP() {
		return hostIP;
	}

	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	private String hostIP;
	private String nodeName;
	private String hostName;
}
