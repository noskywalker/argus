package com.monitor.argus.service.monitor.impl;

import com.monitor.argus.service.monitor.IMonitorService;
import com.monitor.argus.bean.monitor.MonitorHostEntity;
import com.monitor.argus.bean.monitor.MonitorStrategyEntity;
import com.monitor.argus.bean.monitor.MonitorSystemEntity;
import com.monitor.argus.dao.monitor.IMonitorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xuefei on 7/7/16.
 */
@Service("monitorService")
public class MonitorServiceImpl implements IMonitorService {

    @Autowired
    IMonitorDao monitorDao;

    @Override
    public boolean addMonitorHost(MonitorHostEntity monitorHostEntity) {
        return monitorDao.addMonitorHost(monitorHostEntity);
    }

    @Override
    public int addMonitorHostBatch(List<MonitorHostEntity> monitorHostEntityList) {
        return monitorDao.addMonitorHostBatch(monitorHostEntityList);
    }

    @Override
    public boolean addMonitorSystem(MonitorSystemEntity monitorSystemEntity) {
        return monitorDao.addMonitorSystem(monitorSystemEntity);
    }

    @Override
    public boolean addMonitorStrategy(MonitorStrategyEntity monitorStrategyEntity) {
        return monitorDao.addMonitorStrategy(monitorStrategyEntity);
    }

    @Override
    public int addMonitorStrategyBatch(List<MonitorStrategyEntity> monitorStrategyEntityList) {
        return monitorDao.addMonitorStrategyBatch(monitorStrategyEntityList);
    }

    @Override
    public List<MonitorSystemEntity> searchMonitorSystem() {
        return monitorDao.getAllSystems();
    }

    @Override
    public MonitorSystemEntity getMonitorSystemById(String id) {
        return monitorDao.getMonitorSystemById(id);
    }

    @Override
    public List<MonitorHostEntity> getMonitorHostByMonitorSystemId(String monitorSystemId) {
        return monitorDao.getHostsByMonitorSystemId(monitorSystemId);
    }

    @Override
    public List<String> getHostIdsSystemId(String systemId) {
        return monitorDao.getHostIdsSystemId(systemId);
    }

    @Override
    public List<MonitorStrategyEntity> getMonitorStrategyByMonitorSystemId(String monitorSystemId) {
        return monitorDao.getStrategiesByMonitorSystemId(monitorSystemId);
    }

    @Override
    public List<String> getStrategyIdsByMonitorSystemId(String systemId) {
        return monitorDao.getStrategyIdsByMonitorSystemId(systemId);
    }

    @Override
    public boolean updateMonitorSystem(MonitorSystemEntity monitorSystemEntity) {
        return monitorDao.updateMonitorSystem(monitorSystemEntity);
    }

    @Override
    public boolean deleteMonitorHostBySystemId(String systemId) {
        return monitorDao.deleteMonitorHostBySystemId(systemId);
    }

    @Override
    public boolean deleteMonitorStrategyBySystemId(String systemId) {
        return monitorDao.deleteMonitorStrategyBySystemId(systemId);
    }

    @Override
    public boolean deleteMonitorHostById(String id) {
        return monitorDao.deleteMonitorHostById(id);
    }

    @Override
    public boolean deleteMonitorStrategyById(String id) {
        return monitorDao.deleteMonitorStrategyById(id);
    }

    @Override
    public List<MonitorSystemEntity> getAllSystems() {
        return monitorDao.getAllSystems();
    }

    @Override
    public List<MonitorHostEntity> getAllHosts() {
        return monitorDao.getAllHosts();
    }

    @Override
    public List<MonitorStrategyEntity> getStrategyEntityByCondition(MonitorStrategyEntity monitorStrategyEntity) {
        return monitorDao.getStrategyByCondition(monitorStrategyEntity);
    }

    @Override
    public void changeStrategyStatus(String id, String status) {
        monitorDao.changeStrategyStatus(id, status);
    }

    @Override
    public List<MonitorStrategyEntity> getAllEnableStrategies() {
        return monitorDao.getAllEnableStrategies();
    }

    @Override
    public MonitorStrategyEntity getStrategyEntityById(String strategyId) {
        MonitorStrategyEntity entity = new MonitorStrategyEntity();
        entity.setId(strategyId);
        List<MonitorStrategyEntity> list = monitorDao.getStrategyByCondition(entity);
        return list == null || list.size() == 0 ? new MonitorStrategyEntity() : list.get(0);
    }

    @Override
    public boolean updateMonitorStrategy(MonitorStrategyEntity entity) {
        return monitorDao.updateMonitorStrategyById(entity);
    }
}
