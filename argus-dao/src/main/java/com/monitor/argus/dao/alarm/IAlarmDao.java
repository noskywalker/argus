package com.monitor.argus.dao.alarm;


import com.monitor.argus.bean.alarm.AlarmInfoEntity;
import com.monitor.argus.bean.alarm.AlarmStrategyEntity;
import com.monitor.argus.common.model.PageHelper;

import java.util.List;
import java.util.Map;


/**
 *
 */

public interface IAlarmDao {
    boolean addAlarmStrategy(AlarmStrategyEntity alarmStrategyEntity);

    List<AlarmInfoEntity> searchAlarmInfoList();

    List<AlarmStrategyEntity> getAlarmStrategy();

    AlarmStrategyEntity getAlarmStrategyById(String alarmId);

    List<Map<String, String>> getAlarmMethodByAlarmId(String alarmId);

    void insertAlarmInfo(AlarmInfoEntity alarmInfoEntity);

    Long getPageCount();

    List<AlarmInfoEntity> getPageList(PageHelper pageHelper);

    List<AlarmStrategyEntity> getAlarmStrategyByCondition(AlarmStrategyEntity alarmBean);

    boolean editAlarmStrategy(AlarmStrategyEntity alarmStrategyEntity);

    boolean deleteAlarmStrategy(String id);
}
