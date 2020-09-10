package com.monitor.argus.mis.controller.monitor.form;

/**
 * Created by usr on 2017/5/8.
 */
public class BusinessInterfaceMonitorStrategy {

    private String monitorURI;
    private int compareValue;
    private int compareMethod;
    private int monitorCycle;
    private int occurrentedCount;
    private int waitTime;

    public String getMonitorURI() {
        return monitorURI;
    }

    public void setMonitorURI(String monitorURI) {
        this.monitorURI = monitorURI;
    }

    public int getCompareValue() {
        return compareValue;
    }

    public void setCompareValue(int compareValue) {
        this.compareValue = compareValue;
    }

    public int getCompareMethod() {
        return compareMethod;
    }

    public void setCompareMethod(int compareMethod) {
        this.compareMethod = compareMethod;
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
