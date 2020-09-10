package com.monitor.argus.dao.monitor.impl;

import com.monitor.argus.bean.monitor.MonitorHostEntity;
import com.monitor.argus.bean.monitor.MonitorStrategyEntity;
import com.monitor.argus.bean.monitor.MonitorSystemEntity;
import com.monitor.argus.dao.monitor.IMonitorDao;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.common.util.UuidUtil;
import com.monitor.argus.dao.mybatis.IBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by xuefei on 7/11/16.
 */
@Repository("MonitorDao")
public class MonitorDaoImpl implements IMonitorDao {

    @Autowired
    private IBaseDao baseDao;
    private final Logger logger = LoggerFactory.getLogger(MonitorDaoImpl.class);

    @Override
    public boolean addMonitorHost(MonitorHostEntity monitorHostEntity) {
        logger.info("Dao 插入监控主机信息");
        if (StringUtil.isEmpty(monitorHostEntity.getId())) {
            monitorHostEntity.setId(UuidUtil.getUUID());
        }

        return baseDao.insert("monitorHostMapper.insert", monitorHostEntity);
    }

    @Override
    public int addMonitorHostBatch(List<MonitorHostEntity> monitorHostEntityList) {
        logger.info("Dao 批量插入监控主机信息");
        for (MonitorHostEntity monitorHostEntity : monitorHostEntityList) {
            if (StringUtil.isEmpty(monitorHostEntity.getId())) {
                monitorHostEntity.setId(UuidUtil.getUUID());
            }
        }
        int insertCount = baseDao.insertBatch("monitorHostMapper.insert", monitorHostEntityList);
        logger.debug("批量增加监控主机信息-E{}", insertCount);
        return insertCount;
    }

    @Override
    public boolean addMonitorSystem(MonitorSystemEntity monitorSystemEntity) {
        logger.info("Dao 插入监控系统信息");
        if (StringUtil.isEmpty(monitorSystemEntity.getId())) {
            String uuid = UuidUtil.getUUID();
            monitorSystemEntity.setId(uuid);
        }
        monitorSystemEntity.setCreateDate(new Date());
        boolean result = baseDao.insert("monitorSystemMapper.insert", monitorSystemEntity);
        return result;
    }

    @Override
    public boolean addMonitorStrategy(MonitorStrategyEntity monitorStrategyEntity) {
        logger.info("Dao 插入监控系统信息");
        if (StringUtil.isEmpty(monitorStrategyEntity.getId())) {
            monitorStrategyEntity.setId(UuidUtil.getUUID());
        }
        return baseDao.insert("monitorStrategyMapper.insert", monitorStrategyEntity);
    }

    @Override
    public List<MonitorStrategyEntity> getAllEnableStrategies() {
        return baseDao.getList("monitorStrategyMapper.selectAllEnableStrategy");
    }

    @Override
    public List<MonitorSystemEntity> getAllSystems() {
        return baseDao.getList("monitorSystemMapper.selectAllSystems");
    }

    @Override
    public List<MonitorHostEntity> getAllHosts() {
        return baseDao.getList("monitorHostMapper.selectAllHosts");
    }

    @Override
    public int addMonitorStrategyBatch(List<MonitorStrategyEntity> monitorStrategyEntityList) {
        logger.info("Dao 批量插入监控策略信息");
        for (MonitorStrategyEntity monitorStrategyEntity : monitorStrategyEntityList) {
            if (StringUtil.isEmpty(monitorStrategyEntity.getId())) {
                monitorStrategyEntity.setId(UuidUtil.getUUID());
            }
        }
        int insertCount = baseDao.insertBatch("monitorStrategyMapper.insert", monitorStrategyEntityList);
        logger.debug("批量增加监控策略信息-E{}", insertCount);
        return insertCount;
    }

    @Override
    public List<MonitorHostEntity> getHostsByMonitorSystemId(String systemId) {
        return baseDao.getList("monitorHostMapper.getHostsByMonitorSystemId", systemId);
    }

    @Override
    public List<String> getHostIdsSystemId(String systemId) {
        return baseDao.getList("monitorHostMapper.getHostIdsByMonitorSystemId", systemId);
    }

    @Override
    public List<MonitorStrategyEntity> getStrategiesByMonitorSystemId(String systemId) {
        return baseDao.getList("monitorStrategyMapper.getStrategiesByMonitorSystemId", systemId);
    }

    @Override
    public List<String> getStrategyIdsByMonitorSystemId(String systemId) {
        return baseDao.getList("monitorStrategyMapper.getStrategyIdsByMonitorSystemId", systemId);
    }

    @Override
    public MonitorSystemEntity getMonitorSystemById(String id) {
        return baseDao.get("monitorSystemMapper.selectByPrimaryKey", id);
    }

    @Override
    public boolean updateMonitorSystem(MonitorSystemEntity monitorSystemEntity) {
        logger.info("Dao 更新监控系统,id = " + monitorSystemEntity.getSystemName());
        return baseDao.update("monitorSystemMapper.updateMonitorSystem", monitorSystemEntity);
    }

    @Override
    public boolean deleteMonitorHostById(String id) {
        logger.info("Dao 删除监控主机");
        return baseDao.delete("monitorHostMapper.deleteMonitorHostById", id);
    }

    @Override
    public boolean deleteMonitorStrategyById(String id) {
        logger.info("Dao 删除监控策略");
        return baseDao.delete("monitorStrategyMapper.deleteMonitorStrategyById", id);
    }

    @Override
    public boolean deleteMonitorHostBySystemId(String systemId) {
        logger.info("Dao 删除监控系统与主机的映射信息");
        return baseDao.delete("monitorHostMapper.deleteMonitorHostBySystemId", systemId);
    }

    @Override
    public boolean deleteMonitorStrategyBySystemId(String systemId) {
        logger.info("Dao 删除监控系统与策略的映射信息");
        return baseDao.delete("monitorStrategyMapper.deleteMonitorStrategyBySystemId", systemId);
    }

    @Override
    public List<MonitorStrategyEntity> getStrategyByCondition(MonitorStrategyEntity monitorStrategyEntity) {
        logger.info("Dao 通过条件获取监控策略,param:" + JsonUtil.beanToJson(monitorStrategyEntity));
        return baseDao.getList("monitorStrategyMapper.getStrategyByCondition", monitorStrategyEntity);
    }

    @Override
    public void changeStrategyStatus(String id, String status) {
        logger.info("Dao 根据ID 和 状态变成监控策略状态。id={},status={}", id, status);
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("status", status);
        baseDao.update("monitorStrategyMapper.changeStrategyStatus", map);
    }

    @Override
    public boolean updateMonitorStrategyById(MonitorStrategyEntity entity) {
        logger.info("Dao 通过ID更新监控策略。Param:{}", JsonUtil.beanToJson(entity));
        return baseDao.update("monitorStrategyMapper.updateMonitorStrategyById", entity);
    }
}
