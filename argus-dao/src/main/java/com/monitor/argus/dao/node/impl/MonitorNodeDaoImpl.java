package com.monitor.argus.dao.node.impl;

import com.monitor.argus.bean.dataland.AnalyTopologyHourITEntity;
import com.monitor.argus.bean.node.CollectDataObject;
import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.bean.node.TopoAnalyDayUVEntity;
import com.monitor.argus.bean.node.TopoAnalyEntity;
import com.monitor.argus.dao.mybatis.IBaseDao;
import com.monitor.argus.dao.node.IMonitorNodeDao;
import com.monitor.argus.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangfeng on 16/9/19.
 */
@Repository("MonitorNodeDao")
public class MonitorNodeDaoImpl implements IMonitorNodeDao {

    @Autowired
    IBaseDao baseDao;
    private final Logger logger = LoggerFactory.getLogger(MonitorNodeDaoImpl.class);

    @Override
    public boolean insertNode(NodeEntity nodeEntity) {
        logger.info("Dao 插入节点信息。param:{}", JsonUtil.beanToJson(nodeEntity));
        return baseDao.insert("nodeMapper.insert", nodeEntity);
    }

    @Override
    public List<NodeEntity> getAllNodeList() {
        logger.info("Dao 获取所有节点列表");
        return baseDao.getList("nodeMapper.selectAllNode");
    }

    @Override
    public boolean updateNode(NodeEntity nodeEntity) {
        logger.info("Dao 更新节点信息-{}", JsonUtil.beanToJson(nodeEntity));
        return baseDao.update("nodeMapper.updateNode", nodeEntity);
    }

    @Override
    public NodeEntity getNodeByKey(String nodeKey) {
        logger.info("Dao 根据Key获取node信息。key-{}", nodeKey);
        return baseDao.get("nodeMapper.selectByPrimaryKey", nodeKey);
    }

    @Override
    public List<NodeEntity> getNodeList(NodeEntity nodeEntity) {
        logger.info("Dao 根据条件获取node列表。param:{}", JsonUtil.beanToJson(nodeEntity));
        return baseDao.getList("nodeMapper.getNodeList", nodeEntity);
    }

    @Override
    public List<NodeEntity> getAllEnableNodeList() {
        logger.info("Dao 获取所有有效节点列表");
        return baseDao.getList("nodeMapper.selectAllEnableNode");
    }

    @Override
    public List<TopoAnalyEntity> selectAnalyByHours(String beginDate, String endDate, String nodeKey) {
        logger.info("Dao 获取节点信息 小时");
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("nodeKey", nodeKey);
        return baseDao.getList("topoAnalyMapper.selectByHours", map);
    }

    @Override
    public List<TopoAnalyEntity> selectAnalyByMinutes(String beginDate, String endDate, String nodeKey) {
        logger.info("Dao 获取节点信息 分钟");
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("nodeKey", nodeKey);
        return baseDao.getList("topoAnalyMapper.selectByMinutes", map);
    }

    @Override
    public List<TopoAnalyEntity> selectAnalyByDays(String beginDate, String endDate, String nodeKey) {
        logger.info("Dao 获取节点信息 天");
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("nodeKey", nodeKey);
        return baseDao.getList("topoAnalyMapper.selectByDays", map);
    }

    @Override
    public List<TopoAnalyDayUVEntity> selectDayUVByNodeKey(String beginDate, String endDate, String nodeKey) {
        logger.info("Dao 获取节点UV统计信息");
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("nodeKey", nodeKey);
        return baseDao.getList("topoAnalyMapper.selectDayUVByNodeKey", map);
    }

    @Override
    public List<CollectDataObject> selectCountPVByDateCount(String beginDate, String endDate) {
        logger.info("Dao 获取节点日期内PV汇总");
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        return baseDao.getList("topoAnalyMapper.selectCountPVByDateCount", map);
    }

    @Override
    public List<CollectDataObject> selectCountPVByDate(String beginDate, String endDate) {
        logger.info("Dao 获取所有节点每日PV汇总");
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        return baseDao.getList("topoAnalyMapper.selectCountPVByDate", map);
    }

    @Override
    public List<CollectDataObject> selectCountUVByDateCount(String beginDate, String endDate) {
        logger.info("Dao 获取节点日期内UV汇总");
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        return baseDao.getList("topoAnalyMapper.selectCountUVByDateCount", map);
    }

    @Override
    public List<CollectDataObject> selectCountUVByDate(String beginDate, String endDate) {
        logger.info("Dao 获取所有节点每日UV汇总");
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        return baseDao.getList("topoAnalyMapper.selectCountUVByDate", map);
    }

    @Override
    public List<AnalyTopologyHourITEntity> selectInterTimeByNodeKey(String beginDate, String endDate, String nodeKey) {
        logger.info("Dao 获取NodeKey对应的接口调用相应情况。nodeKey={},beginDate={},endDate={}", nodeKey, beginDate, endDate);
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("nodeKey", nodeKey);
        return baseDao.getList("topoAnalyMapper.selectInterTimeByNodeKey", map);
    }

    @Override
    public List<AnalyTopologyHourITEntity> getAvgPertimeByNodeKeyAndDate(String beginDate, String endDate, String nodeKey) {
        logger.info("Dao 获取NodeKey在指定时间对应的接口平均响应时间。nodeKey={},beginDate={},endDate={}", nodeKey, beginDate, endDate);
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("nodeKey", nodeKey);
        return baseDao.getList("topoAnalyMapper.getAvgPertimeByNodeKeyAndDate", map);
    }

    public List<AnalyTopologyHourITEntity> getInterRankingList() {
        return baseDao.getList("topoAnalyMapper.getInterRankingList");
    }

    public List<AnalyTopologyHourITEntity> getInterRankingListAsc() {
        return baseDao.getList("topoAnalyMapper.getInterRankingListAsc");
    }

}
