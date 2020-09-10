package com.monitor.argus.bean.dataland;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huxiaolei on 2016/10/13.
 */
public class AnalyTopologySysDatalandEntity implements Serializable {

    private static final long serialVersionUID = -6889206256857014765L;

    private String id;
    private String logBytes;
    private String diffLogBytes;
    private Date createDate;
    private String systemId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogBytes() {
        return logBytes;
    }

    public void setLogBytes(String logBytes) {
        this.logBytes = logBytes;
    }

    public String getDiffLogBytes() {
        return diffLogBytes;
    }

    public void setDiffLogBytes(String diffLogBytes) {
        this.diffLogBytes = diffLogBytes;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

}
