package com.monitor.argus.monitor.strategy.impl.checker;

import com.monitor.argus.monitor.strategy.config.strategy.KeyWordsStrategyConfig;
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
 *
 *Email:alex zhang
 *Creator:usr
 *CreatedDate:八月
 *Version:V1.0.0
 */
@Service
public class MonitorMergeHandler {

    Logger logger
            = LoggerFactory.getLogger(getClass());
    @Autowired
    RedisService redisService;
    static ConcurrentHashMap<String,String> taskList=new ConcurrentHashMap<>();
    ExecutorService threadPool= Executors.newCachedThreadPool();

    public void submitMergeQueueCheckTask(String queueName, KeyWordsStrategyConfig strategyConfig){
        //进行实例内部线程同步，线程内部还有跨实例的线程同步策略
        Object existValue=taskList.putIfAbsent(queueName,"1");
        if(existValue==null){
            logger.info("队列监控线程池增加一个活动线程,systemId:{},queue:{}",strategyConfig.getSystemId(),queueName);
            threadPool.submit(new StrategyQueueChecker(redisService,queueName,strategyConfig));
            int activeThreadCount = ((ThreadPoolExecutor) threadPool).getActiveCount();
            logger.info("合并队列监控活动线程数量为{}",activeThreadCount);
        }
//        {
//            logger.info("队列监控线程池已存在,ip:{},queue:{}",ip,queueName);
//        }
    }
}
