package com.monitor.argus.mis.controller.monitor.form;

/**
 * Created by huxiaolei on 2016/10
 */
public class BusinessNodeMonitorStrategy {

    private String monitorConfigContent;
    private int monitorCycle;
    private int compareValue;
    private int compareMethod;

    public String getMonitorConfigContent() {
        return monitorConfigContent;
    }

    public void setMonitorConfigContent(String monitorConfigContent) {
        this.monitorConfigContent = monitorConfigContent;
    }

    public int getMonitorCycle() {
        return monitorCycle;
    }

    public void setMonitorCycle(int monitorCycle) {
        this.monitorCycle = monitorCycle;
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

}
