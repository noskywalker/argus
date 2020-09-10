package com.monitor.argus.common.util.sms;

public class DetailsJaxb {

    private String showNo;
    private String mobile;
    private String sendTime;
    private String[] keywords;
    private String remark;
    private String priority;
    private String content;
    private String signInfo;
    private long seqId;

    /**
     * @return the showNo
     */
    public String getShowNo() {
        return showNo;
    }

    /**
     * @param showNo the showNo to set
     */
    public void setShowNo(String showNo) {
        this.showNo = showNo;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the sendTime
     */
    public String getSendTime() {
        return sendTime;
    }

    /**
     * @param sendTime the sendTime to set
     */
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * @return the keywords
     */
    public String[] getKeywords() {
        return keywords;
    }

    /**
     * @param keywords the keywords to set
     */
    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
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
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the signInfo
     */
    public String getSignInfo() {
        return signInfo;
    }

    /**
     * @param signInfo the signInfo to set
     */
    public void setSignInfo(String signInfo) {
        this.signInfo = signInfo;
    }

    /**
     * @return the seqId
     */
    public long getSeqId() {
        return seqId;
    }

    /**
     * @param seqId the seqId to set
     */
    public void setSeqId(long seqId) {
        this.seqId = seqId;
    }

}