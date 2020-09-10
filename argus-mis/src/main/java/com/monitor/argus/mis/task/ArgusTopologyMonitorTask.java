package com.monitor.argus.mis.task;

import com.monitor.argus.bean.MailEntity;
import com.monitor.argus.mis.task.annotations.Job;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.mail.JmsMailSender;
import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.common.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.monitor.argus.common.util.RedisKeyUtils.ARGUS_ALARM_MTHREAD_CHECK_KEY;
import static com.monitor.argus.common.util.RedisKeyUtils.ARGUS_MONITORANAY_MTHREAD_CHECK_KEY;

/**
 * Created by huxiaolei on 2016/9/18.
 */
@Component
public class ArgusTopologyMonitorTask {

    private static Logger logger = LoggerFactory.getLogger(ArgusTopologyMonitorTask.class);
    private long oldTotalValues = 0l;
    private long count = 0l;
    String testFlag = "(测试邮件)";
    String receivers = "";
    String testReceivers = "xiaoleihu2@monitor.cn";
    String stormUrl = "点击查询:http://argus-stormtestmonitor.corp/index.html";

    @Value("${mode}")
    private String mode;

    @Autowired
    RedisService redisService;

    @Autowired
    JmsMailSender mailSender;

    /**
     * 监控storm是否正常
     */
    @Job(name = "storm日志流量监控任务", cron="0 0/10 * * * ?")
    public void taskMonitorCheck(){
        // logger.info("监控argusTopology=====start");
        String systemName = "storm-argusTopology";
        String title = "storm-argusTopology日志获取异常";
        String errorMsg = "storm-argusTopology日志获取异常，流量停止增长";
        String monitorName = "storm-argusTopology日志获取异常";
        boolean isError = false;
        String msg = "";
        try {
            if(!"production".equals(mode)) {
                title += testFlag;
                receivers = testReceivers;
            }
            List<String> statisticsInfos = redisService.hmget(
                    RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_KEY,
                    RedisKeyUtils.MONITOR_STAT_TOTAL_LOG_BYTES,
                    RedisKeyUtils.MONITOR_STAT_TOTAL_LOG_COUNT,
                    RedisKeyUtils.MONITOR_STAT_TOTAL_MONITOR_HAPPENED,
                    RedisKeyUtils.MONITOR_STAT_TOTAL_ALARM_HAPPENED);
            long newTotalValues = NumberUtils.createLong(setDefaultByZero(statisticsInfos.get(0)));
            logger.info("监控argusTopology日志流量=====newTotalValues:" + newTotalValues + ",oldTotalValues:" + oldTotalValues);
            if (count > 0) {
                if ((newTotalValues - oldTotalValues) <= 0) {
                    isError = true;
                }
            }
            oldTotalValues = newTotalValues;
            msg = "oldTotalValues:" + String.valueOf(oldTotalValues) + ",newTotalValues:" + String.valueOf(newTotalValues);
        } catch (Exception e) {
            isError = true;
            msg = e.getMessage();
        }
        count ++;
        if (isError) {
            String content = errorMsg + " | " + msg + " | " + stormUrl;
            sendMail(title, content, monitorName, systemName);
        }
        logger.info("监控argusTopology日志流量=====end;isError=" + isError);
    }

    private String setDefaultByZero(String byteStr) {
        if (StringUtils.isBlank(byteStr)) {
            return "0";
        }
        return byteStr;
    }

    /**
     * 监控alarm是否正常
     */
    @Job(name = "alarm主线程监控任务", cron="0 0/10 * * * ?")
    public void taskAlarmCheck() {
        String systemName = "argus-alarm";
        String title = "argus-alarm报警队列处理线程池更新线程异常";
        String errorMsg = "argus-alarm报警队列处理线程池更新线程未启动或退出，请查看";
        String monitorName = "argus-alarm报警队列处理线程池更新线程异常";
        boolean isError = false;
        String msg = "";
        try {
            if(!"production".equals(mode)) {
                title += testFlag;
                receivers = testReceivers;
            }
            long syscurtimenow = System.currentTimeMillis();
            long syscurtimeold = 0l;
            String syscurtime = redisService.get(ARGUS_ALARM_MTHREAD_CHECK_KEY);
            if (!StringUtil.isEmpty(syscurtime)) {
                syscurtimeold = Long.valueOf(syscurtime);
            }
            if ((syscurtimenow - syscurtimeold) > 120 * 1000) {
                isError = true;
            }
            msg = "syscurtimenow:" + String.valueOf(syscurtimenow) + ",syscurtimeold:" + String.valueOf(syscurtimeold);
            logger.info("监控argus-alarm=====" + msg);
        } catch (Exception e) {
            isError = true;
            msg = e.getMessage();
        }

        if (isError) {
            String content = errorMsg + " | " + msg;
            sendMail(title, content, monitorName, systemName);
        }
        logger.info("监控argus-alarm=====end;isError=" + isError);
    }

    /**
     * 监控storm-节点数据总计汇总统计信息同步线程是否正常
     */
    @Job(name = "storm节点流量监控任务", cron="0 0/10 * * * ?")
    public void taskMonitorAnayCheck() {
        String systemName = "storm-argusTopology";
        String title = "storm-argusTopology节点数据总计汇总统计信息同步线程异常";
        String errorMsg = "storm-argusTopology节点数据总计汇总统计信息同步线程异常，线程退出";
        String monitorName = "storm-argusTopology节点数据总计汇总统计信息同步线程异常";
        boolean isError = false;
        String msg = "";
        try {
            if(!"production".equals(mode)) {
                title += testFlag;
                receivers = testReceivers;
            }
            long syscurtimenow = System.currentTimeMillis();
            long syscurtimeold = 0l;
            String syscurtime = redisService.get(ARGUS_MONITORANAY_MTHREAD_CHECK_KEY);
            if (!StringUtil.isEmpty(syscurtime)) {
                syscurtimeold = Long.valueOf(syscurtime);
            }
            if ((syscurtimenow - syscurtimeold) > 120 * 1000) {
                isError = true;
            }
            msg = "syscurtimenow:" + String.valueOf(syscurtimenow) + ",syscurtimeold:" + String.valueOf(syscurtimeold);
            logger.info("监控argusTopology节点数据=====" + msg);
        } catch (Exception e) {
            isError = true;
            msg = e.getMessage();
        }

        if (isError) {
            String content = errorMsg + " | " + msg + " | " + stormUrl;
            sendMail(title, content, monitorName, systemName);
        }
        logger.info("监控argusTopology节点数据=====end;isError=" + isError);
    }

    private void sendMail(String title, String content, String monitorName, String systemName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MailEntity mailEntity = new MailEntity();
        mailEntity.setAbstractDetail(title);
        mailEntity.setBeginTime(sdf.format(new Date()));
        mailEntity.setEndTime(sdf.format(new Date()));
        mailEntity.setIp("");
        mailEntity.setLogId("");
        mailEntity.setTotalCount(1);
        mailEntity.setMessage(content);
        mailEntity.setMonitorName(monitorName);
        mailEntity.setTitle(title);
        mailEntity.setReceivers(receivers);
        mailEntity.setSystemName(systemName);
        mailSender.sendAsyc(mailEntity);
    }

}
