package com.monitor.argus.bean.user;

/**
 * Created by wangfeng on 16/8/26.
 */
public class WXInfoEntity {
    private String id;
    private String openId;
    private String nickName;
    private String headImgUrl;
    private String sence;
    private Integer enable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getSence() {
        return sence;
    }

    public void setSence(String sence) {
        this.sence = sence;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }
}
