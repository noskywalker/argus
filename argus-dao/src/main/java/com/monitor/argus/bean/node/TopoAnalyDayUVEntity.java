package com.monitor.argus.bean.node;

import java.util.Date;

/**
 * Created by wangfeng on 16/10/31.
 */
public class TopoAnalyDayUVEntity {
    private String id;
    private String nodeKey;
    private String UVCount;
    private String createDateStr;
    private Date createDate;

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

    public String getUVCount() {
        return UVCount;
    }

    public void setUVCount(String UVCount) {
        this.UVCount = UVCount;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
