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
 * Created by huxiaolei on 2016/10/11.
 */
public class BusinessNodePercentStrategyConfig  extends StrategyConfig {

    // fractions分子 匹配内容
    private String monitorContentFrac;
    private Pattern patternFrac;

    // numerator分母 匹配内容
    private String monitorContentNume;
    private Pattern patternNume;

    // 周期
    private double monitorCycle;

    // 比较方式
    private CompareMethod compareMethod;

    // 阈值
    private double compareValue;

    public double getCompareValue() {
        return compareValue;
    }

    public void setCompareValue(double compareValue) {
        this.compareValue = compareValue;
    }

    public String getMonitorContentFrac() {
        return monitorContentFrac;
    }

    public void setMonitorContentFrac(String monitorContentFrac) {
        this.monitorContentFrac = monitorContentFrac;
    }

    public String getMonitorContentNume() {
        return monitorContentNume;
    }

    public void setMonitorContentNume(String monitorContentNume) {
        this.monitorContentNume = monitorContentNume;
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

    @Override
    public void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }

    @Override
    public StrategyType getStrategyType() {
        return strategyType;
    }

    @Override
    public Object parseStrategy() {
        Map<String, Object> strategyItem = (Map<String, Object>) JsonUtil.jsonToBean(getMonitorStrategyContent(), HashMap.class);

        String contentFrac = String.valueOf(strategyItem.get("monitorContentFrac"));
        String contentNume = String.valueOf(strategyItem.get("monitorContentNume"));
        double cycle = NumberUtils.toDouble(String.valueOf(strategyItem.get("monitorCycle")));
        double cValue = NumberUtils.toDouble(String.valueOf(strategyItem.get("compareValue")));
        double methodCode = NumberUtils.toDouble(String.valueOf(strategyItem.get("compareMethod")));
        int methodCodeInt = (int)methodCode;
        CompareMethod method = CompareMethod.methodOf(methodCodeInt);

        this.monitorContentFrac = contentFrac;
        this.monitorContentNume = contentNume;
        this.monitorCycle = cycle;
        this.compareValue = cValue;
        this.compareMethod = method;

        // 预加载正则表达式
        if (!StringUtil.isEmpty(this.monitorContentFrac)) {
            this.patternFrac =  Pattern.compile(this.monitorContentFrac);
        }
        if (!StringUtil.isEmpty(this.monitorContentNume)) {
            this.patternNume =  Pattern.compile(this.monitorContentNume);
        }
        return this;
    }

    public boolean configMatcherFrac(String msg) {
        boolean isFind = false;
        if (this.patternFrac != null && !StringUtil.isEmpty(msg)) {
            isFind = this.patternFrac.matcher(msg).find();
        }
        return isFind;
    }

    public boolean configMatcherNume(String msg) {
        boolean isFind = false;
        if (this.patternNume != null && !StringUtil.isEmpty(msg)) {
            isFind = this.patternNume.matcher(msg).find();
        }
        return isFind;
    }

}
