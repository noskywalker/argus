package com.monitor.argus.monitor.strategy.config.enums;

/**
 * Email:alex zhang
 * Creator:usr
 * CreatedDate:九月
 * Version:V1.0.0
 */
public enum StrategyType {
    /**
     * 业务异常关键词节点监控
     */
    KEYWORD(1),

    /**
     * 业务节点数据监控
     */
    BUSINESS_NODE(2),

    /**
     * 业务节点数据比例监控
     */
    BUSINESS_NODE_PERCENT(3),

    /**
     * 业务节点数据比较监控
     */
    BUSINESS_NODE_NUMCOMPARE(4),

    /**
     * 某个事件一段时间内未按时发生的报警类型,该种类型无需日志流作为驱动检查，由storm进程开启异步线程驱动检查
     */
    EVENT_NOT_HAPPENED(5),

    /**
     * 接口超时监控
     */
    BUSINESS_INTERFACE(6),

    /**
     * 其他
     */
    OTHERS(-1);

    private int code=1;

    private StrategyType(int code_) {
        this.code = code_;
    }

    private int getCode(){
        return code;
    }

    public static StrategyType typeOf(int code_) {
        for (StrategyType type : StrategyType.values()) {
            if (code_ == type.getCode()) {
                return type;
            }
        }
        return OTHERS;
    }

}
