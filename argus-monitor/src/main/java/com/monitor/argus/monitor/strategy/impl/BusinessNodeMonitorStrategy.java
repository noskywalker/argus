package com.monitor.argus.monitor.strategy.impl;

import com.monitor.argus.bean.base.EntityBase;
import com.monitor.argus.bean.log.AlarmEntityDTO;
import com.monitor.argus.bean.log.LogEntityDTO;
import com.monitor.argus.monitor.strategy.config.StrategyConfig;
import com.monitor.argus.monitor.strategy.config.enums.CompareMethod;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import com.monitor.argus.monitor.strategy.config.strategy.BusinessNodeStrategyConfig;
import com.monitor.argus.common.enums.AlarmLevel;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.monitor.strategy.BaseMonitorStrategy;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Email:alex zhang
 * Creator:usr
 * CreatedDate:九月
 * Version:V1.0.0
 */
@Service("businessNodeMonitorStrategy")
public class BusinessNodeMonitorStrategy extends BaseMonitorStrategy {

    final  static ReentrantLock lock=new ReentrantLock(false);
    final  static ReentrantLock lock1 = new ReentrantLock(false);
    // static volatile long lock1time = 0L;

    Logger logger = LoggerFactory.getLogger(getClass());

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
        // BusinessNodeStrategyConfig businessNodeStrategyConfig1 = (BusinessNodeStrategyConfig) config;
        // logger.info("config:" + businessNodeStrategyConfig1.getStrategyType().name() + "|" + businessNodeStrategyConfig1.getMonitorStrategyContent() + "|" +businessNodeStrategyConfig1.getMonitorConfigContent());
        try {
            if (config != null && config instanceof BusinessNodeStrategyConfig) {
                LogEntityDTO logEntityDTO = (LogEntityDTO) entitybase;
                BusinessNodeStrategyConfig businessNodeStrategyConfig = (BusinessNodeStrategyConfig) config;

                CompareMethod compareMethod = businessNodeStrategyConfig.getCompareMethod();
                String queueName = RedisKeyUtils.BusinessNodeKey(businessNodeStrategyConfig.getMonitorId());

                if (getConfig(logEntityDTO.getFullMessage(), businessNodeStrategyConfig) != null) {
                    initQueue(queueName);
                    redisService.lpush(queueName, System.currentTimeMillis() + "");
                    isAlarmShouldGenareted(logEntityDTO, businessNodeStrategyConfig, compareMethod);
                } else {
                    if(compareMethod.equals(CompareMethod.SMALLER) || compareMethod.equals(CompareMethod.EQUAL)) {
                        logDriveStart(queueName, logEntityDTO, businessNodeStrategyConfig, compareMethod);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void logDriveStart(String queueName, LogEntityDTO logEntityDTO,
                               BusinessNodeStrategyConfig businessNodeStrategyConfig, CompareMethod compareMethod) {
        double randomNum = Math.random();
        if (randomNum < 0.01) {
            // tryLock()，马上返回，拿到lock就返回true，不然返回false
            if (lock1.tryLock()) {
                // lock1time = System.currentTimeMillis();
                try {
                    initQueue(queueName);
                    isAlarmShouldGenareted(logEntityDTO, businessNodeStrategyConfig, compareMethod);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock1.unlock();
                }
            }
        }
    }

    private void isAlarmShouldGenareted(LogEntityDTO logEntityDTO,
                                        BusinessNodeStrategyConfig businessNodeStrategyConfig, CompareMethod compareMethod) {
        String queueName = RedisKeyUtils.BusinessNodeKey(businessNodeStrategyConfig.getMonitorId());
        // 秒
        double cycle = businessNodeStrategyConfig.getMonitorCycle();
        double compareValue = businessNodeStrategyConfig.getCompareValue();
        // 取列表最早放入的数据
        String theEarliestTimeString = redisService.getFromlist(queueName, -1);
        if (StringUtils.isNotEmpty(theEarliestTimeString) && !StringUtils.equals("null", theEarliestTimeString)) {

            long theEarliestTime = Long.parseLong(theEarliestTimeString);

            boolean isTriggered = false;
            if (System.currentTimeMillis() - theEarliestTime >= cycle * 1000) {
                String checkFlag = RedisKeyUtils.MONITOR_BNMONITOR_QUEUE_CHECK_PREFIX + queueName;
                boolean setSuccess = redisService.setNX(checkFlag, "1");
                if (setSuccess) {
                    redisService.expire(checkFlag, (int)(cycle/10));
                    long size = redisService.size(queueName);
                    if (size > 0) {
                        size = size -1;
                    }
                    switch (compareMethod) {
                        case EQUAL:
                            if (size == compareValue) {
                                //clear the queue immediately
                                clearAndInitQueue(queueName);
                                isTriggered = true;
                            } else if (size > compareValue) {
                                // 重新计算
                                clearAndInitQueue(queueName);
                            }
                            break;
                        case LARGER:
                            if (size > compareValue) {
                                //clear the queue immediately
                                clearAndInitQueue(queueName);
                                isTriggered = true;
                            }
                            break;
                        case SMALLER:
                            if (size < compareValue) {
                                //clear the queue immediately
                                clearAndInitQueue(queueName);
                                isTriggered = true;
                            } else {
                                // 重新计算
                                clearAndInitQueue(queueName);
                            }
                            break;
                    }
                }
            }
            if (isTriggered) {
                addSystemMonitor(businessNodeStrategyConfig.getSystemId(), businessNodeStrategyConfig.getMonitorId());
                logEntityDTO.setMonitorStrategyId(businessNodeStrategyConfig.getMonitorId());
                generateMonitorAlarm(logEntityDTO, businessNodeStrategyConfig);
            }
        }
    }

    private void clearAndInitQueue(String queueName) {
        try {
            lock.lock();
            if(redisService.exists(queueName)) {
                redisService.delete(queueName);
                redisService.lpush(queueName, System.currentTimeMillis() + "");
            }
        }finally{
            lock.unlock();
        }
    }

    private void initQueue(String queueName) {
        try {
            String checkFlag = RedisKeyUtils.MONITOR_BNMONITOR_INITQUEUE_CHECK_PREFIX + queueName;
            boolean setSuccess = redisService.setNX(checkFlag, "1");
            if (setSuccess) {
                redisService.expire(checkFlag, ArgusUtils.MONITOR_BNMONITOR_INITQUEUE_CHECK_EXPIRE_SECONDS);
                // 初始化时间
                if (redisService.size(queueName) == 0) {
                    redisService.lpush(queueName, System.currentTimeMillis() + "");
                }
            }
        } finally {
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
        alarmDto.setAlarmId(strategyConfig.getAlarmId());

        String queueKey = RedisKeyUtils.alarmQueueKey(entity.getIp(), entity.getMonitorStrategyId());
        redisService.lpush(queueKey, JsonUtil.beanToJson(alarmDto));
        addSystemAlarm(strategyConfig.getSystemId(), strategyConfig.getAlarmId());
        /**set the expire time of the alarm queue,prevent the info always take over the redis space,when after 5 hours,the alarm info always useless*/
        redisService.expire(queueKey, ArgusUtils.ALARM_QUEUE_EXPIRE_SECONDS);

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

    private void shouldAddOrUpdateIntoAlarmQueueName(String alarmQueueName) {
        Long theLastSetTimeStamp = alarmQueueList.get(alarmQueueName);
        if (theLastSetTimeStamp == null || expire(theLastSetTimeStamp)) {

//            //TODO may have a bug
//            Set<String> allQueueNames = redisService.hkeys(RedisKeyUtils.ALARM_QUEUE_NAME);
//            for (String name : allQueueNames) {
//                if (!alarmQueueList.containsKey(name)) {
//                    alarmQueueList.remove(name);
//                }
//            }

            logger.info("检查前更新报警队列列表，更新队列名称列表:{}", alarmQueueName);
            //AlarmHandler can delete the queue name from redis
            redisService.hset(RedisKeyUtils.ALARM_QUEUE_NAME, alarmQueueName, DateUtil.getDateLongTimePlusStr(new java.util.Date()));
            alarmQueueList.put(alarmQueueName, System.currentTimeMillis());
        }
    }

    private StrategyConfig getConfig(String msg, BusinessNodeStrategyConfig config) {
        String content = config.getMonitorConfigContent();
        //TODO to optimize the pattern ,pre compile the pattern and cache them
        if (config.isAvailable() && config.configMatcher(msg)) {
            return config;
        }
        return null;
    }
}
