package com.monitor.argus.bean.group;

/**
 * Created by xuefei on 7/11/16.
 */
public class GroupEntity {
    private String id;
    private String groupName;
    private int enable;
    private int alarmType;
    private String alarmId;
    private String alarmStrategyType;
    private String alarmName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmStrategyType() {
        return alarmStrategyType;
    }

    public void setAlarmStrategyType(String alarmStrategyType) {
        this.alarmStrategyType = alarmStrategyType;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

}
