package com.monitor.argus.bean.usertrace;

import java.io.Serializable;

/**
 * Created by usr on 2017/4/7.
 */
public class UserTraceGroupEntity implements Serializable {

    private static final long serialVersionUID = -5866719842510094836L;

    private String userTrace;
    private Integer count;
    private Integer id;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
