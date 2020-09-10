package com.monitor.argus.mis.controller.alarm;

import com.monitor.argus.bean.alarm.AlarmStrategyEntity;
import com.monitor.argus.bean.alarm.vo.AlarmInfoVO;
import com.monitor.argus.bean.log.AlarmEntityDTO;
import com.monitor.argus.common.util.*;
import com.monitor.argus.mis.controller.alarm.form.SearchAlarmForm;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.alarm.IAlarmService;
import com.monitor.argus.common.annotation.Auth;
import com.monitor.argus.common.enums.AlarmLevel;
import com.monitor.argus.common.model.DataTable;
import com.monitor.argus.common.model.PageHelper;
import com.monitor.argus.common.model.ResponseData;
import com.monitor.argus.mis.controller.alarm.form.AddAlarmStrategyForm;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.monitor.argus.common.util.RedisKeyUtils.ARGUS_QUEUE_LAST_ALARM_KEY;

/**
 * 监控管理
 *
 * @Author xuefei
 * @Date 7/13/16
 * @Version
 */
@Controller
@RequestMapping("/alarm")
public class AlarmController {

    private static Logger logger = LoggerFactory.getLogger(AlarmController.class);

    @Autowired
    private IAlarmService alarmService;
    @Autowired
    protected RedisService redisService;

    /**
     * 显示添加监控页面
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String showAddAlarmPage(Model model) {
        model.addAttribute("addAlarmStrategyForm", new AddAlarmStrategyForm());
        return "/alarm/addAlarmStrategy";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    ResponseData submitAddAlarmStrategyRequest(AddAlarmStrategyForm addAlarmStrategyForm, Model model) {
        ResponseData jsonResponse = new ResponseData();
        AlarmStrategyEntity alarmStrategyEntity = new AlarmStrategyEntity();
        BeanUtil.copyProperties(alarmStrategyEntity, addAlarmStrategyForm);
        alarmStrategyEntity.setAlarmStrategy("{}");
        jsonResponse.setSuccess(true);
        if (StringUtil.isEmpty(alarmStrategyEntity.getId())) {
            alarmService.addAlarmStrategy(alarmStrategyEntity);
            jsonResponse.setMsg("保存成功!");
        } else {
            alarmService.editAlarmStrategy(alarmStrategyEntity);
            jsonResponse.setMsg("修改成功!");
        }
        return jsonResponse;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String searchAlarms(SearchAlarmForm searchAlarmForm, Model model) {
//        List<AlarmInfoVO> alarmInfoVOList = alarmService.searchAlarmsInfo();
//        model.addAttribute("searchResult", alarmInfoVOList);
        return "/alarm/searchAlarms";
    }


    @RequestMapping(value = "/loadAlarmData", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public DataTable<T> loadAlarmData(int draw, int start, int length, Model model) {

        PageHelper pageHelper = new PageHelper();
        pageHelper.setPage(start / length + 1);
        pageHelper.setRows(length);
        Long pageCount = alarmService.getPageCount();
        List<AlarmInfoVO> alarmInfoVOs = alarmService.searchAlarmsInfo(pageHelper);

        DataTable dataTable = new DataTable();
        dataTable.setData(alarmInfoVOs);
        dataTable.setDraw(draw);
        dataTable.setRecordsFiltered(pageCount);
        dataTable.setRecordsTotal(pageCount);

        return dataTable;

    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String showEditAlarmStrategyPage(@PathVariable String id, Model model) {
        AlarmStrategyEntity alarmStrategyEntity = alarmService.getAlarmStrategyById(id);
        AlarmStrategyEntity alarmStrategy = new AlarmStrategyEntity();
        BeanUtil.copyProperties(alarmStrategy, alarmStrategyEntity);
        model.addAttribute("addAlarmStrategyForm", alarmStrategy);
        return "/alarm/addAlarmStrategy";
    }

    @RequestMapping(value = "/getAllAlarmStrategy", method = RequestMethod.GET)
    public String getAllAlarmStrategy(Model model) {
        List<AlarmStrategyEntity> alarmStrategyEntityList = alarmService.getAlarmStrategy();
        model.addAttribute("searchResults", alarmStrategyEntityList);
        return "/alarm/alarmStrategy";
    }

    public String delete(@PathVariable String id, Model model) {
        logger.info("Delete AlarmStrategy.id={}");
        ResponseData jsonResponse = new ResponseData();
        boolean flag = alarmService.deleteAlarmStrategy(id);
        jsonResponse.setSuccess(flag);
        if (flag) {
            List<AlarmStrategyEntity> alarmStrategyEntityList = alarmService.getAlarmStrategy();
            model.addAttribute("searchResults", alarmStrategyEntityList);
            return "/alarm/alarmStrategy";
        } else {
            jsonResponse.setMsg("出现了未知错误。");
            return jsonResponse.getMsg();
        }
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/shield")
    @ResponseBody
    public ResponseData shieldAlarm(@RequestParam("alarmId") String alarmId, @RequestParam("openId") String openId, @RequestParam("hours") String hours) {
        logger.info("微信屏蔽报警,alarmId={},openId={},屏蔽时长为{}", alarmId, openId, hours);
        alarmService.shieldAlarm(alarmId, openId, hours);
        ResponseData jsonResponse = new ResponseData();
        return jsonResponse;
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping("/deleteshield")
    @ResponseBody
    public ResponseData deleteShieldAlarm(@RequestParam("alarmId") String alarmId, @RequestParam("openId") String openId) {
        logger.info("取消微信屏蔽报警,alarmId={},openId={}", alarmId, openId);
        alarmService.deleteShieldAlarm(alarmId, openId);
        ResponseData jsonResponse = new ResponseData();
        return jsonResponse;
    }

    @RequestMapping(value = "/getAlarmNotifyQueInfo", method = RequestMethod.GET)
    public String getBusinessNodePercentMonitorStrategyDetail(Model model) {
        HashMap<String, String> notifyQueMap = new HashMap<>();
        Long alarm = redisService.size(ARGUS_QUEUE_LAST_ALARM_KEY);
        notifyQueMap.put("notifyQueSize", alarm.toString());
        model.addAttribute("searchResults", notifyQueMap);
        return "alarm/alarmNotifyQueInfo";
    }

    @RequestMapping(value = "/testAlarmInfo", method = RequestMethod.GET)
    public String testAlarmInfo(Model model) {
        return "alarm/testAlarmInfo";
    }

    @RequestMapping(value = "/sendTestAlarm")
    @ResponseBody
    public ResponseData sendTestAlarm(@RequestParam("mesg") String mesg, @RequestParam("sysname") String sysname, @RequestParam("alid") String alid) {
        ResponseData jsonResponse = new ResponseData();
        try {
            String uuid = UuidUtil.getUUID();
            String ipstr = IpUtil.localIp();
            String ip = ipstr;
            String hostName = ipstr;
            String monitorId = alid;
            String monitorName = alid;
            String oId = uuid;
            String msg = mesg;
            String systemName = sysname;
            String alarmId = alid;
            String queueKey = RedisKeyUtils.alarmQueueKey(ip, monitorId);
            logger.info("手工推送报警信息queueKey:{}", queueKey);

            AlarmStrategyEntity alarmStrategyEntity = alarmService.getAlarmStrategyById(alarmId);
            if (alarmStrategyEntity != null && !StringUtil.isEmpty(alarmStrategyEntity.getAlarmName())) {

                AlarmEntityDTO alarmDto = getAlarmEntityDTO(hostName, ip, oId, systemName, monitorName, monitorId, alarmId, msg);
                redisService.lpush(queueKey, JsonUtil.beanToJson(alarmDto));
                redisService.expire(queueKey, ArgusUtils.ALARM_TEST_QUEUE_EXPIRE_SECONDS);
                shouldAddOrUpdateIntoAlarmQueueName(queueKey);
                jsonResponse.setSuccess(true);
                jsonResponse.setMsg("发送成功");
            } else {
                jsonResponse.setSuccess(false);
                jsonResponse.setMsg("发送失败--报警策略不存在");
            }

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("发送失败--" + e.getMessage());
        }
        return jsonResponse;
    }

    private void shouldAddOrUpdateIntoAlarmQueueName(String alarmQueueName) {
        redisService.hset(RedisKeyUtils.ALARM_QUEUE_NAME, alarmQueueName,
                DateUtil.getDateLongTimePlusStr(new java.util.Date()));
    }

    private AlarmEntityDTO getAlarmEntityDTO(String hostName, String ip, String oId,
                                             String systemName, String monitorName, String monitorId, String alarmId, String msg) {
        AlarmEntityDTO alarmDto = new AlarmEntityDTO();
        alarmDto.setHostName(hostName);
        alarmDto.setIp(ip);
        alarmDto.setOperateId(oId);
        alarmDto.setTimeStamp(System.currentTimeMillis() + "");
        alarmDto.setEndTime(DateUtil.getDateLongTimePlusStr(new Date()));
        alarmDto.setBeginTime(DateUtil.getDateLongTimePlusStr(new Date()));
        alarmDto.setSystemName(systemName);
        alarmDto.setMonitorStrategyName(monitorName);
        alarmDto.setMonitorStrategyId(monitorId);
        alarmDto.setLevel(AlarmLevel.NORMAL);
        alarmDto.setAlarmId(alarmId);
        alarmDto.setMessage(msg);
        return alarmDto;
    }

}
