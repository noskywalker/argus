package com.monitor.argus.mis.controller.monitor.form;

/**
 * Created by huxiaolei on 2016/10/18.
 */
public class BusinessNodeNumCompareMonitorStrategy {

    private String monitorContentNode;
    private String monitorContentGroup;
    private String monitorContentNumber;
    private int compareValue;
    private int compareMethod;
    private int monitorCycle;
    private int occurrentedCount;
    private int waitTime;

    public String getMonitorContentNode() {
        return monitorContentNode;
    }

    public void setMonitorContentNode(String monitorContentNode) {
        this.monitorContentNode = monitorContentNode;
    }

    public String getMonitorContentGroup() {
        return monitorContentGroup;
    }

    public void setMonitorContentGroup(String monitorContentGroup) {
        this.monitorContentGroup = monitorContentGroup;
    }

    public String getMonitorContentNumber() {
        return monitorContentNumber;
    }

    public void setMonitorContentNumber(String monitorContentNumber) {
        this.monitorContentNumber = monitorContentNumber;
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
