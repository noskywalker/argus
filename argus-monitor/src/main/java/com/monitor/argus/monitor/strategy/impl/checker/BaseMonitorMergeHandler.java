package com.monitor.argus.monitor.strategy.impl.checker;

/**
 * Created by usr on 2016/10/17.
 */

import com.monitor.argus.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by huxiaolei on 2016/10/17.
 */
@Service
public class BaseMonitorMergeHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RedisService redisService;

    static ConcurrentHashMap<String,String> baseTaskList = new ConcurrentHashMap<>();
    ExecutorService threadPool = Executors.newCachedThreadPool();

    public void submitMergeQueueCheckTask(String queueName, String systemId, String monitorStrategyId,
                                          int count, int cycle, int waitSeconds){
        // 进行实例内部线程同步，线程内部还有跨实例的线程同步策略
        Object existValue = baseTaskList.putIfAbsent(queueName, "1");
        if (existValue == null) {
            logger.info("队列监控线程池增加一个活动线程,systemId:{},queue:{}", systemId, queueName);
            threadPool.submit(new BaseStrategyQueueChecker(redisService, queueName, systemId, monitorStrategyId,
                    count, cycle, waitSeconds));
            int activeThreadCount = ((ThreadPoolExecutor) threadPool).getActiveCount();
            logger.info("合并队列监控活动线程数量为{}", activeThreadCount);
        }
    }

}
