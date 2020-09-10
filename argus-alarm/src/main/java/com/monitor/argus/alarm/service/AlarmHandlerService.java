package com.monitor.argus.alarm.service;

import com.monitor.argus.alarm.cache.AlarmCache;
import com.monitor.argus.alarm.handler.AlarmHandler;
import com.monitor.argus.alarm.handler.persist.AlarmEntityPersistService;
import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.dao.alarm.IAlarmDao;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.mail.JmsMailSender;
import com.monitor.argus.service.weixin.WeixinSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static com.monitor.argus.common.util.RedisKeyUtils.ARGUS_ALARM_MTHREAD_CHECK_KEY;


/**
 *
 */
@Service
@DependsOn("alarmConfig")
public class AlarmHandlerService {


    private static final int MAX_THREAD_TIMES = 20;
    Logger logger = LoggerFactory.getLogger(getClass());

    public static volatile Set<String> cachedQueueNameList = Collections.synchronizedSet(new TreeSet<String>());

    @Autowired
    AlarmCache alarmCache;

    @Autowired
    RedisService redisService;

    ExecutorService threadPool;

    @Autowired
    IAlarmDao alarmDao;

    @Autowired
    JmsMailSender mailSender;

    @Autowired
    WeixinSender weixinSender;

    @Autowired
    AlarmEntityPersistService alarmEntityPersistService;

    @PostConstruct
    public void alarm() throws InterruptedException {
        logger.info("注意！alarm()要开始工作啦！");
        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("注意！run()要开始工作啦！");
                /**ensure the alarm cache has finished loading before the thread pool run*/
                while (!AlarmCache.IS_LOADED) {
                    logger.info("注意！AlarmCache.IS_LOADED要开始工作啦！");
                    try {
                        logger.info("报警配置缓存未加载完毕,100ms后重新检查!");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                logger.info("注意！while退出啦！");

                Long queueItemCount = redisService.hlength(RedisKeyUtils.ALARM_QUEUE_NAME);
                if (queueItemCount == null || queueItemCount.intValue() == 0) {
                    queueItemCount = 10L;
                }
                logger.info("注意！要从线程池获取数据啦！");

                threadPool = Executors.newFixedThreadPool(queueItemCount.intValue() * MAX_THREAD_TIMES);

                logger.info("报警配置缓存加载完成,报警处理线程池启动");
                Set<String> allQueueName = redisService.hkeys(RedisKeyUtils.ALARM_QUEUE_NAME);
                while (allQueueName == null || allQueueName.isEmpty()) {
                    logger.info("报警平台的报警消息队列名称为空，等待1s后重试");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    allQueueName = redisService.hkeys(RedisKeyUtils.ALARM_QUEUE_NAME);
                }
                for (String queueName : allQueueName) {
                    cachedQueueNameList.add(queueName);
                    AlarmHandler handler = new AlarmHandler(queueName, redisService, alarmDao, mailSender, alarmCache, alarmEntityPersistService, weixinSender);
                    logger.info("报警守护线程创建队列{}的处理线程！", allQueueName);
                    threadPool.submit(handler);
                }
                logger.info("报警守护线程创建的队列处理线程完毕，队列长度为{}", queueItemCount);
                logger.info("报警队列处理线程池更新线程启动!");
                checkAndAddNewHandler();
            }
        }).start();
//        Thread thread = new Thread(new AlarmHandler(queueName,redisService,alarmDao,mailSender,alarmCache));
//        thread.start();
    }

    public void checkAndAddNewHandler() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long syscurtime = System.currentTimeMillis();
                    // alarm主线程监控key
                    redisService.set(ARGUS_ALARM_MTHREAD_CHECK_KEY, syscurtime + "");
                    Set<String> queueListFromRedis = redisService.hkeys(RedisKeyUtils.ALARM_QUEUE_NAME);
                    for (String name : queueListFromRedis) {
                        if (!cachedQueueNameList.contains(name)) {
                            cachedQueueNameList.add(name);
                            logger.info("【报警守护线程】队列处理更新线程发现一个新加入的报警队列:{}，启动处理线程!");
                            AlarmHandler handler = new AlarmHandler(name, redisService, alarmDao, mailSender, alarmCache, alarmEntityPersistService, weixinSender);
                            threadPool.submit(handler);
                        }
                    }
                    int activeThreadCount = ((ThreadPoolExecutor) threadPool).getActiveCount();
                    if (activeThreadCount <= 0) {
                        logger.info("报警守护线程【当前线程池活动线程为0，清除本地报警队列标识，等待新的报警队列加入】", activeThreadCount);
                        cachedQueueNameList.clear();
                    }
                    logger.info("报警守护线程【当前线程池活动的线程数量为{}】，系统时间:{}", activeThreadCount, syscurtime);
/**
 * sleep five seconds
 */
                    try {
                        logger.info("队列处理更新【报警守护线程】经过一轮检查，休眠5s继续检查!");
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
