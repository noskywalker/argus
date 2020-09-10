package com.monitor.argus.bean.alarm.vo;

/**
 * Created by wangfeng on 16/11/3.
 */
public class SystemMonitorAlarmEntity {
    private Integer num;
    private String systemId;
    private String systemName;
    private String count;
    private String percent;

    public SystemMonitorAlarmEntity() {
        this.count = "0";
        this.percent = "0%";
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
