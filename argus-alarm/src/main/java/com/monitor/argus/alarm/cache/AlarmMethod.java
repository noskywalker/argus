package com.monitor.argus.alarm.cache;


import com.google.common.base.Joiner;
import com.monitor.argus.common.enums.AlarmType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/15.
 */
public class AlarmMethod {
    private AlarmType alarmType;

    public AlarmType getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(AlarmType alarmType) {
        this.alarmType = alarmType;
    }

    private List<String> emails = new ArrayList<>();

    private String emailStrs = "";
    private List<String> weixins = new ArrayList<>();

    private String weixinStrs = "";
    private String phoneStrs = "";

    private List<String> phones = new ArrayList<>();

    public void addEmail(String email) {
        emails.add(email);
    }

    public void addWeixin(String weixin) {
        weixins.add(weixin);
    }

    public void addPhones(String phone) {
        phones.add(phone);
    }

    public List<String> getEmailList() {
        return emails;
    }

    public List<String> getWeixinList() {
        return weixins;
    }

    public List<String> getPhoneList() {
        return phones;
    }

    public String getEmailStrs() {
        return Joiner.on(";").skipNulls().join(emails);
    }

    public String getWeixinStrs() {

        return Joiner.on(";").skipNulls().join(weixins);
    }
}
