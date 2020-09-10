package com.monitor.argus.bean.dataland;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huxiaolei on 2016/9/20.
 */
public class ArgusTopologyDatalandEntity implements Serializable {

    private static final long serialVersionUID = -3478527686216839929L;

    private String id;
    private String totalLogBytes;
    private String totalLogCount;
    private String totalMonitors;
    private String totalAlarms;
    private String diffLogBytes;
    private String diffLogCount;
    private String diffMonitors;
    private String diffAlarms;
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotalLogBytes() {
        return totalLogBytes;
    }

    public void setTotalLogBytes(String totalLogBytes) {
        this.totalLogBytes = totalLogBytes;
    }

    public String getTotalLogCount() {
        return totalLogCount;
    }

    public void setTotalLogCount(String totalLogCount) {
        this.totalLogCount = totalLogCount;
    }

    public String getTotalMonitors() {
        return totalMonitors;
    }

    public void setTotalMonitors(String totalMonitors) {
        this.totalMonitors = totalMonitors;
    }

    public String getTotalAlarms() {
        return totalAlarms;
    }

    public void setTotalAlarms(String totalAlarms) {
        this.totalAlarms = totalAlarms;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDiffLogBytes() {
        return diffLogBytes;
    }

    public void setDiffLogBytes(String diffLogBytes) {
        this.diffLogBytes = diffLogBytes;
    }

    public String getDiffLogCount() {
        return diffLogCount;
    }

    public void setDiffLogCount(String diffLogCount) {
        this.diffLogCount = diffLogCount;
    }

    public String getDiffMonitors() {
        return diffMonitors;
    }

    public void setDiffMonitors(String diffMonitors) {
        this.diffMonitors = diffMonitors;
    }

    public String getDiffAlarms() {
        return diffAlarms;
    }

    public void setDiffAlarms(String diffAlarms) {
        this.diffAlarms = diffAlarms;
    }

}
