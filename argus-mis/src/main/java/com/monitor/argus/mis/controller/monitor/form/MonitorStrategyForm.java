package com.monitor.argus.mis.controller.monitor.form;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xuefei on 7/12/16.
 */
public class MonitorStrategyForm implements Serializable {
    private String id;
    private String monitorName;
    private String alarmId;
    // private MonitorStrategy monitorStrategy;
    private KeyWordMonitorStrategy keyWordMonitorStrategy;
    private BusinessNodeMonitorStrategy businessNodeMonitorStrategy;
    private BusinessNodePercentMonitorStrategy businessNodePercentMonitorStrategy;
    private BusinessNodeNumCompareMonitorStrategy businessNodeNumCompareMonitorStrategy;
    private BusinessInterfaceMonitorStrategy businessInterfaceMonitorStrategy;
    private JobMonitorStrategy jobMonitorStrategy;
    private int strategyType;
    private int priority;
    private int strategyStatus;
    private Date createDate;
    // 自定义的下发内容
    private String sendContent;
    // 是否下发
    private int isSendContent;
    private String systemId;
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

    public KeyWordMonitorStrategy getKeyWordMonitorStrategy() {
        return keyWordMonitorStrategy;
    }

    public void setKeyWordMonitorStrategy(KeyWordMonitorStrategy keyWordMonitorStrategy) {
        this.keyWordMonitorStrategy = keyWordMonitorStrategy;
    }

    public BusinessNodeMonitorStrategy getBusinessNodeMonitorStrategy() {
        return businessNodeMonitorStrategy;
    }

    public void setBusinessNodeMonitorStrategy(BusinessNodeMonitorStrategy businessNodeMonitorStrategy) {
        this.businessNodeMonitorStrategy = businessNodeMonitorStrategy;
    }

    public BusinessNodePercentMonitorStrategy getBusinessNodePercentMonitorStrategy() {
        return businessNodePercentMonitorStrategy;
    }

    public void setBusinessNodePercentMonitorStrategy(BusinessNodePercentMonitorStrategy businessNodePercentMonitorStrategy) {
        this.businessNodePercentMonitorStrategy = businessNodePercentMonitorStrategy;
    }

    public BusinessNodeNumCompareMonitorStrategy getBusinessNodeNumCompareMonitorStrategy() {
        return businessNodeNumCompareMonitorStrategy;
    }

    public void setBusinessNodeNumCompareMonitorStrategy(BusinessNodeNumCompareMonitorStrategy businessNodeNumCompareMonitorStrategy) {
        this.businessNodeNumCompareMonitorStrategy = businessNodeNumCompareMonitorStrategy;
    }

    public JobMonitorStrategy getJobMonitorStrategy() {
        return jobMonitorStrategy;
    }

    public void setJobMonitorStrategy(JobMonitorStrategy jobMonitorStrategy) {
        this.jobMonitorStrategy = jobMonitorStrategy;
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

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    /*public MonitorStrategy getMonitorStrategy() {
        return monitorStrategy;
    }

    public void setMonitorStrategy(MonitorStrategy monitorStrategy) {
        this.monitorStrategy = monitorStrategy;
    }*/

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public BusinessInterfaceMonitorStrategy getBusinessInterfaceMonitorStrategy() {
        return businessInterfaceMonitorStrategy;
    }

    public void setBusinessInterfaceMonitorStrategy(BusinessInterfaceMonitorStrategy businessInterfaceMonitorStrategy) {
        this.businessInterfaceMonitorStrategy = businessInterfaceMonitorStrategy;
    }

}
