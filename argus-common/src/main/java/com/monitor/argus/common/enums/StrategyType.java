package com.monitor.argus.common.enums;

/**
 * Created by wangfeng on 16/9/30.
 */
public enum StrategyType {
    KEYWORD("异常监控", 1), BUSINESS_NODE("节点监控", 2), BUSINESS_NODE_PERCENT("节点比例监控", 3),
    BUSINESS_NODE_NUMCOMPARE("节点数据监控", 4), JOB("任务数据监控", 5), BUSINESS_INTERFACE("接口超时监控", 6);
    private String name;
    private int code;

    private StrategyType(String l, int code) {
        this.name = l;
        this.code = code;
    }

    public String getTypeName() {
        return name;
    }

    public int getTypeCode() {
        return code;
    }

    public static StrategyType getTypeName(int code) {
        for (StrategyType type : StrategyType.values()) {
            if (code == type.getTypeCode()) {
                return type;
            }
        }
        return KEYWORD;
    }

    public String getStrategyTypeName(int code) {
        for (StrategyType type : StrategyType.values()) {
            if (code == type.getTypeCode()) {
                return type.getTypeName();
            }
        }
        return "其它";
    }

    @Override
    public String toString() {
        return name;
    }
}


