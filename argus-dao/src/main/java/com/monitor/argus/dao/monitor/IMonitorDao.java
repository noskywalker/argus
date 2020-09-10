package com.monitor.argus.dao.monitor;


import com.monitor.argus.bean.monitor.MonitorHostEntity;
import com.monitor.argus.bean.monitor.MonitorStrategyEntity;
import com.monitor.argus.bean.monitor.MonitorSystemEntity;

import java.util.List;

/**
 *
 */

public interface IMonitorDao {

    /**
     * 增加:监控信息
     *
     * @param monitorHostEntity
     * @return
     * @Author Fei Xue
     * @Date
     * @Version V1.0
     */
    boolean addMonitorHost(MonitorHostEntity monitorHostEntity);

    int addMonitorHostBatch(List<MonitorHostEntity> monitorHostEntityList);

    boolean addMonitorSystem(MonitorSystemEntity monitorSystemEntity);

    boolean addMonitorStrategy(MonitorStrategyEntity monitorStrategyEntity);

    List<MonitorStrategyEntity> getAllEnableStrategies();

    List<MonitorSystemEntity> getAllSystems();

    List<MonitorHostEntity> getAllHosts();

    int addMonitorStrategyBatch(List<MonitorStrategyEntity> monitorStrategyEntityList);

    List<MonitorHostEntity> getHostsByMonitorSystemId(String systemId);

    List<String> getHostIdsSystemId(String systemId);

    List<MonitorStrategyEntity> getStrategiesByMonitorSystemId(String systemId);

    List<String> getStrategyIdsByMonitorSystemId(String systemId);

    MonitorSystemEntity getMonitorSystemById(String id);

    boolean updateMonitorSystem(MonitorSystemEntity monitorSystemEntity);

    boolean deleteMonitorHostById(String id);

    boolean deleteMonitorStrategyById(String id);

    boolean deleteMonitorHostBySystemId(String systemId);

    boolean deleteMonitorStrategyBySystemId(String systemId);

    List<MonitorStrategyEntity> getStrategyByCondition(MonitorStrategyEntity monitorStrategyEntity);

    void changeStrategyStatus(String id, String status);

    boolean updateMonitorStrategyById(MonitorStrategyEntity entity);
}
