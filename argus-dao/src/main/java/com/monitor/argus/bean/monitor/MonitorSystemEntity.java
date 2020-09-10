package com.monitor.argus.bean.monitor;

import java.util.Date;

/**
 * Created by xuefei on 7/11/16.
 */
public class MonitorSystemEntity {
    private String id;
    private String systemName;
    private String detail;
    private Date createDate;
    private String creator;
    // 是否采集地区数据
    private int isIp;

    public int getIsIp() {
        return isIp;
    }

    public void setIsIp(int isIp) {
        this.isIp = isIp;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

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
}
