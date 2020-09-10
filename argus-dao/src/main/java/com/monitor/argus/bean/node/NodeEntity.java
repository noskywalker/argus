package com.monitor.argus.bean.node;

import com.monitor.argus.common.model.PageHelper;

/**
 * Created by wangfeng on 16/9/19.
 */
public class NodeEntity extends PageHelper implements java.io.Serializable {

    private static final long serialVersionUID = -2944577839429798733L;

    private String id;
    private String nodeName;
    private String nodeKey;
    private String nodeSystemName;
    private String nodeSystemId;
    private String nodeUrl;
    private String enable;
    private String createDate;
    private String timeNum;
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

    public String getTimeNum() {
        return timeNum;
    }

    public void setTimeNum(String timeNum) {
        this.timeNum = timeNum;
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

    public String getNodeUrl() {
        return nodeUrl;
    }

    public void setNodeUrl(String nodeUrl) {
        this.nodeUrl = nodeUrl;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}
