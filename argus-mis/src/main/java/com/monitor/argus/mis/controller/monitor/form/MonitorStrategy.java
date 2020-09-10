package com.monitor.argus.mis.controller.monitor.form;

/**
 * @Param
 * @Return
 * @Author xuefei
 * @Date 7/13/16
 * @Version
 */
public class MonitorStrategy {
    private String monitorContent;
    private int monitorCycle;
    private int occurrentedCount;
    private int waitTime;

    public String getMonitorContent() {
        return monitorContent;
    }

    public void setMonitorContent(String monitorContent) {
        this.monitorContent = monitorContent;
    }

    public int getOccurrentedCount() {
        return occurrentedCount;
    }

    public void setOccurrentedCount(int occurrentedCount) {
        this.occurrentedCount = occurrentedCount;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getMonitorCycle() {
        return monitorCycle;
    }

    public void setMonitorCycle(int monitorCycle) {
        this.monitorCycle = monitorCycle;
    }
}
