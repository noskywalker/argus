package com.monitor.argus.mis.controller.node;

import com.monitor.argus.bean.monitor.MonitorSystemEntity;
import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.bean.node.TopoAnalyEntity;
import com.monitor.argus.bean.system.UserBean;
import com.monitor.argus.common.util.*;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.monitor.IMonitorService;
import com.monitor.argus.service.node.IMonitorNodeService;
import com.monitor.argus.service.system.IUserRedisService;
import com.monitor.argus.common.annotation.Auth;
import com.monitor.argus.common.model.ResponseData;
import com.monitor.argus.mis.controller.node.form.AddNodeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by wangfeng on 16/9/19.
 */
@Controller
@RequestMapping("/node")
public class NodeController {
    private static Logger logger = LoggerFactory.getLogger(NodeController.class);

    @Autowired
    IMonitorNodeService nodeService;
    @Autowired
    private IUserRedisService userRedisService;
    @Autowired
    IMonitorService monitorService;
    @Autowired
    RedisService redisService;

    /**
     * 监控节点管理
     *
     * @param model
     * @return
     * @Author He Ting
     * @Version V1.0
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String showAddMonitorPage(Model model) {
        List<MonitorSystemEntity> list = monitorService.searchMonitorSystem();
        model.addAttribute("systemList", list);
        model.addAttribute("addNodeForm", new AddNodeForm());
        return "node/addNode";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    ResponseData submitAddNodeRequest(AddNodeForm nodeEntity, Model model, HttpServletRequest request) {
        UserBean operator = userRedisService.getUserBeanSessionLocal(request);
        logger.info("addNode 新增节点\n\t@by : " + operator.getId() + "(" + operator.getUserName()
                + ") Start.Json:" + JsonUtil.beanToJson(nodeEntity));
        ResponseData jsonResponse = new ResponseData();
        NodeEntity entity = new NodeEntity();
        BeanUtil.copyProperties(entity, nodeEntity);
        entity.setEnable(nodeEntity.isEnable() ? "0" : "1");
        String id = nodeEntity.getId();
        try {
            if (StringUtil.isEmpty(id)) {
                nodeService.insertNode(entity);
                jsonResponse.setMsg("保存成功");
            } else {
                nodeService.updateNode(entity);
                jsonResponse.setMsg("修改成功");
            }
        } catch (Exception e) {
            jsonResponse.setMsg(e.getMessage());
            jsonResponse.setSuccess(false);
        }
        return jsonResponse;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String showEditNodePage(@PathVariable String id, Model model) {
        NodeEntity nodeEntity = nodeService.getNodeByKey(id);
        AddNodeForm nodeForm = new AddNodeForm();
        BeanUtil.copyProperties(nodeForm, nodeEntity);
        nodeForm.setEnable(Integer.parseInt(nodeEntity.getEnable()) == 0);
        nodeForm.setEnableHidden(nodeForm.isEnable());
        List<MonitorSystemEntity> list = monitorService.searchMonitorSystem();
        model.addAttribute("systemList", list);
        model.addAttribute("addNodeForm", nodeForm);
        return "node/addNode";
    }

    @RequestMapping(value = "/getAllNode", method = RequestMethod.GET)
    public String getAllAlarmStrategy(Model model, String hanId) {
        List<NodeEntity> list = nodeService.getAllNodeList();
        if (!CollectionUtils.isEmpty(list)) {
            for (NodeEntity nodeEntity : list) {
                List<String> statisticsInfos = redisService.hmget(RedisKeyUtils.ARGUS_ANALYSIS_STATISTICS_HZ_KEY,
                        nodeEntity.getNodeKey());
                if (!CollectionUtils.isEmpty(statisticsInfos)) {
                    nodeEntity.setTimeNum(statisticsInfos.get(0));
                }
            }
        }
        model.addAttribute("searchResults", list);
        model.addAttribute("ylsysid", "");
        model.addAttribute("hanId", hanId);
        return "node/nodelist";
    }

    @RequestMapping(value = "/getAllNodeBySysid", method = RequestMethod.GET)
    public String getAllNodeBySysid(@RequestParam(value = "sysid", required = false) String sysid, Model model,
                                    String hanId, String isInt) {

        NodeEntity nodeEntityF = new NodeEntity();
        if (!StringUtil.isEmpty(sysid)) {
            nodeEntityF.setNodeSystemId(sysid);
        }
        if (!StringUtil.isEmpty(isInt)) {
            nodeEntityF.setIsInterface(Integer.parseInt(isInt));
        }
        nodeEntityF.setIsUv(-1);
        List<NodeEntity> list = nodeService.getNodeList(nodeEntityF);
        if (!CollectionUtils.isEmpty(list)) {
            for (NodeEntity nodeEntity : list) {
                List<String> statisticsInfos = redisService.hmget(RedisKeyUtils.ARGUS_ANALYSIS_STATISTICS_HZ_KEY,
                        nodeEntity.getNodeKey());
                if (!CollectionUtils.isEmpty(statisticsInfos)) {
                    nodeEntity.setTimeNum(statisticsInfos.get(0));
                }
            }
        }

        model.addAttribute("searchResults", list);
        model.addAttribute("ylsysid", sysid);
        model.addAttribute("isInt", isInt);
        model.addAttribute("hanId", hanId);
        return "node/nodelist";
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "/sumnode", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getSumNodeByDay(@RequestParam(value = "nodeKey", required = false) String nodeKey) {
        ResponseData jsonResponse = new ResponseData();
        Map<String, List<TopoAnalyEntity>> mmap = nodeService.selectAnalyByMinutes(nodeKey);
        Map<String, List<TopoAnalyEntity>> hmap = nodeService.selectAnalyByHours(nodeKey);
        List<TopoAnalyEntity> daylist = nodeService.selectAnalyByDays(nodeKey);
        List<String> mtoday = new ArrayList<>();
        List<String> myesterday = new ArrayList<>();
        List<String> mtime = new ArrayList<>();
        List<String> htoday = new ArrayList<>();
        List<String> hyesterday = new ArrayList<>();
        List<String> htime = new ArrayList<>();
        List<String> ddata = new ArrayList<>();
        List<String> dtime = new ArrayList<>();

        if (mmap != null) {
            if (mmap.get("todayList") != null) {
                for (TopoAnalyEntity entity : mmap.get("todayList")) {
                    mtoday.add(entity.getDiffLogCount());
                    mtime.add(entity.getCreateDateStr());
                }
            }
            if (mmap.get("yesterdayList") != null) {
                for (TopoAnalyEntity entity : mmap.get("yesterdayList")) {
                    myesterday.add(entity.getDiffLogCount());
                }
            }
        }
        if (hmap != null) {
            if (hmap.get("todayList") != null) {
                for (TopoAnalyEntity entity : hmap.get("todayList")) {
                    htoday.add(entity.getDiffLogCount());
                    htime.add(entity.getCreateDateStr());
                }
            }
            if (hmap.get("yesterdayList") != null) {
                for (TopoAnalyEntity entity : hmap.get("yesterdayList")) {
                    hyesterday.add(entity.getDiffLogCount());
                }
            }
        }

        if (daylist != null && daylist.size() > 0) {
            for (TopoAnalyEntity entity : daylist) {
                ddata.add(entity.getDiffLogCount());
                dtime.add(entity.getCreateDateStr());
            }
        }
        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put("mtoday", mtoday);
        mapResult.put("myesterday", myesterday);
        mapResult.put("mtime", mtime);
        mapResult.put("htoday", htoday);
        mapResult.put("hyesterday", hyesterday);
        mapResult.put("htime", htime);
        mapResult.put("ddata", ddata);
        mapResult.put("dtime", dtime);
        jsonResponse.setObj(mapResult);
        return jsonResponse;
    }

    @RequestMapping(value = "/getEnableAllNode", method = RequestMethod.GET)
    @ResponseBody
    public ResponseData getAllEnableNodeListFromDB() {
        ResponseData jsonResponse = new ResponseData();
        List<NodeEntity> list = nodeService.getAllEnableNodeList();
        jsonResponse.setObj(list);
        return jsonResponse;
    }

    @RequestMapping(value = "/analynode", method = RequestMethod.GET)
    public String showAnaly(Model model) {
        List<NodeEntity> list = nodeService.getAllEnableNodeList();
        model.addAttribute("nodelist", list);
        return "node/analyNode";
    }

    @RequestMapping(value = "/getNodeUVQueueDetail", method = RequestMethod.GET)
    public String getAllMonitorQueueDetail(Model model) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        List<NodeEntity> nodelist = nodeService.getAllNodeList();
        String nodeprefix = RedisKeyUtils.MONITOR_NODEUV_SET_PREFIX;
        if (!CollectionUtils.isEmpty(nodelist)) {
            for (NodeEntity nodee : nodelist) {
                String nodekey = nodee.getNodeKey();
                for (int i = -7; i <= 0; i++) {
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, i);
                    Date sday = c.getTime();
                    String daykey = DateUtil.getDateShortStr(sday);
                    String queueKey = nodeprefix + nodekey + ":" + daykey;
                    Long queueCount = redisService.scard(queueKey);
                    if (queueCount != null && queueCount > 0) {
                        HashMap<String, String> result = new HashMap<String, String>();
                        result.put("queueKey", queueKey);
                        result.put("queueCount", queueCount.toString());
                        list.add(result);
                    }
                }
            }
        }
        model.addAttribute("searchResults", list);
        return "node/nodeUVQueueDetail";
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "/nodeUVQueueDelete/{queueId}", method = RequestMethod.GET)
    public String clearBusinessNodePercentMonitorStrategyRedis(@PathVariable String queueId, Model model) {

        redisService.delete(queueId);

        // 返回页面列表
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        List<NodeEntity> nodelist = nodeService.getAllNodeList();
        String nodeprefix = RedisKeyUtils.MONITOR_NODEUV_SET_PREFIX;
        if (!CollectionUtils.isEmpty(nodelist)) {
            for (NodeEntity nodee : nodelist) {
                String nodekey = nodee.getNodeKey();
                for (int i = -7; i <= 0; i++) {
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, i);
                    Date sday = c.getTime();
                    String daykey = DateUtil.getDateShortStr(sday);
                    String queueKey = nodeprefix + nodekey + ":" + daykey;
                    Long queueCount = redisService.scard(queueKey);
                    if (queueCount != null && queueCount > 0) {
                        HashMap<String, String> result = new HashMap<String, String>();
                        result.put("queueKey", queueKey);
                        result.put("queueCount", queueCount.toString());
                        list.add(result);
                    }
                }
            }
        }
        model.addAttribute("searchResults", list);
        return "node/nodeUVQueueDetail";
    }


    @RequestMapping(value = "/getNodeITQueueDetail", method = RequestMethod.GET)
    public String getNodeITQueueDetail(Model model, String hanId) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        List<NodeEntity> nodelist = nodeService.getAllNodeList();
        String nodeprefix = RedisKeyUtils.MONITOR_NODEINTER_LIST_PREFIX;
        if (!CollectionUtils.isEmpty(nodelist)) {
            for (NodeEntity nodee : nodelist) {
                String nodekey = nodee.getNodeKey();
                for (int i = -12; i <= 0; i++) {
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.HOUR_OF_DAY, i);
                    Date sday = c.getTime();
                    String daykey = DateUtil.getDateShorthhStr(sday);
                    String queueKey = nodeprefix + nodekey + ":" + daykey;
                    Long queueCount = redisService.size(queueKey);
                    if (queueCount != null && queueCount > 0) {
                        HashMap<String, String> result = new HashMap<String, String>();
                        result.put("queueKey", queueKey);
                        result.put("queueCount", queueCount.toString());
                        list.add(result);
                    }
                }
            }
        }
        model.addAttribute("searchResults", list);
        model.addAttribute("hanId", hanId);
        return "node/nodeITQueueDetail";
    }

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "/nodeITQueueDelete/{queueId}", method = RequestMethod.GET)
    public String nodeITQueueDelete(@PathVariable String queueId, Model model) {

        redisService.delete(queueId);

        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        List<NodeEntity> nodelist = nodeService.getAllNodeList();
        String nodeprefix = RedisKeyUtils.MONITOR_NODEINTER_LIST_PREFIX;
        if (!CollectionUtils.isEmpty(nodelist)) {
            for (NodeEntity nodee : nodelist) {
                String nodekey = nodee.getNodeKey();
                for (int i = -12; i <= 0; i++) {
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.HOUR_OF_DAY, i);
                    Date sday = c.getTime();
                    String daykey = DateUtil.getDateShorthhStr(sday);
                    String queueKey = nodeprefix + nodekey + ":" + daykey;
                    Long queueCount = redisService.size(queueKey);
                    if (queueCount != null && queueCount > 0) {
                        HashMap<String, String> result = new HashMap<String, String>();
                        result.put("queueKey", queueKey);
                        result.put("queueCount", queueCount.toString());
                        list.add(result);
                    }
                }
            }
        }
        model.addAttribute("searchResults", list);
        return "node/nodeITQueueDetail";
    }

}
