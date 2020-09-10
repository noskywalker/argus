package com.monitor.argus.mis.controller.group.vo;

import java.io.Serializable;

/**
 * @Param
 * @Return
 * @Author xuefei
 * @Date 18/07/2017
 * @Version
 */
public class MyGroupVO implements Serializable {
    private String groupName;
    private String groupId;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
