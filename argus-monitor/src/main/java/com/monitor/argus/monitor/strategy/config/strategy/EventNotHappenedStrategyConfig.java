package com.monitor.argus.monitor.strategy.config.strategy;

import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import com.monitor.argus.monitor.strategy.config.StrategyConfig;

import java.util.HashMap;
import java.util.Map;

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
@Deprecated
public class EventNotHappenedStrategyConfig extends StrategyConfig {
    /**
     * 节点监控的解析的锚点内容，如：Exception、info等
     */
    private String monitorConfigContent;

    /**
     * 该节点发生的时间点，只设置时间，如:12:30:20
     * 或者2017.12.1 12:30:20
     */
    private String happenedDateOrTime;

    public String getHappenedDateOrTime() {
        return happenedDateOrTime;
    }

    public void setHappenedDateOrTime(String happenedDateOrTime) {
        this.happenedDateOrTime = happenedDateOrTime;
    }

    public Long getAlarmLastSeconds() {
        return alarmLastSeconds;
    }

    public void setAlarmLastSeconds(Long alarmLastSeconds) {
        this.alarmLastSeconds = alarmLastSeconds;
    }

    /**
     * 事件一旦在确定的时间段和时间点未发生，则持续报警的时长（也是过期时间的时长）
     */
    private Long alarmLastSeconds;

    public Long getTimeBetweenedForSeconds() {
        return timeBetweenedForSeconds;
    }

    public void setTimeBetweenedForSeconds(Long timeBetweenedForSeconds) {
        this.timeBetweenedForSeconds = timeBetweenedForSeconds;
    }

    public String getMonitorConfigContent() {
        return monitorConfigContent;
    }

    public void setMonitorConfigContent(String monitorConfigContent) {
        this.monitorConfigContent = monitorConfigContent;
    }

    /**
     * 允许的时间误差，秒
     */
    private Long timeBetweenedForSeconds;

    public EventNotHappenedStrategyConfig() {

    }


    @Override
    public void setStrategyType(StrategyType strategyType) {
        this.strategyType=strategyType;
    }

    @Override
    public StrategyType getStrategyType() {
        return strategyType;
    }

    @Override
    public Object parseStrategy() {
        Map<String, Object> strategyItem = (Map<String, Object>) JsonUtil.jsonToBean(getMonitorStrategyContent(), HashMap.class);
        String monitorConfigContent=(String) strategyItem.get("monitorConfigContent");
        String happdnedDateOrTime=(String) strategyItem.get("happdnedDateOrTime");
        Long timeBetweenedForSeconds=Long.parseLong((String) strategyItem.get("timeBetweenedForSeconds"));

        return null;
    }
}
