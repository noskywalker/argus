package com.monitor.argus.mis.controller.monitor.form;

import java.io.Serializable;
import java.util.List;

public class AddMonitorForm implements Serializable {

	private static final long serialVersionUID = 984338627092550556L;

	private String id;

	private String systemName;
	
	private String detail;

	// 是否采集地区数据
	private int isIp;

	public int getIsIp() {
		return isIp;
	}

	public void setIsIp(int isIp) {
		this.isIp = isIp;
	}

	private List<MonitorHostForm> monitorHostFormList;

	private List<MonitorStrategyForm> monitorStrategyFormList;

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public List<MonitorHostForm> getMonitorHostFormList() {
		return monitorHostFormList;
	}

	public void setMonitorHostFormList(List<MonitorHostForm> monitorHostFormList) {
		this.monitorHostFormList = monitorHostFormList;
	}

	public List<MonitorStrategyForm> getMonitorStrategyFormList() {
		return monitorStrategyFormList;
	}

	public void setMonitorStrategyFormList(List<MonitorStrategyForm> monitorStrategyFormList) {
		this.monitorStrategyFormList = monitorStrategyFormList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
