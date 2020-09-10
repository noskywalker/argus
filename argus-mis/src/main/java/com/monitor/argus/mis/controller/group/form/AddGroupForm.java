package com.monitor.argus.mis.controller.group.form;

import java.io.Serializable;

/**
 * @Param
 * @Return
 * @Author xuefei
 * @Date 7/15/16
 * @Version
 */
public class AddGroupForm implements Serializable{
    private String id;
    private String groupName;
    private boolean groupEnable;
    private boolean groupEnableHidden;
    private int alarmType;
    private String alarmId;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public boolean isGroupEnable() {
        return groupEnable;
    }

    public void setGroupEnable(boolean groupEnable) {
        this.groupEnable = groupEnable;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public boolean isGroupEnableHidden() {
        return groupEnableHidden;
    }

    public void setGroupEnableHidden(boolean groupEnableHidden) {
        this.groupEnableHidden = groupEnableHidden;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
