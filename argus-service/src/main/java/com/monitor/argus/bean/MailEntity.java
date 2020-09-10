package com.monitor.argus.bean;

import com.monitor.argus.bean.log.AlarmEntityDTO;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/15.
 */
public class MailEntity {

    private String abstractDetail;
    private String systemName;
    private String logId;
    private String ip;

    private int totalCount;
    public String getAbstractDetail() {
        return abstractDetail;
    }

    public void setAbstractDetail(String abstractDetail) {
        this.abstractDetail = abstractDetail;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String beginTime;
    private String endTime="";
    private String monitorName;
    private String message;

    private String title;

    private String receivers;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public static MailEntity generateByAlarmEntity(AlarmEntityDTO alarmEntityDTO){
        MailEntity mail=new MailEntity();
        mail.setSystemName(alarmEntityDTO.getSystemName());
        String absMsg=String.format(ArgusUtils.alarmTtemplate,alarmEntityDTO.getLevel().getLevelName(),alarmEntityDTO.getSystemName(),alarmEntityDTO.getTotalAlarmCount(),alarmEntityDTO.getMonitorStrategyName());
        mail.setAbstractDetail(absMsg);
        mail.setBeginTime(alarmEntityDTO.getBeginTime());
        mail.setEndTime(alarmEntityDTO.getEndTime());
        mail.setIp(alarmEntityDTO.getIp());

        String logId = alarmEntityDTO.getOperateId();
        if (!StringUtil.isEmpty(logId)) {
            if (logId.length() > 50) {
                logId = logId.substring(0, 45);
            }
            mail.setLogId(logId);
        }


        mail.setTotalCount(alarmEntityDTO.getTotalAlarmCount());
        mail.setMessage(alarmEntityDTO.getMessage());
        mail.setMonitorName(alarmEntityDTO.getMonitorStrategyName());
        mail.setTitle(absMsg);
        return mail;
    }

    public Map<String,Object> getMailExtInfo(){
        Map<String,Object> extInfo=new HashMap<>();
        extInfo.put("EMAIL_TYPE",ArgusUtils.MAIL_TYPE);
        extInfo.put("abstract",abstractDetail);
        extInfo.put("systemName",systemName);
        extInfo.put("ip",ip);
        extInfo.put("time",beginTime+"---"+endTime);
        extInfo.put("totalCount",totalCount+"");
        extInfo.put("logId",logId);
        extInfo.put("monitorName",monitorName);
        extInfo.put("logMessage",message);
        return extInfo;
    }
}
