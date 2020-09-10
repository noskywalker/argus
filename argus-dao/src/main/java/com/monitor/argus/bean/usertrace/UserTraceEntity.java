package com.monitor.argus.bean.usertrace;

import com.monitor.argus.common.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by usr on 2017/4/6.
 */
public class UserTraceEntity implements Serializable {

    private static final long serialVersionUID = -5927545870338780030L;

    private Integer id;
    private String userTrace;
    private Integer count;
    private Date createTime;
    private String createTimeStr;

    public String getCreateTimeStr() {
        createTimeStr = DateUtil.getDateLongTimePlusStr(createTime);
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserTrace() {
        return userTrace;
    }

    public void setUserTrace(String userTrace) {
        this.userTrace = userTrace;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
