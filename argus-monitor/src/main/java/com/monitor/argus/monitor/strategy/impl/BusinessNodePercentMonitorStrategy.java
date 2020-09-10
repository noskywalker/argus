package com.monitor.argus.monitor.strategy.impl;

import com.monitor.argus.bean.base.EntityBase;
import com.monitor.argus.bean.log.AlarmEntityDTO;
import com.monitor.argus.bean.log.LogEntityDTO;
import com.monitor.argus.monitor.strategy.config.StrategyConfig;
import com.monitor.argus.monitor.strategy.config.enums.CompareMethod;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import com.monitor.argus.monitor.strategy.config.strategy.BusinessNodePercentStrategyConfig;
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

import java.text.DecimalFormat;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by huxiaolei on 2016/10/11.
 */
@Service("BusinessNodePercentMonitorStrategy")
public class BusinessNodePercentMonitorStrategy  extends BaseMonitorStrategy {

    final  static ReentrantLock lockBnp1 = new ReentrantLock(false);
    final  static ReentrantLock lockBnp2 = new ReentrantLock(false);

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
        try {
            if (config != null && config instanceof BusinessNodePercentStrategyConfig) {
                LogEntityDTO logEntityDTO = (LogEntityDTO) entitybase;
                BusinessNodePercentStrategyConfig businessNodePercentStrategyConfig = (BusinessNodePercentStrategyConfig) config;

                CompareMethod compareMethod = businessNodePercentStrategyConfig.getCompareMethod();
                String queueName = RedisKeyUtils.BusinessNodeKey(businessNodePercentStrategyConfig.getMonitorId());

                if (getConfigFrac(logEntityDTO.getFullMessage(), businessNodePercentStrategyConfig) != null) {
                    String queueNameFrac = ArgusUtils.BUS_NODE_FRAC_PREFIX + queueName;
                    initQueue(queueNameFrac);
                    redisService.lpush(queueNameFrac, System.currentTimeMillis() + "");
                    isAlarmShouldGenareted(queueName, logEntityDTO, businessNodePercentStrategyConfig, compareMethod);
                } else if(getConfigNume(logEntityDTO.getFullMessage(), businessNodePercentStrategyConfig) != null) {
                    String queueNameNume = ArgusUtils.BUS_NODE_NUME_PREFIX + queueName;
                    initQueue(queueNameNume);
                    redisService.lpush(queueNameNume, System.currentTimeMillis() + "");
                    isAlarmShouldGenareted(queueName, logEntityDTO, businessNodePercentStrategyConfig, compareMethod);
                } else {
                    if(compareMethod.equals(CompareMethod.SMALLER) || compareMethod.equals(CompareMethod.EQUAL)) {
                        logDriveStart(queueName, logEntityDTO, businessNodePercentStrategyConfig, compareMethod);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private StrategyConfig getConfigFrac(String msg, BusinessNodePercentStrategyConfig config) {
        //TODO to optimize the pattern ,pre compile the pattern and cache them
        if (config.isAvailable() && config.configMatcherFrac(msg)) {
            return config;
        }
        return null;
    }

    private StrategyConfig getConfigNume(String msg, BusinessNodePercentStrategyConfig config) {
        //TODO to optimize the pattern ,pre compile the pattern and cache them
        if (config.isAvailable() && config.configMatcherNume(msg)) {
            return config;
        }
        return null;
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

    private void logDriveStart(String queueName, LogEntityDTO logEntityDTO,
                               BusinessNodePercentStrategyConfig businessNodePercentStrategyConfig, CompareMethod compareMethod) {
        double randomNum = Math.random();
        if (randomNum < 0.01) {
            // tryLock()，马上返回，拿到lock就返回true，不然返回false
            if (lockBnp2.tryLock()) {
                // lock1time = System.currentTimeMillis();
                try {
                    String queueNameFrac = ArgusUtils.BUS_NODE_FRAC_PREFIX + queueName;
                    String queueNameNume = ArgusUtils.BUS_NODE_NUME_PREFIX + queueName;
                    initQueue(queueNameFrac);
                    initQueue(queueNameNume);
                    isAlarmShouldGenareted(queueName, logEntityDTO, businessNodePercentStrategyConfig, compareMethod);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lockBnp2.unlock();
                }
            }
        }
    }

    private void isAlarmShouldGenareted(String queueName, LogEntityDTO logEntityDTO,
                                        BusinessNodePercentStrategyConfig businessNodePercentStrategyConfig, CompareMethod compareMethod) {

        String queueNameFrac = ArgusUtils.BUS_NODE_FRAC_PREFIX + queueName;
        String queueNameNume = ArgusUtils.BUS_NODE_NUME_PREFIX + queueName;
        // 秒
        double cycle = businessNodePercentStrategyConfig.getMonitorCycle();
        double compareValue = businessNodePercentStrategyConfig.getCompareValue();
        // 取列表最早放入的数据
        String theEarliestTimeString = redisService.getFromlist(queueNameFrac, -1);
        if (StringUtils.isNotEmpty(theEarliestTimeString) && !StringUtils.equals("null", theEarliestTimeString)) {

            long theEarliestTime = Long.parseLong(theEarliestTimeString);

            boolean isTriggered = false;
            double resultdata = 0;
            if (System.currentTimeMillis() - theEarliestTime >= cycle * 1000) {
                String checkFlag = RedisKeyUtils.MONITOR_BNMONITOR_QUEUE_CHECK_PREFIX + queueName;
                boolean setSuccess = redisService.setNX(checkFlag, "1");
                if (setSuccess) {
                    redisService.expire(checkFlag, (int)(cycle/10));
                    long sizeFrac = redisService.size(queueNameFrac) - 1;
                    long sizeNume = redisService.size(queueNameNume) - 1;
                    // double resultdata = 0;
                    // 精度计算
                    if (sizeFrac > 0 && sizeNume > 0) {
                        if (sizeNume > sizeFrac) {
                            resultdata = (double) sizeFrac/(double)sizeNume;
                        } else {
                            resultdata = 1;
                        }
                    }
                    resultdata = resultdata*100;
                    switch (compareMethod) {
                        case EQUAL:
                            if (resultdata == compareValue) {
                                //clear the queue immediately
                                clearAndInitQueue(queueName);
                                isTriggered = true;
                            } else if (resultdata > compareValue) {
                                // 重新计算
                                clearAndInitQueue(queueName);
                            }
                            break;
                        case LARGER:
                            if (resultdata > compareValue) {
                                //clear the queue immediately
                                clearAndInitQueue(queueName);
                                isTriggered = true;
                            }
                            break;
                        case SMALLER:
                            if (resultdata < compareValue) {
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
                addSystemMonitor(businessNodePercentStrategyConfig.getSystemId(), businessNodePercentStrategyConfig.getMonitorId());
                logEntityDTO.setMonitorStrategyId(businessNodePercentStrategyConfig.getMonitorId());
                generateMonitorAlarm(logEntityDTO, businessNodePercentStrategyConfig, resultdata);
            }
        }
    }

    private void clearAndInitQueue(String queueName) {
        try {
            lockBnp1.lock();
            String queueNameFrac = ArgusUtils.BUS_NODE_FRAC_PREFIX + queueName;
            String queueNameNume = ArgusUtils.BUS_NODE_NUME_PREFIX + queueName;
            if(redisService.exists(queueNameFrac)) {
                redisService.delete(queueNameFrac);
                redisService.lpush(queueNameFrac, System.currentTimeMillis() + "");
            }
            if(redisService.exists(queueNameNume)) {
                redisService.delete(queueNameNume);
                redisService.lpush(queueNameNume, System.currentTimeMillis() + "");
            }
        } finally {
            lockBnp1.unlock();
        }
    }

    private void generateMonitorAlarm(LogEntityDTO entity, StrategyConfig strategyConfig, double resultdata) {
        AlarmEntityDTO alarmDto = AlarmEntityDTO.generateByLogEntity(entity);

        alarmDto.setSystemName(strategyConfig.getSystemName());
        alarmDto.setMonitorStrategyName(strategyConfig.getStrategyName());
        alarmDto.setLevel(AlarmLevel.getAlarmLevel(strategyConfig.getLevel()));
        alarmDto.setAlarmId(strategyConfig.getAlarmId());
        // 替换为自定义内容
        if (strategyConfig.getIsSendContent() == 1) {
            DecimalFormat decimalFormat = new DecimalFormat("#");
            String sendMst = strategyConfig.getSendContent() + ";实际:" + decimalFormat.format(resultdata) + "%";
            alarmDto.setMessage(sendMst);
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
