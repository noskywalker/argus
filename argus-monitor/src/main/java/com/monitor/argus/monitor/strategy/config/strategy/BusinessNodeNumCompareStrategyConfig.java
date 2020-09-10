package com.monitor.argus.monitor.strategy.config.strategy;

import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import com.monitor.argus.monitor.strategy.config.StrategyConfig;
import com.monitor.argus.monitor.strategy.config.enums.CompareMethod;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huxiaolei on 2016/10/17.
 */
public class BusinessNodeNumCompareStrategyConfig extends StrategyConfig {

    private StrategyType strategyType;

    // 节点匹配内容
    private String monitorContentNode;
    private Pattern patternNode;

    // 分组匹配内容
    private String monitorContentGroup;
    private Pattern patternGroup;

    // 数字匹配内容
    private String monitorContentNumber;
    private Pattern patternNumber;

    // 比较方式
    private CompareMethod compareMethod;

    // 阈值
    private double compareValue;

    private int secondsBeforeMerge;
    private int monitorCountBeforeMerge;
    private int waitSecondsAfterMerge;

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

    public String getMonitorContentNode() {
        return monitorContentNode;
    }

    public void setMonitorContentNode(String monitorContentNode) {
        this.monitorContentNode = monitorContentNode;
    }

    public Pattern getPatternNode() {
        return patternNode;
    }

    public void setPatternNode(Pattern patternNode) {
        this.patternNode = patternNode;
    }

    public String getMonitorContentGroup() {
        return monitorContentGroup;
    }

    public void setMonitorContentGroup(String monitorContentGroup) {
        this.monitorContentGroup = monitorContentGroup;
    }

    public Pattern getPatternGroup() {
        return patternGroup;
    }

    public void setPatternGroup(Pattern patternGroup) {
        this.patternGroup = patternGroup;
    }

    public String getMonitorContentNumber() {
        return monitorContentNumber;
    }

    public void setMonitorContentNumber(String monitorContentNumber) {
        this.monitorContentNumber = monitorContentNumber;
    }

    public Pattern getPatternNumber() {
        return patternNumber;
    }

    public void setPatternNumber(Pattern patternNumber) {
        this.patternNumber = patternNumber;
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

        String contentNode = String.valueOf(strategyItem.get("monitorContentNode"));
        String contentGroup = String.valueOf(strategyItem.get("monitorContentGroup"));
        String contentNumber = String.valueOf(strategyItem.get("monitorContentNumber"));

        double cValue = NumberUtils.toDouble(String.valueOf(strategyItem.get("compareValue")));
        double methodCode = NumberUtils.toDouble(String.valueOf(strategyItem.get("compareMethod")));
        int methodCodeInt = (int)methodCode;
        CompareMethod method = CompareMethod.methodOf(methodCodeInt);

        double cycle = NumberUtils.toDouble(String.valueOf(strategyItem.get("monitorCycle")));
        double monitorCountBeforeMerge = NumberUtils.toDouble(String.valueOf(strategyItem.get("occurrentedCount")));
        double mergeWaitTimes = NumberUtils.toDouble(String.valueOf(strategyItem.get("waitTime")));

        this.monitorContentNode = contentNode;
        this.monitorContentGroup = contentGroup;
        this.monitorContentNumber = contentNumber;
        this.compareValue = cValue;
        this.compareMethod = method;
        this.secondsBeforeMerge=(int)cycle;
        this.monitorCountBeforeMerge=(int) monitorCountBeforeMerge;
        this.waitSecondsAfterMerge=(int) mergeWaitTimes;

        // 预加载正则表达式
        if (!StringUtil.isEmpty(this.monitorContentNode)) {
            this.patternNode =  Pattern.compile(this.monitorContentNode);
        }
        if (!StringUtil.isEmpty(this.monitorContentGroup)) {
            this.patternGroup =  Pattern.compile(this.monitorContentGroup);
        }
        if (!StringUtil.isEmpty(this.monitorContentNumber)) {
            this.patternNumber =  Pattern.compile(this.monitorContentNumber);
        }
        return this;
    }

    public boolean configMatcherNode(String msg) {
        boolean isFind = false;
        if (this.patternNode != null && !StringUtil.isEmpty(msg)) {
            isFind = this.patternNode.matcher(msg).find();
        }
        return isFind;
    }

    public List<String> getMatcherGroup(String msg) {
        List<String> groupList  = new ArrayList<>();
        if (this.patternGroup != null && !StringUtil.isEmpty(msg)) {
            Matcher gourpMatcher = this.patternGroup.matcher(msg);
            if (gourpMatcher != null) {
                while(gourpMatcher.find()) {
                    String groupStr = gourpMatcher.group();
                    groupList.add(groupStr);
                }
            }
        }
        return groupList;
    }

    public String getMatcherNumber(String msg) {
        String resultData = "";
        if (this.patternNumber != null && !StringUtil.isEmpty(msg)) {
            Matcher numberMatcher = this.patternNumber.matcher(msg);
            if (numberMatcher != null) {
                if(numberMatcher.find()) {
                    resultData = numberMatcher.group(1);
                }
            }
        }
        return resultData;
    }

}
