package com.monitor.argus.bean.statistic.vo;

import com.monitor.argus.common.enums.StatisticType;

/**
 * Created by Administrator on 2016/7/20.
 */
public class StatisticsValueVO implements java.io.Serializable{
    private long formerValues;
    private long currentValues;

    public StatisticType getType() {
        return type;
    }

    public void setType(StatisticType type) {
        this.type = type;
    }

    private StatisticType type;
    public long getFormerValues() {
        return formerValues;
    }

    public void setFormerValues(long formerValues) {
        this.formerValues = formerValues;
    }

    public long getCurrentValues() {
        return currentValues;
    }

    public void setCurrentValues(long currentValues) {
        this.currentValues = currentValues;
    }

    public long getDifferenceValues() {
        return differenceValues;
    }

    public void setDifferenceValues(long differenceValues) {
        this.differenceValues = differenceValues;
    }

    private long differenceValues;

}
