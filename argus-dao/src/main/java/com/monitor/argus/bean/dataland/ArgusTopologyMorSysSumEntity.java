package com.monitor.argus.bean.dataland;

/**
 * Created by wangfeng on 16/10/14.
 */
public class ArgusTopologyMorSysSumEntity {
    private String diffMonitors;
    private String diffAlarms;
    private String createDate;

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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
