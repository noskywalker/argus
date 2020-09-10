package com.monitor.argus.service.alarm;

import com.monitor.argus.bean.alarm.AlarmStrategyEntity;
import com.monitor.argus.bean.alarm.vo.AlarmInfoVO;
import com.monitor.argus.common.model.PageHelper;

import java.util.List;

/**
 * Created by xuefei on 7/7/16.
 */
public interface IAlarmService {
    boolean addAlarmStrategy(AlarmStrategyEntity alarmStrategyEntity);

    List<AlarmInfoVO> searchAlarmsInfo(PageHelper pageHelper);

    List<AlarmStrategyEntity> getAlarmStrategy();

    AlarmStrategyEntity getAlarmStrategyById(String alarmId);

    Long getPageCount();

    List<AlarmStrategyEntity> getAlarmStrategyByCondition(AlarmStrategyEntity alarmBean);

    boolean editAlarmStrategy(AlarmStrategyEntity alarmStrategyEntity);

    boolean deleteAlarmStrategy(String id);

    void shieldAlarm(String alarmId, String openId, String hours);

    void deleteShieldAlarm(String alarmId, String openId);
}
