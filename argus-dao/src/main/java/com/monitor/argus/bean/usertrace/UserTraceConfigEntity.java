package com.monitor.argus.bean.usertrace;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by usr on 2017/4/7.
 */
public class UserTraceConfigEntity implements Serializable {

    private static final long serialVersionUID = 2978005821080371416L;

    private Integer id;
    private String nodeUrl;
    private String nodeName;
    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }

    public void setNodeUrl(String nodeUrl) {
        this.nodeUrl = nodeUrl;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
