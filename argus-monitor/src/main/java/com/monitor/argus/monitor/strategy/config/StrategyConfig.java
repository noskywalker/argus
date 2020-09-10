package com.monitor.argus.monitor.strategy.config;

import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.regex.Pattern;

/**
 * Created by zhangxianshuang on 2016/7/12.
 */
public abstract class StrategyConfig {

    protected StrategyType strategyType;
    private static final int STATUS_OK=1;
    private String systemId;
    private String hostName;
    private String ip;
    protected Pattern pattern;
    protected Pattern noPattern;
    // 自定义的下发内容
    private String sendContent;
    // 是否下发
    private int isSendContent;

    public Pattern getNoPattern() {
        return noPattern;
    }

    public void setNoPattern(Pattern noPattern) {
        this.noPattern = noPattern;
    }

    public int getIsSendContent() {
        return isSendContent;
    }

    public void setIsSendContent(int isSendContent) {
        this.isSendContent = isSendContent;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public abstract void setStrategyType(StrategyType strategyType);
    public abstract StrategyType getStrategyType();
    public abstract Object parseStrategy();

    public boolean isAvailable() {
        return status==STATUS_OK;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;
    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    private String alarmId;
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private int level;
    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    private String systemName;


    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    private String strategyName;
    private String monitorId;

    public String getMonitorStrategyContent() {
        return monitorStrategyContent;
    }

    public void setMonitorStrategyContent(String monitorStrategyContent) {
        this.monitorStrategyContent = monitorStrategyContent;
    }

    private String monitorStrategyContent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StrategyConfig config = (StrategyConfig) o;

        return new EqualsBuilder()
                .append(systemId, config.systemId)
                .append(hostName, config.hostName)
                .append(ip, config.ip)
                .append(alarmId, config.alarmId)
                .append(monitorId, config.monitorId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(systemId)
                .append(hostName)
                .append(ip)
                .append(alarmId)
                .append(monitorId)
                .toHashCode();
    }

    public boolean configMatcher(String msg) {
        boolean isFind = false;
        if (this.pattern != null && !StringUtil.isEmpty(msg)) {
            isFind = this.pattern.matcher(msg).find();
        }
        return isFind;
    }

    public boolean configNoMatcher(String msg) {
        boolean isFind = false;
        if (this.noPattern != null && !StringUtil.isEmpty(msg)) {
            isFind = this.noPattern.matcher(msg).find();
        }
        return isFind;
    }

}
