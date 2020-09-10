package com.monitor.argus.mis.controller.statistic;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.monitor.argus.bean.IpAddressFlowBean;
import com.monitor.argus.bean.alarm.vo.SystemMonitorAlarmDetailEntity;
import com.monitor.argus.bean.alarm.vo.SystemMonitorAlarmEntity;
import com.monitor.argus.bean.dataland.AnalyTopologyDatalandLogDiff;
import com.monitor.argus.bean.dataland.AnalyTopologyHourITEntity;
import com.monitor.argus.bean.dataland.ArgusTopologyDatalandEntity;
import com.monitor.argus.bean.dataland.ArgusTopologyMorSysSumEntity;
import com.monitor.argus.bean.monitor.MonitorHostEntity;
import com.monitor.argus.bean.monitor.MonitorSystemEntity;
import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.bean.node.NodePVCollectEntity;
import com.monitor.argus.bean.node.TopoAnalyDayUVEntity;
import com.monitor.argus.bean.node.TopoAnalyEntity;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.dataland.IArgusTopologyDatalandService;
import com.monitor.argus.service.monitor.IMonitorService;
import com.monitor.argus.service.node.IMonitorNodeService;
import com.monitor.argus.service.statistics.StatisticMonitorService;
import com.monitor.argus.common.annotation.Auth;
import com.monitor.argus.common.enums.GlobalParam;
import com.monitor.argus.common.model.ResponseData;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

import static com.monitor.argus.common.util.ArgusUtils.ARGUS_RUNNTIME_STATISTICS_IP;

/**
 * Created by wangfeng on 16/10/24.
 */
@Controller
@RequestMapping("/statisticsmonitor")
public class StatisticsMonitorController {

    private static Logger logger = LoggerFactory.getLogger(StatisticsMonitorController.class);
    @Autowired
    StatisticMonitorService statisticMonitorService;

    @Autowired
    IMonitorNodeService nodeService;
    @Autowired
    IMonitorService monitorService;
    @Autowired
    RedisService redisService;

    @Autowired
    IArgusTopologyDatalandService argusTopologyDatalandService;

    @Value("${systemFlowId}")
    private String systemFlowId;

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "/bytesBykey", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    ResponseData fetchTotalMonitorBytes(String nodeKey) {
        ResponseData jsonResponse = new ResponseData();
        String statisticsInfo = statisticMonitorService.fetchNodeStatisticsInfo(nodeKey);
        jsonResponse.setSuccess(true);
        jsonResponse.setMsg("获取成功！");
        jsonResponse.setObj(statisticsInfo);
        return jsonResponse;
    }

    @RequestMapping(value = "/rtf", method = RequestMethod.GET)
    public String showAnaly(Model model, String nodeKey, String hanId) {
        logger.info("初始化nodeKey-{}", nodeKey);
        List<NodeEntity> list = nodeService.getAllEnableNodeList();
        String max = "100";
        if (nodeKey != null) {
            max = GlobalParam.getValueByKey(nodeKey);
        }
        if (nodeKey != null) {
            model.addAttribute("nodeKey", nodeKey);
        } else {
            model.addAttribute("nodeKey", "Intouchables");
        }
        model.addAttribute("nodelist", list);
        model.addAttribute("max", max);
        model.addAttribute("hanId", hanId);
        return "/node/realTimeFlow";
    }


    @RequestMapping(value = "/dayuv", method = RequestMethod.GET)
    public String showDayUV(Model model, String nodeKey) {
        logger.info("初始化nodeKey-{}", nodeKey);
        List<NodeEntity> list = nodeService.getAllEnableNodeList();
        model.addAttribute("nodelist", list);
        return "/node/nodedayuv";
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "/getuvbynode", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    ResponseData getDayUVByNodeKey(String nodeKey) {
        ResponseData jsonResponse = new ResponseData();
        List<TopoAnalyDayUVEntity> uvList = statisticMonitorService.selectDayUVByNodeKey(nodeKey);
        List<String> time = new ArrayList<>();
        List<String> uv = new ArrayList<>();
        for (TopoAnalyDayUVEntity entity : uvList) {
            time.add(entity.getCreateDateStr());
            uv.add(entity.getUVCount());
        }
        Map<String, List<String>> map = new HashMap<>();
        map.put("time", time);
        map.put("uv", uv);
        jsonResponse.setSuccess(true);
        jsonResponse.setMsg("获取成功！");
        jsonResponse.setObj(map);
        return jsonResponse;
    }

    @RequestMapping(value = "/daypvuv", method = RequestMethod.GET)
    public String showDayPVUV(Model model, String nodeKey) {
        logger.info("初始化nodeKey-{}", nodeKey);
        List<NodeEntity> list = nodeService.getAllEnableNodeList();
        model.addAttribute("nodelist", list);
        return "/node/nodedaypvuv";
    }

    @RequestMapping(value = "/getpvuv", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    ResponseData getPVUVByNodeKey(String nodeKey) {
        ResponseData jsonResponse = new ResponseData();
        List<TopoAnalyDayUVEntity> uvList = statisticMonitorService.selectDayUVByNodeKey(nodeKey);
        List<TopoAnalyEntity> pvList = nodeService.selectAnalyByDays(nodeKey);
        List<String> time = new ArrayList<>();
        List<String> uv = new ArrayList<>();
        List<String> pv = new ArrayList<>();
        Map<String, String> uvMap = new HashMap<>();

        for (TopoAnalyDayUVEntity entity : uvList) {
            uvMap.put(entity.getCreateDateStr(), Long.parseLong(entity.getUVCount()) > 0 ? entity.getUVCount() : "0");
        }
        for (TopoAnalyEntity entity : pvList) {
            pv.add(Long.parseLong(entity.getDiffLogCount()) > 0 ? entity.getDiffLogCount() : "0");
            time.add(entity.getCreateDateStr());
            if (uvMap.get(entity.getCreateDateStr()) != null) {
                uv.add(uvMap.get(entity.getCreateDateStr()));
            } else {
                uv.add("0");
            }
        }
        Map<String, List<String>> map = new HashMap<>();
        map.put("time", time);
        map.put("uv", uv);
        map.put("pv", pv);
        jsonResponse.setSuccess(true);
        jsonResponse.setMsg("获取成功！");
        jsonResponse.setObj(map);
        return jsonResponse;
    }

    @RequestMapping(value = "/nodepvcollect", method = RequestMethod.GET)
    public String showPVCollect(Model model) {
        String max = "";
        model.addAttribute("max", max);
        return "/node/NodePvCollect";
    }


    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "/getpvbydate", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    ResponseData getPVByDate(String beginDate, String endDate) {
        ResponseData jsonResponse = new ResponseData();
        List<NodePVCollectEntity> list = statisticMonitorService.getPVCollectByDate(beginDate, endDate);
        jsonResponse.setSuccess(true);
        jsonResponse.setMsg("获取成功！");
        jsonResponse.setObj(list);
        return jsonResponse;
    }

    @RequestMapping(value = "/monitoralarmList", method = RequestMethod.GET)
    public String monitoralarmList(Model model) {
        String monitorName = RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_SYSMONI_KEY;
        String alarmName = RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_SYSALAR_KEY;
        List<SystemMonitorAlarmEntity> monitorList = statisticMonitorService.fetchSystemMonitorAlarmData(monitorName);
        List<SystemMonitorAlarmEntity> alarmList = statisticMonitorService.fetchSystemMonitorAlarmData(alarmName);

        model.addAttribute("monitorList", monitorList);
        model.addAttribute("alarmList", alarmList);

        return "/monitor/systemMonitorAlarm";
    }

    @RequestMapping(value = "/monitoralarmDetailList", method = RequestMethod.GET)
    public String monitoralarmDetailList(Model model, String systemId, String hanId) {
        String systemName = "";
        List<MonitorSystemEntity> systemEntityList = monitorService.getAllSystems();
        if (systemId == null) {
            systemId = systemEntityList.get(0).getId();
            systemName = systemEntityList.get(0).getSystemName();
        } else {
            for (MonitorSystemEntity monitorSystemEntitys : systemEntityList) {
                if (systemId.equals(monitorSystemEntitys.getId())) {
                    systemName = monitorSystemEntitys.getSystemName();
                }
            }
        }

        String monitorName = RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_SYSMXMONITOR_KEY + systemId;
        String alarmName = RedisKeyUtils.ARGUS_RUNNTIME_STATISTICS_SYSMXALARM_KEY + systemId;

        List<SystemMonitorAlarmDetailEntity> monitorList = statisticMonitorService.fetchSystemMonitorAlarmDetailData(monitorName, "monitor", systemId);
        List<SystemMonitorAlarmDetailEntity> alarmList = statisticMonitorService.fetchSystemMonitorAlarmDetailData(alarmName, "alarm", systemId);

        model.addAttribute("systemList", systemEntityList);
        model.addAttribute("monitorList", monitorList);
        model.addAttribute("alarmList", alarmList);
        model.addAttribute("systemId", systemId);
        model.addAttribute("systemName", systemName);
        model.addAttribute("hanIdg", hanId);
        return "/monitor/systemMonitorAlarmDetail";
    }


    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "/dayinfo", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getDataByDate() {
        ResponseData jsonResponse = new ResponseData();
        Map<String, List<ArgusTopologyDatalandEntity>> map = argusTopologyDatalandService.getDataByDate(24);
        if (map != null) {
            List<ArgusTopologyDatalandEntity> todayList = map.get("todayData");
            List<ArgusTopologyDatalandEntity> yesterdayList = map.get("yesterdayData");
            List<String> numLista = new ArrayList<>();
            List<String> dateLista = new ArrayList<>();
            List<String> numListb = new ArrayList<>();
            List<String> dateListb = new ArrayList<>();
            getList(todayList, dateLista, numLista);
            getList(yesterdayList, dateListb, numListb);
            Map<String, Object> mapResult = new HashMap<>();
            mapResult.put("todaytime", dateLista);
            mapResult.put("todaydata", numLista);
            setYesterdayList(dateLista, dateListb, numListb);

            mapResult.put("yesterdaytime", dateListb);
            mapResult.put("yesterdaydata", numListb);
            //  logger.info(json);
            jsonResponse.setObj(mapResult);
        } else {
            jsonResponse.setSuccess(false);
        }
        return jsonResponse;
    }

    private void getList(List<ArgusTopologyDatalandEntity> todayList, List<String> dateList, List<String> numList) {
        List<Date> dateAList = new ArrayList<>();
        for (ArgusTopologyDatalandEntity entity : todayList) {
            if (entity.getCreateDate().getMinutes() != 59 || entity.getCreateDate().getSeconds() != 59) {
                continue;
            }
            if (dateAList.size() > 0) {
                int a = dateAList.get(dateAList.size() - 1).getHours();
                int b = entity.getCreateDate().getHours();
                Integer diff = b - a;
                if (diff > 1) {
                    while (diff-- > 1) {
                        dateAList.add(DateUtils.addHours(dateAList.get(dateAList.size() - 1), 1));
                        numList.add("0");
                    }
                }
            }
            dateAList.add(entity.getCreateDate());
            numList.add("0");
        }
        Integer i = 0;
        for (Date date : dateAList) {
            for (ArgusTopologyDatalandEntity entity : todayList) {
                if (date.equals(entity.getCreateDate())) {
                    String num = Long.parseLong(entity.getDiffLogBytes()) >= 0 ? entity.getDiffLogBytes() : "0";
                    num = String.valueOf(Long.parseLong(num) / 1024 / 1024);
                    numList.set(i, num);
                }
            }
            i++;
        }

        for (Date d : dateAList) {
            dateList.add(d.getHours() + ":00");
        }

    }

    private void setYesterdayList(List<String> todayDate, List<String> yesterdayDate, List<String> yesterdayNum) {
        Integer i = 0;
        for (String today : todayDate) {
            if (!yesterdayDate.contains(today)) {
                yesterdayNum.add(i, "0");
            }
            i++;
        }

    }

    @RequestMapping(value = "/traffic", method = RequestMethod.GET)
    public String toTrafficHtml(Model model) {
        return "/monitor/traffic";
    }


    @RequestMapping(value = "/systemflow", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getSystemFlowFromRedis() {
        ResponseData jsonResponse = new ResponseData();
        List<MonitorSystemEntity> systemList = monitorService.getAllSystems();
        List<MonitorHostEntity> allHosts = monitorService.getAllHosts();
        Map<String, String> flowMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (!StringUtil.isEmpty(o1) && !o1.contains("未知")) {
                    if (!StringUtil.isEmpty(o2) && o2.contains("未知")) {
                        return -1;
                    }
                }
                if (!StringUtil.isEmpty(o2) && !o2.contains("未知")) {
                    if (!StringUtil.isEmpty(o1) && o1.contains("未知")) {
                        return 1;
                    }
                }
                return o1.compareTo(o2);
            }
        });
        Map<String, String> map = redisService.hgetAll(ARGUS_RUNNTIME_STATISTICS_IP);
        if (!MapUtils.isEmpty(map)) {
            Set<String> ipKeys = map.keySet();
            if (!CollectionUtils.isEmpty(ipKeys)) {
                for (String ipKey : ipKeys) {
                    if (!StringUtil.isEmpty(ipKey)) {
                        MonitorSystemEntity monitorSystemEntity = getSystemByIp(ipKey, allHosts, systemList);
                        String ipValue = map.get(ipKey);
                        if (ipValue != null && ipValue.length() > 0) {
                            ipValue = dealWithValue(ipValue);
                            if (monitorSystemEntity != null) {
                                flowMap.put(monitorSystemEntity.getSystemName() + "(" + ipKey + ")", ipValue);
                            } else {
                                flowMap.put("未知(" + ipKey + ")", ipValue);
                            }
                        }
                    }
                }
            }
        }
        jsonResponse.setObj(flowMap);
        return jsonResponse;
    }

    private MonitorSystemEntity getSystemByIp(String ip, List<MonitorHostEntity> allHosts, List<MonitorSystemEntity> systemList) {
        MonitorSystemEntity monitorSystemEntity = null;
        String systemId = "";
        if (!CollectionUtils.isEmpty(allHosts)) {
            for (MonitorHostEntity host : allHosts) {
                if (ip.equals(host.getIp())) {
                    systemId = host.getSystemId();
                    break;
                }
            }
        }
        if (!StringUtil.isEmpty(systemId) && !CollectionUtils.isEmpty(systemList)) {
            for (MonitorSystemEntity system : systemList) {
                if (systemId.equals(system.getId())) {
                    monitorSystemEntity = system;
                    break;
                }
            }
        }

        return monitorSystemEntity;
    }


    List<String> bList = new ArrayList<String>() {{
        add("B");
        add("KB");
        add("MB");
        add("GB");
        add("TB");
    }};

    private String dealWithValue(String redisValue) {
        Long num = Long.parseLong(redisValue);
        double a = num;
        int idx = 0;
        while (a >= 1024.0 && idx <= bList.size()) {
            a /= 1024.0;
            idx += 1;
        }
        double b = (double) (Math.round(a * 10000)) / 10000;
        redisValue = b + bList.get(idx);
        return redisValue;
    }

    @RequestMapping(value = "/trend", method = RequestMethod.GET)
    public String toTrendHtml(Model model) {
        return "/monitor/trend";
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "/monitortimes", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getMonitorAlarmTimes() {
        ResponseData jsonResponse = new ResponseData();
        List<ArgusTopologyMorSysSumEntity> list = argusTopologyDatalandService.getMorSysSumData();
        List<String> monitor = new ArrayList<>();
        List<String> alarm = new ArrayList<>();
        List<String> time = new ArrayList<>();
        for (ArgusTopologyMorSysSumEntity entity : list) {
            monitor.add(Float.parseFloat(entity.getDiffMonitors()) > 0 ? entity.getDiffMonitors() : "0");
            alarm.add(Float.parseFloat(entity.getDiffAlarms()) > 0 ? entity.getDiffAlarms() : "0");
            time.add(entity.getCreateDate());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("monitor", monitor);
        map.put("alarm", alarm);
        map.put("time", time);
        jsonResponse.setObj(map);
        return jsonResponse;
    }

    @RequestMapping(value = "/sumdayinfo", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getSumDiffLogBytesByDay() {
        ResponseData jsonResponse = new ResponseData();
        List<AnalyTopologyDatalandLogDiff> sumList = argusTopologyDatalandService.getDiffLogBytes(13);
        List<String> time = new ArrayList<>();
        List<String> diffBytes = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        if (sumList != null && sumList.size() > 0) {
            for (AnalyTopologyDatalandLogDiff entity : sumList) {
                time.add(entity.getCreateDate());
                diffBytes.add(String.valueOf(Long.parseLong(entity.getDiffLogBytes() == null ? "0" : entity.getDiffLogBytes()) / 1024 / 1024));
            }
            map.put("time", time);
            map.put("diffBytes", diffBytes);
            jsonResponse.setObj(map);
        } else {
            jsonResponse.setSuccess(false);
        }
        return jsonResponse;
    }


    @RequestMapping(value = "/summonitor", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getSumMonitorByDay() {

        ResponseData jsonResponse = new ResponseData();
        List<ArgusTopologyDatalandEntity> sumList = argusTopologyDatalandService.getSumMonitorByDay(24);
        if (sumList != null && sumList.size() > 0) {
            List<String> numLista = new ArrayList<>();
            List<String> numListb = new ArrayList<>();
            List<String> dateList = new ArrayList<>();
            for (ArgusTopologyDatalandEntity entity : sumList) {
                dateList.add(entity.getCreateDate().getHours() + ":00");
                numLista.add(entity.getDiffAlarms());
                numListb.add(entity.getDiffMonitors());
            }
            Map<String, Object> mapResult = new HashMap<>();
            mapResult.put("time", dateList);
            mapResult.put("alarm", numLista);
            mapResult.put("monitor", numListb);
            //  logger.info(json);
            jsonResponse.setObj(mapResult);
        } else {
            jsonResponse.setSuccess(false);
        }
        return jsonResponse;
    }


    @RequestMapping(value = "/systemcityflow", method = RequestMethod.GET)
    public String systemCityFlowList(Model model, String systemId, String hanId) {
        logger.info("获取系统城市流量。systemId:" + systemId);
        List<MonitorSystemEntity> systemEntityList = monitorService.getAllSystems();
        if (systemId == null) {
            systemId = systemEntityList.get(0).getId();
        }
        List<IpAddressFlowBean> flowBeanList = statisticMonitorService.getCityIpAddress(systemId);
        model.addAttribute("systemList", systemEntityList);
        model.addAttribute("flowList", flowBeanList);
        model.addAttribute("systemId", systemId);
        model.addAttribute("hanId", hanId);
        return "/monitor/systemflow";
    }

    @RequestMapping(value = "/getipaddress", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getIpAddress(String systemId) {
        logger.info("读取城市流量信息");
        ResponseData jsonResponse = new ResponseData();
        if (StringUtil.isEmpty(systemId)) {
            systemId = systemFlowId;
        }
        List<IpAddressFlowBean> flowBeanList = statisticMonitorService.getCityIpAddress(systemId);
        List<IpAddressFlowBean> flowList = new ArrayList<>();
        for (IpAddressFlowBean bean : flowBeanList) {
            if (bean.getCityName().contains("市")) {
                bean.setPercent(bean.getPercent().replace("%", ""));
                flowList.add(bean);
            }
            if (flowList.size() == 300) {
                break;
            }
        }
        logger.info("调用结果:" + flowList.size());
        jsonResponse.setObj(flowList);
        return jsonResponse;
    }

    @RequestMapping(value = "/systemcityflowClear", method = RequestMethod.GET)
    public String systemcityflowClear(Model model, String systemId, String hanId) {
        logger.info("清理系统城市流量。systemId:" + systemId);
        if (!StringUtil.isEmpty(systemId)) {
            statisticMonitorService.systemcityflowClear(systemId);
        }

        List<MonitorSystemEntity> systemEntityList = monitorService.getAllSystems();
        if (systemId == null) {
            systemId = systemEntityList.get(0).getId();
        }
        List<IpAddressFlowBean> flowBeanList = statisticMonitorService.getCityIpAddress(systemId);
        model.addAttribute("systemList", systemEntityList);
        model.addAttribute("flowList", flowBeanList);
        model.addAttribute("systemId", systemId);
        model.addAttribute("hanId", hanId);
        return "/monitor/systemflow";
    }

    @RequestMapping(value = "/interinfo", method = RequestMethod.GET)
    public String interInfo(Model model, String nodeKey, String hanId) {
        logger.info("接口调用情况查询。nodekey:" + nodeKey);
        NodeEntity nodeEntity = new NodeEntity();
        nodeEntity.setIsUv(-1);
        nodeEntity.setIsInterface(1);
        List<NodeEntity> nodeEntityList = nodeService.getNodeList(nodeEntity);
        if (nodeKey == null && nodeEntityList != null) {
            nodeKey = nodeEntityList.get(0).getNodeKey();
        }
        Map<String, List<AnalyTopologyHourITEntity>> map = statisticMonitorService.selectInterTimeByNodeKey(nodeKey);
        model.addAttribute("nodeEntityList", nodeEntityList);
        model.addAttribute("nodeKey", nodeKey);
        List<AnalyTopologyHourITEntity> list = map.get("today");
        Collections.reverse(list);
        model.addAttribute("todayList", list);
        model.addAttribute("hanId", hanId);
        return "/monitor/interinfo";
    }

    @RequestMapping(value = "/intercompare", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getInterCompare(String nodeKey) {
        logger.info("接口响应对比情况查询。nodekey:" + nodeKey);
        ResponseData jsonResponse = new ResponseData();
        Map<String, List<AnalyTopologyHourITEntity>> map = statisticMonitorService.selectInterTimeByNodeKey(nodeKey);

        List<String> time = new ArrayList<>();
        List<Double> today = new ArrayList<>();
        List<Double> yesterday = new ArrayList<>();
        List<Long> yesterdaycount = new ArrayList<>();
        List<Long> todaycount = new ArrayList<>();
        Map<Integer, AnalyTopologyHourITEntity> yesterdayData = new HashMap<>();
        for (AnalyTopologyHourITEntity entity : map.get("yesterday")) {
            yesterdayData.put(entity.getCreateDate().getHours(), entity);
        }
        for (AnalyTopologyHourITEntity entity : map.get("today")) {
            time.add(DateUtil.getTimeHHmmStr(entity.getCreateDate()));
            today.add(entity.getPertime());
            todaycount.add(entity.getCount());
            int hour = entity.getCreateDate().getHours();
            if (yesterdayData.get(hour) == null) {
                yesterday.add(Double.valueOf(0));
                yesterdaycount.add(Long.valueOf(0));
            } else {
                yesterday.add(yesterdayData.get(hour).getPertime());
                yesterdaycount.add(yesterdayData.get(hour).getCount());
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("today", today);
        resultMap.put("yesterday", yesterday);
        resultMap.put("time", time);
        resultMap.put("yesterdaycount", yesterdaycount);
        resultMap.put("todaycount", todaycount);
        jsonResponse.setObj(resultMap);
        return jsonResponse;
    }


    @RequestMapping(value = "/intercol", method = RequestMethod.GET)
    public String getAvgPerTime(Model model,  String hanId) {
        logger.info("跳转到接口响应按天汇总页面。");
        NodeEntity nodeEntity = new NodeEntity();
        nodeEntity.setIsUv(-1);
        nodeEntity.setIsInterface(1);
        List<NodeEntity> nodeEntityList = nodeService.getNodeList(nodeEntity);
        model.addAttribute("nodeEntityList", nodeEntityList);
        model.addAttribute("hanId", hanId);
        return "/monitor/intercollect";
    }


    @RequestMapping(value = "/intercollect", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getInterCollect(String nodeKey, String beginDate, String endDate) {
        logger.info("接口响应对比情况查询。nodekey:" + nodeKey);
        ResponseData jsonResponse = new ResponseData();
        List<AnalyTopologyHourITEntity> list = statisticMonitorService.getAvgPertimeByNodeKeyAndDate(nodeKey, beginDate, endDate);
        Map<String, Object> resultMap = new HashMap<>();
        List<String> time = new ArrayList<>();
        List<Double> avgdata = new ArrayList<>();
        List<Long> itcount = new ArrayList<>();
        for (AnalyTopologyHourITEntity entity : list) {
            time.add(entity.getCreateDateStr());
            avgdata.add(entity.getPertime());
            itcount.add(entity.getCount());
        }
        resultMap.put("time", time);
        resultMap.put("data", avgdata);
        resultMap.put("count", itcount);
        jsonResponse.setObj(resultMap);
        return jsonResponse;
    }

    @RequestMapping(value = "/interRankingList", method = RequestMethod.GET)
    public String interRankingList(Model model) {
        List<Map<String, String>> resultMap = Lists.newArrayList();
        List<NodeEntity> nodes = nodeService.getAllNodeList();
        List<AnalyTopologyHourITEntity> interRankingList = statisticMonitorService.getInterRankingList();
        if (!CollectionUtils.isEmpty(interRankingList) && !CollectionUtils.isEmpty(nodes)) {
            int count = 0;
            for (AnalyTopologyHourITEntity itEntity : interRankingList) {
                count ++;
                HashMap<String, String> objmp = Maps.newHashMap();
                String nodeName = "";
                for (NodeEntity node : nodes) {
                    if (node.getNodeKey().equals(itEntity.getNodeKey())) {
                        nodeName = node.getNodeName();
                    }
                }
                objmp.put("num", String.valueOf(count));
                objmp.put("nodeName", nodeName);
                objmp.put("pertime", String.valueOf(itEntity.getPertime()));
                objmp.put("count", String.valueOf(itEntity.getCount()));
                resultMap.add(objmp);
            }
        }

        model.addAttribute("interRankingList", resultMap);

        return "/monitor/interRankingList";
    }

    @RequestMapping(value = "/interRankingMap", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData interRankingMap() {
        ResponseData jsonResponse = new ResponseData();
        Map<String, Object> resultMap = Maps.newHashMap();
        List<String> time = new ArrayList<>();
        List<String> name = new ArrayList<>();
        List<AnalyTopologyHourITEntity> interRankingList = statisticMonitorService.getInterRankingListAsc();
        List<NodeEntity> nodes = nodeService.getAllNodeList();
        if (!CollectionUtils.isEmpty(interRankingList)) {
            for (AnalyTopologyHourITEntity itEntity : interRankingList) {
                String nodeName = "";
                time.add(String.valueOf(itEntity.getPertime()));
                for (NodeEntity node : nodes) {
                    if (node.getNodeKey().equals(itEntity.getNodeKey())) {
                        nodeName = node.getNodeName();
                    }
                }
                name.add(nodeName);
            }
        }
        resultMap.put("name", name);
        resultMap.put("time", time);
        jsonResponse.setObj(resultMap);
        return jsonResponse;
    }

}
