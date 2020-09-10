package com.monitor.argus.mis.task;

import com.monitor.argus.bean.dataland.AnalyTopologySysDatalandEntity;
import com.monitor.argus.bean.monitor.MonitorHostEntity;
import com.monitor.argus.dao.dataland.IArgusTopologyDatalandDao;
import com.monitor.argus.dao.monitor.IMonitorDao;
import com.monitor.argus.mis.task.annotations.Job;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.DateUtil;
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
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huxiaolei on 2016/10/13.
 * 各日志数据落地
 */

@Component
public class ArgusTopologySysDatalandingTask {

    private static Logger logger = LoggerFactory.getLogger(ArgusTopologySysDatalandingTask.class);

    private static String lockKey = "ArgusTopologySysDatalandingTaskLock";
    private static String lockValue = "ArgusTopologySysDatalandingTaskValue";

    private ConcurrentHashMap<String, Long> oldLogBytesH = new ConcurrentHashMap();
    private ConcurrentHashMap<String, Long> logCounterH = new ConcurrentHashMap();

    @Autowired
    RedisService redisService;

    @Autowired
    IMonitorDao monitorDao;

    @Autowired
    IArgusTopologyDatalandDao iArgusTopologySysDatalandDao;

    @Job(name = "系统IP流量按小时汇总任务", cron="59 59 * * * ?")
    public void taskCycle() {
        Date newDate = new Date();
        try {
            if (redisService.setNX(lockKey, lockValue)) {
                List<MonitorHostEntity> allHosts = monitorDao.getAllHosts();
                if (!CollectionUtils.isEmpty(allHosts)) {
                    for (MonitorHostEntity hostEntity : allHosts) {
                        String systemIp = hostEntity.getIp();
                        List<String> statisticsInfos = redisService.hmget(ArgusUtils.ARGUS_RUNNTIME_STATISTICS_IP, systemIp);
                        if (!CollectionUtils.isEmpty(statisticsInfos)) {
                            String logBytes = statisticsInfos.get(0);
                            String diffLogBytes = "0";
                            long newLogBytes = NumberUtils.createLong(setDefaultByZero(logBytes));
                            long count = logCounterH.get(systemIp) == null ? 0l : logCounterH.get(systemIp);
                            Long oldLogBytes = oldLogBytesH.get(systemIp) == null ? 0l : oldLogBytesH.get(systemIp);
                            if (count > 0) {
                                diffLogBytes = String.valueOf(newLogBytes - oldLogBytes);
                            } else {
                                // 取旧数据
                                Date endDate = new Date();
                                Date beginDate = DateUtils.addHours(endDate, -12);
                                String beginStr = DateUtil.getDateLongTimePlusNoMinuteStr(beginDate);
                                String endStr = DateUtil.getDateLongTimePlusNoMinuteStr(endDate);
                                List<AnalyTopologySysDatalandEntity> list1 = iArgusTopologySysDatalandDao.getSysDataByDateHour(beginStr, endStr, systemIp);
                                if (!CollectionUtils.isEmpty(list1)) {
                                    AnalyTopologySysDatalandEntity lastAnalyTopologySysDatalandEntity = list1.get(list1.size() - 1);
                                    if (lastAnalyTopologySysDatalandEntity != null) {
                                        oldLogBytes = NumberUtils.createLong(setDefaultByZero(lastAnalyTopologySysDatalandEntity.getLogBytes()));
                                        diffLogBytes = String.valueOf(newLogBytes - oldLogBytes);
                                    }
                                }
                            }
                            oldLogBytesH.put(systemIp, newLogBytes);
                            count++;
                            logCounterH.put(systemIp, count);
                            AnalyTopologySysDatalandEntity analyTopologySysDatalandEntity = new AnalyTopologySysDatalandEntity();
                            analyTopologySysDatalandEntity.setId(UuidUtil.getUUID());
                            analyTopologySysDatalandEntity.setCreateDate(newDate);
                            analyTopologySysDatalandEntity.setSystemId(systemIp);
                            // 当前合计
                            analyTopologySysDatalandEntity.setLogBytes(setDefaultByZero(logBytes));
                            // 差值
                            analyTopologySysDatalandEntity.setDiffLogBytes(setDefaultByZero(diffLogBytes));
                            iArgusTopologySysDatalandDao.addSysTopologyDatalandHour(analyTopologySysDatalandEntity);
                        }
                    }
                }
                // 只保留近一个月的数据
                Date nowDateM = new Date();
                Date nowDateMBefor = DateUtils.addMonths(nowDateM, -1);
                String nowDateMBeforSdf = DateUtil.getSimpleDateTimeStr(nowDateMBefor);
                if (!StringUtil.isEmpty(nowDateMBeforSdf)) {
                    iArgusTopologySysDatalandDao.delSysDataTopologyDatalandByM(nowDateMBeforSdf);
                }
            }
            logger.info("systemdata===taskHourCycle===end");
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
