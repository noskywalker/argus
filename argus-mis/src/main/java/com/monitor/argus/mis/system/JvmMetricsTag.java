package com.monitor.argus.mis.system;

/**
 * Created by huxiaolei on 2016/11/4.
 */
public class JvmMetricsTag {
    private String tagName;
    private double tagValue;

    public JvmMetricsTag(String tagName, double tagValue) {
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public double getTagValue() {
        return tagValue;
    }

    public void setTagValue(double tagValue) {
        this.tagValue = tagValue;
    }

}
