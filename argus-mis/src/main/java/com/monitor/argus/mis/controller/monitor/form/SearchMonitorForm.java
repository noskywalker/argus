package com.monitor.argus.mis.controller.monitor.form;

import com.monitor.argus.bean.monitor.MonitorHostEntity;
import com.monitor.argus.bean.monitor.MonitorStrategyEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by xuefei on 7/12/16.
 */
public class SearchMonitorForm implements Serializable {

    private String id;
    private String systemName;

    private Date createDate;

    private String creator;

    private String detail;

    private String createDateStr;

    // 是否采集地区数据
    private int isIp;

    public int getIsIp() {
        return isIp;
    }

    public void setIsIp(int isIp) {
        this.isIp = isIp;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    private List<MonitorHostEntity> monitorHostList;

    private List<MonitorStrategyEntity> monitorStrategyList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<MonitorHostEntity> getMonitorHostList() {
        return monitorHostList;
    }

    public void setMonitorHostList(List<MonitorHostEntity> monitorHostList) {
        this.monitorHostList = monitorHostList;
    }

    public List<MonitorStrategyEntity> getMonitorStrategyList() {
        return monitorStrategyList;
    }

    public void setMonitorStrategyList(List<MonitorStrategyEntity> monitorStrategyList) {
        this.monitorStrategyList = monitorStrategyList;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
