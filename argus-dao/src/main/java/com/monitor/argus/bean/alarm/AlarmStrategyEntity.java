package com.monitor.argus.bean.alarm;

import com.monitor.argus.common.model.PageHelper;

/**
 * Created by xuefei on 7/11/16.
 */
public class AlarmStrategyEntity extends PageHelper implements java.io.Serializable {
    private String id;
    private String alarmName;
    private String alarmStrategy;
    private String alarmType;
    private String createDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
