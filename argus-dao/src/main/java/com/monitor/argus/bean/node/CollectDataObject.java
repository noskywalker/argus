package com.monitor.argus.bean.node;

/**
 * 列表展示项
 * Created by wangfeng on 16/11/1.
 */

public class CollectDataObject {
    private String date;
    private String nodeName;
    private String countpv;
    private String countuv;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getCountpv() {
        return countpv;
    }

    public void setCountpv(String countpv) {
        this.countpv = countpv;
    }

    public String getCountuv() {
        return countuv;
    }

    public void setCountuv(String countuv) {
        this.countuv = countuv;
    }
}
