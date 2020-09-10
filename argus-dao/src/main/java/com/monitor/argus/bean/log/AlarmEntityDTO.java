package com.monitor.argus.bean.log;

import com.monitor.argus.common.enums.AlarmLevel;
import com.monitor.argus.common.util.DateUtil;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/13.
 */
public class AlarmEntityDTO implements Serializable{

    public static void main(String[] args) {
        String memorymbean = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(memorymbean);
    }
    public static AlarmEntityDTO generateByLogEntity(LogEntityDTO entityDTO){
        AlarmEntityDTO alarmEntityDTO=new AlarmEntityDTO();
        alarmEntityDTO.setHostName(entityDTO.getHostName());
        alarmEntityDTO.setIp(entityDTO.getIp());
        alarmEntityDTO.setMessage(entityDTO.getFullMessage());
        alarmEntityDTO.setOperateId(entityDTO.getLogId());
        alarmEntityDTO.setTimeStamp(System.currentTimeMillis()+"");
        alarmEntityDTO.setEndTime(DateUtil.getDateLongTimePlusStr(new Date()));
        alarmEntityDTO.setBeginTime(entityDTO.getTimeStamp());
        return alarmEntityDTO;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    private String alarmId;
    public AlarmLevel getLevel() {
        return level;
    }

    public void setLevel(AlarmLevel level) {
        this.level = level;
    }

    private String message;
    private String operateId;

    private AlarmLevel level;

    public int getTotalAlarmCount() {
        return totalAlarmCount;
    }

    public void setTotalAlarmCount(int totalAlarmCount) {
        this.totalAlarmCount = totalAlarmCount;
    }

    private int totalAlarmCount=1;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMonitorStrategyId() {
        return monitorStrategyId;
    }

    public void setMonitorStrategyId(String monitorStrategyId) {
        this.monitorStrategyId = monitorStrategyId;
    }

    public String getMonitorStrategyName() {
        return monitorStrategyName;
    }

    public void setMonitorStrategyName(String monitorStrategyName) {
        this.monitorStrategyName = monitorStrategyName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    private String beginTime;
    private String endTime;
    private String monitorStrategyId;
    private String monitorStrategyName;
    private String systemName;
    private String ip;
    private String hostName;
    private String timeStamp;
}
