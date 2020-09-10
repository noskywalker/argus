package com.monitor.argus.bean.dataland;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by usr on 2016/11/15.
 */
public class AnalyTopologyHourITEntity implements Serializable {

    private static final long serialVersionUID = -4149756389281558908L;

    private Long id;
    private Long count;
    private Long alltime;
    private double pertime;
    private Date createDate;
    private String nodeKey;
    private String createDateStr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getAlltime() {
        return alltime;
    }

    public void setAlltime(Long alltime) {
        this.alltime = alltime;
    }

    public double getPertime() {
        return pertime;
    }

    public void setPertime(double pertime) {
        this.pertime = pertime;
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

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }
}
