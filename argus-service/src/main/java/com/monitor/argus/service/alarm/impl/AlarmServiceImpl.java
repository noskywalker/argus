package com.monitor.argus.service.alarm.impl;

import com.monitor.argus.service.alarm.IAlarmService;
import com.monitor.argus.bean.alarm.AlarmInfoEntity;
import com.monitor.argus.bean.alarm.AlarmStrategyEntity;
import com.monitor.argus.bean.alarm.vo.AlarmInfoVO;
import com.monitor.argus.common.model.PageHelper;
import com.monitor.argus.common.util.BeanUtil;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.dao.alarm.IAlarmDao;
import com.monitor.argus.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuefei on 7/7/16.
 */
@Service("alarmService")
public class AlarmServiceImpl implements IAlarmService {

    private final Logger logger = LoggerFactory.getLogger(AlarmServiceImpl.class);

    @Autowired
    IAlarmDao alarmDao;

    @Autowired
    RedisService redisService;

    @Override
    public boolean addAlarmStrategy(AlarmStrategyEntity alarmStrategyEntity) {
        return alarmDao.addAlarmStrategy(alarmStrategyEntity);
    }

    @Override
    public List<AlarmInfoVO> searchAlarmsInfo(PageHelper pageHelper) {
        List<AlarmInfoEntity> pageList = alarmDao.getPageList(pageHelper);
        List<AlarmInfoVO> alarmInfoVOList = new ArrayList<>();
        for (AlarmInfoEntity alarmInfoEntity : pageList) {
            AlarmInfoVO alarmInfoVO = new AlarmInfoVO();
            BeanUtil.copyProperties(alarmInfoVO, alarmInfoEntity);
            alarmInfoVO.setTriggerTime(DateUtil.getDateLongTimePlusStr(alarmInfoEntity.getTriggerTime()));
            alarmInfoVOList.add(alarmInfoVO);
        }
        return alarmInfoVOList;
    }

    @Override
    public List<AlarmStrategyEntity> getAlarmStrategy() {
        return alarmDao.getAlarmStrategy();
    }

    @Override
    public AlarmStrategyEntity getAlarmStrategyById(String alarmId) {
        return alarmDao.getAlarmStrategyById(alarmId);
    }

    @Override
    public Long getPageCount() {
        return alarmDao.getPageCount();
    }

    @Override
    public List<AlarmStrategyEntity> getAlarmStrategyByCondition(AlarmStrategyEntity alarmBean) {
        return alarmDao.getAlarmStrategyByCondition(alarmBean);
    }

    @Override
    public boolean editAlarmStrategy(AlarmStrategyEntity alarmStrategyEntity) {
        logger.info("Service editAlarmStrategy.param:{}", JsonUtil.beanToJson(alarmStrategyEntity));
        return alarmDao.editAlarmStrategy(alarmStrategyEntity);
    }

    @Override
    @Transactional
    public boolean deleteAlarmStrategy(String id) {
        logger.info("Service deleteAlarmStrategy.");
        alarmDao.deleteAlarmStrategy(id);
        return true;
    }

    @Override
    public void shieldAlarm(String alarmId, String openId, String hours) {
        String key = alarmId + ":" + openId;
        Integer time = Integer.parseInt(hours) * 3600;
        redisService.set(key, "0_0");
        redisService.expire(key, time);
    }

    @Override
    public void deleteShieldAlarm(String alarmId, String openId) {
        String key = alarmId + ":" + openId;
        redisService.delete(key);
    }
}
