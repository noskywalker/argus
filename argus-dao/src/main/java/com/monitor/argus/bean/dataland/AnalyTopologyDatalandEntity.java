package com.monitor.argus.bean.dataland;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huxiaolei on 2016/9/28.
 */
public class AnalyTopologyDatalandEntity implements Serializable {

    private static final long serialVersionUID = 3163035459092353884L;

    private String id;
    private String logCount;
    private String diffLogCount;
    private Date createDate;
    private String nodeKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogCount() {
        return logCount;
    }

    public void setLogCount(String logCount) {
        this.logCount = logCount;
    }

    public String getDiffLogCount() {
        return diffLogCount;
    }

    public void setDiffLogCount(String diffLogCount) {
        this.diffLogCount = diffLogCount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

}
