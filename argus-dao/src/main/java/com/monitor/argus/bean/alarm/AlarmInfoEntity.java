package com.monitor.argus.bean.alarm;

import java.util.Date;

/**
 * Created by xuefei on 7/11/16.
 */
public class AlarmInfoEntity {
    private String id;
    private String monitorId;
    private String alarmId;
    private Date triggerTime;
    private int triggerCount;

    @Override
    public String toString() {
        return "AlarmInfoEntity{" +
                "ip='" + ip + '\'' +
                ", systemName='" + systemName + '\'' +
                ", alarmDetail='" + alarmDetail + '\'' +
                ", alarmType=" + alarmType +
                ", triggerCount=" + triggerCount +
                ", triggerTime=" + triggerTime +
                ", alarmId='" + alarmId + '\'' +
                ", monitorId='" + monitorId + '\'' +
                '}';
    }

    private int alarmType;
    private String alarmDetail;
    private String systemName;
    private String ip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getAlarmId() {
        return alarmId;
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

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public int getTriggerCount() {
        return triggerCount;
    }

    public void setTriggerCount(int triggerCount) {
        this.triggerCount = triggerCount;
        if(this.triggerCount==0){
            /**single alarm*/
            this.alarmType=0;
        }else{
            /**merge alarm*/
            this.alarmType=1;
        }
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmDetail() {
        return alarmDetail;
    }

    public void setAlarmDetail(String alarmDetail) {
        this.alarmDetail = alarmDetail;
    }

}
