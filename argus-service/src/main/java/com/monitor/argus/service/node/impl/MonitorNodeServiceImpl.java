package com.monitor.argus.service.node.impl;

import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.bean.node.TopoAnalyEntity;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.UuidUtil;
import com.monitor.argus.dao.node.IMonitorNodeDao;
import com.monitor.argus.service.node.IMonitorNodeService;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangfeng on 16/9/20.
 */
@Service("monitorNodeService")
public class MonitorNodeServiceImpl implements IMonitorNodeService {

    @Autowired
    IMonitorNodeDao nodeDao;

    private final Logger logger = LoggerFactory.getLogger(MonitorNodeServiceImpl.class);

    @Override
    public void insertNode(NodeEntity nodeEntity) {
        logger.info("Service 插入节点信息。param:{}", JsonUtil.beanToJson(nodeEntity));
        checkIsExist(nodeEntity);
        nodeEntity.setNodeKey(UuidUtil.getUUID());
        nodeDao.insertNode(nodeEntity);
    }

    @Override
    public void updateNode(NodeEntity nodeEntity) {
        logger.info("Service 更新节点信息,{}", JsonUtil.beanToJson(nodeEntity));
        NodeEntity entity = nodeDao.getNodeByKey(nodeEntity.getNodeKey());
        if (entity == null || entity.getId() == null) {
            throw new RuntimeException("key对应的节点不存在。");
        }
        if (!nodeEntity.getNodeSystemId().equals(entity.getNodeSystemId()) || (nodeEntity.getNodeUrl() != null && !nodeEntity.getNodeUrl().equals(entity.getNodeUrl()))) {
            checkIsExist(nodeEntity);
        }
        nodeDao.updateNode(nodeEntity);
    }

    @Override
    public NodeEntity getNodeByKey(String nodeKey) {
        logger.info("Service 通过Key获取节点信息");
        return nodeDao.getNodeByKey(nodeKey);
    }

    @Override
    public List<NodeEntity> getAllNodeList() {
        logger.info("Service 获取全量节点列表");
        return nodeDao.getAllNodeList();
    }

    @Override
    public List<NodeEntity> getAllEnableNodeList() {
        logger.info("Service 获取全量有效节点列表");
        return nodeDao.getAllEnableNodeList();
    }

    @Override
    public List<NodeEntity> getNodeList(NodeEntity nodeEntity) {
        logger.info("Service 通过条件获取节点列表");
        return nodeDao.getNodeList(nodeEntity);
    }

    private boolean checkIsExist(NodeEntity nodeEntity) {
        NodeEntity entity = new NodeEntity();
        entity.setNodeSystemId(nodeEntity.getNodeSystemId());
        entity.setNodeUrl(nodeEntity.getNodeUrl());
        List<NodeEntity> oldNode = nodeDao.getNodeList(entity);
        if (oldNode != null && oldNode.size() > 0) {
            throw new RuntimeException("该system-url对应的节点已存在。");
        }
        return true;
    }

    @Override
    public Map<String, List<TopoAnalyEntity>> selectAnalyByMinutes(String nodeKey) {
        Date endDate = new Date();
        Date beginDate = DateUtils.addHours(endDate, -2);
        String beginStr = DateUtil.getDateLongTimePlusNoSecondStr(beginDate);
        String endStr = DateUtil.getDateLongTimePlusNoSecondStr(endDate);
        List<TopoAnalyEntity> todayList = nodeDao.selectAnalyByMinutes(beginStr, endStr, nodeKey);
        beginDate = DateUtils.addHours(beginDate, -24);
        endDate = DateUtils.addHours(endDate, -24);
        beginStr = DateUtil.getDateLongTimePlusNoSecondStr(beginDate);
        endStr = DateUtil.getDateLongTimePlusNoSecondStr(endDate);
        List<TopoAnalyEntity> yesterdayList = nodeDao.selectAnalyByMinutes(beginStr, endStr, nodeKey);
        Map<String, List<TopoAnalyEntity>> map = new HashMap<>();
        map.put("todayList", todayList);
        map.put("yesterdayList", yesterdayList);
        return map;
    }

    @Override
    public Map<String, List<TopoAnalyEntity>> selectAnalyByHours(String nodeKey) {
        Date endDate = new Date();
        Date beginDate = DateUtils.addHours(endDate, -24);
        String beginStr = DateUtil.getDateLongTimePlusNoSecondStr(beginDate);
        String endStr = DateUtil.getDateLongTimePlusNoSecondStr(endDate);
        List<TopoAnalyEntity> todayList = nodeDao.selectAnalyByHours(beginStr, endStr, nodeKey);
        beginDate = DateUtils.addHours(beginDate, -24);
        endDate = DateUtils.addHours(endDate, -24);
        beginStr = DateUtil.getDateLongTimePlusNoSecondStr(beginDate);
        endStr = DateUtil.getDateLongTimePlusNoSecondStr(endDate);
        List<TopoAnalyEntity> yesterdayList = nodeDao.selectAnalyByHours(beginStr, endStr, nodeKey);
        Map<String, List<TopoAnalyEntity>> map = new HashMap<>();
        map.put("todayList", todayList);
        map.put("yesterdayList", yesterdayList);
        return map;
    }

    @Override
    public List<TopoAnalyEntity> selectAnalyByDays(String nodeKey) {
        Date endDate = new Date();
        Date beginDate = DateUtils.addDays(endDate, -14);
        endDate = DateUtils.addDays(endDate, 1);
        String beginStr = DateUtil.getSimpleDateTimeStr(beginDate);
        String endStr = DateUtil.getSimpleDateTimeStr(endDate);

        List<TopoAnalyEntity> dayList = nodeDao.selectAnalyByDays(beginStr, endStr, nodeKey);
        return dayList;

    }
}
