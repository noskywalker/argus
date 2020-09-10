package com.monitor.argus.mis.controller.monitor.form;

/**
 * Created by wangfeng on 16/10/12.
 */
public class BusinessNodePercentMonitorStrategy {
    private String monitorContentFrac;
    private String monitorContentNume;
    private int compareValue;
    private int compareMethod;
    private int monitorCycle;

    public String getMonitorContentFrac() {
        return monitorContentFrac;
    }

    public void setMonitorContentFrac(String monitorContentFrac) {
        this.monitorContentFrac = monitorContentFrac;
    }

    public String getMonitorContentNume() {
        return monitorContentNume;
    }

    public void setMonitorContentNume(String monitorContentNume) {
        this.monitorContentNume = monitorContentNume;
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
}
