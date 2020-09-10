package com.monitor.argus.monitor.strategy;

import com.monitor.argus.bean.base.EntityBase;
import com.monitor.argus.monitor.statistics.Statistics;
import com.monitor.argus.monitor.strategy.config.StrategyCache;
import com.monitor.argus.monitor.strategy.config.StrategyConfig;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.common.util.ArgusUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2016/7/12.
 */
public abstract class BaseMonitorStrategy {

    @Autowired
    protected RedisService redisService;

    @Autowired
    protected StrategyCache strategyCache;

    protected TreeMap<String, Long> alarmQueueList = new TreeMap<String, Long>();

    protected StrategyType strategyType;
    protected StrategyConfig strategyConfig;

    public abstract void setStrategyType(StrategyType strategyType);

    public abstract void setStrategyConfig(StrategyConfig config);

    /**
     * process the log entity by the monitor strategy
     * @param entitybase the entity base strategy
     * @return
     */
    public abstract boolean process(EntityBase entitybase, StrategyConfig config);

    public boolean expire(Long time) {
        return System.currentTimeMillis() - time > ArgusUtils.QUEUE_NAME_UPDATE_EXPIRE_TIME;
    }


    public void addSystemMonitor(String systemId, String moitorId) {
        Statistics.totalMonitorHappened.incrementAndGet();

        if (!MapUtils.isEmpty(Statistics.statisticsSystemMonitor) && Statistics.statisticsSystemMonitor.get(systemId) != null) {
            AtomicLong monitorCounts = Statistics.statisticsSystemMonitor.get(systemId);
            monitorCounts.incrementAndGet();
        }

        ConcurrentHashMap<String, AtomicLong> statisticsMonitorCount = Statistics.statisticsSystemMXMonitor.get(systemId);
        if (!MapUtils.isEmpty(statisticsMonitorCount)) {
            AtomicLong mCounts = statisticsMonitorCount.get(moitorId);
            if (mCounts != null) {
                mCounts.incrementAndGet();
            }
        }
    }

    public void addSystemAlarm(String systemId, String alarmId) {
        Statistics.totalAlarmHappened.incrementAndGet();

        if (!MapUtils.isEmpty(Statistics.statisticsSystemAlarm) && Statistics.statisticsSystemAlarm.get(systemId) != null) {
            AtomicLong alarmCounts = Statistics.statisticsSystemAlarm.get(systemId);
            alarmCounts.incrementAndGet();
        }

        ConcurrentHashMap<String, AtomicLong> statisticsAlarmCount = Statistics.statisticsSystemMXAlarm.get(systemId);
        if (!MapUtils.isEmpty(statisticsAlarmCount)) {
            AtomicLong aCounts = statisticsAlarmCount.get(alarmId);
            if (aCounts != null) {
                aCounts.incrementAndGet();
            }
        }
    }

}
