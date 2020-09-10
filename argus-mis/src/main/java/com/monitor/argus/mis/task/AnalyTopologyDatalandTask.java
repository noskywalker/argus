package com.monitor.argus.mis.task;

import com.monitor.argus.bean.dataland.AnalyTopologyDatalandEntity;
import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.dao.dataland.IArgusTopologyDatalandDao;
import com.monitor.argus.mis.task.annotations.Job;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.node.IMonitorNodeService;
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
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huxiaolei on 2016/9/28.
 */
@Component
public class AnalyTopologyDatalandTask {

    private static Logger logger = LoggerFactory.getLogger(AnalyTopologyDatalandTask.class);
    private static String lockKey = "AnalyTopologyDatalandHTaskLock";
    private static String lockValue = "AnalyTopologyDatalandHTaskValue";
    private static String lockKey1 = "AnalyTopologyDatalandMTaskLock";
    private static String lockValue1 = "AnalyTopologyDatalandMTaskValue";

    private ConcurrentHashMap<String, Long> oldLogCountH = new ConcurrentHashMap();
    private ConcurrentHashMap<String, Long> logCounterH = new ConcurrentHashMap();
    private ConcurrentHashMap<String, Long> oldLogCountM = new ConcurrentHashMap();
    private ConcurrentHashMap<String, Long> logCounterM = new ConcurrentHashMap();

    @Autowired
    RedisService redisService;

    @Autowired
    IMonitorNodeService nodeService;

    @Autowired
    IArgusTopologyDatalandDao iArgusTopologyDatalandDao;

    @Job(name = "节点数据按小时汇总任务", cron="59 59 * * * ?")
    public void taskHourCycle() {
        Date newDate = new Date();
        try {
            if (redisService.setNX(lockKey, lockValue)) {
                List<NodeEntity> list = nodeService.getAllEnableNodeList();
                if (!CollectionUtils.isEmpty(list)) {
                    for (NodeEntity nodeEntity : list) {
                        List<String> statisticsInfos = redisService.hmget(RedisKeyUtils.ARGUS_ANALYSIS_STATISTICS_HZ_KEY,
                                nodeEntity.getNodeKey());
                        if (!CollectionUtils.isEmpty(statisticsInfos)) {
                            String logCount = statisticsInfos.get(0);
                            String diffLogCount = "0";
                            long newLogCount = NumberUtils.createLong(setDefaultByZero(logCount));
                            long count = logCounterH.get(nodeEntity.getNodeKey()) == null ? 0l : logCounterH.get(nodeEntity.getNodeKey());
                            Long oldLogCount = oldLogCountH.get(nodeEntity.getNodeKey()) == null ? 0l : oldLogCountH.get(nodeEntity.getNodeKey());;
                            if (count > 0) {
                                diffLogCount = String.valueOf(newLogCount - oldLogCount);
                            } else {
                                // 取旧数据
                                Date endDate = new Date();
                                Date beginDate = DateUtils.addHours(endDate, -12);
                                String beginStr = DateUtil.getDateLongTimePlusNoMinuteStr(beginDate);
                                String endStr = DateUtil.getDateLongTimePlusNoMinuteStr(endDate);
                                List<AnalyTopologyDatalandEntity> list1 = iArgusTopologyDatalandDao.getAnalyDataByDateHour(beginStr, endStr, nodeEntity.getNodeKey());
                                if (!CollectionUtils.isEmpty(list1)) {
                                    AnalyTopologyDatalandEntity lastAnalyTopologyDatalandEntity = list1.get(list1.size() - 1);
                                    if (lastAnalyTopologyDatalandEntity != null) {
                                        oldLogCount = NumberUtils.createLong(setDefaultByZero(lastAnalyTopologyDatalandEntity.getLogCount()));
                                        diffLogCount = String.valueOf(newLogCount - oldLogCount);
                                    }
                                }
                            }
                            oldLogCountH.put(nodeEntity.getNodeKey(), newLogCount);
                            count++;
                            logCounterH.put(nodeEntity.getNodeKey(), count);
                            AnalyTopologyDatalandEntity analyTopologyDatalandEntity = new AnalyTopologyDatalandEntity();
                            analyTopologyDatalandEntity.setId(UuidUtil.getUUID());
                            analyTopologyDatalandEntity.setCreateDate(newDate);
                            analyTopologyDatalandEntity.setNodeKey(nodeEntity.getNodeKey());
                            // 当前合计
                            analyTopologyDatalandEntity.setLogCount(setDefaultByZero(logCount));
                            // 差值
                            analyTopologyDatalandEntity.setDiffLogCount(setDefaultByZero(diffLogCount));
                            iArgusTopologyDatalandDao.addAnalyTopologyDatalandHour(analyTopologyDatalandEntity);
                        }
                    }
                }

                // 只保留近一个月的数据
                Date nowDateM = new Date();
                Date nowDateMBefor = DateUtils.addMonths(nowDateM, -1);
                String nowDateMBeforSdf = DateUtil.getSimpleDateTimeStr(nowDateMBefor);
                if (!StringUtil.isEmpty(nowDateMBeforSdf)) {
                    iArgusTopologyDatalandDao.delAnalyTopologyDatalandByDateHour(nowDateMBeforSdf);
                }
            }
            logger.info("taskHourCycle===end");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisService.expire(lockKey, 300);
        }
    }

    @Job(name = "节点数据按分钟汇总任务", cron="0 0/5 * * * ?")
    public void taskMinCycle() {
        Date newDate = new Date();
        try {
            if (redisService.setNX(lockKey1, lockValue1)) {
                List<NodeEntity> list = nodeService.getAllEnableNodeList();
                if (!CollectionUtils.isEmpty(list)) {
                    for (NodeEntity nodeEntity : list) {
                        List<String> statisticsInfos = redisService.hmget(RedisKeyUtils.ARGUS_ANALYSIS_STATISTICS_HZ_KEY,
                                nodeEntity.getNodeKey());
                        if (!CollectionUtils.isEmpty(statisticsInfos)) {
                            String logCount = statisticsInfos.get(0);
                            String diffLogCount = "0";
                            long newLogCount = NumberUtils.createLong(setDefaultByZero(logCount));
                            long count = logCounterM.get(nodeEntity.getNodeKey()) == null ? 0l : logCounterM.get(nodeEntity.getNodeKey());
                            Long oldLogCount = oldLogCountM.get(nodeEntity.getNodeKey()) == null ? 0l : oldLogCountM.get(nodeEntity.getNodeKey());;
                            if (count > 0) {
                                diffLogCount = String.valueOf(newLogCount - oldLogCount);
                            } else {
                                // 取旧数据
                                Date endDate = new Date();
                                Date beginDate = DateUtils.addHours(endDate, -12);
                                String beginStr = DateUtil.getDateLongTimePlusNoSecondStr(beginDate);
                                String endStr = DateUtil.getDateLongTimePlusNoSecondStr(endDate);
                                List<AnalyTopologyDatalandEntity> list1 = iArgusTopologyDatalandDao.getAnalyDataByDateMin(beginStr, endStr, nodeEntity.getNodeKey());
                                if (!CollectionUtils.isEmpty(list1)) {
                                    AnalyTopologyDatalandEntity lastAnalyTopologyDatalandEntity = list1.get(list1.size() - 1);
                                    if (lastAnalyTopologyDatalandEntity != null) {
                                        oldLogCount = NumberUtils.createLong(setDefaultByZero(lastAnalyTopologyDatalandEntity.getLogCount()));
                                        diffLogCount = String.valueOf(newLogCount - oldLogCount);
                                    }
                                }
                            }
                            oldLogCountM.put(nodeEntity.getNodeKey(), newLogCount);
                            count++;
                            logCounterM.put(nodeEntity.getNodeKey(), count);
                            AnalyTopologyDatalandEntity analyTopologyDatalandEntity = new AnalyTopologyDatalandEntity();
                            analyTopologyDatalandEntity.setId(UuidUtil.getUUID());
                            analyTopologyDatalandEntity.setCreateDate(newDate);
                            analyTopologyDatalandEntity.setNodeKey(nodeEntity.getNodeKey());
                            // 当前合计
                            analyTopologyDatalandEntity.setLogCount(setDefaultByZero(logCount));
                            // 差值
                            analyTopologyDatalandEntity.setDiffLogCount(setDefaultByZero(diffLogCount));
                            iArgusTopologyDatalandDao.addAnalyTopologyDatalandMin(analyTopologyDatalandEntity);
                        }
                    }
                }

                // 只保留近7天的数据
                Date nowDateM = new Date();
                Date nowDateMBefor = DateUtils.addDays(nowDateM, -7);
                String nowDateMBeforSdf = DateUtil.getSimpleDateTimeStr(nowDateMBefor);
                if (!StringUtil.isEmpty(nowDateMBeforSdf)) {
                    iArgusTopologyDatalandDao.delAnalyTopologyDatalandByDateMin(nowDateMBeforSdf);
                }
            }
            logger.info("taskMinCycle===end");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisService.expire(lockKey1, 30);
        }
    }

    private String setDefaultByZero(String byteStr) {
        if (StringUtils.isBlank(byteStr)) {
            return "0";
        }
        return byteStr;
    }

}
