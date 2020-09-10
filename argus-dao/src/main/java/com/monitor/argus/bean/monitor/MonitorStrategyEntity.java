package com.monitor.argus.bean.monitor;

import com.monitor.argus.common.enums.StrategyType;

import java.util.Date;

/**
 * Created by xuefei on 7/11/16.
 */
public class MonitorStrategyEntity {
    private String id;
    private String monitorName;
    private String systemId;
    private String alarmId;
    private String monitorContent;
    private String monitorStrategy;
    private int strategyType;
    private int priority;
    private int strategyStatus;
    private Date createDate;
    private String alarmName;
    private String systemName;
    private String strategyStatusStr;
    private String strategyTypeStr;
    private String createDateStr;
    // 自定义的下发内容
    private String sendContent;
    // 是否下发
    private int isSendContent;
    // 是否为实时策略 1是0否
    private Integer isRunTime;

    public Integer getIsRunTime() {
        return isRunTime;
    }

    public void setIsRunTime(Integer isRunTime) {
        this.isRunTime = isRunTime;
    }

    public int getIsSendContent() {
        return isSendContent;
    }

    public void setIsSendContent(int isSendContent) {
        this.isSendContent = isSendContent;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public String getStrategyStatusStr() {
        return strategyStatusStr;
    }

    public void setStrategyStatusStr(String strategyStatusStr) {
        this.strategyStatusStr = strategyStatusStr;
    }

    public String getStrategyTypeStr() {
        return StrategyType.getTypeName(this.strategyType).getTypeName();
    }

    public void setStrategyTypeStr(String strategyTypeStr) {
        this.strategyTypeStr = strategyTypeStr;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getMonitorContent() {
        return monitorContent;
    }

    public void setMonitorContent(String monitorContent) {
        this.monitorContent = monitorContent;
    }

    public String getMonitorStrategy() {
        return monitorStrategy;
    }

    public void setMonitorStrategy(String monitorStrategy) {
        this.monitorStrategy = monitorStrategy;
    }

    public int getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(int strategyType) {
        this.strategyType = strategyType;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStrategyStatus() {
        return strategyStatus;
    }

    public void setStrategyStatus(int strategyStatus) {
        this.strategyStatus = strategyStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
