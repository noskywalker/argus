package com.monitor.argus.mis.controller.monitor;

import com.monitor.argus.bean.alarm.AlarmStrategyEntity;
import com.monitor.argus.bean.monitor.MonitorHostEntity;
import com.monitor.argus.bean.monitor.MonitorStrategyEntity;
import com.monitor.argus.bean.monitor.MonitorSystemEntity;
import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.common.util.*;
import com.monitor.argus.mis.controller.monitor.form.*;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.alarm.IAlarmService;
import com.monitor.argus.service.monitor.IMonitorService;
import com.monitor.argus.common.annotation.Auth;
import com.monitor.argus.common.enums.StrategyType;
import com.monitor.argus.common.model.ResponseData;
import com.monitor.argus.mis.controller.monitor.form.*;
import com.monitor.argus.mis.system.SystemIPJvmInfo;
import com.monitor.argus.mis.system.SystemIPJvmInfoLoad;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;

import static com.monitor.argus.common.enums.StrategyType.BUSINESS_NODE;
import static com.monitor.argus.common.enums.StrategyType.BUSINESS_NODE_PERCENT;

/**
 *
 */
@Controller
@RequestMapping("/monitor")
public class MonitorController {

    private static Logger logger = LoggerFactory.getLogger(MonitorController.class);

    @Autowired
    private IMonitorService monitorService;
    @Autowired
    private IAlarmService alarmService;
    @Autowired
    protected RedisService redisService;
    @Autowired
    private SystemIPJvmInfoLoad systemIPJvmInfoLoad;

    /**
     * 监控管理
     *
     * @param model
     * @return
     * @Author Xue Fei
     * @Date 2016-07-14 上午11:31:54
     * @Version V1.0
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String showAddMonitorPage(Model model) {
        List<AlarmStrategyEntity> alarmStrategy = alarmService.getAlarmStrategy();
        model.addAttribute("addMonitorForm", new AddMonitorForm());
        model.addAttribute("alarmStrategies", alarmStrategy);
        return "/monitor/addMonitor";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseData submitAddMonitorRequest(AddMonitorForm addMonitorForm, Model model, HttpServletRequest request) {
        UserBean userBean = (UserBean) request.getSession().getAttribute(LocalSessionUtil.SEESION_KEY_LOGIN_USER);
        ResponseData jsonResponse = new ResponseData();

        String systemName = addMonitorForm.getSystemName();
        String detail = addMonitorForm.getDetail();

        String id = addMonitorForm.getId();
        MonitorSystemEntity monitorSystemEntity;
        boolean result;
        String msg;
        if (StringUtil.isEmpty(id)) {
            // add monitor system entity
            monitorSystemEntity = new MonitorSystemEntity();
            monitorSystemEntity.setSystemName(systemName);
            monitorSystemEntity.setDetail(detail);
            monitorSystemEntity.setCreator(userBean.getEmail());
            monitorSystemEntity.setIsIp(addMonitorForm.getIsIp());
            result = monitorService.addMonitorSystem(monitorSystemEntity);
            msg = "保存成功";
        } else {
            monitorSystemEntity = monitorService.getMonitorSystemById(id);
            monitorSystemEntity.setSystemName(addMonitorForm.getSystemName());
            monitorSystemEntity.setDetail(addMonitorForm.getDetail());
            monitorSystemEntity.setCreator(userBean.getEmail());
            monitorSystemEntity.setIsIp(addMonitorForm.getIsIp());
            result = monitorService.updateMonitorSystem(monitorSystemEntity);
            msg = "修改成功";
        }

        if (result) {
            List<String> hostIds = monitorService.getHostIdsSystemId(monitorSystemEntity.getId());
            List<String> strategyIds = monitorService.getStrategyIdsByMonitorSystemId(monitorSystemEntity.getId());

            // add monitor host entity
            List<MonitorHostForm> monitorHostFormList = addMonitorForm.getMonitorHostFormList();
            List<MonitorHostEntity> monitorHostEntityList = new ArrayList<>();
            for (MonitorHostForm monitorHostForm : monitorHostFormList) {
                String hostId = monitorHostForm.getId();
                if (!StringUtil.isEmpty(hostId)) {
                    hostIds.remove(hostId);
                }
                MonitorHostEntity monitorHostEntity = new MonitorHostEntity();
                BeanUtil.copyProperties(monitorHostEntity, monitorHostForm);
                monitorHostEntity.setSystemId(monitorSystemEntity.getId());
                monitorHostEntityList.add(monitorHostEntity);
            }

            for (String deleteHostId : hostIds) {
                monitorService.deleteMonitorHostById(deleteHostId);
            }

            monitorService.addMonitorHostBatch(monitorHostEntityList);

            // add monitor strategy entity
            List<MonitorStrategyForm> monitorStrategyFormList = addMonitorForm.getMonitorStrategyFormList();
            List<MonitorStrategyEntity> monitorStrategyEntityList = new ArrayList<>();
            for (MonitorStrategyForm monitorStrategyForm : monitorStrategyFormList) {

                String strategyId = monitorStrategyForm.getId();
                if (!StringUtil.isEmpty(strategyId)) {
                    strategyIds.remove(strategyId);
                }

                MonitorStrategyEntity monitorStrategyEntity = new MonitorStrategyEntity();
                BeanUtil.copyProperties(monitorStrategyEntity, monitorStrategyForm);
                String json_MonitorStrategy1 = "";
                switch (StrategyType.getTypeName(monitorStrategyForm.getStrategyType())) {
                    case KEYWORD:
                        json_MonitorStrategy1 = JsonUtil.beanToJson(monitorStrategyForm.getKeyWordMonitorStrategy());
                        break;
                    case BUSINESS_NODE:
                        json_MonitorStrategy1 = JsonUtil.beanToJson(monitorStrategyForm.getBusinessNodeMonitorStrategy());
                        break;
                    case BUSINESS_NODE_PERCENT:
                        json_MonitorStrategy1 = JsonUtil.beanToJson(monitorStrategyForm.getBusinessNodePercentMonitorStrategy());
                        break;
                    case BUSINESS_NODE_NUMCOMPARE:
                        json_MonitorStrategy1 = JsonUtil.beanToJson(monitorStrategyForm.getBusinessNodeNumCompareMonitorStrategy());
                        break;
                    case JOB:
                        json_MonitorStrategy1 = JsonUtil.beanToJson(monitorStrategyForm.getJobMonitorStrategy());
                        break;
                    case BUSINESS_INTERFACE:
                        json_MonitorStrategy1 = JsonUtil.beanToJson(monitorStrategyForm.getBusinessInterfaceMonitorStrategy());
                        break;
                    default:
                        json_MonitorStrategy1 = "";
                }
                if (json_MonitorStrategy1 == null || "null".equals(json_MonitorStrategy1)) {
                    json_MonitorStrategy1 = "";
                }
                monitorStrategyEntity.setSystemId(monitorSystemEntity.getId());
                monitorStrategyEntity.setMonitorStrategy(json_MonitorStrategy1);
                monitorStrategyEntityList.add(monitorStrategyEntity);
            }

            for (String deleteStrategyId : strategyIds) {
                monitorService.deleteMonitorStrategyById(deleteStrategyId);
            }

            monitorService.addMonitorStrategyBatch(monitorStrategyEntityList);

            jsonResponse.setSuccess(true);
        } else {
            jsonResponse.setSuccess(false);
            msg = "操作失败";
        }
        jsonResponse.setMsg(msg);

        return jsonResponse;
    }

    @RequestMapping(value = "/addnew", method = RequestMethod.GET)
    public String showAddMonitorHostPage(Model model) {
        model.addAttribute("addMonitorForm", new AddMonitorForm());
        return "/monitor/addMonitorNew";
    }

    @RequestMapping(value = "/addnew", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseData submitAddMonitorHostReq(AddMonitorForm addMonitorForm, Model model, HttpServletRequest request) {
        UserBean userBean = (UserBean) request.getSession().getAttribute(LocalSessionUtil.SEESION_KEY_LOGIN_USER);
        ResponseData jsonResponse = new ResponseData();

        String systemName = addMonitorForm.getSystemName();
        String detail = addMonitorForm.getDetail();

        String id = addMonitorForm.getId();
        MonitorSystemEntity monitorSystemEntity;
        boolean result;
        String msg;
        if (StringUtil.isEmpty(id)) {
            // add monitor system entity
            monitorSystemEntity = new MonitorSystemEntity();
            monitorSystemEntity.setSystemName(systemName);
            monitorSystemEntity.setDetail(detail);
            monitorSystemEntity.setCreator(userBean.getEmail());
            monitorSystemEntity.setIsIp(addMonitorForm.getIsIp());
            result = monitorService.addMonitorSystem(monitorSystemEntity);
            msg = "保存成功";
        } else {
            monitorSystemEntity = monitorService.getMonitorSystemById(id);
            monitorSystemEntity.setSystemName(addMonitorForm.getSystemName());
            monitorSystemEntity.setDetail(addMonitorForm.getDetail());
            monitorSystemEntity.setCreator(userBean.getEmail());
            monitorSystemEntity.setIsIp(addMonitorForm.getIsIp());
            result = monitorService.updateMonitorSystem(monitorSystemEntity);
            msg = "修改成功";
        }

        if (result) {
            List<String> hostIds = monitorService.getHostIdsSystemId(monitorSystemEntity.getId());

            // add monitor host entity
            List<MonitorHostForm> monitorHostFormList = addMonitorForm.getMonitorHostFormList();
            List<MonitorHostEntity> monitorHostEntityList = new ArrayList<>();
            for (MonitorHostForm monitorHostForm : monitorHostFormList) {
                String hostId = monitorHostForm.getId();
                if (!StringUtil.isEmpty(hostId)) {
                    hostIds.remove(hostId);
                }
                MonitorHostEntity monitorHostEntity = new MonitorHostEntity();
                BeanUtil.copyProperties(monitorHostEntity, monitorHostForm);
                monitorHostEntity.setSystemId(monitorSystemEntity.getId());
                monitorHostEntityList.add(monitorHostEntity);
            }

            for (String deleteHostId : hostIds) {
                monitorService.deleteMonitorHostById(deleteHostId);
            }

            monitorService.addMonitorHostBatch(monitorHostEntityList);
            jsonResponse.setSuccess(true);
        } else {
            jsonResponse.setSuccess(false);
            msg = "操作失败";
        }
        jsonResponse.setMsg(msg);
        return jsonResponse;
    }

    @RequestMapping(value = "/system/search", method = RequestMethod.GET)
    public String searchMonitorSystem(Model model, String hanId) {
        List<MonitorSystemEntity> monitorSystemEntityList = monitorService.searchMonitorSystem();

        List<SearchMonitorForm> searchMonitorFormList = new ArrayList<>();

        for (MonitorSystemEntity monitorSystemEntity : monitorSystemEntityList) {
            SearchMonitorForm searchMonitorForm = new SearchMonitorForm();
            String monitorSystemId = monitorSystemEntity.getId();

            List<MonitorHostEntity> monitorHostList = monitorService.getMonitorHostByMonitorSystemId(monitorSystemId);
            List<MonitorStrategyEntity> monitorStrategyList = monitorService.getMonitorStrategyByMonitorSystemId(monitorSystemId);
            if (!CollectionUtils.isEmpty(monitorStrategyList)) {
                for (MonitorStrategyEntity monitorStrategyEntity : monitorStrategyList) {
                    AlarmStrategyEntity alarmStrategyEntity = alarmService.getAlarmStrategyById(monitorStrategyEntity.getAlarmId());
                    if (alarmStrategyEntity != null) {
                        monitorStrategyEntity.setAlarmName(alarmStrategyEntity.getAlarmName());
                    } else {
                        monitorStrategyEntity.setAlarmName("");
                    }
                    int strategyStatus = monitorStrategyEntity.getStrategyStatus();
                    if (strategyStatus == 1) {
                        monitorStrategyEntity.setStrategyStatusStr("有效");
                    } else if (strategyStatus == 2) {
                        monitorStrategyEntity.setStrategyStatusStr("无效");
                    } else if (strategyStatus == 3) {
                        monitorStrategyEntity.setStrategyStatusStr("暂停");
                    } else {
                        monitorStrategyEntity.setStrategyStatusStr("");
                    }
                    monitorStrategyEntity.setCreateDateStr(DateUtil.getSimpleDateTimeStr(monitorStrategyEntity.getCreateDate()));
                }
            }

            BeanUtil.copyProperties(searchMonitorForm, monitorSystemEntity);
            searchMonitorForm.setMonitorHostList(monitorHostList);
            searchMonitorForm.setMonitorStrategyList(monitorStrategyList);
            searchMonitorForm.setCreateDateStr(DateUtil.getSimpleDateTimeStr(searchMonitorForm.getCreateDate()));

            searchMonitorFormList.add(searchMonitorForm);
        }

        Collections.sort(searchMonitorFormList, new ComparatorSearchMonitorForm());
        model.addAttribute("searchResult", searchMonitorFormList);
        model.addAttribute("hanId", hanId);
        return "/monitor/searchMonitors";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String showEditMonitorPage(@RequestParam(value = "id", required = true) String systemId, Model model, String hanId) {

        MonitorSystemEntity monitorSystem = monitorService.getMonitorSystemById(systemId);
        List<MonitorHostEntity> monitorHostList = monitorService.getMonitorHostByMonitorSystemId(systemId);

        AddMonitorForm addMonitorForm = new AddMonitorForm();
        BeanUtil.copyProperties(addMonitorForm, monitorSystem);

        List<MonitorHostForm> monitorHostFormList = new ArrayList<>();
        for (MonitorHostEntity monitorHostEntity : monitorHostList) {
            MonitorHostForm monitorHostForm = new MonitorHostForm();
            BeanUtil.copyProperties(monitorHostForm, monitorHostEntity);
            monitorHostFormList.add(monitorHostForm);
        }
        addMonitorForm.setMonitorHostFormList(monitorHostFormList);

        model.addAttribute("addMonitorForm", addMonitorForm);
        model.addAttribute("hanId", hanId);
        return "/monitor/addMonitorNew";
    }

    /**
     * @Description: 系统list
     * @Author:huxiaolei
     */
    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/system/list")
    @ResponseBody
    public ResponseData getSystemList(HttpServletRequest request) throws Exception {
        ResponseData jsonResponse = new ResponseData();
        Object list = monitorService.getAllSystems();
        jsonResponse.setSuccess(true);
        jsonResponse.setObj(list);
        return jsonResponse;
    }

    /**
     * @param sysid
     * @param model
     * @return
     */

    @RequestMapping(value = "/getAllMonitorStrategyBySysid", method = RequestMethod.GET)
    public String getAllMonitorStrategyBySysid(@RequestParam(value = "sysid", required = false) String sysid, Model model, String hanId) {
        MonitorStrategyEntity entity = new MonitorStrategyEntity();
        entity.setSystemId(sysid);
        List<MonitorStrategyEntity> list = monitorService.getStrategyEntityByCondition(entity);
        List<AlarmStrategyEntity> alarmStrategyEntityList = alarmService.getAlarmStrategy();
        List<MonitorSystemEntity> systemEntityList = monitorService.getAllSystems();
        Map<String, String> alarmMap = new HashMap<>();
        Map<String, String> systemMap = new HashMap<>();

        for (AlarmStrategyEntity alarm : alarmStrategyEntityList) {
            alarmMap.put(alarm.getId(), alarm.getAlarmName());
        }
        for (MonitorSystemEntity system : systemEntityList) {
            systemMap.put(system.getId(), system.getSystemName());
        }

        for (MonitorStrategyEntity entity1 : list) {
            entity1.setAlarmName(alarmMap.get(entity1.getAlarmId()));
            entity1.setSystemName(systemMap.get(entity1.getSystemId()));
            entity1.setCreateDateStr(DateUtil.getSimpleDateTimeStr(entity1.getCreateDate()));

        }
        model.addAttribute("searchResults", list);
        model.addAttribute("ylsysid", sysid);
        model.addAttribute("hanId", hanId);
        return "monitor/strategylist";
    }

    @RequestMapping(value = "/changestatus/{id}", method = RequestMethod.GET)
    public String changeStategyStatus(@PathVariable String id, Model model) {
        MonitorStrategyEntity entity = new MonitorStrategyEntity();
        entity.setId(id);
        List<MonitorStrategyEntity> strategy = monitorService.getStrategyEntityByCondition(entity);
        if (strategy != null) {
            String status = strategy.get(0).getStrategyStatus() == 1 ? "2" : "1";
            monitorService.changeStrategyStatus(id, status);
        }
        entity = new MonitorStrategyEntity();
        List<MonitorStrategyEntity> list = monitorService.getStrategyEntityByCondition(entity);
        List<AlarmStrategyEntity> alarmStrategyEntityList = alarmService.getAlarmStrategy();
        Map<String, String> alarmMap = new HashMap<>();
        for (AlarmStrategyEntity alarm : alarmStrategyEntityList) {
            alarmMap.put(alarm.getId(), alarm.getAlarmName());
        }
        for (MonitorStrategyEntity entity1 : list) {
            entity1.setAlarmName(alarmMap.get(entity1.getAlarmId()));
            entity1.setCreateDateStr(DateUtil.getSimpleDateTimeStr(entity1.getCreateDate()));
        }
        model.addAttribute("searchResults", list);
        model.addAttribute("ylsysid", 0);
        return "monitor/strategylist";
    }

    @RequestMapping(value = "/getBusinessNodeMonitorStrategyDetail", method = RequestMethod.GET)
    public String getBusinessNodeMonitorStrategyDetail(Model model) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        List<MonitorStrategyEntity> monitorStrategyEntitys = monitorService.getAllEnableStrategies();
        if (!CollectionUtils.isEmpty(monitorStrategyEntitys)) {
            for (MonitorStrategyEntity monitorStrategyEntity : monitorStrategyEntitys) {
                if (BUSINESS_NODE.equals(StrategyType.getTypeName(monitorStrategyEntity.getStrategyType()))) {
                    HashMap<String, String> result = new HashMap<String, String>();
                    result.put("id", monitorStrategyEntity.getId());
                    result.put("monitorName", monitorStrategyEntity.getMonitorName());
                    result.put("monitorStrategy", monitorStrategyEntity.getMonitorStrategy());
                    result.put("sendContent", monitorStrategyEntity.getSendContent());
                    String queueName = RedisKeyUtils.BusinessNodeKey(monitorStrategyEntity.getId());
                    boolean queueExists = redisService.exists(queueName);
                    result.put("queueExists", String.valueOf(queueExists));
                    long queueSize = redisService.size(queueName);
                    queueSize = queueSize - 1;
                    result.put("queueSize", String.valueOf(queueSize));
                    String queueTheEarliestTimeString = redisService.getFromlist(queueName, -1);
                    String queueTheEarliestTime = "";
                    long diffSecond = 0;
                    if (StringUtils.isNotEmpty(queueTheEarliestTimeString)
                            && !StringUtils.equals("null", queueTheEarliestTimeString)) {
                        long theEarliestTime = Long.parseLong(queueTheEarliestTimeString);
                        queueTheEarliestTime = DateUtil.getDateLongTimePlusStr(new Date(theEarliestTime));
                        diffSecond = (System.currentTimeMillis() - theEarliestTime) / 1000;
                    }
                    result.put("queueTheEarliestTime", queueTheEarliestTime);
                    result.put("diffSecond", String.valueOf(diffSecond));
                    result.put("isSendContent", String.valueOf(monitorStrategyEntity.getIsSendContent()));
                    list.add(result);
                }
            }
        }
        model.addAttribute("searchResults", list);
        return "monitor/businessNodeMonitorStrategyDetail";
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "/clearBusinessNodeMonitorStrategyRedis/{id}", method = RequestMethod.GET)
    public String clearBusinessNodeMonitorStrategyRedis(@PathVariable String id, Model model) {
        // 清理队列锁
        String queueName1 = RedisKeyUtils.BusinessNodeKey(id);
        String key1 = RedisKeyUtils.MONITOR_BNMONITOR_QUEUE_CHECK_PREFIX + queueName1;
        String key2 = RedisKeyUtils.MONITOR_BNMONITOR_INITQUEUE_CHECK_PREFIX + queueName1;
        redisService.delete(key1);
        redisService.delete(key2);
        logger.info("清理队列{}的锁{},{}", queueName1, key1, key2);

        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        List<MonitorStrategyEntity> monitorStrategyEntitys = monitorService.getAllEnableStrategies();
        if (!CollectionUtils.isEmpty(monitorStrategyEntitys)) {
            for (MonitorStrategyEntity monitorStrategyEntity : monitorStrategyEntitys) {
                if (BUSINESS_NODE.equals(StrategyType.getTypeName(monitorStrategyEntity.getStrategyType()))) {
                    HashMap<String, String> result = new HashMap<String, String>();
                    result.put("id", monitorStrategyEntity.getId());
                    result.put("monitorName", monitorStrategyEntity.getMonitorName());
                    result.put("monitorStrategy", monitorStrategyEntity.getMonitorStrategy());
                    result.put("sendContent", monitorStrategyEntity.getSendContent());
                    String queueName = RedisKeyUtils.BusinessNodeKey(monitorStrategyEntity.getId());
                    boolean queueExists = redisService.exists(queueName);
                    result.put("queueExists", String.valueOf(queueExists));
                    long queueSize = redisService.size(queueName);
                    queueSize = queueSize - 1;
                    result.put("queueSize", String.valueOf(queueSize));
                    String queueTheEarliestTimeString = redisService.getFromlist(queueName, -1);
                    String queueTheEarliestTime = "";
                    long diffSecond = 0;
                    if (StringUtils.isNotEmpty(queueTheEarliestTimeString)
                            && !StringUtils.equals("null", queueTheEarliestTimeString)) {
                        long theEarliestTime = Long.parseLong(queueTheEarliestTimeString);
                        queueTheEarliestTime = DateUtil.getDateLongTimePlusStr(new Date(theEarliestTime));
                        diffSecond = (System.currentTimeMillis() - theEarliestTime) / 1000;
                    }
                    result.put("queueTheEarliestTime", queueTheEarliestTime);
                    result.put("diffSecond", String.valueOf(diffSecond));
                    list.add(result);
                }
            }
        }
        model.addAttribute("searchResults", list);
        return "monitor/businessNodeMonitorStrategyDetail";

    }

    @RequestMapping(value = "/getBusinessNodePercentMonitorStrategyDetail", method = RequestMethod.GET)
    public String getBusinessNodePercentMonitorStrategyDetail(Model model) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        List<MonitorStrategyEntity> monitorStrategyEntitys = monitorService.getAllEnableStrategies();
        if (!CollectionUtils.isEmpty(monitorStrategyEntitys)) {
            for (MonitorStrategyEntity monitorStrategyEntity : monitorStrategyEntitys) {
                if (BUSINESS_NODE_PERCENT.equals(StrategyType.getTypeName(monitorStrategyEntity.getStrategyType()))) {
                    HashMap<String, String> result = new HashMap<String, String>();
                    result.put("id", monitorStrategyEntity.getId());
                    result.put("monitorName", monitorStrategyEntity.getMonitorName());
                    result.put("monitorStrategy", monitorStrategyEntity.getMonitorStrategy());
                    result.put("sendContent", monitorStrategyEntity.getSendContent());
                    String queueName = RedisKeyUtils.BusinessNodeKey(monitorStrategyEntity.getId());
                    String queueNameFrac = ArgusUtils.BUS_NODE_FRAC_PREFIX + queueName;
                    String queueNameNume = ArgusUtils.BUS_NODE_NUME_PREFIX + queueName;
                    boolean queueNameFracExists = redisService.exists(queueNameFrac);
                    boolean queueNameNumeExists = redisService.exists(queueNameNume);
                    result.put("queueNameFracExists", String.valueOf(queueNameFracExists));
                    result.put("queueNameNumeExists", String.valueOf(queueNameNumeExists));
                    long queueNameFracSize = redisService.size(queueNameFrac) - 1;
                    long queueNameNumeSize = redisService.size(queueNameNume) - 1;
                    DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                    double resultdata = 0;
                    // 精度计算
                    if (queueNameFracSize > 0 && queueNameNumeSize > 0) {
                        if (queueNameNumeSize > queueNameFracSize) {
                            resultdata = (double) queueNameFracSize / (double) queueNameNumeSize;
                        } else {
                            resultdata = 1;
                        }
                    }

                    String resultdataStr = decimalFormat.format(resultdata);
                    String resultdataPer = String.valueOf(Double.valueOf(resultdataStr) * 100);
                    result.put("resultPercent", resultdataPer);
                    result.put("queueNameFracSize", String.valueOf(queueNameFracSize));
                    result.put("queueNameNumeSize", String.valueOf(queueNameNumeSize));
                    String queueTheEarliestTimeString = redisService.getFromlist(queueNameFrac, -1);
                    String queueTheEarliestTime = "";
                    long diffSecond = 0;
                    if (StringUtils.isNotEmpty(queueTheEarliestTimeString)
                            && !StringUtils.equals("null", queueTheEarliestTimeString)) {
                        long theEarliestTime = Long.parseLong(queueTheEarliestTimeString);
                        queueTheEarliestTime = DateUtil.getDateLongTimePlusStr(new Date(theEarliestTime));
                        diffSecond = (System.currentTimeMillis() - theEarliestTime) / 1000;
                    }
                    result.put("queueTheEarliestTime", queueTheEarliestTime);
                    result.put("diffSecond", String.valueOf(diffSecond));
                    result.put("isSendContent", String.valueOf(monitorStrategyEntity.getIsSendContent()));
                    list.add(result);
                }
            }
        }
        model.addAttribute("searchResults", list);
        return "monitor/businessNodePercentMonitorStrategyDetail";
    }


    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "/clearBusinessNodePercentMonitorStrategyRedis/{id}", method = RequestMethod.GET)
    public String clearBusinessNodePercentMonitorStrategyRedis(@PathVariable String id, Model model) {
        // 清理队列锁
        String queueName1 = RedisKeyUtils.BusinessNodeKey(id);
        String queueNameFrac1 = ArgusUtils.BUS_NODE_FRAC_PREFIX + queueName1;
        String queueNameNume1 = ArgusUtils.BUS_NODE_NUME_PREFIX + queueName1;

        String key1 = RedisKeyUtils.MONITOR_BNMONITOR_QUEUE_CHECK_PREFIX + queueName1;
        String key2 = RedisKeyUtils.MONITOR_BNMONITOR_INITQUEUE_CHECK_PREFIX + queueNameFrac1;
        String key3 = RedisKeyUtils.MONITOR_BNMONITOR_INITQUEUE_CHECK_PREFIX + queueNameNume1;

        redisService.delete(key1);
        redisService.delete(key2);
        redisService.delete(key3);
        logger.info("清理队列{}的锁{},{},{}", queueName1, key1, key2, key3);

        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        List<MonitorStrategyEntity> monitorStrategyEntitys = monitorService.getAllEnableStrategies();
        if (!CollectionUtils.isEmpty(monitorStrategyEntitys)) {
            for (MonitorStrategyEntity monitorStrategyEntity : monitorStrategyEntitys) {
                if (BUSINESS_NODE_PERCENT.equals(StrategyType.getTypeName(monitorStrategyEntity.getStrategyType()))) {
                    HashMap<String, String> result = new HashMap<String, String>();
                    result.put("id", monitorStrategyEntity.getId());
                    result.put("monitorName", monitorStrategyEntity.getMonitorName());
                    result.put("monitorStrategy", monitorStrategyEntity.getMonitorStrategy());
                    result.put("sendContent", monitorStrategyEntity.getSendContent());
                    String queueName = RedisKeyUtils.BusinessNodeKey(monitorStrategyEntity.getId());
                    String queueNameFrac = ArgusUtils.BUS_NODE_FRAC_PREFIX + queueName;
                    String queueNameNume = ArgusUtils.BUS_NODE_NUME_PREFIX + queueName;
                    boolean queueNameFracExists = redisService.exists(queueNameFrac);
                    boolean queueNameNumeExists = redisService.exists(queueNameNume);
                    result.put("queueNameFracExists", String.valueOf(queueNameFracExists));
                    result.put("queueNameNumeExists", String.valueOf(queueNameNumeExists));
                    long queueNameFracSize = redisService.size(queueNameFrac) - 1;
                    long queueNameNumeSize = redisService.size(queueNameNume) - 1;
                    DecimalFormat decimalFormat = new DecimalFormat("######0.00");
                    double resultdata = 0;
                    // 精度计算
                    if (queueNameFracSize > 0 && queueNameNumeSize > 0) {
                        if (queueNameNumeSize > queueNameFracSize) {
                            resultdata = (double) queueNameFracSize / (double) queueNameNumeSize;
                        } else {
                            resultdata = 1;
                        }
                    }

                    String resultdataStr = decimalFormat.format(resultdata);
                    String resultdataPer = String.valueOf(Double.valueOf(resultdataStr) * 100);
                    result.put("resultPercent", resultdataPer);
                    result.put("queueNameFracSize", String.valueOf(queueNameFracSize));
                    result.put("queueNameNumeSize", String.valueOf(queueNameNumeSize));
                    String queueTheEarliestTimeString = redisService.getFromlist(queueNameFrac, -1);
                    String queueTheEarliestTime = "";
                    long diffSecond = 0;
                    if (StringUtils.isNotEmpty(queueTheEarliestTimeString)
                            && !StringUtils.equals("null", queueTheEarliestTimeString)) {
                        long theEarliestTime = Long.parseLong(queueTheEarliestTimeString);
                        queueTheEarliestTime = DateUtil.getDateLongTimePlusStr(new Date(theEarliestTime));
                        diffSecond = (System.currentTimeMillis() - theEarliestTime) / 1000;
                    }
                    result.put("queueTheEarliestTime", queueTheEarliestTime);
                    result.put("diffSecond", String.valueOf(diffSecond));
                    result.put("isSendContent", String.valueOf(monitorStrategyEntity.getIsSendContent()));
                    list.add(result);
                }
            }
        }
        model.addAttribute("searchResults", list);
        return "monitor/businessNodePercentMonitorStrategyDetail";
    }

    @RequestMapping(value = "/getAllMonitorQueueDetail", method = RequestMethod.GET)
    public String getAllMonitorQueueDetail(Model model) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        List<MonitorStrategyEntity> allStrategies = monitorService.getAllEnableStrategies();
        List<MonitorHostEntity> allHosts = monitorService.getAllHosts();
        if (!CollectionUtils.isEmpty(allHosts) && !CollectionUtils.isEmpty(allStrategies)) {
            for (MonitorHostEntity mhost : allHosts) {
                for (MonitorStrategyEntity mStrategy : allStrategies) {
                    if (!StringUtil.isEmpty(mhost.getSystemId()) && !StringUtil.isEmpty(mStrategy.getSystemId())
                            && mhost.getSystemId().equals(mStrategy.getSystemId())) {
                        String queueKey = RedisKeyUtils.alarmQueueKey(mhost.getIp(), mStrategy.getId());
                        if (!StringUtil.isEmpty(queueKey)) {
                            Long alarm = redisService.size(queueKey);
                            HashMap<String, String> result = new HashMap<String, String>();
                            result.put("queueKey", queueKey);
                            result.put("queueCount", alarm.toString());
                            list.add(result);
                        }
                    }
                }
            }
        }
        model.addAttribute("searchResults", list);
        return "monitor/allMonitorQueueDetail";
    }

    @RequestMapping(value = "/appendJvmInfo", method = RequestMethod.GET)
    public String appendJvmInfo(Model model) {
        SystemIPJvmInfo systemIPJvmInfo = systemIPJvmInfoLoad.appendJvmInfo();
        model.addAttribute("systemIPJvmInfo", systemIPJvmInfo);
        return "monitor/jvmInfoDetail";
    }

}
