package com.monitor.argus.bean.dataland;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huxiaolei on 2016/10/30.
 */
public class AnalyTopologyDayUVEntity implements Serializable {

    private static final long serialVersionUID = -9072094941763190369L;

    private String id;
    private String uVCount;
    private Date createDate;
    private String nodeKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuVCount() {
        return uVCount;
    }

    public void setuVCount(String uVCount) {
        this.uVCount = uVCount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNodeKey() {
        return nodeKey;
    }

    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }

}
