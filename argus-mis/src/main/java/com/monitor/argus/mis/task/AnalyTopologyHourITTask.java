package com.monitor.argus.mis.task;

import com.monitor.argus.bean.dataland.AnalyTopologyHourITEntity;
import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.dao.dataland.IArgusTopologyDatalandDao;
import com.monitor.argus.mis.task.annotations.Job;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.node.IMonitorNodeService;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.common.util.StringUtil;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by hxl on 2016/11/15.
 */
@Component
public class AnalyTopologyHourITTask {

    private static Logger logger = LoggerFactory.getLogger(AnalyTopologyHourITTask.class);

    private static String itlockKey = "AnalyTopologyHourITTaskLock";
    private static String itlockValue = "AnalyTopologyHourITTaskValue";
    private static Pattern numPattern = Pattern.compile("^(0|[1-9][0-9]*)$");

    @Autowired
    IMonitorNodeService nodeService;

    @Autowired
    RedisService redisService;

    @Autowired
    IArgusTopologyDatalandDao iArgusTopologyDatalandDao;

    @Job(name = "接口数据按小时汇总任务", cron="0 20 * * * ?")
    public void dayITTask() {
        logger.info("dayITTask====start");
        Date newDate = new Date();
        int delcount = 0;
        try {
            if (redisService.setNX(itlockKey, itlockValue)) {
                List<NodeEntity> nodelist = nodeService.getAllNodeList();
                String nodeprefix = RedisKeyUtils.MONITOR_NODEINTER_LIST_PREFIX;
                if (!CollectionUtils.isEmpty(nodelist)) {
                    for (NodeEntity nodee : nodelist) {
                        String nodekey = nodee.getNodeKey();
                        // 最近12个小时
                        for (int i = -12; i < 0; i++) {
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.HOUR_OF_DAY, i);
                            Date sday = c.getTime();
                            String dayhhkey = DateUtil.getDateShorthhStr(sday);
                            String queueKey = nodeprefix + nodekey + ":" + dayhhkey;
                            List<String> queueTimes = redisService.listAll(queueKey);
                            if (!CollectionUtils.isEmpty(queueTimes)) {
                                boolean isStore = false;

                                long count = queueTimes.size();
                                long allTimes = 0l;
                                for (String times : queueTimes) {
                                    if (!StringUtil.isEmpty(times) ) {
                                        times = times.trim();
                                        if (numPattern.matcher(times).find()) {
                                            allTimes += Long.valueOf(times);
                                        }
                                    }
                                }
                                double pertime = 0l;
                                if (allTimes > 0 && count > 0) {
                                    pertime = (double)(allTimes/count);
                                }
                                AnalyTopologyHourITEntity analyTopologyHourITEntity = new AnalyTopologyHourITEntity();
                                analyTopologyHourITEntity.setNodeKey(nodekey);
                                analyTopologyHourITEntity.setCount(count);
                                analyTopologyHourITEntity.setAlltime(allTimes);
                                analyTopologyHourITEntity.setPertime(pertime);
                                analyTopologyHourITEntity.setCreateDate(DateUtil.getDateHHLong(dayhhkey));
                                isStore = iArgusTopologyDatalandDao.addAnalyTopologyDayIT(analyTopologyHourITEntity);
                                // 入库
                                if (isStore) {
                                    // 清除key
                                    redisService.delete(queueKey);
                                    delcount ++;
                                }
                            }
                        }
                    }
                }
                // 只保留近一个月的数据
                Date nowDateM = new Date();
                Date nowDateMBefor = DateUtils.addMonths(nowDateM, -1);
                String nowDateMBeforSdf = DateUtil.getSimpleDateTimeStr(nowDateMBefor);
                if (!StringUtil.isEmpty(nowDateMBeforSdf)) {
                    iArgusTopologyDatalandDao.delAnalyTopologyDayIT(nowDateMBeforSdf);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisService.expire(itlockKey, 600);
        }
        logger.info("dayITTask====end,delcount:{}", delcount);
    }

}
