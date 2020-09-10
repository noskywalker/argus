package com.monitor.argus.common.util.sms;

/**
 * 请求封装类
 * 
 * @author Peder Yang
 *
 */
public class MessageReqJaxb {

    private String batchId;
    private String orgNo;
    private String typeNo;
    private DetailsJaxb[] detailsJaxb;
    private String version;
    private String channelCode;

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
     * @return the typeNo
     */
    public String getTypeNo() {
        return typeNo;
    }

    /**
     * @param typeNo the typeNo to set
     */
    public void setTypeNo(String typeNo) {
        this.typeNo = typeNo;
    }

    /**
     * @return the detailsJaxb
     */
    public DetailsJaxb[] getDetailsJaxb() {
        return detailsJaxb;
    }

    /**
     * @param detailsJaxb the detailsJaxb to set
     */
    public void setDetailsJaxb(DetailsJaxb[] detailsJaxb) {
        this.detailsJaxb = detailsJaxb;
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

    /**
     * @return the channelCode
     */
    public String getChannelCode() {
        return channelCode;
    }

    /**
     * @param channelCode the channelCode to set
     */
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

}