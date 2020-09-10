package com.monitor.argus.mis.controller.alarm.form;

import java.io.Serializable;

public class AddAlarmStrategyForm implements Serializable {

    private static final long serialVersionUID = 984338627092550556L;

    private String id;
    private String alarmName;
    private String alarmStrategy;
    private String alarmType;

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmStrategy() {
        return alarmStrategy;
    }

    public void setAlarmStrategy(String alarmStrategy) {
        this.alarmStrategy = alarmStrategy;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
