package com.monitor.argus.monitor.strategy.config.strategy;

import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import com.monitor.argus.monitor.strategy.config.StrategyConfig;
import org.apache.commons.lang.math.NumberUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 业务异常关键词监控
 * Email:alex zhang
 * Creator:usr
 * CreatedDate:九月
 * Version:V1.0.0
 */
public class KeyWordsStrategyConfig extends StrategyConfig {
    private StrategyType strategyType;
    private int secondsBeforeMerge;
    private int monitorCountBeforeMerge;
    private int waitSecondsAfterMerge;
    private String monitorContent;
    private String noMonitorContent;

    public String getNoMonitorContent() {
        return noMonitorContent;
    }

    public void setNoMonitorContent(String noMonitorContent) {
        this.noMonitorContent = noMonitorContent;
    }

    public String getMonitorContent() {
        return monitorContent;
    }

    public void setMonitorContent(String monitorContent) {
        this.monitorContent = monitorContent;
    }
    public int getSecondsBeforeMerge() {
        return secondsBeforeMerge;
    }

    public void setSecondsBeforeMerge(int secondsBeforeMerge) {
        this.secondsBeforeMerge = secondsBeforeMerge;
    }

    public int getMonitorCountBeforeMerge() {
        return monitorCountBeforeMerge;
    }

    public void setMonitorCountBeforeMerge(int monitorCountBeforeMerge) {
        this.monitorCountBeforeMerge = monitorCountBeforeMerge;
    }

    public int getWaitSecondsAfterMerge() {
        return waitSecondsAfterMerge;
    }

    public void setWaitSecondsAfterMerge(int waitSecondsAfterMerge) {
        this.waitSecondsAfterMerge = waitSecondsAfterMerge;
    }

    @Override
    public void setStrategyType(StrategyType strategyType) {
        this.strategyType=strategyType;
    }

    @Override
    public StrategyType getStrategyType(){
        return strategyType;
    }


    public boolean alarmDirectly(){
        return secondsBeforeMerge==0&&monitorCountBeforeMerge==0&&waitSecondsAfterMerge==0;
    }
    @Override
    public Object parseStrategy() {
        Map<String, Object> strategyItem = (Map<String, Object>) JsonUtil.jsonToBean(getMonitorStrategyContent(), HashMap.class);
        double cycle = NumberUtils.toDouble(String.valueOf(strategyItem.get("monitorCycle")),0);
        double monitorCountBeforeMerge = NumberUtils.toDouble(String.valueOf(strategyItem.get("occurrentedCount")),0);
        double mergeWaitTimes = NumberUtils.toDouble(String.valueOf(strategyItem.get("waitTime")),0);
        String content = String.valueOf(strategyItem.get("monitorContent"));
        String noContent = "";
        if (strategyItem.get("noMonitorContent") != null) {
            noContent = String.valueOf(strategyItem.get("noMonitorContent"));
        }


        this.secondsBeforeMerge=(int)cycle;
        this.monitorCountBeforeMerge=(int) monitorCountBeforeMerge;
        this.waitSecondsAfterMerge=(int) mergeWaitTimes;

        this.monitorContent=content;
        this.noMonitorContent = noContent;

        // 预加载正则表达式
        if (!StringUtil.isEmpty(this.monitorContent)) {
            this.pattern =  Pattern.compile(this.monitorContent);
        }

        if (!StringUtil.isEmpty(this.noMonitorContent)) {
            this.noPattern =  Pattern.compile(this.noMonitorContent);
        }
        return this;
    }

}
