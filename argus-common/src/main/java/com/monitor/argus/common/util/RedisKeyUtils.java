package com.monitor.argus.common.util;

import static com.monitor.argus.common.util.ArgusUtils.ARGUS_RUNNTIME_ACTIVE_THREAD;

/**
 * Created by Administrator on 2016/7/13.
 */
public class RedisKeyUtils {

    public static String monitorKey(String ip,String monitorStrategyId){
        return ArgusUtils.MONITOR_PREFIX+":"+ip+":"+monitorStrategyId;
    }
    public static String MONITOR_NODEUV_SET_PREFIX = "MONITOR_NODEUV_SET_PREFIX:";
    public static String MONITOR_NODEINTER_LIST_PREFIX = "MONITOR_NODEINTER_LIST_PREFIX:";
    public static String MONITOR_BNMONITOR_QUEUE_CHECK_PREFIX = "ARGUS:BNMONITOR:QUEUE:CHECK:";
    public static String MONITOR_BNMONITOR_INITQUEUE_CHECK_PREFIX = "ARGUS:BNMONITOR:INITQUEUE:CHECK:";
    public static String ARGUS_RUNNTIME_STATISTICS_KEY=ArgusUtils.ARGUS_RUNNTIME_STATISTICS;
    public static String MONITOR_STAT_TOTAL_LOG_COUNT="TOTAL_LOG_COUNTS";

    public static String MONITOR_STAT_TOTAL_LOG_BYTES="TOTAL_LOG_BYTES";
    /**所有发生的报警条数*/
    public static String MONITOR_STAT_TOTAL_ALARM_HAPPENED="TOTAL_ALARM_HAPPENED";

    /**所有发生的监控条数，未合并*/
    public static String MONITOR_STAT_TOTAL_MONITOR_HAPPENED="TOTAL_MONITOR_HAPPENED";

    // 临时报警队列Key
    public static String ARGUS_QUEUE_LAST_ALARM_KEY = "ARGUS_QUEUE_LAST_ALARM_KEY";
    // 临时报警队列锁
    public static String ARGUS_QUEUE_LAST_ALARM_LOCK = "ARGUS:QUEUE:LAST:ALARM:LOCK:";

    public static String ARGUS_ALARM_MTHREAD_CHECK_KEY = "ARGUS_ALARM_MTHREAD_CHECK_KEY";
    public static String ARGUS_MONITORANAY_MTHREAD_CHECK_KEY = "ARGUS_MONITORANAY_MTHREAD_CHECK_KEY";

    public static String ALARM_QUEUE_NAME=ArgusUtils.ALARM_QUEUE_NAME;

    public static String ARGUS_ANALYSIS_STATISTICS_HZ_KEY = ArgusUtils.ARGUS_ANALYSIS_STATISTICS_HZ;
    public static String ARGUS_ANALYSIS_STATISTICS_HOUR_KEY = ArgusUtils.ARGUS_ANALYSIS_STATISTICS_HOUR;
    public static String ANALYSIS_STAT_LOG_COUNT="PV_COUNTS:";

    public static String ARGUS_RUNNTIME_STATISTICS_IP_KEY = ArgusUtils.ARGUS_RUNNTIME_STATISTICS_IP;
    public static String MONITOR_MERGE_QUEUE_CHECK_PREFIX="ARGUS:MERGE:QUEUE:CHECK:";

    public static String ARGUS_RUNNTIME_STATISTICS_SYSMONI_KEY = ArgusUtils.ARGUS_RUNNTIME_STATISTICS_SYSMONI;
    public static String ARGUS_RUNNTIME_STATISTICS_IPADDRESS_KEY = ArgusUtils.ARGUS_RUNNTIME_STATISTICS_IPADDRESS;
    public static String ARGUS_RUNNTIME_STATISTICS_SYSALAR_KEY = ArgusUtils.ARGUS_RUNNTIME_STATISTICS_SYSALAR;

    public static String ARGUS_RUNNTIME_STATISTICS_SYSMXMONITOR_KEY = ArgusUtils.ARGUS_RUNNTIME_STATISTICS_SYSMXMONITOR;
    public static String ARGUS_RUNNTIME_STATISTICS_SYSMXALARM_KEY = ArgusUtils.ARGUS_RUNNTIME_STATISTICS_SYSMXALARM;

    public static String getArgusActiveThreadInfo(String ip,String pid,String tid){
        return ARGUS_RUNNTIME_ACTIVE_THREAD+"ACTIVE_THREAD:"+ip+":jvm_"+pid+":tid_"+tid;
    }


    public static String happenedKey(String ip,String monitorStrategyId){
        return ArgusUtils.HAPPEN_PREFIX+":"+ip+":"+monitorStrategyId;
    }

    public static String monitorMergeBuffer(String ip,String monitorStrategyId){
        return ArgusUtils.MONITOR_MERGE_BUFFER_PREFIX+":"+ip+":"+monitorStrategyId;
    }

    public static String hwlKey(String ip,String monitorStrategyId){
        return ArgusUtils.HIGH_WATER_LINE_PREFIX+":"+ip+":"+monitorStrategyId;
    }
    public static String shouldMerge(String ip,String monitorStrategyId){
        return ArgusUtils.SHOULD_MERGE_PREFIX+":"+ip+":"+monitorStrategyId;
    }
    public static String tempDenyKey(String ip,String monitorStrategyId){
        return ArgusUtils.HIGH_WATER_LINE_PREFIX+":"+ip+":"+monitorStrategyId;
    }

    public static String asyncCheckList(){
        return ArgusUtils.ASYNC_CHECK_PREFIX;
    }

    public static String monitorPausedPrefix(String ip,String strategyId){
        return ArgusUtils.TEMP_PAUSE_PREFIX+":"+ip+":"+strategyId;
    }

    public static String alarmQueueKey(String ip,String strategyId){
        return ArgusUtils.ALARM_QUEUE_PREFIX+":"+ip+":"+strategyId;
    }

    public static String BusinessNodeKey(String monitorStrategyId){
        return ArgusUtils.BUS_NODE_PREFIX+":"+monitorStrategyId;
    }

}
