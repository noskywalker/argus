package com.monitor.argus.bean.alarm.vo;

/**
 * @Param
 * @Return
 * @Author xuefei
 * @Date 7/14/16
 * @Version
 */
public class AlarmInfoVO implements java.io.Serializable {
    private String ip;
    private String triggerTime;
    private int triggerCount;
    private String alarmType;
    private String alarmDetail;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
    }

    public int getTriggerCount() {
        return triggerCount;
    }

    public void setTriggerCount(int triggerCount) {
        this.triggerCount = triggerCount;
    }

    public String getAlarmType() {
        return alarmType.equals("0") ? "单次": "合并";
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmDetail() {
        return alarmDetail;
    }

    public void setAlarmDetail(String alarmDetail) {
        this.alarmDetail = alarmDetail;
    }
}
