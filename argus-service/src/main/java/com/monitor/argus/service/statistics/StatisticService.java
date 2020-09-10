package com.monitor.argus.service.statistics;

import com.monitor.argus.bean.statistic.vo.StatisticsValueVO;
import com.monitor.argus.common.enums.StatisticType;
import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.redis.RedisService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/20.
 */
@Service
public class StatisticService {

    private static Logger logger = LoggerFactory.getLogger(StatisticService.class);

    public static ArrayList<Double> realtimeTrafficList = new ArrayList<Double>();

    @Value("${mode}")
    private String mode;

    @Autowired
    RedisService redisService;

    private String setDefaultByZero(String byteStr) {
        if (StringUtils.isBlank(byteStr)) {
            return "0";
        }
        return byteStr;
    }

    public StatisticsValueVO fetchStaticticInfo(String currentValueStr, long formerValues, StatisticType type) {
        long totalValues = NumberUtils.createLong(setDefaultByZero(currentValueStr));
        StatisticsValueVO statisticsValueVO = new StatisticsValueVO();
        statisticsValueVO.setCurrentValues(totalValues);
        statisticsValueVO.setFormerValues(formerValues);
        statisticsValueVO.setDifferenceValues(totalValues - formerValues);
        statisticsValueVO.setType(type);
        return statisticsValueVO;
    }

    public List<StatisticsValueVO> fetchFullStatistics(long formerBytes, long formerMonitors, long formerAlarms, long formerLogs) {

        /* TODO this method should to be refactored in the future */
        List<StatisticsValueVO> vos = new ArrayList<>();
        List<String> statisticsInfos = redisService.hmget(
                RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_KEY
                , RedisKeyUtils.MONITOR_STAT_TOTAL_LOG_BYTES
                , RedisKeyUtils.MONITOR_STAT_TOTAL_LOG_COUNT
                , RedisKeyUtils.MONITOR_STAT_TOTAL_MONITOR_HAPPENED
                , RedisKeyUtils.MONITOR_STAT_TOTAL_ALARM_HAPPENED);
        StatisticsValueVO bytesVO = fetchStaticticInfo(statisticsInfos.get(0), formerBytes,StatisticType.BYTE);
        StatisticsValueVO logsVO = fetchStaticticInfo(statisticsInfos.get(1), formerLogs,StatisticType.LOGCOUNT);
        StatisticsValueVO monitorsVO = fetchStaticticInfo(statisticsInfos.get(2), formerMonitors,StatisticType.MONITOR);
        StatisticsValueVO alarmsVO = fetchStaticticInfo(statisticsInfos.get(3), formerAlarms,StatisticType.ALARM);
        vos.add(bytesVO);
        vos.add(logsVO);
        vos.add(monitorsVO);
        vos.add(alarmsVO);

        return vos;
    }

    public ArrayList<Double> fetchRealtimeTraffic(int cmaximum, Double chaBytes, long oldBytes) {
        ArrayList<Double> realtimeTrafficListCopy = new ArrayList<Double>();
        Double chaBytesByS = 0d;
        int per = 10;
        // 每10s的差额
        if (chaBytes > 0 && chaBytes < oldBytes) {

            // 转kb
            if("production".equals(mode)) {
                // 线上
                chaBytes = chaBytes/1024;
            } else {
                // 测试
                chaBytes = chaBytes * 10;
            }

            // per平均差额
            chaBytesByS = chaBytes/per;
            for (int i = 0; i < per; i++) {
                // 随机正负1%-11%的浮动
                int halfPercent = (int)(Math.random() * 2 + 1);
                int positiveAndNegative = (int)(Math.pow(-1, halfPercent));
                double floatingPercent = (Math.random() + 0.1) * 0.1;
                Long floatingNum = (long)(chaBytesByS * floatingPercent);
                Double chaBytesBySf = chaBytesByS + floatingNum * positiveAndNegative;

                realtimeTrafficList.add(chaBytesBySf);
            }
            if (realtimeTrafficList.size() > cmaximum) {
                int chai = realtimeTrafficList.size() - cmaximum;
                for (int j = 0; j < chai; j++) {
                    realtimeTrafficList.remove(0);
                }
            }
        } else {
            for (int i = 0; i < per; i++) {
                realtimeTrafficList.add(0d);
            }
            if (realtimeTrafficList.size() > cmaximum) {
                int chai = realtimeTrafficList.size() - cmaximum;
                for (int j = 0; j < chai; j++) {
                    realtimeTrafficList.remove(0);
                }
            }
        }

        // 复制一份
        for (Double relad : realtimeTrafficList) {
            realtimeTrafficListCopy.add(relad);
        }
        logger.info("cmaximum=" + cmaximum + ";chaBytes=" + chaBytes + ";chaBytesByS=" + chaBytesByS);
        logger.info("realtimeTrafficListCopy=" + realtimeTrafficListCopy.size() + "=======" + realtimeTrafficListCopy.toString());
        return realtimeTrafficListCopy;
    }

}