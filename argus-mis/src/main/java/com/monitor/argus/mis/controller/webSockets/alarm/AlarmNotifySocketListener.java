package com.monitor.argus.mis.controller.webSockets.alarm;

import com.monitor.argus.bean.log.AlarmEntityDTO;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.concurrent.locks.ReentrantLock;

import static com.monitor.argus.common.util.RedisKeyUtils.ARGUS_QUEUE_LAST_ALARM_KEY;

/**
 * Created by huxiaolei on 2016/10/21.
 */
@Component("alarmNotifySocketListener")
public class AlarmNotifySocketListener {

    private static Logger logger = LoggerFactory.getLogger(AlarmNotifySocketListener.class);
    public static volatile ReentrantLock alarmLock = new ReentrantLock();

    @Autowired
    protected RedisService redisService;

    /**
     * 群发信息
     */
    @PostConstruct
    public void sendAllMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (alarmLock.tryLock()) {
                    try {
                        logger.info("websocket群发报警通知启动====success");
                        while (true) {
                            String alarm = redisService.rpop(ARGUS_QUEUE_LAST_ALARM_KEY);
                            if(!StringUtil.isEmpty(alarm) && !CollectionUtils.isEmpty(AlarmNotifySocket.webSocketSet)) {
                                AlarmEntityDTO dto = (AlarmEntityDTO) JsonUtil.jsonToBean(alarm, AlarmEntityDTO.class);
                                if (dto != null) {
                                    String message = "{\"systemName\": \"" + dto.getSystemName() + "\"," +
                                            "\"monitorName\": \"" + dto.getMonitorStrategyName() + "\"," +
                                            "\"alarmContent\": \"" + dto.getMessage() + "\"}";
                                    logger.info("websocket群发报警通知:" + dto.getSystemName());
                                    for(AlarmNotifySocket item : AlarmNotifySocket.webSocketSet) {
                                        try {
                                            if (item != null) {
                                                item.sendMessage(message);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            continue;
                                        }
                                    }
                                }
                            }
                            Thread.sleep(ArgusUtils.ALARM_SOCKET_INTERVAL_MILLIS);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        logger.info("websocket群发报警通知线程ID为:{}发生异常退出！释放锁", Thread.currentThread().getId());
                        alarmLock.unlock();
                    }
                }
            }
        }).start();
    }

}
