package com.monitor.argus.monitor.strategy.impl;

import com.monitor.argus.bean.base.EntityBase;
import com.monitor.argus.bean.log.AlarmEntityDTO;
import com.monitor.argus.bean.log.LogEntityDTO;
import com.monitor.argus.monitor.strategy.config.StrategyConfig;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import com.monitor.argus.monitor.strategy.config.strategy.KeyWordsStrategyConfig;
import com.monitor.argus.monitor.strategy.impl.checker.BaseMonitorMergeHandler;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.common.enums.AlarmLevel;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.monitor.strategy.BaseMonitorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;

/**
* Email:alex zhang
* Creator:usr
 * CreatedDate:七月
 * Version:V1.0.0
 */
@Service("keyWordMonitorStrategy")
public class KeyWordMonitorStrategy extends BaseMonitorStrategy {

    @Autowired
    protected BaseMonitorMergeHandler baseMonitorMergeHandler;

    ThreadLocal<StrategyConfig> configThreadLocal = new ThreadLocal<>();


    Logger logger = LoggerFactory.getLogger(getClass());



    public RedisService getRedisService() {
        return redisService;
    }



    @Override
    public void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }

    @Override
    public void setStrategyConfig(StrategyConfig config) {
        this.strategyConfig = config;
    }

    @Override
    public boolean process(EntityBase entitybase, StrategyConfig config) {
        // 测试
        // KeyWordsStrategyConfig keyWordConfig1 = (KeyWordsStrategyConfig) config;
        // logger.info("config:" + keyWordConfig1.getStrategyType().name() + "|" + keyWordConfig1.getMonitorStrategyContent() + "|" +keyWordConfig1.getMonitorContent());
        try {
            if (config != null && config instanceof KeyWordsStrategyConfig) {
                KeyWordsStrategyConfig keyWordConfig = (KeyWordsStrategyConfig) config;
                LogEntityDTO logEntity = (LogEntityDTO) entitybase;
                if (getConfig(logEntity.getFullMessage(), keyWordConfig) != null) {
                    logEntity.setMonitorStrategyId(config.getMonitorId());
                     logger.info("捕获一个异常，ip:{},monitorID:{},title:{}", logEntity.getIp(), config.getMonitorId(),keyWordConfig.getStrategyName());
                    addSystemMonitor(config.getSystemId(), config.getMonitorId());
                    String queueName = RedisKeyUtils.happenedKey(config.getSystemId(), config.getMonitorId());
                    redisService.lpush(queueName, System.currentTimeMillis() + "");
                    // monitorMergeHandler.submitMergeQueueCheckTask(queueName, keyWordConfig);
                    String systemId = keyWordConfig.getSystemId();
                    String monitorStrategyId = keyWordConfig.getMonitorId();
                    int count = keyWordConfig.getMonitorCountBeforeMerge();
                    int cycle = keyWordConfig.getSecondsBeforeMerge();
                    int waitSeconds = keyWordConfig.getWaitSecondsAfterMerge();

                    if(keyWordConfig.alarmDirectly()) {
                        logger.info("拒绝合并直接产生报警，ip:{},monitorID:{},title:{}", logEntity.getIp(), config.getMonitorId(),keyWordConfig.getStrategyName());
                        generateMonitorAlarm(logEntity, strategyConfig);
                    }else {
                        baseMonitorMergeHandler.submitMergeQueueCheckTask(queueName, systemId, monitorStrategyId,
                                count, cycle, waitSeconds);
//                        isMonitorHappened(logEntity, keyWordConfig);
                        addMonitorMergeBufferQueue(logEntity, keyWordConfig);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            configThreadLocal.remove();
        }
        return false;
    }




    public static final int MERGE_TIME_ZONE_SECONDS = 60;

    static HashMap<String, BaseMonitorStrategy> strategies = new HashMap<String, BaseMonitorStrategy>();

    private StrategyConfig getConfig(String msg,KeyWordsStrategyConfig config) {
        // 过滤
        if (config.isAvailable() && config.configNoMatcher(msg)) {
            return null;
        }
        // 匹配
        if (config.isAvailable() && config.configMatcher(msg)) {
            return config;
        }
        return null;
    }

    @Deprecated
    private boolean isMonitorHappened(LogEntityDTO entity, KeyWordsStrategyConfig strategyConfig) {
        String strategyId = strategyConfig.getMonitorId();
//        Todo：fulfill the count from strategyConfig
        int count = strategyConfig.getMonitorCountBeforeMerge();
        int cycle = strategyConfig.getSecondsBeforeMerge();
        logger.debug("==cycle:{},count:{}",cycle,count);
        String current = System.currentTimeMillis() + "";
        String redisKey = RedisKeyUtils.happenedKey(entity.getIp(), strategyId);
        redisService.lpush(redisKey, current);
        redisService.expire(redisKey, ArgusUtils.HAPPEN_QUEUE_EXPIRE_SECONDS);
        long monitorCount = redisService.size(redisKey);
        boolean shouldMergeAfterCurrent = false;
        while (monitorCount >= count) {
            String earlyPushValue = redisService.rpop(redisKey);
            monitorCount = redisService.size(redisKey);
            //this monitor strategy has exceeded the normal monitor range
            if (monitorCount < count) {
                if (Long.valueOf(current) - Long.valueOf(earlyPushValue) <= cycle * 1000) {
                    //this monitor has triggered more than [count] times,should be merged in future
                    logger.debug("监控超过阈值需要合并,增加合并标识,周期为{},次数为{}", strategyConfig.getSecondsBeforeMerge(), strategyConfig.getMonitorCountBeforeMerge());
                    /* TODO add the queue name into another list,we should to check and clear the queue by async thread in future */
                    // redisService.lpush(RedisKeyUtils.asyncCheckList(),redisKey);
                    monitorShouldMerge(entity, strategyId, strategyConfig.getWaitSecondsAfterMerge());
                }
            }

        }
        return true;
    }

    private void addMonitorMergeBufferQueue(LogEntityDTO entity, KeyWordsStrategyConfig strategyConfig) {
        String strategyId = strategyConfig.getMonitorId();
        long current = System.currentTimeMillis();
        String mergeBufferKey = RedisKeyUtils.monitorMergeBuffer(strategyConfig.getSystemId(), strategyId);
        long mergeBufferQueueSize = redisService.size(mergeBufferKey);

        boolean isTheTimeWaittedFinished = false;
        if (mergeBufferQueueSize > 0) {
            long theHeadMonitorTime = Long.valueOf(redisService.getFromlist(mergeBufferKey, -1));
            if (current - theHeadMonitorTime >= strategyConfig.getWaitSecondsAfterMerge() * 1000) {
                logger.debug("报警合并等待时间结束,队列当前个数为:{}", mergeBufferKey, mergeBufferQueueSize);
                isTheTimeWaittedFinished = true;
            }
        }
        if (isMonitorShouldMerged(strategyConfig, strategyId) && !isTheTimeWaittedFinished) {
            logger.debug("监控信息放入合并队列,KEY为{}", mergeBufferKey);
            redisService.lpush(mergeBufferKey, current + "");
//            redisService.expire(mergeBufferKey,strategyConfig.getWaitSecondsAfterMerge());
            /**ensure the merge buffer queue not clear too early,avoid data losting*/
            redisService.expire(mergeBufferKey,ArgusUtils.MERGE_BUFFER_QUEUE_EXPIRE_SECONDS);
        } else {
            //default is 0,not including current LogEntity itself
            int totalMonitorAlarmCount = 0;
            if (mergeBufferQueueSize > 0) {
                logger.debug("监控合并队列不为空，合并产生报警,KEY为:{},报警个数为:{}", mergeBufferKey, mergeBufferQueueSize);
                long theFirstTriggeredTime = Long.valueOf(redisService.rpop(mergeBufferKey));
                totalMonitorAlarmCount += mergeBufferQueueSize;
                redisService.delete(mergeBufferKey);
                generateMonitorAlarm(entity, theFirstTriggeredTime, totalMonitorAlarmCount, strategyConfig);
            } else {
                logger.debug("监控合并队列为空，直接产生报警");
                generateMonitorAlarm(entity, strategyConfig);
            }
        }
    }

    private void generateMonitorAlarm(LogEntityDTO entity, long theFirstTriggeredTime, int totalMonitorAlarmCount, StrategyConfig strategyConfig) {
        AlarmEntityDTO alarmDto = AlarmEntityDTO.generateByLogEntity(entity);
        alarmDto.setLevel(AlarmLevel.getAlarmLevel(strategyConfig.getLevel()));
        alarmDto.setBeginTime(DateUtil.getDateLongTimePlusStr(new Date(theFirstTriggeredTime)));
        alarmDto.setTotalAlarmCount(totalMonitorAlarmCount);
        alarmDto.setSystemName(strategyConfig.getSystemName());
        alarmDto.setMonitorStrategyName(strategyConfig.getStrategyName());
        alarmDto.setAlarmId(strategyConfig.getAlarmId());
        // alarmDto.setAlarmId(strategyConfig.getAlarmId());
        // 替换为自定义内容
        if (strategyConfig.getIsSendContent() == 1) {
            alarmDto.setMessage(strategyConfig.getSendContent());
        }
        String queueKey = RedisKeyUtils.alarmQueueKey(entity.getIp(), entity.getMonitorStrategyId());
        redisService.lpush(queueKey, JsonUtil.beanToJson(alarmDto));
        addSystemAlarm(strategyConfig.getSystemId(), strategyConfig.getAlarmId());
        /**set the expire time of the alarm queue,prevent the info always take over the redis space,when after 5 hours,the alarm info always useless*/
        redisService.expire(queueKey,ArgusUtils.ALARM_QUEUE_EXPIRE_SECONDS);

        shouldAddOrUpdateIntoAlarmQueueName(queueKey);
    }

    private void generateMonitorAlarm(LogEntityDTO entity, StrategyConfig strategyConfig) {
        AlarmEntityDTO alarmDto = AlarmEntityDTO.generateByLogEntity(entity);

        alarmDto.setSystemName(strategyConfig.getSystemName());
        alarmDto.setMonitorStrategyName(strategyConfig.getStrategyName());
        alarmDto.setLevel(AlarmLevel.getAlarmLevel(strategyConfig.getLevel()));
        alarmDto.setAlarmId(strategyConfig.getAlarmId());
        // 替换为自定义内容
        if (strategyConfig.getIsSendContent() == 1) {
            alarmDto.setMessage(strategyConfig.getSendContent());
        }
        String queueKey = RedisKeyUtils.alarmQueueKey(entity.getIp(), entity.getMonitorStrategyId());
        redisService.lpush(queueKey, JsonUtil.beanToJson(alarmDto));
        addSystemAlarm(strategyConfig.getSystemId(), strategyConfig.getAlarmId());
        /**set the expire time of the alarm queue,prevent the info always take over the redis space,when after 5 hours,the alarm info always useless*/
        redisService.expire(queueKey,ArgusUtils.ALARM_QUEUE_EXPIRE_SECONDS);
        shouldAddOrUpdateIntoAlarmQueueName(queueKey);
    }

    private boolean isMonitorShouldMerged(StrategyConfig strategyConfig, String strategyId) {
        String mergeKey = RedisKeyUtils.shouldMerge(strategyConfig.getSystemId(), strategyId);
        return redisService.exists(mergeKey);
    }

    private void monitorShouldMerge(LogEntityDTO entity, String strategyId, int waitSeconds) {
        String mergeKey = RedisKeyUtils.shouldMerge(entity.getIp(), strategyId);
        redisService.setNX(mergeKey, strategyId + "");
        redisService.expire(mergeKey, waitSeconds);
    }

    private void shouldAddOrUpdateIntoAlarmQueueName(String alarmQueueName) {
        Long theLastSetTimeStamp = alarmQueueList.get(alarmQueueName);
        if (theLastSetTimeStamp == null || expire(theLastSetTimeStamp)) {
            logger.info("检查前更新报警队列列表，更新队列名称列表:{}", alarmQueueName);
            //AlarmHandler can delete the queue name from redis
            redisService.hset(RedisKeyUtils.ALARM_QUEUE_NAME, alarmQueueName, DateUtil.getDateLongTimePlusStr(new java.util.Date()));
            alarmQueueList.put(alarmQueueName, System.currentTimeMillis());
        }
    }
}
