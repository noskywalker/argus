package com.monitor.argus.service.console.vo;

/**
 * Created by Administrator on 2016/7/16.
 */
public class RuntimeAlarmActiveThreadVO {
    private String threadId;
    private String queueName;

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    private String startTime;
}
