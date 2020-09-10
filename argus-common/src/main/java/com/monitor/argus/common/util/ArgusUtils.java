/**
 *
 */
package com.monitor.argus.common.util;

import java.lang.management.ManagementFactory;

/**
 * All rights Reserved, Designed By alex zhang
 *
 * @version V1.0
 * @Title: ArgusUtils.java
 * @Package com.monitor.argus.common.util
 * @Description: TODO
 * @author: alex zhang
 * @date: 2016年6月14日 下午3:42:23
 */
public class ArgusUtils {


    public static final String JVM_PID = ManagementFactory.getRuntimeMXBean().getName();
    public static final String ENVIRONMENT_MODE = "development";


    public static boolean IS_DEBUG_MODE = false;
    public static final String ARGUS_RUNNTIME_ACTIVE_THREAD = "ARGUS:RUNNTIME:";
    public static final String ARGUS_RUNNTIME_STATISTICS = "ARGUS:RUNNTIME:STATISTICS:";
    public static final String ARGUS_RUNNTIME_STATISTICS_SYSTEM = "ARGUS:RUNNTIME:STATISTICS:SYSTEM:";
    public static final String ARGUS_RUNNTIME_STATISTICS_IP = "ARGUS:RUNNTIME:STATISTICS:IP:";
    public static final String ARGUS_ANALYSIS_STATISTICS_HZ = "ARGUS:ANALYSIS:STATISTICS:HZ:";
    public static final String ARGUS_ANALYSIS_STATISTICS_HOUR = "ARGUS:ANALYSIS:STATISTICS:HOUR:";
    public static final String ARGUS_RUNNTIME_STATISTICS_SYSMONI = "ARGUS:RUNNTIME:STATISTICS:SYSMONI:";
    public static final String ARGUS_RUNNTIME_STATISTICS_SYSALAR = "ARGUS:RUNNTIME:STATISTICS:SYSALAR:";
    public static final String ARGUS_RUNNTIME_STATISTICS_IPADDRESS = "ARGUS:RUNNTIME:STATISTICS:IPADDRESS:";

    public static final String ARGUS_RUNNTIME_STATISTICS_SYSMXMONITOR = "ARGUS:RUNNTIME:STATISTICS:SYSMXMONITOR:";
    public static final String ARGUS_RUNNTIME_STATISTICS_SYSMXALARM = "ARGUS:RUNNTIME:STATISTICS:SYSMXALARM:";

    public static final String SMS_MESSAGE_TEMPLATE = "【%s】%s产生%s条策略为【%s】的报警,IP为%s，请关注!";
    public static final String WEIXIN_MESSAGE_TEMPLATE = "【%s】%s产生%s条策略为【%s】的报警,IP为%s，请关注!以下是日志详情:%s";

    /**
     * 维护报警队列的列表，监控处理线程会自动将报警队列名称进行维护，该值决定每隔多久重新设置队列名称
     */
    public static final Long QUEUE_NAME_UPDATE_EXPIRE_TIME = 30L * 1000;

    public static final String ALARM_QUEUE_NAME = "ARGUS_QUEUE_LIST";

    /**
     * 报警处理线程最大的空闲次数，每隔5s检查一次，超过该次数线程将自动停止运行，
     */
    public static final int THREAD_MAX_IDLE_COUNT = 60;//5s per count,total is 5 minutes
    /**
     * 标识监控策略是否满足调剂的检查队列过期时间，默认为1小时
     */
    public static final int HAPPEN_QUEUE_EXPIRE_SECONDS = 3600*5;
    public static final int MERGE_BUFFER_QUEUE_EXPIRE_SECONDS = 3600*5;
    /**
     * 报警信息队列的队列过期时间，默认为5小时
     */
    public static final int ALARM_QUEUE_EXPIRE_SECONDS = 3600 * 5;
    public static final int ALARM_TEST_QUEUE_EXPIRE_SECONDS = 300;
    /**
     * storm进程往redis中同步集群统计信息的实际间隔，默认为10秒
     */
    public static final int SYNC_INTERVAL_MILLIS = 10 * 1000;
    public static final int SYNC_IPADD_INTERVAL_MILLIS = 120 * 1000;
    public static final int SYNC_IPADD_ZT_INTERVAL_MILLIS = 1 * 1000;
    public static final int SYNC_SYSMA_INTERVAL_MILLIS = 30 * 1000;
    /**
     * 每隔2分钟重置一下当前活动线程标识的过期时间
     */
    public static final long MAX_INTERVAL_FOR_RESET_ACTIVE_THREAD_EXPIRE_SECONDS = 60 * 2;
    /**
     * 活动报警处理线程标识在redis中的过期时间，必须要小于MAX_INTERVAL_FOR_RESET_ACTIVE_THREAD_EXPIRE_TIME的值
     */
    public static final int ACTIVE_ALARM_EXPIRE_SECONDS = 60 * 3;
    public static final int INTERVAL_FOR_MONITOR_CONFIG_RELOAD = 60 * 1000;
    public static final int INTERVAL_FOR_ALARM_CONFIG_RELOAD = 60 * 1000;

    public static String alarmTtemplate = "【%s】系统%s产生%s条策略为【%s】的报警，请尽快关注!";

    /**
     * 线上邮件系统报警模板，请勿修改
     */
    public static String MAIL_TYPE = "0201112";
    public static boolean debugMode = true;


    public static String LOG_PATTERN = "%date{yyyy-MM-dd HH:mm:ss.SSS} | %thread | %-5level | %class{20}:%file(%line) | %X{clientDatetime} | %X{ip} | %X{clientIp} | %X{upIp} | %X{tokenId} | %X{operateId} | %X{deviceId} | %chain | %X{systemId} | %X{clientSystemId} | %X{serverSystemId} | %X{clientType} | %X{clientSystemVersion} | %X{channelId} | %X{clientVersion} | %X{screenSize} | %X{networkType} | %X{location} | %X{cellphone} | %msg%n";
    /////////////////////////////////////////////////////////////////
    public static String CONS_CLIENT_IP = "clientip";
    /////////////////////////////////////////////////////////////////


    public static String LOG_PATTERN_SPLIT = "|";
    /////////////////////////////////////////////////////////////////
    /**
     * argus log的节点key值
     */
    public static String KEY_LOG_IP = "log_ip";
    /**
     * 确信股票的系统名称,当做ip的作用
     */
    public static String KEY_QUEXIN_LOG_SYS_NAME = "snm";
    /**
     * 确信股票的日志信息代表的key
     */
    public static String KEY_QUEXIN_LOG_TXT = "ct";
    public static String KEY_QUEXIN_LOG_TIMESTAMP = "ctms";

    public static String KEY_LOG_MESSAGE = "message";
    public static String KEY_LOG_TIMESTAMP = "@timestamp";
    public static String KEY_LOG_HOST = "host";
    public static String ENTRY_LOG_KEY = "LOG_MESSAGE";
    // 用户uv标识字段
    public static String KEY_UV_ID = "tokenId";
    //////////////////////monitor key///////////////////////////////////////////
    public static String MONITOR_PREFIX = "monitor";
    public static String HAPPEN_PREFIX = "happen";
    public static String HIGH_WATER_LINE_PREFIX = "hwl";
    public static String TEMP_PAUSE_PREFIX = "pause";
    public static String SHOULD_MERGE_PREFIX = "merge";
    public static String MONITOR_MERGE_BUFFER_PREFIX = "buffer";
    public static String ASYNC_CHECK_PREFIX = "asyncheck";
    /////////////////////////////////////////////////////////////////
    public static String ALARM_QUEUE_PREFIX = "alarm";
    /////////////////////////////////////////////////////////////////
    public static String BUS_NODE_PREFIX = "busnode";

    // 分子内容前缀
    public static String BUS_NODE_FRAC_PREFIX = "fractions";
    // 分母内容前缀
    public static String BUS_NODE_NUME_PREFIX = "numerator";

    public static final int ALARM_SOCKET_INTERVAL_MILLIS = 2 * 1000;
    public static final int ARGUS_QUEUE_LAST_ALARM_LOCK_SECONDS = 60 * 1;
    public static final int INTERVAL_FOR_ANALYSIS_CONFIG_RELOAD = 120 * 1000;
    public static final int MONITOR_BNMONITOR_INITQUEUE_CHECK_EXPIRE_SECONDS = 30;
    public static final int INTERVAL_FOR_MONITOR_MERGE_QUEUE = 30;
    public static final int INTERVAL_FOR_IPADDR_CONFIG_RELOAD = 300 * 1000;
}
