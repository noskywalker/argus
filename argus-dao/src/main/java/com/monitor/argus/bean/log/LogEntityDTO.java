package com.monitor.argus.bean.log;

import com.monitor.argus.bean.base.EntityBase;
import com.monitor.argus.common.util.LogType;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * All rights Reserved, Designed By alex zhang
 * @Title: 	LogEntity.java
 * @Package com.monitor.argus.common.bean
 * @description: 	TODO
 * @author:	alex zhang
 * @date:	2016年6月14日 下午2:27:42 
 * @version	V1.0
 */
public class LogEntityDTO extends EntityBase implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -8941208665177146634L;

	private String ct;
	private String snm;
	private String ctms;

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getSnm() {
		return snm;
	}

	public void setSnm(String snm) {
		this.snm = snm;
	}

	public String getCtms() {
		return ctms;
	}

	public void setCtms(String ctms) {
		this.ctms = ctms;
	}

	private String monitorStrategyId;

	public String getMonitorStrategyId() {
		return monitorStrategyId;
	}

	public void setMonitorStrategyId(String monitorStrategyId) {
		this.monitorStrategyId = monitorStrategyId;
	}

	private String ip;
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getFullMessage() {
		return fullMessage;
	}

	public void setFullMessage(String fullMessage) {
		this.fullMessage = fullMessage;
	}

	private String fullMessage;;
	public LogType getLogType() {
		return logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}

	private LogType logType;
	/**
	 * other extra info about log,such as business,userid and so on
	 */
	private Map<String,String> attach=new TreeMap<String,String>();
	/**
	 * the log id comes from log info(operateId),in order to trace the problems
	 */
	private String logId;
	/**
	 * identify the joined up system using a name,e.g app bg service
	 */
	private String systemName;
	/**
	 * the timestamp of system
	 */
	private String systemTime;
	/**
	 * the timestamp comes from the application log,nginx or apache tomcat
	 */
	private String timeStamp;

	private long logLength;

	private String tokenId;

	private String deviceId;

	private String times;

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public long getLogLength() {
		return logLength;
	}

	public void setLogLength(long logLength) {
		this.logLength = logLength;
	}

	public void addAttach(String key, String value){
		attach.put(key, value);
	}

	public Map<String, String> getAttach() {
		return attach;
	}


	public String getLogId() {
		return logId;
	}


	public String getSystemName() {
		return systemName;
	}

	public String getSystemTime() {
		return systemTime;
	}

	public String getTimeStamp() {
		return timeStamp;
	}


	public void setAttach(Map<String, String> attach) {
		this.attach = attach;
	}


	public void setLogId(String logId) {
		this.logId = logId;
	}


	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public void setSystemTime(String systemTime) {
		this.systemTime = systemTime;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}



}
