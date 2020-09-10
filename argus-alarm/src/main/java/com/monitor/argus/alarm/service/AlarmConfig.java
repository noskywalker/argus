package com.monitor.argus.alarm.service;

/**
 * Email:alex zhang
 * Creator:usr
 * CreatedDate:九月
 * Version:V1.0.0
 */

public class AlarmConfig {

    public static String getWeixinRestUrl() {
        return WEIXIN_REST_URL;
    }

    public static String WEIXIN_REST_URL;

    public void setWeixinRestUrl(String weixinRestUrl) {
        WEIXIN_REST_URL = weixinRestUrl;
    }
}
