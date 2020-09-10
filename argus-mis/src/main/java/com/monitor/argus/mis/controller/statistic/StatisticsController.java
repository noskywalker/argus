package com.monitor.argus.mis.controller.statistic;

import com.monitor.argus.bean.statistic.vo.StatisticsValueVO;
import com.monitor.argus.service.dataland.IArgusTopologyDatalandService;
import com.monitor.argus.service.statistics.StatisticService;
import com.monitor.argus.common.annotation.Auth;
import com.monitor.argus.common.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2016/7/20.
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController {

    public static volatile ReentrantLock syncRedisAndDbLock = new ReentrantLock();

    @Autowired
    StatisticService statisticService;

    @Autowired
    IArgusTopologyDatalandService argusTopologyDatalandService;

    @Auth(verifyLogin = false, verifyURL = false)
    @RequestMapping(value = "/totalBytes", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseData fetchTotalMonitorBytes(long formerBytes, long formerMonitors, long formerAlarms, long formerLogs) {
        ResponseData jsonResponse = new ResponseData();
        List<StatisticsValueVO> statisticsValueList = statisticService.fetchFullStatistics(formerBytes, formerMonitors, formerAlarms, formerLogs);
        jsonResponse.setSuccess(true);
        jsonResponse.setMsg("获取成功！");
        jsonResponse.setObj(statisticsValueList);
        return jsonResponse;
    }

    @RequestMapping(value = "/realtimeTraffic", produces = "application/json")
    @ResponseBody
    public ResponseData realtimeTraffic(int cmaximum, Double chaBytes, long oldBytes) {
        ResponseData jsonResponse = new ResponseData();
        try {
            syncRedisAndDbLock.lock();
            ArrayList<Double> realtimeTrafficListCopy = statisticService.fetchRealtimeTraffic(cmaximum, chaBytes, oldBytes);

            jsonResponse.setSuccess(true);
            jsonResponse.setMsg("获取成功！");
            jsonResponse.setObj(realtimeTrafficListCopy);
        } catch (Exception e) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMsg("获取失败！");
            jsonResponse.setObj(null);
        } finally {
            syncRedisAndDbLock.unlock();
            return jsonResponse;
        }
    }

}
