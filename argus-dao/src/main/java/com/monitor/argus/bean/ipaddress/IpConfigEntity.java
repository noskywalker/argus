package com.monitor.argus.bean.ipaddress;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huxiaolei on 2016/11/7.
 */
public class IpConfigEntity implements Serializable {

    private static final long serialVersionUID = 7216798064360323618L;
    private long id;
    private String ipStart;
    private String ipEnd;
    private long ipStartNum;
    private long ipEndNum;
    private String addressDetail;
    private String addressShort;
    private Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIpStart() {
        return ipStart;
    }

    public void setIpStart(String ipStart) {
        this.ipStart = ipStart;
    }

    public String getIpEnd() {
        return ipEnd;
    }

    public void setIpEnd(String ipEnd) {
        this.ipEnd = ipEnd;
    }

    public long getIpStartNum() {
        return ipStartNum;
    }

    public void setIpStartNum(long ipStartNum) {
        this.ipStartNum = ipStartNum;
    }

    public long getIpEndNum() {
        return ipEndNum;
    }

    public void setIpEndNum(long ipEndNum) {
        this.ipEndNum = ipEndNum;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getAddressShort() {
        return addressShort;
    }

    public void setAddressShort(String addressShort) {
        this.addressShort = addressShort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
