package com.monitor.argus.mis.controller.node;

import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.node.IMonitorNodeService;
import com.monitor.argus.common.annotation.Auth;
import com.monitor.argus.common.model.ResponseData;
import com.monitor.argus.common.util.RedisKeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huxiaolei on 2016/9/23.
 */
@Controller
@RequestMapping("/node/data")
public class NodeDataQueryController {

    private static Logger logger = LoggerFactory.getLogger(NodeDataQueryController.class);

    @Autowired
    RedisService redisService;

    @Autowired
    IMonitorNodeService nodeService;

    @RequestMapping(value = "/query/allEnable")
    @ResponseBody
    @Auth(verifyLogin = false, verifyURL = false)
    public ResponseData showAddMonitorPage(Model model) {
        ResponseData jsonResponse = new ResponseData();
        Map<String, Object> mapResult = new HashMap<>();
        Map<String, String> keyfieldsMap1 = new HashMap();
        Map<String, String> keyfieldsMap3 = new HashMap();
        List<NodeEntity> nodeList = nodeService.getAllEnableNodeList();
        if (!CollectionUtils.isEmpty(nodeList)) {
            for (NodeEntity node : nodeList) {
                keyfieldsMap1.put(node.getNodeKey(), node.getNodeUrl());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
                String hourkey = sdf.format(new Date());
                List<String> statisticsInfos = redisService.hmget(RedisKeyUtils.ARGUS_ANALYSIS_STATISTICS_HOUR_KEY + node.getNodeKey(),
                        RedisKeyUtils.ANALYSIS_STAT_LOG_COUNT + hourkey);
                keyfieldsMap3.put(node.getNodeKey(), hourkey + "|" + statisticsInfos.get(0));

            }
        }

        Map<String, String> keyfieldsMap2 = new HashMap();
        Set<String> keyfields = redisService.hkeys(RedisKeyUtils.ARGUS_ANALYSIS_STATISTICS_HZ_KEY);
        if (!CollectionUtils.isEmpty(keyfields)) {
            for (String keyfield : keyfields) {
                List<String> statisticsInfos = redisService.hmget(RedisKeyUtils.ARGUS_ANALYSIS_STATISTICS_HZ_KEY, keyfield);
                keyfieldsMap2.put(keyfield, statisticsInfos.get(0));
            }
        }
        mapResult.put("key-url", keyfieldsMap1);
        mapResult.put("key-count", keyfieldsMap2);
        mapResult.put("key-hour-count", keyfieldsMap3);
        jsonResponse.setObj(mapResult);

        return jsonResponse;
    }

}
