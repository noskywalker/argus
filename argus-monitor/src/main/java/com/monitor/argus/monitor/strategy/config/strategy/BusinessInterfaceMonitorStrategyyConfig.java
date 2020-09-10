package com.monitor.argus.monitor.strategy.config.strategy;

import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.monitor.strategy.config.StrategyConfig;
import com.monitor.argus.monitor.strategy.config.enums.CompareMethod;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import org.apache.commons.lang.math.NumberUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by usr on 2017/5/8.
 */
public class BusinessInterfaceMonitorStrategyyConfig extends StrategyConfig {

    private StrategyType strategyType;

    private String monitorURI;
    private Pattern patternMonitorURI;

    private CompareMethod compareMethod;
    private double compareValue;
    private int secondsBeforeMerge;
    private int monitorCountBeforeMerge;
    private int waitSecondsAfterMerge;

    @Override
    public StrategyType getStrategyType() {
        return strategyType;
    }

    @Override
    public void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }

    public String getMonitorURI() {
        return monitorURI;
    }

    public void setMonitorURI(String monitorURI) {
        this.monitorURI = monitorURI;
    }

    public Pattern getPatternMonitorURI() {
        return patternMonitorURI;
    }

    public void setPatternMonitorURI(Pattern patternMonitorURI) {
        this.patternMonitorURI = patternMonitorURI;
    }

    public CompareMethod getCompareMethod() {
        return compareMethod;
    }

    public void setCompareMethod(CompareMethod compareMethod) {
        this.compareMethod = compareMethod;
    }

    public double getCompareValue() {
        return compareValue;
    }

    public void setCompareValue(double compareValue) {
        this.compareValue = compareValue;
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
    public Object parseStrategy() {
        Map<String, Object> strategyItem = (Map<String, Object>) JsonUtil.jsonToBean(getMonitorStrategyContent(), HashMap.class);

        String monitorURIStr = String.valueOf(strategyItem.get("monitorURI"));
        double cValue = NumberUtils.toDouble(String.valueOf(strategyItem.get("compareValue")));
        double methodCode = NumberUtils.toDouble(String.valueOf(strategyItem.get("compareMethod")));
        int methodCodeInt = (int)methodCode;
        CompareMethod method = CompareMethod.methodOf(methodCodeInt);
        double cycle = NumberUtils.toDouble(String.valueOf(strategyItem.get("monitorCycle")));
        double monitorCountBeforeMerge = NumberUtils.toDouble(String.valueOf(strategyItem.get("occurrentedCount")));
        double mergeWaitTimes = NumberUtils.toDouble(String.valueOf(strategyItem.get("waitTime")));

        this.monitorURI = monitorURIStr;
        this.compareValue = cValue;
        this.compareMethod = method;
        this.secondsBeforeMerge=(int)cycle;
        this.monitorCountBeforeMerge=(int) monitorCountBeforeMerge;
        this.waitSecondsAfterMerge=(int) mergeWaitTimes;

        if (!StringUtil.isEmpty(this.monitorURI)) {
            this.patternMonitorURI =  Pattern.compile(this.monitorURI);
        }
        return this;
    }

    public boolean configMatcherInterface(String msg) {
        boolean isFind = false;
        if (this.patternMonitorURI != null && !StringUtil.isEmpty(msg)) {
            isFind = this.patternMonitorURI.matcher(msg).find();
        }
        return isFind;
    }

}
