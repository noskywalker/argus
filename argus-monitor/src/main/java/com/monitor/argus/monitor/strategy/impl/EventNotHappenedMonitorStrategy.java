package com.monitor.argus.monitor.strategy.impl;

import com.monitor.argus.bean.base.EntityBase;
import com.monitor.argus.monitor.strategy.BaseMonitorStrategy;
import com.monitor.argus.monitor.strategy.config.StrategyConfig;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import com.monitor.argus.monitor.strategy.config.strategy.EventNotHappenedStrategyConfig;
import org.joda.time.DateTime;

import java.util.Date;

/**
 *
 * 事件发生监控策略，使用场景如下：
        * 1、某个事件需要在每天的13:00发生，允许误差时间为600秒，则在12:50-13:10之间发生则有效，否则报警
        * 2、某个事件需要在2012.12.1 13:00发生，允许误差时间为600s，则在2012.12.1的12:50-13:10之间发生则有效，其他时间则报警
 * Email:alex zhang
 * Creator:usr
 * CreatedDate:七月
 * Version:V1.0.0
 */
//@Service("eventNotHappenedMonitorStrategy")
    @Deprecated
public class EventNotHappenedMonitorStrategy extends BaseMonitorStrategy {

    volatile ThreadLocal<DateRangeForEvent> dateThreadLocal=new ThreadLocal<DateRangeForEvent>();


    /**
     * argus:event:hostIp:strategyId:DateTime
     */
    public static String EVENT_ALARM_KEY_TEMPLATE="argus:event:%s:%s:%s";

    @Override
    public void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }

    @Override
    public void setStrategyConfig(StrategyConfig config) {
        this.strategyConfig = strategyConfig;
    }

    @Override
    public boolean process(EntityBase entitybase, StrategyConfig config) {

        if(config!=null && config instanceof EventNotHappenedStrategyConfig){
            EventNotHappenedStrategyConfig eventNotHappenedStrategyConfig= (EventNotHappenedStrategyConfig) config;
            String fetchContent=eventNotHappenedStrategyConfig.getMonitorConfigContent();

            String timeOrDate=eventNotHappenedStrategyConfig.getHappenedDateOrTime();

            long betweened=eventNotHappenedStrategyConfig.getTimeBetweenedForSeconds();

            DateType dateType=parseDateCompareType(timeOrDate);

            switch (dateType){
                case DATE:break;
                case TIME:break;
            }
        }

        return false;
    }

    protected void doEventHappenedJudgement4Date(EntityBase entityBase,EventNotHappenedStrategyConfig strategyConfig){

        String timeOrDate=strategyConfig.getHappenedDateOrTime();
        long betweened=strategyConfig.getTimeBetweenedForSeconds();

        DateRangeForEvent dateRangeForEvent=dateThreadLocal.get();

        if(dateRangeForEvent==null){
            DateTime original=new DateTime(timeOrDate);
            Date begin=original.minus(betweened).toDate();
            Date end=original.plus(betweened).toDate();
            DateRangeForEvent dateRange=new DateRangeForEvent();
            dateRange.setBegin(begin);
            dateRange.setEnd(end);
            dateThreadLocal.set(dateRange);
        }
        if(dateRangeForEvent.currentIsInDateRange()){
            //当前时间在需要判断的区间内，如果该区间内没有该事件发生，则需要报警

        }

    }

    private String monitorKeyForEventAlarm(EntityBase entityBase,EventNotHappenedStrategyConfig strategyConfig){

        String redisKey=String.format(EVENT_ALARM_KEY_TEMPLATE,entityBase.getId(),strategyConfig.getMonitorId(),DateTime.now());
        return redisKey;
    }

    protected void doEventHappenedJudgement4Time(EntityBase entityBase,StrategyConfig strategyConfig){


    }

    private DateType parseDateCompareType(String timeOrDate) {
        if (timeOrDate.length() >= 10) {
            return DateType.DATE;
        } else {
            return DateType.TIME;
        }
    }

    private enum DateType{
        DATE,TIME
    }

    private class DateRangeForEvent{
        Date begin;

        public Date getBegin() {
            return begin;
        }

        public void setBegin(Date begin) {
            this.begin = begin;
        }

        public Date getEnd() {
            return end;
        }

        public void setEnd(Date end) {
            this.end = end;
        }

        Date end;

        public boolean currentIsInDateRange(){
            long millsForMiddle=System.currentTimeMillis();
            long millsForLeft=begin.getTime();
            long millsForRight=end.getTime();
            return millsForMiddle>=millsForLeft&&millsForMiddle<=millsForRight;
        }

        public boolean isEventExpired(long eventLastSeconds){
            long millsForCurrent=System.currentTimeMillis();
            long millsForRight=end.getTime();
            return millsForCurrent-millsForRight>(eventLastSeconds*1000);
        }

    }
}
