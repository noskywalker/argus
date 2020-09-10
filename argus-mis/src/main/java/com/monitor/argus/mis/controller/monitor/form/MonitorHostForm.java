package com.monitor.argus.mis.controller.monitor.form;

import java.io.Serializable;

/**
 * Created by xuefei on 7/12/16.
 */
public class MonitorHostForm implements Serializable {
    private String id;
    private String hostName;
    private String ip;
    private String os;
    private String detail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
