package com.monitor.argus.mis.system;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huxiaolei on 2016/11/4.
 */
public class SystemIPJvmInfo {

    private String systemId;
    private String ip;
    private List<JvmMetrics> jvmMetricss = new ArrayList<JvmMetrics>();

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<JvmMetrics> getJvmMetricss() {
        return jvmMetricss;
    }

    public void setJvmMetricss(List<JvmMetrics> jvmMetricsTags) {
        this.jvmMetricss = jvmMetricsTags;
    }

    public void addJvmMetrics(JvmMetrics jvmMetrics) {
        jvmMetricss.add(jvmMetrics);
    }

}
