package com.monitor.argus.common.util.sms;

/**
 * 返回封装类
 * 
 * @author Peder Yang
 *
 */
public class MessageResJaxb {

    private String batchId;
    private String orgNo;
    private String remark;
    private String retCode;
    private String retInfo;
    private String version;

    /**
     * @return the batchId
     */
    public String getBatchId() {
        return batchId;
    }

    /**
     * @param batchId the batchId to set
     */
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    /**
     * @return the orgNo
     */
    public String getOrgNo() {
        return orgNo;
    }

    /**
     * @param orgNo the orgNo to set
     */
    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the retCode
     */
    public String getRetCode() {
        return retCode;
    }

    /**
     * @param retCode the retCode to set
     */
    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    /**
     * @return the retInfo
     */
    public String getRetInfo() {
        return retInfo;
    }

    /**
     * @param retInfo the retInfo to set
     */
    public void setRetInfo(String retInfo) {
        this.retInfo = retInfo;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

}