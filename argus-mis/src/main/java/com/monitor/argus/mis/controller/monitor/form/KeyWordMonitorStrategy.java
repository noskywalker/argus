package com.monitor.argus.mis.controller.monitor.form;

/**
 * Created by huxiaolei on 2016/10
 */
public class KeyWordMonitorStrategy {

    private String noMonitorContent;
    private String monitorContent;
    private int monitorCycle;
    private int occurrentedCount;
    private int waitTime;

    public String getNoMonitorContent() {
        return noMonitorContent;
    }

    public void setNoMonitorContent(String noMonitorContent) {
        this.noMonitorContent = noMonitorContent;
    }

    public String getMonitorContent() {
        return monitorContent;
    }

    public void setMonitorContent(String monitorContent) {
        this.monitorContent = monitorContent;
    }

    public int getMonitorCycle() {
        return monitorCycle;
    }

    public void setMonitorCycle(int monitorCycle) {
        this.monitorCycle = monitorCycle;
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

}
