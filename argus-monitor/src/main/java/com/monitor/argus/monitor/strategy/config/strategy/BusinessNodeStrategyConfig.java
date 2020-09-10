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
 * 业务节点监控
 * Email:alex zhang
 * Creator:usr
 * CreatedDate:九月
 * Version:V1.0.0
 */
public class BusinessNodeStrategyConfig extends StrategyConfig {

    private String monitorConfigContent;
    private double monitorCycle;

    public String getMonitorConfigContent() {
        return monitorConfigContent;
    }

    public void setMonitorConfigContent(String monitorConfigContent) {
        this.monitorConfigContent = monitorConfigContent;
    }

    public double getMonitorCycle() {
        return monitorCycle;
    }

    public void setMonitorCycle(double monitorCycle) {
        this.monitorCycle = monitorCycle;
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

    private CompareMethod compareMethod;
    private double compareValue;
    public BusinessNodeStrategyConfig() {
    }

    @Override
    public void setStrategyType(StrategyType strategyType) {
        this.strategyType=strategyType;
    }

    @Override
    public StrategyType getStrategyType(){
        return strategyType;
    }

    @Override
    public Object parseStrategy() {
        Map<String, Object> strategyItem = (Map<String, Object>) JsonUtil.jsonToBean(getMonitorStrategyContent(), HashMap.class);
        double cycle = NumberUtils.toDouble(String.valueOf(strategyItem.get("monitorCycle")));
        double _compareValue = NumberUtils.toDouble(String.valueOf(strategyItem.get("compareValue")));
        String content = String.valueOf(strategyItem.get("monitorConfigContent"));
        double methodCode = NumberUtils.toDouble(String.valueOf(strategyItem.get("compareMethod")));
        int methodCodeInt = (int)methodCode;
        CompareMethod method = CompareMethod.methodOf(methodCodeInt);

        this.monitorCycle = cycle;
        this.compareValue = _compareValue;
        this.monitorConfigContent = content;
        this.compareMethod = method;

        // 预加载正则表达式
        if (!StringUtil.isEmpty(this.monitorConfigContent)) {
            this.pattern =  Pattern.compile(this.monitorConfigContent);
        }
        return this;
    }

}
