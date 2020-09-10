package com.monitor.argus.monitor.strategy.impl.checker;

import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by usr on 2016/10/17.
 */
public class BaseStrategyQueueChecker implements Runnable {

    Logger logger = LoggerFactory.getLogger(getClass());
    RedisService redisService;

    private String queueName;
    private String systemId;
    private String monitorStrategyId;
    private int count;
    private int cycle;
    private int waitSeconds;
    private long lastSetTimeStamp = 0;

    public BaseStrategyQueueChecker(RedisService redisService, String queueName, String systemId, String monitorStrategyId,
                                    int count, int cycle, int waitSeconds) {
        this.redisService = redisService;
        this.queueName = queueName;
        this.systemId = systemId;
        this.monitorStrategyId = monitorStrategyId;
        this.count = count;
        this.cycle = cycle;
        this.waitSeconds = waitSeconds;
    }

    @Override
    public void run() {
        String checkFlag = RedisKeyUtils.MONITOR_MERGE_QUEUE_CHECK_PREFIX + queueName;
        lastSetTimeStamp=System.currentTimeMillis();
        boolean setSuccess = redisService.setNX(checkFlag, "1");
        redisService.expire(checkFlag, ArgusUtils.INTERVAL_FOR_MONITOR_MERGE_QUEUE);
        if (setSuccess) {
            logger.info("线程:{}已经获取检查是否应该合并队列的权限",Thread.currentThread().getId());
            doCheck(checkFlag);
        } else {
            waitMomentAndContinueToCheck(checkFlag);
        }
    }

    public void waitMomentAndContinueToCheck(String checkFlag) {
        logger.info("线程:{}等待接替检查其他线程合并队列权限",Thread.currentThread().getId());
        while(true) {
            boolean setSuccess = redisService.setNX(checkFlag, "1");
            if (setSuccess) {
                logger.info("线程{}已经从其他线程接替检查是否应该合并队列的权限", Thread.currentThread().getId());
                doCheck(checkFlag);
            } else {
                try {
                    Thread.sleep(ArgusUtils.INTERVAL_FOR_MONITOR_MERGE_QUEUE*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void doCheck(String checkFlag){
        while(true){
            check(checkFlag);
            redisService.expire(checkFlag,ArgusUtils.INTERVAL_FOR_MONITOR_MERGE_QUEUE);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void check(String checkFlag){
        String strategyId = this.monitorStrategyId;
        int count = this.count;
        int cycle = this.cycle;
        String current = System.currentTimeMillis() + "";
        String redisKey = queueName;
        redisService.expire(redisKey,ArgusUtils.HAPPEN_QUEUE_EXPIRE_SECONDS);
        long monitorCount = redisService.size(redisKey);
        boolean shouldMergeAfterCurrent = false;
        while (monitorCount >= count) {
            String earlyPushValue = redisService.rpop(redisKey);
            monitorCount = redisService.size(redisKey);
            //this monitor strategy has exceeded the normal monitor range
            if (monitorCount < count) {
                if (Long.valueOf(current) - Long.valueOf(earlyPushValue) <= cycle * 1000) {
                    //this monitor has triggered more than [count] times,should be merged in future
                    logger.debug("监控超过阈值需要合并,增加合并标识,周期为{},次数为{}", this.cycle, this.count);
                    /* TODO add the queue name into another list,we should to check and clear the queue by async thread in future */
                    // redisService.lpush(RedisKeyUtils.asyncCheckList(),redisKey);
                    monitorShouldMerge(strategyId, this.waitSeconds);
                }
            }
        }
    }

    private void monitorShouldMerge(String strategyId, int waitSeconds) {
        String mergeKey = RedisKeyUtils.shouldMerge(systemId, strategyId);
        redisService.setNX(mergeKey, strategyId + "");
        redisService.expire(mergeKey, waitSeconds);
    }

}
