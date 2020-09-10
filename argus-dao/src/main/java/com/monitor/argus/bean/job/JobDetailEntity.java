package com.monitor.argus.bean.job;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by usr on 2016/11/28.
 */
public class JobDetailEntity implements Serializable {

    private static final long serialVersionUID = 8448456397282135775L;

    private String id;
    private String jobName;
    private String cronExp;
    // 最后执行时间
    private Date lastExecTime;
    // 下一次执行时间
    private Date nextExecTime;
    private Boolean success;
    private Boolean valid;

    private String jobClass;
    private String methodName;
    private String operator;
    private String lastExecTimeStr;
    private String nextExecTimeStr;
    private String monitorId;
    private String monitorName;
    private String monitorStrategy;

    public String getMonitorStrategy() {
        return monitorStrategy;
    }

    public void setMonitorStrategy(String monitorStrategy) {
        this.monitorStrategy = monitorStrategy;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getLastExecTimeStr() {
        return lastExecTimeStr;
    }

    public void setLastExecTimeStr(String lastExecTimeStr) {
        this.lastExecTimeStr = lastExecTimeStr;
    }

    public String getNextExecTimeStr() {
        return nextExecTimeStr;
    }

    public void setNextExecTimeStr(String nextExecTimeStr) {
        this.nextExecTimeStr = nextExecTimeStr;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCronExp() {
        return cronExp;
    }

    public void setCronExp(String cronExp) {
        this.cronExp = cronExp;
    }

    public Date getLastExecTime() {
        return lastExecTime;
    }

    public void setLastExecTime(Date lastExecTime) {
        this.lastExecTime = lastExecTime;
    }

    public Date getNextExecTime() {
        return nextExecTime;
    }

    public void setNextExecTime(Date nextExecTime) {
        this.nextExecTime = nextExecTime;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

}
