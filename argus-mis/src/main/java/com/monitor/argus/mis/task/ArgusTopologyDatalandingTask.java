package com.monitor.argus.mis.task;

import com.monitor.argus.bean.dataland.ArgusTopologyDatalandEntity;
import com.monitor.argus.dao.dataland.IArgusTopologyDatalandDao;
import com.monitor.argus.mis.task.annotations.Job;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.dataland.IArgusTopologyDatalandService;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.common.util.UuidUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by huxiaolei on 2016/9/20.
 * 日志数据落地
 */
@Component
public class ArgusTopologyDatalandingTask {

    private static Logger logger = LoggerFactory.getLogger(ArgusTopologyDatalandingTask.class);
    private static String lockKey = "ArgusTopologyDatalandingTaskLock";
    private static String lockValue = "ArgusTopologyDatalandingTaskValue";

    private long oldTotalLogBytes = 0l;
    private long oldTotalLogCount = 0l;
    private long oldTotalMonitors = 0l;
    private long oldTotalAlarms = 0l;
    private long count = 0l;

    @Autowired
    RedisService redisService;

    @Autowired
    IArgusTopologyDatalandService iArgusTopologyDatalandService;

    @Autowired
    IArgusTopologyDatalandDao iArgusTopologyDatalandDao;

    @Job(name = "日志总流量按小时汇总任务", cron="59 59 * * * ?")
    public void taskCycle() {
        String diffLogBytes = "0";
        String diffLogCount = "0";
        String diffMonitors = "0";
        String diffAlarms = "0";
        Date newDate = new Date();
        try {
            if (redisService.setNX(lockKey, lockValue)) {
                List<String> statisticsInfos = redisService.hmget(
                        RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_KEY,
                        RedisKeyUtils.MONITOR_STAT_TOTAL_LOG_BYTES,
                        RedisKeyUtils.MONITOR_STAT_TOTAL_LOG_COUNT,
                        RedisKeyUtils.MONITOR_STAT_TOTAL_MONITOR_HAPPENED,
                        RedisKeyUtils.MONITOR_STAT_TOTAL_ALARM_HAPPENED);
                String totalLogBytes = statisticsInfos.get(0);
                String totalLogCount = statisticsInfos.get(1);
                String totalMonitors = statisticsInfos.get(2);
                String totalAlarms = statisticsInfos.get(3);

                long totalLogBytesL = NumberUtils.createLong(setDefaultByZero(totalLogBytes));
                long totalLogCountL = NumberUtils.createLong(setDefaultByZero(totalLogCount));
                long totalMonitorsL = NumberUtils.createLong(setDefaultByZero(totalMonitors));
                long totalAlarmsL = NumberUtils.createLong(setDefaultByZero(totalAlarms));

                if (count > 0) {
                    diffLogBytes = String.valueOf(totalLogBytesL - oldTotalLogBytes);
                    diffLogCount = String.valueOf(totalLogCountL - oldTotalLogCount);
                    diffMonitors = String.valueOf(totalMonitorsL - oldTotalMonitors);
                    diffAlarms = String.valueOf(totalAlarmsL - oldTotalAlarms);
                } else {
                    // 取旧数据
                    Date endDate = new Date();
                    Date beginDate = DateUtils.addHours(endDate, -12);
                    String beginStr = DateUtil.getDateLongTimePlusNoMinuteStr(beginDate);
                    String endStr = DateUtil.getDateLongTimePlusNoMinuteStr(endDate);
                    List<ArgusTopologyDatalandEntity> list1 = iArgusTopologyDatalandDao.getDataByDate(beginStr, endStr);
                    if (!CollectionUtils.isEmpty(list1)) {
                        ArgusTopologyDatalandEntity lastArgusTopologyDatalandEntity = list1.get(list1.size() - 1);
                        if (lastArgusTopologyDatalandEntity != null) {
                            oldTotalLogBytes = NumberUtils.createLong(setDefaultByZero(lastArgusTopologyDatalandEntity.getTotalLogBytes()));
                            oldTotalLogCount = NumberUtils.createLong(setDefaultByZero(lastArgusTopologyDatalandEntity.getTotalLogCount()));
                            oldTotalMonitors = NumberUtils.createLong(setDefaultByZero(lastArgusTopologyDatalandEntity.getTotalMonitors()));
                            oldTotalAlarms = NumberUtils.createLong(setDefaultByZero(lastArgusTopologyDatalandEntity.getTotalAlarms()));
                            diffLogBytes = String.valueOf(totalLogBytesL - oldTotalLogBytes);
                            diffLogCount = String.valueOf(totalLogCountL - oldTotalLogCount);
                            diffMonitors = String.valueOf(totalMonitorsL - oldTotalMonitors);
                            diffAlarms = String.valueOf(totalAlarmsL - oldTotalAlarms);
                        }

                    }
                }

                oldTotalLogBytes = totalLogBytesL;
                oldTotalLogCount = totalLogCountL;
                oldTotalMonitors = totalMonitorsL;
                oldTotalAlarms = totalAlarmsL;

                ArgusTopologyDatalandEntity argusTopologyDatalandEntity = new ArgusTopologyDatalandEntity();
                // 当前合计
                argusTopologyDatalandEntity.setTotalLogBytes(setDefaultByZero(totalLogBytes));
                argusTopologyDatalandEntity.setTotalLogCount(setDefaultByZero(totalLogCount));
                argusTopologyDatalandEntity.setTotalMonitors(setDefaultByZero(totalMonitors));
                argusTopologyDatalandEntity.setTotalAlarms(setDefaultByZero(totalAlarms));
                // 差值
                argusTopologyDatalandEntity.setDiffLogBytes(setDefaultByZero(diffLogBytes));
                argusTopologyDatalandEntity.setDiffLogCount(setDefaultByZero(diffLogCount));
                argusTopologyDatalandEntity.setDiffMonitors(setDefaultByZero(diffMonitors));
                argusTopologyDatalandEntity.setDiffAlarms(setDefaultByZero(diffAlarms));
                argusTopologyDatalandEntity.setId(UuidUtil.getUUID());
                argusTopologyDatalandEntity.setCreateDate(newDate);
                iArgusTopologyDatalandService.addArgusTopologyDataland(argusTopologyDatalandEntity);

                // 只保留近一个月的数据
                Date nowDateM = new Date();
                Date nowDateMBefor = DateUtils.addMonths(nowDateM, -1);
                String nowDateMBeforSdf = DateUtil.getSimpleDateTimeStr(nowDateMBefor);
                if (!StringUtil.isEmpty(nowDateMBeforSdf)) {
                    iArgusTopologyDatalandService.delArgusTopologyDataByDate(nowDateMBeforSdf);
                }

                logger.info("totalLogBytes={},totalLogCount={},totalMonitors={},totalAlarms={}," +
                                "diffLogBytes={},diffLogCount={},diffMonitors={},diffAlarms={}",
                        totalLogBytes, totalLogCount, totalMonitors, totalAlarms, diffLogBytes, diffLogCount, diffMonitors, diffAlarms);
                count ++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisService.expire(lockKey, 300);
        }
    }

    private String setDefaultByZero(String byteStr) {
        if (StringUtils.isBlank(byteStr)) {
            return "0";
        }
        return byteStr;
    }

}
