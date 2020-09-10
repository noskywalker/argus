package com.monitor.argus.bean.node;

import java.util.Date;

/**
 * Created by wangfeng on 16/10/20.
 */
public class TopoAnalyEntity {
    private String id;
    private String nodeKey;
    private String logCount;
    private String diffLogCount;
    private Date createDate;
    private String createDateStr;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
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

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }
}
