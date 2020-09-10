package com.monitor.argus.mis.task;

import com.monitor.argus.bean.dataland.AnalyTopologyDatalandEntity;
import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.dao.dataland.IArgusTopologyDatalandDao;
import com.monitor.argus.service.node.IMonitorNodeService;
import com.monitor.argus.common.enums.GlobalParam;
import com.monitor.argus.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by wangfeng on 16/10/26.
 */
@Component("staticForNodeMaxTask")
public class StaticForNodeMaxTask {

    private static Logger logger = LoggerFactory.getLogger(StaticForNodeMaxTask.class);
    protected IMonitorNodeService nodeService;
    protected IArgusTopologyDatalandDao argusTopologyDatalandDao;

    static final Integer timer = 1000 * 3600;

    public StaticForNodeMaxTask(IMonitorNodeService nodeService, IArgusTopologyDatalandDao argusTopologyDatalandDao) {
        this.nodeService = nodeService;
        this.argusTopologyDatalandDao = argusTopologyDatalandDao;
    }

    @PostConstruct
    public void fetch() {
        logger.info("抓取node数据线程即将开始。。。");
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        logger.info("原NodeMap数据:-{}", JsonUtil.beanToJson(GlobalParam.diffNodeKeyValue));
                        List<NodeEntity> nodeEntityList = nodeService.getAllEnableNodeList();
                        if (nodeEntityList != null && nodeEntityList.size() > 0) {
                            for (NodeEntity entity : nodeEntityList) {
                                AnalyTopologyDatalandEntity diff = argusTopologyDatalandDao.getDiffLogCountByKey(entity.getNodeKey());
                                if (diff == null) {
                                    continue;
                                }
                                Long num = Long.parseLong(diff.getDiffLogCount()) / 300 * 2;
                                GlobalParam.diffNodeKeyValue.put(entity.getNodeKey(), num.toString());
                            }
                        }
                        logger.info("抓取后NodeMap数据:-{}", JsonUtil.beanToJson(GlobalParam.diffNodeKeyValue));
                        Thread.sleep(timer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}




