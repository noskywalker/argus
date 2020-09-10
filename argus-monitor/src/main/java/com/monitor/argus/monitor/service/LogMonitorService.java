/**
 *
 */
package com.monitor.argus.monitor.service;

import com.monitor.argus.bean.base.EntityBase;
import com.monitor.argus.bean.log.LogEntityDTO;
import com.monitor.argus.monitor.statistics.Statistics;
import com.monitor.argus.monitor.strategy.BaseMonitorStrategy;
import com.monitor.argus.monitor.strategy.config.StrategyCache;
import com.monitor.argus.monitor.strategy.config.StrategyConfig;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.monitor.strategy.StrategyFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * All rights Reserved, Designed By alex zhang
 *
 * @version V1.0
 * @Title: LogMonitorService.java
 * @Package com.monitor.argus.monitor.service
 * @Description: TODO
 * @author: alex zhang
 * @date: 2016年7月12日 上午10:37:48
 */
@Service
public class LogMonitorService extends BaseLogService {

    public static final int MERGE_TIME_ZONE_SECONDS = 60;
    static HashMap<String, BaseMonitorStrategy> strategies = new HashMap<String, BaseMonitorStrategy>();

    Logger logger = LoggerFactory.getLogger(getClass());
    TreeMap<String, Long> alarmQueueList = new TreeMap<String, Long>();


    @Autowired
    RedisService redisService;

    public RedisService getRedisService() {
        return redisService;
    }

    @Autowired
    StrategyCache strategyCache;

    public boolean doMonitor(EntityBase entityBase, ApplicationContext appContext) {
        while (!strategyCache.IS_LOADED) {
            logger.info("waiting for the strategy loading finished!");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        LogEntityDTO logEntity = (LogEntityDTO) entityBase;
        String ip = logEntity.getIp();
        if(StringUtils.isEmpty(ip)){
            //logger.warn("日志实体没有找到ip地址，无法解析,logid：{}",logEntity.getLogId());
            return false;
        }

        // 根据ip计算流量
        statisticsSystemDataByIp(ip, logEntity.getLogLength());

        List<StrategyConfig> configs = strategyCache.getStrategyConfig(ip);
        if(CollectionUtils.isEmpty(configs)){
            //logger.warn("日志没有找到监控策略配置，无法解析,ip:{},logid：{}",logEntity.getIp(),logEntity.getLogId());
            return false;
        }

        for(StrategyConfig config : configs){
            StrategyType strategyType = config.getStrategyType();
            BaseMonitorStrategy monitorStrategy= StrategyFactory.getStrategy(strategyType,appContext);
            if (monitorStrategy != null) {
                monitorStrategy.process(entityBase,config);
            }
        }

        return true;
    }

    private void statisticsSystemDataByIp(String ip, long logLength) {
        if (!StringUtil.isEmpty(ip) && Statistics.ipStatisticsData != null) {
            if (Statistics.ipStatisticsData.containsKey(ip)) {
                AtomicLong systemDate = Statistics.ipStatisticsData.get(ip);
                if (systemDate != null) {
                    systemDate.getAndAdd(logLength);
                }
            } else {
                Statistics.ipStatisticsData.put(ip, new AtomicLong(logLength));
            }
        }
    }

}