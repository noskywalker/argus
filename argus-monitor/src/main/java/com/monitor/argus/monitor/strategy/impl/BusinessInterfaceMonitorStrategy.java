package com.monitor.argus.monitor.strategy.impl;

import com.monitor.argus.bean.base.EntityBase;
import com.monitor.argus.bean.log.AlarmEntityDTO;
import com.monitor.argus.bean.log.LogEntityDTO;
import com.monitor.argus.common.util.*;
import com.monitor.argus.monitor.strategy.config.AsycConfigCacheService;
import com.monitor.argus.monitor.strategy.config.StrategyConfig;
import com.monitor.argus.monitor.strategy.config.enums.CompareMethod;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import com.monitor.argus.monitor.strategy.config.strategy.BusinessInterfaceMonitorStrategyyConfig;
import com.monitor.argus.monitor.strategy.impl.checker.BaseMonitorMergeHandler;
import com.monitor.argus.common.enums.AlarmLevel;
import com.monitor.argus.monitor.strategy.BaseMonitorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.regex.Matcher;

/**
 * Created by usr on 2017/5/8.
 */
@Service("businessInterfaceMonitorStrategy")
public class BusinessInterfaceMonitorStrategy extends BaseMonitorStrategy {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected BaseMonitorMergeHandler baseMonitorMergeHandler;

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
        try {
            if (config != null && config instanceof BusinessInterfaceMonitorStrategyyConfig) {
                BusinessInterfaceMonitorStrategyyConfig businessInterfaceMonitorStrategyyConfig = (BusinessInterfaceMonitorStrategyyConfig) config;
                LogEntityDTO logEntityDTO = (LogEntityDTO) entitybase;
                String times = logEntityDTO.getTimes();
                String msg = logEntityDTO.getFullMessage();
                if (getConfig(msg, times, businessInterfaceMonitorStrategyyConfig) != null) {
                    boolean isNeed = false;
                    double compareValue = businessInterfaceMonitorStrategyyConfig.getCompareValue();
                    CompareMethod compareMethod = businessInterfaceMonitorStrategyyConfig.getCompareMethod();
                    // 阈值比较
                    double timesNum = Double.valueOf(times);
                    switch (compareMethod) {
                        case EQUAL:
                            if (timesNum == compareValue) {
                                isNeed = true;
                            }
                            break;
                        case LARGER:
                            if (timesNum > compareValue) {
                                isNeed = true;
                            }
                            break;
                        case SMALLER:
                            if (timesNum < compareValue) {
                                isNeed = true;
                            }
                            break;
                    }
                    if (isNeed) {
                        // 监控
                        logEntityDTO.setMonitorStrategyId(config.getMonitorId());
                        String addContent = ",接口的延迟时间为:" + timesNum;
                        addSystemMonitor(businessInterfaceMonitorStrategyyConfig.getSystemId(), config.getMonitorId());
                        String queueName = RedisKeyUtils.happenedKey(config.getSystemId(), config.getMonitorId());
                        redisService.lpush(queueName, System.currentTimeMillis() + "");
                        String systemId = businessInterfaceMonitorStrategyyConfig.getSystemId();
                        String monitorStrategyId = businessInterfaceMonitorStrategyyConfig.getMonitorId();
                        int count = businessInterfaceMonitorStrategyyConfig.getMonitorCountBeforeMerge();
                        int cycle = businessInterfaceMonitorStrategyyConfig.getSecondsBeforeMerge();
                        int waitSeconds = businessInterfaceMonitorStrategyyConfig.getWaitSecondsAfterMerge();
                        baseMonitorMergeHandler.submitMergeQueueCheckTask(queueName, systemId, monitorStrategyId, count, cycle, waitSeconds);
                        addMonitorMergeBufferQueue(logEntityDTO, businessInterfaceMonitorStrategyyConfig, addContent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private StrategyConfig getConfig(String msg, String times, BusinessInterfaceMonitorStrategyyConfig config) {
        if (!StringUtil.isEmpty(times)) {
            times = times.trim();
            Matcher ipMatcher = AsycConfigCacheService.numPattern.matcher(times);
            if (ipMatcher != null && ipMatcher.find()) {
                if (config.isAvailable() && config.configMatcherInterface(msg)) {
                    return config;
                }
            }
        }
        return null;
    }

    private void addMonitorMergeBufferQueue(LogEntityDTO entity, BusinessInterfaceMonitorStrategyyConfig strategyConfig,
                                            String addContent) {
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
            redisService.expire(mergeBufferKey, ArgusUtils.MERGE_BUFFER_QUEUE_EXPIRE_SECONDS);
        } else {
            //default is 0,not including current LogEntity itself
            int totalMonitorAlarmCount = 0;
            if (mergeBufferQueueSize > 0) {
                logger.debug("监控合并队列不为空，合并产生报警,KEY为:{},报警个数为:{}", mergeBufferKey, mergeBufferQueueSize);
                long theFirstTriggeredTime = Long.valueOf(redisService.rpop(mergeBufferKey));
                totalMonitorAlarmCount += mergeBufferQueueSize;
                redisService.delete(mergeBufferKey);
                generateMonitorAlarm(entity, theFirstTriggeredTime, totalMonitorAlarmCount, strategyConfig, addContent);
            } else {
                logger.debug("监控合并队列为空，直接产生报警");
                generateMonitorAlarm(entity, strategyConfig, addContent);
            }
        }
    }

    private void generateMonitorAlarm(LogEntityDTO entity, long theFirstTriggeredTime, int totalMonitorAlarmCount,
                                      StrategyConfig strategyConfig, String addContent) {
        AlarmEntityDTO alarmDto = AlarmEntityDTO.generateByLogEntity(entity);
        alarmDto.setLevel(AlarmLevel.getAlarmLevel(strategyConfig.getLevel()));
        alarmDto.setBeginTime(DateUtil.getDateLongTimePlusStr(new Date(theFirstTriggeredTime)));
        alarmDto.setTotalAlarmCount(totalMonitorAlarmCount);
        alarmDto.setSystemName(strategyConfig.getSystemName());
        alarmDto.setMonitorStrategyName(strategyConfig.getStrategyName());
        alarmDto.setAlarmId(strategyConfig.getAlarmId());
        alarmDto.setMessage(entity.getFullMessage() + addContent);
        // alarmDto.setAlarmId(strategyConfig.getAlarmId());
        // 替换为自定义内容
        if (strategyConfig.getIsSendContent() == 1) {
            alarmDto.setMessage(strategyConfig.getSendContent() + addContent);
        }
        String queueKey = RedisKeyUtils.alarmQueueKey(entity.getIp(), entity.getMonitorStrategyId());
        redisService.lpush(queueKey, JsonUtil.beanToJson(alarmDto));
        addSystemAlarm(strategyConfig.getSystemId(), strategyConfig.getAlarmId());
        /**set the expire time of the alarm queue,prevent the info always take over the redis space,when after 5 hours,the alarm info always useless*/
        redisService.expire(queueKey,ArgusUtils.ALARM_QUEUE_EXPIRE_SECONDS);

        shouldAddOrUpdateIntoAlarmQueueName(queueKey);
    }

    private void generateMonitorAlarm(LogEntityDTO entity, StrategyConfig strategyConfig,
                                      String addContent) {
        AlarmEntityDTO alarmDto = AlarmEntityDTO.generateByLogEntity(entity);

        alarmDto.setSystemName(strategyConfig.getSystemName());
        alarmDto.setMonitorStrategyName(strategyConfig.getStrategyName());
        alarmDto.setLevel(AlarmLevel.getAlarmLevel(strategyConfig.getLevel()));
        alarmDto.setAlarmId(strategyConfig.getAlarmId());
        alarmDto.setMessage(entity.getFullMessage() + addContent);
        // 替换为自定义内容
        if (strategyConfig.getIsSendContent() == 1) {
            alarmDto.setMessage(strategyConfig.getSendContent() + addContent);
        }
        String queueKey = RedisKeyUtils.alarmQueueKey(entity.getIp(), entity.getMonitorStrategyId());
        redisService.lpush(queueKey, JsonUtil.beanToJson(alarmDto));
        addSystemAlarm(strategyConfig.getSystemId(), strategyConfig.getAlarmId());
        /**set the expire time of the alarm queue,prevent the info always take over the redis space,when after 5 hours,the alarm info always useless*/
        redisService.expire(queueKey,ArgusUtils.ALARM_QUEUE_EXPIRE_SECONDS);
        shouldAddOrUpdateIntoAlarmQueueName(queueKey);
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

    private boolean isMonitorShouldMerged(StrategyConfig strategyConfig, String strategyId) {
        String mergeKey = RedisKeyUtils.shouldMerge(strategyConfig.getSystemId(), strategyId);
        return redisService.exists(mergeKey);
    }

}
