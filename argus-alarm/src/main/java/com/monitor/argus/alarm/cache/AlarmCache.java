package com.monitor.argus.alarm.cache;

import com.monitor.argus.bean.alarm.AlarmStrategyEntity;
import com.monitor.argus.common.enums.AlarmType;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.dao.alarm.IAlarmDao;
import com.monitor.argus.dao.user.IUserDao;
import com.monitor.argus.redis.RedisService;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/15.
 */
@Service
public class AlarmCache {

    Logger logger = LoggerFactory.getLogger(getClass());

    static ConcurrentHashMap<String, AlarmMethod> alarmMethods = new ConcurrentHashMap<>();

    @Autowired
    IAlarmDao alarmDao;

    @Autowired
    IUserDao userDao;

    @Autowired
    RedisService redisService;

    public static volatile boolean IS_LOADED = false;

    @PostConstruct
    public void init() {

        Runnable loadThread = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    loadConfig();
                    try {
                        Thread.sleep(ArgusUtils.INTERVAL_FOR_ALARM_CONFIG_RELOAD);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(loadThread);
        thread.start();
    }

    //报警ID对应不同的接收方式号码
    private void loadConfig() {
        logger.info("清除原有报警信息并重新加载报警配置..");
        alarmMethods.clear();
        List<AlarmStrategyEntity> alarmStrategy = alarmDao.getAlarmStrategy();
        for (AlarmStrategyEntity entity : alarmStrategy) {
            AlarmMethod method = new AlarmMethod();
            //获取报警策略里面的报警方式
            method.setAlarmType(AlarmType.getAlarmType(Integer.parseInt(entity.getAlarmType())));
            String alarmId = entity.getId();
            //根据报警策略找到接收人的联系方式
            List<Map<String, String>> alarmMethod = getGroupAlarmMethodByAlarmId(entity.getId());
            for (Map<String, String> userMethod : alarmMethod) {
                String email = userMethod.get("email");
                String phone = userMethod.get("cell_phone");
                String weixin = userMethod.get("open_id");
                method.addEmail(email);
                method.addPhones(phone);
                if (!StringUtil.isEmpty(weixin)) {
                    if(!isFilterWeixin(weixin, alarmId)) {
                        method.addWeixin(weixin);
                    }
                }
            }
            alarmMethods.put(entity.getId(), method);

        }
        logger.info("加载报警配置信息加载完成，加载数量:{}", alarmMethods.size());
        IS_LOADED = true;
    }

    public List<Map<String, String>> getGroupAlarmMethodByAlarmId(String alarmId) {
        List<Map<String, String>> alarmMethod = alarmDao.getAlarmMethodByAlarmId(alarmId);
        return alarmMethod;
    }

    public AlarmMethod getMethodObjectByAlarmId(String alarmId) {
        return alarmMethods.get(alarmId);
    }

    /**
     * 排除暂停订阅的微信
     * @param weixin
     * @param alarmId
     */
    public boolean isFilterWeixin(String weixin, String alarmId) {
        boolean isFilter = false;
        try {
            String result = redisService.get(alarmId + ":" + weixin);
            if (!StringUtil.isEmpty(result)) {
                isFilter = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isFilter;
    }
}
