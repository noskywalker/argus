package com.monitor.argus.bean.node;

/**
 * 子类
 * Created by wangfeng on 16/11/1.
 */
public class CollectChildObject {
    private String id;
    private String pid;
    private CollectDataObject dataObject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public CollectDataObject getDataObject() {
        return dataObject;
    }

    public void setDataObject(CollectDataObject dataObject) {
        this.dataObject = dataObject;
    }
}
