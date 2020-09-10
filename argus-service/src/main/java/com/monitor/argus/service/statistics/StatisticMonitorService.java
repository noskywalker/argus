package com.monitor.argus.service.statistics;

import com.monitor.argus.bean.IpAddressFlowBean;
import com.monitor.argus.bean.alarm.AlarmStrategyEntity;
import com.monitor.argus.bean.alarm.vo.*;
import com.monitor.argus.bean.node.*;
import com.monitor.argus.bean.alarm.vo.*;
import com.monitor.argus.bean.dataland.AnalyTopologyHourITEntity;
import com.monitor.argus.bean.monitor.MonitorStrategyEntity;
import com.monitor.argus.bean.monitor.MonitorSystemEntity;
import com.monitor.argus.bean.node.*;
import com.monitor.argus.common.enums.GlobalParam;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.dao.alarm.IAlarmDao;
import com.monitor.argus.dao.monitor.IMonitorDao;
import com.monitor.argus.dao.node.IMonitorNodeDao;
import com.monitor.argus.redis.RedisService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by wangfeng on 16/10/24.
 */
@Service
public class StatisticMonitorService {
    private static Logger logger = LoggerFactory.getLogger(StatisticService.class);

    public static ArrayList<Double> realtimeTrafficList = new ArrayList<Double>();

    @Value("${mode}")
    private String mode;

    @Autowired
    RedisService redisService;

    @Autowired
    IMonitorNodeDao nodeDao;

    @Autowired
    IMonitorDao monitorDao;

    @Autowired
    IAlarmDao alarmDao;

    public String fetchNodeStatisticsInfo(String nodekey) {

        Map<String, String> map = redisService.hgetAll("ARGUS:ANALYSIS:STATISTICS:HZ:");
        if (map == null || map.size() == 0) {
            return "0";
        }
        String statisticsInfo = map.get(nodekey);
        if (statisticsInfo == null || statisticsInfo.length() == 0) {
            return "0";
        } else {
            return statisticsInfo;
        }
    }

    public List<TopoAnalyDayUVEntity> selectDayUVByNodeKey(String nodeKey) {
        Date endDate = new Date();
        Date beginDate = DateUtils.addDays(endDate, -14);
        endDate = DateUtils.addDays(endDate, 1);
        String beginStr = DateUtil.getSimpleDateTimeStr(beginDate);
        String endStr = DateUtil.getSimpleDateTimeStr(endDate);
        return nodeDao.selectDayUVByNodeKey(beginStr, endStr, nodeKey);

    }


    public List<NodePVCollectEntity> getPVCollectByDate(String beginDate, String endDate) {
        if (beginDate == null || beginDate.length() == 0 || endDate == null || endDate.length() == 0) {
            Date end = new Date();
            beginDate = DateUtil.getSimpleDateTimeStr(DateUtils.addDays(end, -30));
            endDate = DateUtil.getSimpleDateTimeStr(DateUtils.addDays(end, 1));
        } else {
            Date end = DateUtil.getDateLong(endDate);
            endDate = DateUtil.getSimpleDateTimeStr(DateUtils.addDays(end, 1));
        }
        List<CollectDataObject> listpv1 = nodeDao.selectCountPVByDateCount(beginDate, endDate);
        List<CollectDataObject> listpv2 = nodeDao.selectCountPVByDate(beginDate, endDate);
        List<CollectDataObject> listuv1 = nodeDao.selectCountUVByDateCount(beginDate, endDate);
        List<CollectDataObject> listuv2 = nodeDao.selectCountUVByDate(beginDate, endDate);
        List<NodeEntity> nodeEntityList = nodeDao.getAllNodeList();
        Map<String, String> uvParentMap = new HashMap<>();
        Map<String, String> uvChildMap = new HashMap<>();
        Map<String, String> nodeMap = new HashMap<>();
        for (CollectDataObject parent : listuv1) {
            uvParentMap.put(parent.getDate(), parent.getCountuv());
        }
        for (CollectDataObject child : listuv2) {
            uvChildMap.put(child.getDate() + child.getNodeName(), child.getCountuv());
        }
        for (NodeEntity entity : nodeEntityList) {
            nodeMap.put(entity.getNodeKey(), entity.getNodeName());
        }
        List<NodePVCollectEntity> parentList = new ArrayList<>();
        Integer i = 1, j = 1;
        for (CollectDataObject entity : listpv1) {
            NodePVCollectEntity collectEntity = new NodePVCollectEntity();
            entity.setCountuv(uvParentMap.get(entity.getDate()));
            entity.setNodeName("--");
            collectEntity.setDataObject(entity);
            collectEntity.setId(entity.getDate());
            collectEntity.setIsLeaf("false");
            collectEntity.setOrder(i++);
            CollectUserObject userObject = new CollectUserObject();
            userObject.setIsGroup("true");
            collectEntity.setUserObject(userObject);
            parentList.add(collectEntity);
        }
        for (CollectDataObject entity : listpv2) {
            NodePVCollectEntity child = new NodePVCollectEntity();
            entity.setCountuv(uvChildMap.get(entity.getDate() + entity.getNodeName()));
            entity.setNodeName(nodeMap.get(entity.getNodeName()));
            child.setPid(entity.getDate());
            child.setId(j.toString());
            entity.setDate(" ");
            child.setDataObject(entity);
            parentList.add(child);
            j++;
        }
        return parentList;
    }


    public List<SystemMonitorAlarmEntity> fetchSystemMonitorAlarmData(String redisName) {
        List<MonitorSystemEntity> systemEntityList = monitorDao.getAllSystems();
        Map<String, String> map = redisService.hgetAll(redisName);
        List<SystemMonitorAlarmEntity> systemMonitorAlarmEntityList = new ArrayList<>();
        if (map == null || map.size() == 0) {
            return null;
        }
        BigDecimal count = BigDecimal.ZERO;
        for (String s : map.values()) {
            count = count.add(new BigDecimal(s));
        }
        Integer i = 1;
        for (MonitorSystemEntity entity : systemEntityList) {
            SystemMonitorAlarmEntity systemMonitorAlarmEntity = new SystemMonitorAlarmEntity();
            systemMonitorAlarmEntity.setSystemName(entity.getSystemName());
            if (map.get(entity.getId()) != null) {
                systemMonitorAlarmEntity.setCount(map.get(entity.getId()));
                systemMonitorAlarmEntity.setPercent(new BigDecimal(systemMonitorAlarmEntity.getCount()).divide(count, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).toString() + "%");
            }
            systemMonitorAlarmEntityList.add(systemMonitorAlarmEntity);
        }
        Collections.sort(systemMonitorAlarmEntityList, new ComparatorImpl());
        for (SystemMonitorAlarmEntity entity : systemMonitorAlarmEntityList) {
            entity.setNum(i++);
        }
        return systemMonitorAlarmEntityList;

    }

    public List<SystemMonitorAlarmDetailEntity> fetchSystemMonitorAlarmDetailData(String redisName, String type, String systemId) {

        Map<String, String> map = redisService.hgetAll(redisName);
        if (MapUtils.isEmpty(map)) {
            return null;
        }
        // 合计
        BigDecimal count = BigDecimal.ZERO;
        for (String s : map.values()) {
            count = count.add(new BigDecimal(s));
        }

        List<SystemMonitorAlarmDetailEntity> systemMonitorAlarmEntityList = new ArrayList<SystemMonitorAlarmDetailEntity>();
        if ("monitor".equals(type)) {
            List<MonitorStrategyEntity> monitorStrategyList = monitorDao.getAllEnableStrategies();
            for (MonitorStrategyEntity entity : monitorStrategyList) {
                if (entity.getSystemId().equals(systemId)) {
                    SystemMonitorAlarmDetailEntity systemMonitorAlarmDetailEntity = new SystemMonitorAlarmDetailEntity();
                    systemMonitorAlarmDetailEntity.setName(entity.getMonitorName());
                    if (map.get(entity.getId()) != null) {
                        systemMonitorAlarmDetailEntity.setCount(map.get(entity.getId()));
                        systemMonitorAlarmDetailEntity.setPercent(new BigDecimal(systemMonitorAlarmDetailEntity.getCount()).divide(count, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).toString() + "%");
                    }
                    systemMonitorAlarmEntityList.add(systemMonitorAlarmDetailEntity);
                }
            }
        } else if ("alarm".equals(type)) {
            List<String> aids = new ArrayList<String>();
            List<MonitorStrategyEntity> monitorStrategyList = monitorDao.getAllEnableStrategies();
            for (MonitorStrategyEntity entity : monitorStrategyList) {
                if (entity.getSystemId().equals(systemId)) {
                    aids.add(entity.getAlarmId());
                }
            }
            List<AlarmStrategyEntity> alarmStrategyList = alarmDao.getAlarmStrategy();
            for (AlarmStrategyEntity entity : alarmStrategyList) {
                if (aids.contains(entity.getId())) {
                    SystemMonitorAlarmDetailEntity systemMonitorAlarmDetailEntity = new SystemMonitorAlarmDetailEntity();
                    systemMonitorAlarmDetailEntity.setName(entity.getAlarmName());
                    if (map.get(entity.getId()) != null) {
                        systemMonitorAlarmDetailEntity.setCount(map.get(entity.getId()));
                        systemMonitorAlarmDetailEntity.setPercent(new BigDecimal(systemMonitorAlarmDetailEntity.getCount()).divide(count, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).toString() + "%");
                    }
                    systemMonitorAlarmEntityList.add(systemMonitorAlarmDetailEntity);
                }
            }
        }

        Collections.sort(systemMonitorAlarmEntityList, new ComparatorDetailImpl());
        Integer i = 1;
        for (SystemMonitorAlarmDetailEntity entity : systemMonitorAlarmEntityList) {
            entity.setNum(i++);
        }
        return systemMonitorAlarmEntityList;

    }

    public List<IpAddressFlowBean> getCityIpAddress(String systemId) {
        logger.info("获取城市数据。系统ID:" + systemId);
        Map<String, String> map = new HashMap<>();
        map.putAll(redisService.hgetAll(RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_IPADDRESS_KEY + systemId));
        List<IpAddressFlowBean> flowList = new ArrayList<>();
        BigDecimal count = BigDecimal.ZERO;
        for (String addressMd5Key : GlobalParam.addressMd5Map.keySet()) {
            IpAddressFlowBean bean = new IpAddressFlowBean();
            bean.setCityNameMd5(addressMd5Key);
            bean.setCityName(GlobalParam.addressMd5Map.get(addressMd5Key));
            if (map.get(addressMd5Key) != null) {
                bean.setIpFlow(map.get(addressMd5Key));
                count = count.add(new BigDecimal(map.get(addressMd5Key)));
                flowList.add(bean);
            }
        }
        Collections.sort(flowList, new ComparatorSystemFlowImpl());
        Integer i = 1;
        for (IpAddressFlowBean bean : flowList) {
            if (!count.equals(BigDecimal.ZERO)) {
                bean.setPercent(new BigDecimal(bean.getIpFlow()).divide(count, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).toString() + "%");
            } else {
                bean.setPercent("0.0000%");
            }

            bean.setNum(i++);
        }
        return flowList;
    }

    public void systemcityflowClear(String systemId) {
        redisService.delete(RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_IPADDRESS_KEY + systemId);
    }

    public Map<String, List<AnalyTopologyHourITEntity>> selectInterTimeByNodeKey(String nodeKey) {
        Date endDate = new Date();
        Date beginDate = DateUtils.addHours(endDate, -24);
        String beginStr = DateUtil.getDateLongTimePlusStr(beginDate);
        String endStr = DateUtil.getDateLongTimePlusStr(endDate);
        List<AnalyTopologyHourITEntity> todayList = nodeDao.selectInterTimeByNodeKey(beginStr, endStr, nodeKey);
        endDate = DateUtils.addHours(endDate, -24);
        beginDate = DateUtils.addHours(beginDate, -24);
        beginStr = DateUtil.getDateLongTimePlusStr(beginDate);
        endStr = DateUtil.getDateLongTimePlusStr(endDate);
        List<AnalyTopologyHourITEntity> yesterdayList = nodeDao.selectInterTimeByNodeKey(beginStr, endStr, nodeKey);
        Map<String, List<AnalyTopologyHourITEntity>> map = new HashMap<>();
        map.put("today", todayList);
        map.put("yesterday", yesterdayList);
        return map;
    }

    public List<AnalyTopologyHourITEntity> getAvgPertimeByNodeKeyAndDate(String nodeKey, String beginDate, String endDate) {
        return nodeDao.getAvgPertimeByNodeKeyAndDate(beginDate, endDate, nodeKey);
    }

    public List<AnalyTopologyHourITEntity> getInterRankingList() {
        return nodeDao.getInterRankingList();
    }

    public List<AnalyTopologyHourITEntity> getInterRankingListAsc() {
        return nodeDao.getInterRankingListAsc();
    }

}
