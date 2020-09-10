package com.monitor.argus.monitor.strategy.impl;

import com.monitor.argus.bean.base.EntityBase;
import com.monitor.argus.bean.log.AlarmEntityDTO;
import com.monitor.argus.bean.log.LogEntityDTO;
import com.monitor.argus.common.util.*;
import com.monitor.argus.monitor.strategy.BaseMonitorStrategy;
import com.monitor.argus.monitor.strategy.config.StrategyConfig;
import com.monitor.argus.monitor.strategy.config.enums.CompareMethod;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import com.monitor.argus.monitor.strategy.config.strategy.BusinessNodeNumCompareStrategyConfig;
import com.monitor.argus.monitor.strategy.impl.checker.BaseMonitorMergeHandler;
import com.monitor.argus.common.enums.AlarmLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by huxiaolei on 2016/10/17.
 */
@Service("businessNodeNumCompareMonitorStrategy")
public class BusinessNodeNumCompareMonitorStrategy  extends BaseMonitorStrategy {

    Logger logger = LoggerFactory.getLogger(getClass());

    final  static ReentrantLock lockbnnm = new ReentrantLock(false);

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
            if (config != null && config instanceof BusinessNodeNumCompareStrategyConfig) {
                LogEntityDTO logEntityDTO = (LogEntityDTO) entitybase;
                BusinessNodeNumCompareStrategyConfig businessNodeNumCompareStrategyConfig = (BusinessNodeNumCompareStrategyConfig) config;
                String msg = logEntityDTO.getFullMessage();
                if (lockbnnm.isLocked()) {
                    return false;
                }
                // 匹配节点
                if (getConfig(msg, businessNodeNumCompareStrategyConfig) != null) {
                    // 单点串行执行
                    if (lockbnnm.tryLock()) {
                        try {
                            // 获取所有分组
                            List<String> groupList = businessNodeNumCompareStrategyConfig.getMatcherGroup(msg);
                            if (!CollectionUtils.isEmpty(groupList)) {
                                // 分组内数据比较,返回可触发报警的分组
                                List<String> alarmGroupList = compareNumInGroup(groupList, businessNodeNumCompareStrategyConfig);
                                if (!CollectionUtils.isEmpty(alarmGroupList)) {
                                    logEntityDTO.setMonitorStrategyId(config.getMonitorId());
                                    String addContent = ";实际数据:" + alarmGroupList.toString();
                                    addSystemMonitor(businessNodeNumCompareStrategyConfig.getSystemId(), config.getMonitorId());
                                    String queueName = RedisKeyUtils.happenedKey(config.getSystemId(), config.getMonitorId());
                                    redisService.lpush(queueName, System.currentTimeMillis() + "");
                                    String systemId = businessNodeNumCompareStrategyConfig.getSystemId();
                                    String monitorStrategyId = businessNodeNumCompareStrategyConfig.getMonitorId();
                                    int count = businessNodeNumCompareStrategyConfig.getMonitorCountBeforeMerge();
                                    int cycle = businessNodeNumCompareStrategyConfig.getSecondsBeforeMerge();
                                    int waitSeconds = businessNodeNumCompareStrategyConfig.getWaitSecondsAfterMerge();
                                    baseMonitorMergeHandler.submitMergeQueueCheckTask(queueName, systemId, monitorStrategyId, count, cycle, waitSeconds);
                                    // isMonitorHappened(logEntity, config);
                                    addMonitorMergeBufferQueue(logEntityDTO, businessNodeNumCompareStrategyConfig, addContent);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            lockbnnm.unlock();
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<String> compareNumInGroup(List<String> groupList, BusinessNodeNumCompareStrategyConfig businessNodeNumCompareStrategyConfig) {
        List<String> alarmGroupList  = new ArrayList<>();
        boolean isNeed = false;
        for (String groupStr : groupList) {
            String resultData = businessNodeNumCompareStrategyConfig.getMatcherNumber(groupStr);
            if (!StringUtil.isEmpty(resultData) && CharacterUtil.isNumberForFloat3(resultData)) {
                double compareValue = businessNodeNumCompareStrategyConfig.getCompareValue();
                CompareMethod compareMethod = businessNodeNumCompareStrategyConfig.getCompareMethod();
                // 阈值比较
                double nums = Double.valueOf(resultData);
                switch (compareMethod) {
                    case EQUAL:
                        if (nums == compareValue) {
                            isNeed = true;
                        }
                        break;
                    case LARGER:
                        if (nums > compareValue) {
                            isNeed = true;
                        }
                        break;
                    case SMALLER:
                        if (nums < compareValue) {
                            isNeed = true;
                        }
                        break;
                }
                if (isNeed) {
                    alarmGroupList.add(groupStr);
                }
            }
        }
        return alarmGroupList;
    }

    private StrategyConfig getConfig(String msg, BusinessNodeNumCompareStrategyConfig config) {
        if (config.isAvailable() && config.configMatcherNode(msg)) {
            return config;
        }
        return null;
    }

    private void addMonitorMergeBufferQueue(LogEntityDTO entity, BusinessNodeNumCompareStrategyConfig strategyConfig,
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

    private boolean isMonitorShouldMerged(StrategyConfig strategyConfig, String strategyId) {
        String mergeKey = RedisKeyUtils.shouldMerge(strategyConfig.getSystemId(), strategyId);
        return redisService.exists(mergeKey);
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

}
