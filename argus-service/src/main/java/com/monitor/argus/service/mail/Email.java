package com.monitor.argus.service.mail;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class Email {

    /** 收件人 */
    private String addressee;

    /** 发件人,必填 */
    private String from;

    /** 抄送,可选 */
    private String cc;

    /** 暗送,可选 */
    private String bcc;

    /** 邮件主题,可选 */
    private String subject;

    /** 邮件内容,可选 */
    private String content;

    /** 邮件格式,可选 */
    private boolean html = false;

    /** 邮件内联文件地址如图片等,可选 */
    private Map<String, String> inLineFile;

    /** 邮件附件,可选 */
    private Map<String, String> attachmentFiles;

    /**
     * 编辑邮件模版显示内容 固定格式 EMAIL_TYPE: 邮件类型
     */
    private Map<String, Object> model;

    public String getAddressee() {
        return addressee;
    }

    @SuppressWarnings("static-access")
    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getCc() {
        return cc;
    }

    public String[] getCcs() {
        if (StringUtils.isEmpty(this.cc)) {
            return null;
        }
        cc = cc.trim();
        cc.replaceAll("；", ";");
        cc.replaceAll(" ", ";");
        cc.replaceAll(",", ";");
        cc.replaceAll("，", ";");
        cc.replaceAll("|", ";");
        return cc.split(";");
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getAddress() {
        if (StringUtils.isEmpty(this.addressee)) {
            return null;
        }
        addressee = addressee.trim();
        addressee.replaceAll("；", ";");
        addressee.replaceAll(" ", ";");
        addressee.replaceAll(",", ";");
        addressee.replaceAll("，", ";");
        addressee.replaceAll("|", ";");
        return addressee.split(";");
    }

    public Map<String, String> getInLineFile() {
        return inLineFile;
    }

    public void setInLineFile(Map<String, String> inLineFile) {
        this.inLineFile = inLineFile;
    }

    public Map<String, String> getAttachmentFiles() {
        return attachmentFiles;
    }

    public void setAttachmentFiles(Map<String, String> iles) {
        this.attachmentFiles = iles;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

}
