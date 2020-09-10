package com.monitor.argus.bean.node;

/**
 * 父类
 * Created by wangfeng on 16/11/1.
 */
public class NodePVCollectEntity {
    private String id;
    private String pid;
    private Integer order;
    private String isLeaf;
    private CollectDataObject dataObject;
    private CollectUserObject userObject;

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }

    public CollectDataObject getDataObject() {
        return dataObject;
    }

    public void setDataObject(CollectDataObject dataObject) {
        this.dataObject = dataObject;
    }

    public CollectUserObject getUserObject() {
        return userObject;
    }

    public void setUserObject(CollectUserObject userObject) {
        this.userObject = userObject;
    }
}
