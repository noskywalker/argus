package com.monitor.argus.mis.task;

import com.monitor.argus.bean.dataland.AnalyTopologyDayUVEntity;
import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.dao.dataland.IArgusTopologyDatalandDao;
import com.monitor.argus.mis.task.annotations.Job;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.service.node.IMonitorNodeService;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.RedisKeyUtils;
import com.monitor.argus.common.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by huxiaolei on 2016/10/30.
 */
@Component
public class AnalyTopologyDayUVTask {

    private static Logger logger = LoggerFactory.getLogger(AnalyTopologyDayUVTask.class);

    private static String uvlockKey = "AnalyTopologyDayUVTaskLock";
    private static String uvlockValue = "AnalyTopologyDayUVTaskValue";

    @Autowired
    IMonitorNodeService nodeService;

    @Autowired
    RedisService redisService;

    @Autowired
    IArgusTopologyDatalandDao iArgusTopologyDatalandDao;

    @Job(name = "节点日UV数据汇总任务", cron="0 0 1,11,21 * * ?")
    public void dayUVTask() {
        logger.info("dayUVTask====start");
        Date newDate = new Date();
        try {
            if (redisService.setNX(uvlockKey, uvlockValue)) {
                List<NodeEntity> nodelist = nodeService.getAllNodeList();
                String nodeprefix = RedisKeyUtils.MONITOR_NODEUV_SET_PREFIX;
                if (!CollectionUtils.isEmpty(nodelist)) {
                    for (NodeEntity nodee : nodelist) {
                        String nodekey = nodee.getNodeKey();
                        // 最近3天
                        for (int i = -3; i < 0; i++) {
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.DATE, i);
                            Date sday = c.getTime();
                            String daykey = DateUtil.getDateShortStr(sday);
                            String queueKey = nodeprefix + nodekey + ":" + daykey;
                            Long queueCount = redisService.scard(queueKey);
                            if (queueCount != null && queueCount > 0) {
                                boolean isStore = false;
                                // 入库
                                AnalyTopologyDayUVEntity analyTopologyDayUVEntity = new AnalyTopologyDayUVEntity();
                                analyTopologyDayUVEntity.setId(UuidUtil.getUUID());
                                analyTopologyDayUVEntity.setCreateDate(sday);
                                analyTopologyDayUVEntity.setNodeKey(nodekey);
                                analyTopologyDayUVEntity.setuVCount(String.valueOf(queueCount));
                                isStore = iArgusTopologyDatalandDao.addAnalyTopologyDayUV(analyTopologyDayUVEntity);
                                if (isStore) {
                                    // 清除key
                                    redisService.delete(queueKey);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisService.expire(uvlockKey, 600);
        }
        logger.info("dayUVTask====end");
    }

}
