package com.monitor.argus.bean.alarm.vo;

import java.io.Serializable;

/**
 * Created by usr on 2016/11/12.
 */
public class SystemMonitorAlarmDetailEntity implements Serializable {

    private static final long serialVersionUID = -5773215748824479585L;

    private String name;
    private String count;
    private String percent;
    private Integer num;

    public SystemMonitorAlarmDetailEntity() {
        this.count = "0";
        this.percent = "0%";
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
