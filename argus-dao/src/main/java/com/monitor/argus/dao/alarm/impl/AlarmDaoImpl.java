package com.monitor.argus.dao.alarm.impl;

import com.monitor.argus.bean.alarm.AlarmInfoEntity;
import com.monitor.argus.bean.alarm.AlarmStrategyEntity;
import com.monitor.argus.dao.alarm.IAlarmDao;
import com.monitor.argus.dao.mybatis.IBaseDao;
import com.monitor.argus.common.model.PageHelper;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by xuefei on 7/11/16.
 */
@Repository("AlarmDao")
public class AlarmDaoImpl implements IAlarmDao {

    @Autowired
    private IBaseDao baseDao;

    private final Logger logger = LoggerFactory.getLogger(AlarmDaoImpl.class);

    @Override
    public boolean addAlarmStrategy(AlarmStrategyEntity alarmStrategyEntity) {
        logger.info("Dao 插入报警策略信息");
        alarmStrategyEntity.setId(UuidUtil.getUUID());
        return baseDao.insert("alertStrategyMapper.insert", alarmStrategyEntity);
    }

    @Override
    public List<AlarmInfoEntity> searchAlarmInfoList() {
        logger.info("Dao 搜索报警信息");
        return baseDao.getList("alertInfoMapper.selectAllAlarmInfo");
    }

    @Override
    public List<AlarmStrategyEntity> getAlarmStrategy() {
        logger.info("Dao 查询报警策略");
        return baseDao.getList("alertStrategyMapper.selectAllAlarmStrategy");
    }

    @Override
    public AlarmStrategyEntity getAlarmStrategyById(String alarmId) {
        logger.info("Dao 查询报警策略 alarmId : " + alarmId);
        return baseDao.get("alertStrategyMapper.selectAlarmStrategyByPrimaryKey", alarmId);
    }

    @Override
    public List<Map<String, String>> getAlarmMethodByAlarmId(String alarmId) {
        return baseDao.getList("alertStrategyMapper.selectAlarmOfAlarmGroup", alarmId);
    }

    @Override
    public void insertAlarmInfo(AlarmInfoEntity alarmInfoEntity) {
        baseDao.insert("alertInfoMapper.insert", alarmInfoEntity);
    }

    @Override
    public Long getPageCount() {
        return baseDao.get("alertInfoMapper.selectPageCount");
    }

    @Override
    public List<AlarmInfoEntity> getPageList(PageHelper pageHelper) {
        return baseDao.getList("alertInfoMapper.selectPageList", pageHelper);
    }

    @Override
    public List<AlarmStrategyEntity> getAlarmStrategyByCondition(AlarmStrategyEntity alarmBean) {
        logger.info("Dao 查询报警策略,param:" + JsonUtil.beanToJson(alarmBean));
        return baseDao.getList("alertStrategyMapper.selectAlarmStrategy", alarmBean);
    }

    @Override
    public boolean editAlarmStrategy(AlarmStrategyEntity alarmStrategyEntity) {
        logger.info("Dao 更新报警策略,Param:{}", JsonUtil.beanToJson(alarmStrategyEntity));
        return baseDao.update("alertStrategyMapper.updateAlarmStrategy", alarmStrategyEntity);
    }

    @Override
    public boolean deleteAlarmStrategy(String id) {
        logger.info("Dao 删除报警策略。param:id={}", id);
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        int i = baseDao.delete("alertStrategyMapper.deleteAlarmStrategy", map);
        return i > 0 ? true : false;
    }

}
