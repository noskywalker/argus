package com.monitor.argus.mis.controller.node.form;

/**
 * Created by wangfeng on 16/9/20.
 */
public class AddNodeForm {
    private String id;
    private String nodeName;
    private String nodeKey;
    private String nodeSystemName;
    private String nodeSystemId;
    private String nodeUrl;
    private boolean enable;
    private String createDate;
    private boolean enableHidden;
    // 是否采集uv数据
    private int isUv;
    // 是否接口计算
    private int isInterface;

    public int getIsInterface() {
        return isInterface;
    }

    public void setIsInterface(int isInterface) {
        this.isInterface = isInterface;
    }

    public int getIsUv() {
        return isUv;
    }

    public void setIsUv(int isUv) {
        this.isUv = isUv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }

    public void setNodeUrl(String nodeUrl) {
        this.nodeUrl = nodeUrl;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnableHidden() {
        return enableHidden;
    }

    public void setEnableHidden(boolean enableHidden) {
        this.enableHidden = enableHidden;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getNodeSystemName() {
        return nodeSystemName;
    }

    public void setNodeSystemName(String nodeSystemName) {
        this.nodeSystemName = nodeSystemName;
    }

    public String getNodeSystemId() {
        return nodeSystemId;
    }

    public void setNodeSystemId(String nodeSystemId) {
        this.nodeSystemId = nodeSystemId;
    }
}
