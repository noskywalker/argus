package com.monitor.argus.monitor.statistics;

import com.monitor.argus.bean.ipaddress.IpConfigEntity;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.common.util.security.Md5Util;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import static com.monitor.argus.common.util.RedisKeyUtils.ARGUS_MONITORANAY_MTHREAD_CHECK_KEY;
import static com.monitor.argus.monitor.strategy.config.IpConfigCache.IS_LOADED_IPCONFIG;
import static com.monitor.argus.monitor.strategy.config.IpConfigCache.ipConfigs;

/**
 * Created by Administrator on 2016/7/17.
 */
public class Statistics {

    public static volatile AtomicLong totalLogCounts = new AtomicLong(0L);
    public static volatile AtomicLong totalLogBytes = new AtomicLong(0L);
    public static volatile AtomicLong totalMonitorHappened = new AtomicLong(0L);
    public static volatile AtomicLong totalAlarmHappened = new AtomicLong(0L);
    public static volatile ReentrantLock syncRedisAndDbLock = new ReentrantLock();

    // 计算每个系统的日志流量
    public static volatile ConcurrentHashMap<String, AtomicLong> ipStatisticsData = new  ConcurrentHashMap<String, AtomicLong>();

    // 为合计汇总服务
    public static volatile ReentrantLock syncRedisAndDbLock01 = new ReentrantLock();
    // 数据汇总缓存-为合计汇总服务
    public static volatile ConcurrentHashMap<String, AtomicLong>  statisticsData01 = new  ConcurrentHashMap<String, AtomicLong>();

    // UV计算服务map,key:nodekey,value:用户标识集合
    public static volatile ConcurrentHashMap<String, Set<String>> nodeUVMap = new ConcurrentHashMap<String, Set<String>>();
    // 节点对应接口待计算临时集合
    public static volatile ConcurrentHashMap<String, List<String>> nodeInterCountMap = new ConcurrentHashMap<String, List<String>>();

    // 记录各系统报警、监控次数
    public static volatile ConcurrentHashMap<String, AtomicLong> statisticsSystemMonitor = new  ConcurrentHashMap<String, AtomicLong>();
    public static volatile ConcurrentHashMap<String, AtomicLong> statisticsSystemAlarm = new  ConcurrentHashMap<String, AtomicLong>();
    // 记录各系统明细报警、监控次数
    public static volatile ConcurrentHashMap<String, ConcurrentHashMap<String, AtomicLong>> statisticsSystemMXMonitor = new  ConcurrentHashMap<String, ConcurrentHashMap<String, AtomicLong>>();
    public static volatile ConcurrentHashMap<String, ConcurrentHashMap<String, AtomicLong>> statisticsSystemMXAlarm = new  ConcurrentHashMap<String, ConcurrentHashMap<String, AtomicLong>>();
    public static volatile ReentrantLock syncRedisAndDbLock02 = new ReentrantLock();


    // 记录每个系统各地区访问次数
    public static volatile ConcurrentHashMap<String, ConcurrentHashMap<String, AtomicLong>> statisticsSysIpAddressCount = new  ConcurrentHashMap<String, ConcurrentHashMap<String, AtomicLong>>();
    // public static volatile ConcurrentHashMap<String, AtomicLong> statisticsIpAddressCount = new  ConcurrentHashMap<String, AtomicLong>();
    public static volatile ConcurrentHashMap<String, Set<Long>> sysIpnums = new ConcurrentHashMap<String, Set<Long>>();
    public static volatile ReentrantLock syncRedisAndDbLock03 = new ReentrantLock();

    public static RedisService redis;
    static Logger logger = LoggerFactory.getLogger(Statistics.class);

    public static void setRedisService(RedisService redisService) {
        redis = redisService;
    }

    public static void startSyncStatistics() {

        Runnable syncThread = new Runnable() {
            @Override
            public void run() {
                try {
                    syncRedisAndDbLock.lock();
                    logger.info("统计信息同步线程ID为:{}获得锁,开始统计信息同步", Thread.currentThread().getId());
                    while (true) {
                        redis.hincr(RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_KEY,RedisKeyUtils.MONITOR_STAT_TOTAL_LOG_BYTES, totalLogBytes.longValue());
                        redis.hincr(RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_KEY,RedisKeyUtils.MONITOR_STAT_TOTAL_LOG_COUNT, totalLogCounts.longValue());
                        redis.hincr(RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_KEY,RedisKeyUtils.MONITOR_STAT_TOTAL_MONITOR_HAPPENED, totalMonitorHappened.longValue());
                        redis.hincr(RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_KEY,RedisKeyUtils.MONITOR_STAT_TOTAL_ALARM_HAPPENED, totalAlarmHappened.longValue());
                        logger.info("统计信息同步线程ID为:{}进行一次同步,日志字节数:{},日志条数:{},监控发生数:{},报警发生数:{}", Thread.currentThread().getId(), totalLogBytes.longValue(), totalLogCounts.longValue(), totalMonitorHappened.longValue(),totalAlarmHappened.longValue());
                        totalLogBytes.set(0L);
                        totalLogCounts.set(0L);
                        totalMonitorHappened.set(0L);
                        totalAlarmHappened.set(0L);

                        // 每个系统的日志流量
                        if (!MapUtils.isEmpty(ipStatisticsData)) {
                            Set<String> ipKeys = ipStatisticsData.keySet();
                            if (!CollectionUtils.isEmpty(ipKeys)) {
                                for (String ipKey : ipKeys) {
                                    if (!StringUtil.isEmpty(ipKey)) {
                                        AtomicLong ipValue = ipStatisticsData.get(ipKey);
                                        if (ipValue != null) {
                                            redis.hincr(RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_IP_KEY, ipKey, ipValue.longValue());
                                            ipValue.set(0L);
                                        }
                                    }

                                }
                            }
                        }

                        Thread.sleep(ArgusUtils.SYNC_INTERVAL_MILLIS);
                    }
                } catch (Exception e) {
                    logger.info("统计信息同步线程ID为:{}发生异常退出！原因为{}", Thread.currentThread().getId(),e.getMessage());
                } finally {
                    logger.info("统计信息同步线程ID为:{}发生异常退出！释放锁", Thread.currentThread().getId());
                    syncRedisAndDbLock.unlock();
                }
            }
        };
        new Thread(syncThread).start();
    }

    public static void startSyncStatistics01() {

        Runnable syncThread = new Runnable() {
            @Override
            public void run() {
                try {
                    syncRedisAndDbLock01.lock();
                    logger.info("节点数据总计汇总统计信息同步线程ID为:{}获得锁,开始统计信息同步", Thread.currentThread().getId());
                    while (true) {
                        long syscurtime = System.currentTimeMillis();
                        // 线程监控key
                        redis.set(ARGUS_MONITORANAY_MTHREAD_CHECK_KEY, syscurtime + "");
                        if (!MapUtils.isEmpty(statisticsData01)) {
                            Set<String> nodekeys = statisticsData01.keySet();
                            if (!CollectionUtils.isEmpty(nodekeys)) {
                                for (String nodekey : nodekeys) {
                                    if (!StringUtil.isEmpty(nodekey)) {
                                        AtomicLong nodeValue = statisticsData01.get(nodekey);
                                        if (nodeValue != null) {
                                            redis.hincr(RedisKeyUtils.ARGUS_ANALYSIS_STATISTICS_HZ_KEY, nodekey, nodeValue.longValue());
                                            nodeValue.set(0L);
                                        }
                                    }
                                }
                            }
                        }
                        // 采集uv数据
                        statisticsNodeUVMapRun();
                        // 采集接口信息（调用次数、响应时间）
                        statisticsNodeInterCountMapRun();


                        logger.info("节点数据总计汇总统计信息同步线程ID为:{}进行一次同步,节点数量为:{},uv节点数量为:{},接口数量为:{}",
                                Thread.currentThread().getId(),
                                statisticsData01 != null ? statisticsData01.size() : 0,
                                nodeUVMap != null ? nodeUVMap.size() : 0,
                                nodeInterCountMap != null ? nodeInterCountMap.size() : 0);

                        Thread.sleep(ArgusUtils.SYNC_INTERVAL_MILLIS);
                    }
                } catch (Exception e) {
                    logger.info("节点数据总计汇总统计信息同步线程ID为:{}发生异常退出！原因为{}", Thread.currentThread().getId(),e.getMessage());
                } finally {
                    logger.info("节点数据总计汇总统计信息同步线程ID为:{}发生异常退出！释放锁", Thread.currentThread().getId());
                    syncRedisAndDbLock01.unlock();
                }
            }
        };
        new Thread(syncThread).start();
    }

    private static void statisticsNodeUVMapRun() {
        if (!MapUtils.isEmpty(nodeUVMap)) {
            Set<String> nodeUVMapKeys = nodeUVMap.keySet();
            if (!CollectionUtils.isEmpty(nodeUVMapKeys)) {
                for (String nodeUVMapKey : nodeUVMapKeys) {
                    if (!StringUtil.isEmpty(nodeUVMapKey)) {
                        Set nodeUVMapSet = nodeUVMap.get(nodeUVMapKey);
                        if (!CollectionUtils.isEmpty(nodeUVMapSet)) {
                            String daykey = DateUtil.getDateShortStr(new Date());
                            String[] dataUvs = new String[nodeUVMapSet.size()];
                            String[] toArray = (String[]) nodeUVMapSet.toArray(dataUvs);
                            redis.sadd(RedisKeyUtils.MONITOR_NODEUV_SET_PREFIX + nodeUVMapKey + ":" + daykey, toArray);
                            nodeUVMapSet.clear();
                        }
                    }
                }
            }
        }
    }

    private static void statisticsNodeInterCountMapRun() {
        if (!MapUtils.isEmpty(nodeInterCountMap)) {
            Set<String> interMapKeys = nodeInterCountMap.keySet();
            if (!CollectionUtils.isEmpty(interMapKeys)) {
                for (String interMapKey : interMapKeys) {
                    if (!StringUtil.isEmpty(interMapKey)) {
                        List nodeInterCountList = nodeInterCountMap.get(interMapKey);
                        if (!CollectionUtils.isEmpty(nodeInterCountList)) {
                            String dayHHkey = DateUtil.getDateShorthhStr(new Date());
                            String[] dataUvs = new String[nodeInterCountList.size()];
                            String[] toArray = (String[]) nodeInterCountList.toArray(dataUvs);
                            redis.lpushs(RedisKeyUtils.MONITOR_NODEINTER_LIST_PREFIX + interMapKey + ":" + dayHHkey, toArray);
                            nodeInterCountList.clear();
                        }
                    }
                }
            }
        }
    }

    public static void startSyncStatistics02() {

        Runnable syncThread = new Runnable() {
            @Override
            public void run() {
                try {
                    syncRedisAndDbLock02.lock();
                    logger.info("系统监控&报警次数统计线程ID为:{}获得锁,开始统计信息同步", Thread.currentThread().getId());
                    while (true) {
                        // 每个系统的监控次数
                        if (!MapUtils.isEmpty(statisticsSystemMonitor)) {
                            Set<String> ssmKeys = statisticsSystemMonitor.keySet();
                            if (!CollectionUtils.isEmpty(ssmKeys)) {
                                for (String ssmKey : ssmKeys) {
                                    if (!StringUtil.isEmpty(ssmKey)) {
                                        AtomicLong ssmValue = statisticsSystemMonitor.get(ssmKey);
                                        if (ssmValue != null) {
                                            redis.hincr(RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_SYSMONI_KEY, ssmKey, ssmValue.longValue());
                                            ssmValue.set(0L);
                                        }
                                    }
                                }
                            }
                        }

                        // 每个系统的报警次数
                        if (!MapUtils.isEmpty(statisticsSystemAlarm)) {
                            Set<String> ssaKeys = statisticsSystemAlarm.keySet();
                            if (!CollectionUtils.isEmpty(ssaKeys)) {
                                for (String ssaKey : ssaKeys) {
                                    if (!StringUtil.isEmpty(ssaKey)) {
                                        AtomicLong ssaValue = statisticsSystemAlarm.get(ssaKey);
                                        if (ssaValue != null) {
                                            redis.hincr(RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_SYSALAR_KEY, ssaKey, ssaValue.longValue());
                                            ssaValue.set(0L);
                                        }
                                    }
                                }
                            }
                        }
                        // 每个系统的明细监控次数
                        statisticsSystemMXMonitorRun();
                        // 每个系统的明细报警次数
                        statisticsSystemMXAlarmRun();

                        logger.info("系统监控&报警次数统计线程ID为:{}进行一次同步,系统监控列表为:{},系统报警列表为:{}," +
                                "系统监控明细列表为:{},系统报警明细列表为:{}",
                                Thread.currentThread().getId(),
                                statisticsSystemMonitor != null ? statisticsSystemMonitor.size() : 0,
                                statisticsSystemAlarm != null ? statisticsSystemAlarm.size() : 0,
                                statisticsSystemMXMonitor != null ? statisticsSystemMXMonitor.size() : 0,
                                statisticsSystemMXAlarm != null ? statisticsSystemMXAlarm.size() : 0);
                        Thread.sleep(ArgusUtils.SYNC_SYSMA_INTERVAL_MILLIS);
                    }
                } catch (Exception e) {
                    logger.info("系统监控&报警次数统计线程ID为:{}发生异常退出！原因为{}", Thread.currentThread().getId(),e.getMessage());
                } finally {
                    logger.info("系统监控&报警次数统计线程ID为:{}发生异常退出！释放锁", Thread.currentThread().getId());
                    syncRedisAndDbLock02.unlock();
                }
            }
        };
        new Thread(syncThread).start();
    }

    private static void statisticsSystemMXMonitorRun() {
        Set<String> systemIds = statisticsSystemMXMonitor.keySet();
        if (!CollectionUtils.isEmpty(systemIds)) {
            for (String systemId : systemIds) {
                if (!StringUtil.isEmpty(systemId)) {
                    ConcurrentHashMap<String, AtomicLong> statisticsMonitorCount = statisticsSystemMXMonitor.get(systemId);
                    if (!MapUtils.isEmpty(statisticsMonitorCount)) {
                        Set<String> monitorIds = statisticsMonitorCount.keySet();
                        if (!CollectionUtils.isEmpty(monitorIds)) {
                            for (String monitorId : monitorIds) {
                                if (!StringUtil.isEmpty(monitorId)) {
                                    AtomicLong mcount = statisticsMonitorCount.get(monitorId);
                                    if (mcount != null && mcount.longValue() > 0) {
                                        redis.hincr(RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_SYSMXMONITOR_KEY + systemId, monitorId, mcount.longValue());
                                        mcount.set(0L);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void statisticsSystemMXAlarmRun() {
        Set<String> systemIds = statisticsSystemMXAlarm.keySet();
        if (!CollectionUtils.isEmpty(systemIds)) {
            for (String systemId : systemIds) {
                if (!StringUtil.isEmpty(systemId)) {
                    ConcurrentHashMap<String, AtomicLong> statisticsAlarmCount = statisticsSystemMXAlarm.get(systemId);
                    if (!MapUtils.isEmpty(statisticsAlarmCount)) {
                        Set<String> alarmIds = statisticsAlarmCount.keySet();
                        if (!CollectionUtils.isEmpty(alarmIds)) {
                            for (String alarmId : alarmIds) {
                                if (!StringUtil.isEmpty(alarmId)) {
                                    AtomicLong acount = statisticsAlarmCount.get(alarmId);
                                    if (acount != null && acount.longValue() > 0) {
                                        redis.hincr(RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_SYSMXALARM_KEY + systemId, alarmId, acount.longValue());
                                        acount.set(0L);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void startSyncStatistics03() {

        Runnable syncThread = new Runnable() {
            @Override
            public void run() {
                try {
                    syncRedisAndDbLock03.lock();
                    logger.info("IP地址分布情况记录线程ID为:{}获得锁,开始统计信息同步", Thread.currentThread().getId());
                    while (true) {
                        if (!IS_LOADED_IPCONFIG) {
                            Thread.sleep(ArgusUtils.SYNC_IPADD_INTERVAL_MILLIS);
                            continue;
                        }
                        IS_LOADED_IPCONFIG = false;
                        Thread.sleep(ArgusUtils.SYNC_IPADD_ZT_INTERVAL_MILLIS);
                        long times = System.currentTimeMillis();
                        int ipConfigsLength = ipConfigs.size();

                        // 计算每个系统各地址次数
                        if (!MapUtils.isEmpty(sysIpnums)) {
                            Set<String> systemIds = sysIpnums.keySet();
                            if (!CollectionUtils.isEmpty(systemIds)) {
                                for (String systemId : systemIds) {
                                    if (!StringUtil.isEmpty(systemId)) {
                                        Set<Long> ipnums = sysIpnums.get(systemId);
                                        if (!CollectionUtils.isEmpty(ipnums)) {
                                            int ipnumsCount = 0;
                                            for (Long ipnum : ipnums) {
                                                if (ipnum == null) {
                                                    continue;
                                                }
                                                if (ipnumsCount > 5000) {
                                                    break;
                                                }
                                                String ipnumAddress = "";
                                                for (int i = 0; i < ipConfigsLength; i++) {
                                                    IpConfigEntity ipConfigEntity = ipConfigs.get(i);
                                                    if (ipConfigEntity == null) {
                                                        continue;
                                                    }
                                                    String addressShort = ipConfigEntity.getAddressShort();
                                                    if (StringUtil.isEmpty(addressShort)) {
                                                        continue;
                                                    }
                                                    long ipstartNum = ipConfigEntity.getIpStartNum();
                                                    long ipendNum = ipConfigEntity.getIpEndNum();
                                                    if (ipnum >= ipstartNum && ipnum <= ipendNum) {
                                                        ipnumAddress = addressShort;
                                                        break;
                                                    }
                                                }
                                                if (!StringUtil.isEmpty(ipnumAddress)) {
                                                    ConcurrentHashMap<String, AtomicLong> statisticsIpAddressCount = statisticsSysIpAddressCount.get(systemId);
                                                    if (!MapUtils.isEmpty(statisticsIpAddressCount)) {
                                                        AtomicLong totalCounts = statisticsIpAddressCount.get(ipnumAddress);
                                                        if (totalCounts != null) {
                                                            totalCounts.incrementAndGet();
                                                        }
                                                    }
                                                }
                                                ipnumsCount ++;
                                            }
                                            ipnums.clear();
                                        }
                                    }
                                }
                            }
                        }

                        // 数据统计结果录入redis
                        Set<String> systemIds = statisticsSysIpAddressCount.keySet();
                        if (!CollectionUtils.isEmpty(systemIds)) {
                            for (String systemId : systemIds) {
                                if (!StringUtil.isEmpty(systemId)) {
                                    ConcurrentHashMap<String, AtomicLong> statisticsIpAddressCount = statisticsSysIpAddressCount.get(systemId);
                                    if (!MapUtils.isEmpty(statisticsIpAddressCount)) {
                                        Set<String> addresses = statisticsIpAddressCount.keySet();
                                        if (!CollectionUtils.isEmpty(addresses)) {
                                            for (String address : addresses) {
                                                if (!StringUtil.isEmpty(address)) {
                                                    AtomicLong addressCount = statisticsIpAddressCount.get(address);
                                                    if (addressCount != null && addressCount.longValue() > 0) {
                                                        String addressKey = Md5Util.digist(address);
                                                        redis.hincr(RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_IPADDRESS_KEY + systemId, addressKey, addressCount.longValue());
                                                        addressCount.set(0L);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        logger.info("IP地址分布情况记录线程ID为:{}进行一次同步xxxx,IP地址分布情况列表为:{},耗时:{}ms",
                                Thread.currentThread().getId(),
                                statisticsSysIpAddressCount != null ? statisticsSysIpAddressCount.size() : 0, System.currentTimeMillis() - times);
                        IS_LOADED_IPCONFIG = true;
                        Thread.sleep(ArgusUtils.SYNC_IPADD_INTERVAL_MILLIS);
                    }
                } catch (Exception e) {
                    logger.info("IP地址分布情况记录线程ID为:{}发生异常退出！原因为{}", Thread.currentThread().getId(),e.getMessage());
                    e.printStackTrace();
                } finally {
                    logger.info("IP地址分布情况记录线程ID为:{}发生异常退出！释放锁", Thread.currentThread().getId());
                    syncRedisAndDbLock03.unlock();
                }
            }
        };
        new Thread(syncThread).start();
    }

}
