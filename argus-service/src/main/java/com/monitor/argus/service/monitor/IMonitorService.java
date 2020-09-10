package com.monitor.argus.service.monitor;

import com.monitor.argus.bean.monitor.MonitorHostEntity;
import com.monitor.argus.bean.monitor.MonitorStrategyEntity;
import com.monitor.argus.bean.monitor.MonitorSystemEntity;

import java.util.List;

/**
 * Created by xuefei on 7/7/16.
 */
public interface IMonitorService {
    boolean addMonitorHost(MonitorHostEntity monitorHostEntity);

    int addMonitorHostBatch(List<MonitorHostEntity> monitorHostEntityList);

    boolean addMonitorSystem(MonitorSystemEntity monitorSystemEntity);

    boolean addMonitorStrategy(MonitorStrategyEntity monitorStrategyEntity);

    int addMonitorStrategyBatch(List<MonitorStrategyEntity> monitorStrategyEntityList);

    List<MonitorSystemEntity> searchMonitorSystem();

    MonitorSystemEntity getMonitorSystemById(String id);

    List<MonitorHostEntity> getMonitorHostByMonitorSystemId(String monitorSystemId);

    List<String> getHostIdsSystemId(String systemId);

    List<MonitorStrategyEntity> getMonitorStrategyByMonitorSystemId(String monitorSystemId);

    List<String> getStrategyIdsByMonitorSystemId(String systemId);

    boolean updateMonitorSystem(MonitorSystemEntity monitorSystemEntity);

    boolean deleteMonitorHostBySystemId(String systemId);

    boolean deleteMonitorStrategyBySystemId(String systemId);

    boolean deleteMonitorHostById(String id);

    boolean deleteMonitorStrategyById(String id);

    List<MonitorSystemEntity> getAllSystems();

    List<MonitorHostEntity> getAllHosts();

    List<MonitorStrategyEntity> getStrategyEntityByCondition(MonitorStrategyEntity monitorStrategyEntity);

    void changeStrategyStatus(String id, String status);

    List<MonitorStrategyEntity> getAllEnableStrategies();

    MonitorStrategyEntity getStrategyEntityById(String strategyId);

    boolean updateMonitorStrategy(MonitorStrategyEntity entity);
}

