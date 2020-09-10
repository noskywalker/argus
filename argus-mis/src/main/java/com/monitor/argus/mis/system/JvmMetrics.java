package com.monitor.argus.mis.system;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huxiaolei on 2016/11/4.
 */
public class JvmMetrics {
    private String metricsName;
    private List<JvmMetricsTag> jvmMetricsTags = new ArrayList<JvmMetricsTag>();

    public JvmMetrics(String metricsName) {
        this.metricsName = metricsName;
    }

    public String getMetricsName() {
        return metricsName;
    }

    public void setMetricsName(String metricsName) {
        this.metricsName = metricsName;
    }

    public List<JvmMetricsTag> getJvmMetricsTags() {
        return jvmMetricsTags;
    }

    public void setJvmMetricsTags(List<JvmMetricsTag> jvmMetricsTags) {
        this.jvmMetricsTags = jvmMetricsTags;
    }

    public JvmMetrics addJvmMetricsTag(JvmMetricsTag jvmMetricsTag) {
        this.jvmMetricsTags.add(jvmMetricsTag);
        return this;
    }

}
