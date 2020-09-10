package com.monitor.argus.alarm.handler;

import com.monitor.argus.alarm.cache.AlarmCache;
import com.monitor.argus.alarm.cache.AlarmMethod;
import com.monitor.argus.alarm.msg.MessageGenerator;
import com.monitor.argus.alarm.msg.impl.EmailMessageGenerator;
import com.monitor.argus.alarm.msg.impl.SmsMessageGenerator;
import com.monitor.argus.alarm.msg.impl.WeixinMessageGenarator;
import com.monitor.argus.alarm.service.AlarmHandlerService;
import com.monitor.argus.alarm.handler.persist.AlarmEntityPersistService;
import com.monitor.argus.alarm.service.AlarmConfig;
import com.monitor.argus.bean.MailEntity;
import com.monitor.argus.bean.WeixinEntity;
import com.monitor.argus.bean.alarm.AlarmInfoEntity;
import com.monitor.argus.bean.log.AlarmEntityDTO;
import com.monitor.argus.common.enums.AlarmType;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.dao.alarm.IAlarmDao;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.mail.JmsMailSender;
import com.monitor.argus.service.weixin.WeixinSender;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.monitor.argus.common.util.RedisKeyUtils.ARGUS_QUEUE_LAST_ALARM_KEY;

/**
 * Created by Administrator on 2016/7/15.
 */
public class AlarmHandler implements Runnable {


    Logger logger = LoggerFactory.getLogger(getClass());
    JmsMailSender mailSender;
    WeixinSender weixinSender;
    String queue;
    RedisService redisService;
    IAlarmDao alarmDao;
    AlarmCache alarmCache;
    AlarmEntityPersistService alarmEntityPersistService;
    private String wxAlarmUrl;

    public void setQueueName(String queue) {
        this.queue = queue;
    }

    public AlarmHandler(String queue, RedisService redisService, IAlarmDao dao, JmsMailSender mailSender,
                        AlarmCache alarmCache, AlarmEntityPersistService alarmEntityPersistService, WeixinSender weixinSender) {
        this.queue = (queue);
        this.alarmCache = alarmCache;
        this.redisService = redisService;
        this.alarmDao = dao;
        this.mailSender = mailSender;
        this.weixinSender = weixinSender;
        this.alarmEntityPersistService = alarmEntityPersistService;
        wxAlarmUrl = AlarmConfig.WEIXIN_REST_URL;
    }


    @Override
    public void run() {
        String ip = "unknown_host";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        long tid = Thread.currentThread().getId();
        String activeThreadKey = RedisKeyUtils.getArgusActiveThreadInfo(ip, ArgusUtils.JVM_PID, tid + "");
        /**add the active thread prefix into redis,in order to monitor the system thread status*/
        addActiveThreadPrefix(activeThreadKey);

        long reSetActiveThreadInterval = ArgusUtils.MAX_INTERVAL_FOR_RESET_ACTIVE_THREAD_EXPIRE_SECONDS;
        long theLastTimeStampForSetExpireTime = System.currentTimeMillis();
        int continuousIdleCount = 0;
        while (true) {
            logger.warn("线程ID为{}的线程已经启动处理队列{}", Thread.currentThread().getId(), queue);
            String alarm = redisService.rpop(queue);

            /**if the reset time is coming,then reset the expire time,avoid the prefix of the thread in redis is dirty when the thread dies*/
            if (System.currentTimeMillis() - theLastTimeStampForSetExpireTime >= reSetActiveThreadInterval) {
                redisService.expire(activeThreadKey, ArgusUtils.ACTIVE_ALARM_EXPIRE_SECONDS);
            }


            if (StringUtils.isNotBlank(alarm)) {
                AlarmEntityDTO dto = (AlarmEntityDTO) JsonUtil.jsonToBean(alarm, AlarmEntityDTO.class);
                //获取报警人联系方式
                AlarmMethod method = alarmCache.getMethodObjectByAlarmId(dto.getAlarmId());
                if (method == null) {
                    continuousIdleCount++;
                    try {
                        logger.warn("队列{}的报警策略定义没有找到,报警策略id为{}，当前已经重试{}次，最多重试{}次,请检查!",
                                queue, dto.getAlarmId(), continuousIdleCount, ArgusUtils.THREAD_MAX_IDLE_COUNT);
                        Thread.sleep(3 * 1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    continuousIdleCount = 0;
                    //存储报警信息
                    persistAlarm(dto, method);

                    // 暂存报警信息
                    cacheAlarm(alarm);

                    //获取报警类型
                    AlarmType type = method.getAlarmType();
                    switch (type) {
                        case MAIL:
                            sendWeixin(dto, method);
                            sendEmail(dto, method);
                            break;
                        case PHONE:
                            sendSms(dto, method);
                            break;
                        case WEIXIN:
                            sendWeixin(dto, method);
                            sendEmail(dto, method);
                            break;
                    }
                }
            } else {
                continuousIdleCount++;
                try {
                    logger.warn("处理队列【{}】线程发现队列为空，休眠5s重试,当前已经重试{}次，最多重试{}次!", queue, continuousIdleCount, ArgusUtils.THREAD_MAX_IDLE_COUNT);
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            /** check current thead is idle reach the continuousIdleCount,if true then terminated*/
            if (continuousIdleCount >= ArgusUtils.THREAD_MAX_IDLE_COUNT) {
                logger.warn("处理队列【{}】的线程连续空闲【%s】次，清除活动线程标识并终止运行!", queue, ArgusUtils.THREAD_MAX_IDLE_COUNT);

                removeActiveThreadPrefix(activeThreadKey);
                logger.warn("处理队列【{}】的线程连续空闲【%s】次，先从队列{}中移除名称!", queue, ArgusUtils.THREAD_MAX_IDLE_COUNT, ArgusUtils.ALARM_QUEUE_NAME);
                redisService.hdel(ArgusUtils.ALARM_QUEUE_NAME, queue);
                AlarmHandlerService.cachedQueueNameList.remove(queue);
                return;
                //Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 暂存报警信息-触发报警通知
     * @param alarm
     */
    private void cacheAlarm(String alarm) {
        try {
            redisService.lpush(ARGUS_QUEUE_LAST_ALARM_KEY, alarm);
        } catch (Exception e) {
            logger.warn("暂存报警信息失败，详情为{},原因{}", alarm, e.getMessage());
        }
    }

    private void persistAlarm(AlarmEntityDTO dto, AlarmMethod method) {
        AlarmInfoEntity infoEntity = new AlarmInfoEntity();
        try {
            infoEntity.setId(UUID.randomUUID().toString());
            infoEntity.setAlarmDetail(dto.getMessage());
            infoEntity.setAlarmId(dto.getAlarmId());
            infoEntity.setIp(dto.getIp());
            infoEntity.setMonitorId(dto.getMonitorStrategyId());
            infoEntity.setSystemName(dto.getSystemName());
            infoEntity.setAlarmType(method.getAlarmType().getCode());
            infoEntity.setTriggerTime(DateUtil.getSimpleDateTime(dto.getBeginTime()));
            infoEntity.setTriggerCount(dto.getTotalAlarmCount());
            alarmEntityPersistService.insertAsync(infoEntity);
        } catch (Exception e) {
            logger.warn("持久化报警对象失败·，详情为{},原因{}", infoEntity.toString(), e.getMessage());
        }
    }

    private void sendEmail(AlarmEntityDTO dto, AlarmMethod method) {
        MessageGenerator<AlarmEntityDTO> generator = new EmailMessageGenerator();
        MailEntity mailEntity = (MailEntity) generator.generate(dto);
        //设置收件人
        mailEntity.setReceivers(method.getEmailStrs());
        logger.debug("产生邮件报警，接收人{},标题{}", mailEntity.getReceivers(), mailEntity.getTitle());
        if (mailEntity.getReceivers() == null || mailEntity.getReceivers().length() <= 0) {
            return;
        }
        mailSender.sendAsyc(mailEntity);
    }

    private void sendSms(AlarmEntityDTO dto, AlarmMethod method) {
        MessageGenerator<AlarmEntityDTO> generator = new SmsMessageGenerator();
        String smsMessage = (String) generator.generate(dto);
        List phones = method.getPhoneList();
        logger.debug("产生短信报警,{}", smsMessage);
        //TODO support sms msg send
    }

    //从这里开始测试
    public void sendWeixin(AlarmEntityDTO dto, AlarmMethod method) {
        MessageGenerator<AlarmEntityDTO> generator = new WeixinMessageGenarator();
        WeixinEntity weixinEntity = (WeixinEntity) generator.generate(dto);
        weixinEntity.setReceivers(method.getWeixinStrs());
        logger.debug("产生微信报警，接收人{},消息详情:{}", weixinEntity.getReceivers(), weixinEntity.getTitle(),weixinEntity.getMessage());
        if (weixinEntity.getReceivers() == null || weixinEntity.getReceivers().length() <= 0) {
            return;
        }
        //调用微信报警接口
        weixinSender.sendAsyc(weixinEntity, wxAlarmUrl);
    }

    private void addActiveThreadPrefix(String key) {
        Tinfo tinfo = new Tinfo();
        tinfo.setQueue_name(queue);
        tinfo.setBegin(DateUtil.getDateLongTimePlusStr(new Date()));
        //只有不存在的时候才设置
        redisService.setNX(key, tinfo.toString());
    }

    private void removeActiveThreadPrefix(String key) {
        long tid = Thread.currentThread().getId();
        redisService.delete(key);
    }

}
