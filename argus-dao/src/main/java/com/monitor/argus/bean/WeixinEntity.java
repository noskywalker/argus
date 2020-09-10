package com.monitor.argus.bean;

import com.monitor.argus.bean.log.AlarmEntityDTO;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.common.util.UuidUtil;

import java.util.Date;

/**
 * Created by wangfeng on 16/8/26.
 */
public class WeixinEntity {
    private Long id;

    private String systemName;

    private String ip;

    private String time;

    private String logId;

    private String receivers;

    private String title;

    private Integer totalCount;

    private Integer level;

    private String monitorName;

    private Date createTime;

    private String message;

    private String beginTime;
    private String endTime;
    private String alarmId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName == null ? null : systemName.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId == null ? null : logId.trim();
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers == null ? null : receivers.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName == null ? null : monitorName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
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

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public static WeixinEntity generateByAlarmEntity(AlarmEntityDTO alarmEntityDTO) {
        WeixinEntity weixin = new WeixinEntity();
        weixin.setSystemName(alarmEntityDTO.getSystemName());
        String absMsg = String.format(ArgusUtils.alarmTtemplate, alarmEntityDTO.getLevel().getLevelName(), alarmEntityDTO.getSystemName(), alarmEntityDTO.getTotalAlarmCount(), alarmEntityDTO.getMonitorStrategyName());
        weixin.setBeginTime(alarmEntityDTO.getBeginTime());
        weixin.setEndTime(alarmEntityDTO.getEndTime());
        weixin.setIp(alarmEntityDTO.getIp());
        String logId = alarmEntityDTO.getOperateId();
        if (!StringUtil.isEmpty(logId)) {
            if (logId.length() > 50) {
                logId = UuidUtil.getUUID();
            }
            weixin.setLogId(logId);
        }


        weixin.setTotalCount(alarmEntityDTO.getTotalAlarmCount());

        String message = alarmEntityDTO.getMessage();
        if (!StringUtil.isEmpty(message)) {
            if (message.length() > 5000) {
                message = message.substring(0, 5000);
            }
            weixin.setMessage(message);
        }

        weixin.setMonitorName(alarmEntityDTO.getMonitorStrategyName());
        weixin.setTitle(absMsg);
        weixin.setLevel(alarmEntityDTO.getLevel().getLevelCode());
        weixin.setTime(alarmEntityDTO.getBeginTime() + "---" + alarmEntityDTO.getEndTime());
        weixin.setAlarmId(alarmEntityDTO.getAlarmId());
        return weixin;
    }
}
