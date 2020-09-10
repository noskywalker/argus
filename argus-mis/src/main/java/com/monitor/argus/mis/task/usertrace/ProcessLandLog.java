package com.monitor.argus.mis.task.usertrace;

import java.io.Serializable;

/**
 * Created by usr on 2017/4/6.
 */
public class ProcessLandLog implements Serializable {

    private static final long serialVersionUID = 6986651294517773638L;
    private String token;
    private String clientType;
    private String channelId;
    private String clientversion;
    private String action;
    private String clientIP;
    private String createTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getClientversion() {
        return clientversion;
    }

    public void setClientversion(String clientversion) {
        this.clientversion = clientversion;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
