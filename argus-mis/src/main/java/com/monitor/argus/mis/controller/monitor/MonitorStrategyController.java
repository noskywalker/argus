package com.monitor.argus.mis.controller.monitor;

import com.monitor.argus.bean.alarm.AlarmStrategyEntity;
import com.monitor.argus.bean.monitor.MonitorStrategyEntity;
import com.monitor.argus.bean.monitor.MonitorSystemEntity;
import com.monitor.argus.mis.controller.monitor.form.*;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.alarm.IAlarmService;
import com.monitor.argus.service.monitor.IMonitorService;
import com.monitor.argus.common.enums.StrategyType;
import com.monitor.argus.common.util.BeanUtil;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.mis.controller.monitor.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by wangfeng on 16/10/17.
 */
@Controller
@RequestMapping("/monitorStrategy")
public class MonitorStrategyController {

    private static Logger logger = LoggerFactory.getLogger(MonitorStrategyController.class);

    @Autowired
    private IMonitorService monitorService;
    @Autowired
    private IAlarmService alarmService;
    @Autowired
    protected RedisService redisService;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String showAddMonitorPage(Model model) {
        List<AlarmStrategyEntity> alarmStrategy = alarmService.getAlarmStrategy();
        List<MonitorSystemEntity> monitorSystem = monitorService.getAllSystems();
        model.addAttribute("monitorStrategyForm", new MonitorStrategyForm());
        model.addAttribute("alarmStrategies", alarmStrategy);
        model.addAttribute("monitorSystem", monitorSystem);
        return "/monitor/addStrategy";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    public String submitAddMonitorRequest(MonitorStrategyForm monitorStrategyForm, Model model) {
        MonitorStrategyEntity monitorStrategyEntity = new MonitorStrategyEntity();
        BeanUtil.copyProperties(monitorStrategyEntity, monitorStrategyForm);
        String strategyJson = "";
        switch (StrategyType.getTypeName(monitorStrategyForm.getStrategyType())) {
            case KEYWORD:
                strategyJson = JsonUtil.beanToJson(monitorStrategyForm.getKeyWordMonitorStrategy());
                break;
            case BUSINESS_NODE:
                strategyJson = JsonUtil.beanToJson(monitorStrategyForm.getBusinessNodeMonitorStrategy());
                break;
            case BUSINESS_NODE_PERCENT:
                strategyJson = JsonUtil.beanToJson(monitorStrategyForm.getBusinessNodePercentMonitorStrategy());
                break;
            case BUSINESS_NODE_NUMCOMPARE:
                strategyJson = JsonUtil.beanToJson(monitorStrategyForm.getBusinessNodeNumCompareMonitorStrategy());
                break;
            case JOB:
                strategyJson = JsonUtil.beanToJson(monitorStrategyForm.getJobMonitorStrategy());
            case BUSINESS_INTERFACE:
                strategyJson = JsonUtil.beanToJson(monitorStrategyForm.getBusinessInterfaceMonitorStrategy());
                break;
            default:
                strategyJson = "";
        }
        if (strategyJson == null || "null".equals(strategyJson)) {
            strategyJson = "";
        }
        monitorStrategyEntity.setMonitorStrategy(strategyJson);
        if (StringUtil.isEmpty(monitorStrategyEntity.getId())) {
            // add monitor strategy entity
            monitorService.addMonitorStrategy(monitorStrategyEntity);
        } else {
            monitorService.updateMonitorStrategy(monitorStrategyEntity);
        }
        return "redirect:/monitor/getAllMonitorStrategyBySysid";
    }

    @RequestMapping(value = "/edit/{strategyId}", method = RequestMethod.GET)
    public String showEditMonitorPage(@PathVariable String strategyId, Model model) {
        MonitorStrategyEntity entity = monitorService.getStrategyEntityById(strategyId);
        MonitorStrategyForm monitorStrategyForm = new MonitorStrategyForm();
        BeanUtil.copyProperties(monitorStrategyForm, entity);

        String monitorContent = entity.getMonitorStrategy();

        switch (StrategyType.getTypeName(entity.getStrategyType())) {
            case KEYWORD:
                KeyWordMonitorStrategy keyWordMonitorStrategy = (KeyWordMonitorStrategy) JsonUtil.jsonToBean(monitorContent, KeyWordMonitorStrategy.class);
                monitorStrategyForm.setKeyWordMonitorStrategy(keyWordMonitorStrategy);
                break;
            case BUSINESS_NODE:
                BusinessNodeMonitorStrategy businessNodeMonitorStrategy = (BusinessNodeMonitorStrategy) JsonUtil.jsonToBean(monitorContent, BusinessNodeMonitorStrategy.class);
                monitorStrategyForm.setBusinessNodeMonitorStrategy(businessNodeMonitorStrategy);
                break;
            case BUSINESS_NODE_PERCENT:
                BusinessNodePercentMonitorStrategy businessNodePercentMonitorStrategy = (BusinessNodePercentMonitorStrategy) JsonUtil.jsonToBean(monitorContent, BusinessNodePercentMonitorStrategy.class);
                monitorStrategyForm.setBusinessNodePercentMonitorStrategy(businessNodePercentMonitorStrategy);
                break;
            case BUSINESS_NODE_NUMCOMPARE:
                BusinessNodeNumCompareMonitorStrategy businessNodeNumCompareMonitorStrategy = (BusinessNodeNumCompareMonitorStrategy) JsonUtil.jsonToBean(monitorContent, BusinessNodeNumCompareMonitorStrategy.class);
                monitorStrategyForm.setBusinessNodeNumCompareMonitorStrategy(businessNodeNumCompareMonitorStrategy);
                break;
            case JOB:
                JobMonitorStrategy jobMonitorStrategy = (JobMonitorStrategy) JsonUtil.jsonToBean(monitorContent, JobMonitorStrategy.class);
                monitorStrategyForm.setJobMonitorStrategy(jobMonitorStrategy);
                break;
            case BUSINESS_INTERFACE:
                BusinessInterfaceMonitorStrategy businessInterfaceMonitorStrategy = (BusinessInterfaceMonitorStrategy) JsonUtil.jsonToBean(monitorContent, BusinessInterfaceMonitorStrategy.class);
                monitorStrategyForm.setBusinessInterfaceMonitorStrategy(businessInterfaceMonitorStrategy);
                break;
            default:
        }
        model.addAttribute("monitorStrategyForm", monitorStrategyForm);
        List<AlarmStrategyEntity> alarmStrategy = alarmService.getAlarmStrategy();
        List<MonitorSystemEntity> monitorSystem = monitorService.getAllSystems();
        model.addAttribute("alarmStrategies", alarmStrategy);
        model.addAttribute("monitorSystem", monitorSystem);
        return "/monitor/addStrategy";
    }

}
