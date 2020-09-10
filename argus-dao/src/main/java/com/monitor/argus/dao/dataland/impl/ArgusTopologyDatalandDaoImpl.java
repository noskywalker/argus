package com.monitor.argus.dao.dataland.impl;

import com.monitor.argus.bean.dataland.*;
import com.monitor.argus.dao.dataland.IArgusTopologyDatalandDao;
import com.monitor.argus.bean.dataland.*;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.common.util.UuidUtil;
import com.monitor.argus.dao.mybatis.IBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huxiaolei on 2016/9/20.
 */
@Repository("argusTopologyDatalandDao")
public class ArgusTopologyDatalandDaoImpl implements IArgusTopologyDatalandDao {

    @Autowired
    private IBaseDao baseDao;
    private final Logger logger = LoggerFactory.getLogger(ArgusTopologyDatalandDaoImpl.class);

    @Override
    public boolean addArgusTopologyDataland(ArgusTopologyDatalandEntity argusTopologyDatalandEntity) {
        if (StringUtil.isEmpty(argusTopologyDatalandEntity.getId())) {
            argusTopologyDatalandEntity.setId(UuidUtil.getUUID());
        }
        return baseDao.insert("argusTopologyDatalandMapper.insert", argusTopologyDatalandEntity);
    }

    @Override
    public List<ArgusTopologyDatalandEntity> getDataByDate(String beginDate, String endDate) {
        logger.info("Dao 根据时间获取日志列表,param:{},{}", beginDate, endDate);
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        return baseDao.getList("argusTopologyDatalandMapper.getDataByDate", map);
    }

    @Override
    public int delArgusTopologyDataByDate(String date) {
        logger.info("delArgusTopologyDataByDate,param:{}", date);
        if (!StringUtil.isEmpty(date)) {
            Map<String, String> map = new HashMap<>();
            map.put("endDate", date);
            return baseDao.delete("argusTopologyDatalandMapper.delByDate", map);
        } else {
            return -1;
        }
    }

    @Override
    public boolean addAnalyTopologyDatalandHour(AnalyTopologyDatalandEntity analyTopologyDatalandEntity) {
        if (StringUtil.isEmpty(analyTopologyDatalandEntity.getId())) {
            analyTopologyDatalandEntity.setId(UuidUtil.getUUID());
        }
        return baseDao.insert("analyTopologyDatalandHourMapper.insert", analyTopologyDatalandEntity);
    }

    @Override
    public List<AnalyTopologyDatalandEntity> getAnalyDataByDateHour(String beginDate, String endDate, String nodeKey) {
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("nodeKey", nodeKey);
        return baseDao.getList("analyTopologyDatalandHourMapper.getDataByDate", map);
    }

    @Override
    public int delAnalyTopologyDatalandByDateHour(String date) {
        if (!StringUtil.isEmpty(date)) {
            Map<String, String> map = new HashMap<>();
            map.put("endDate", date);
            return baseDao.delete("analyTopologyDatalandHourMapper.delByDate", map);
        } else {
            return -1;
        }
    }

    @Override
    public boolean addAnalyTopologyDatalandMin(AnalyTopologyDatalandEntity analyTopologyDatalandEntity) {
        if (StringUtil.isEmpty(analyTopologyDatalandEntity.getId())) {
            analyTopologyDatalandEntity.setId(UuidUtil.getUUID());
        }
        return baseDao.insert("analyTopologyDatalandMinMapper.insert", analyTopologyDatalandEntity);
    }

    @Override
    public List<AnalyTopologyDatalandEntity> getAnalyDataByDateMin(String beginDate, String endDate, String nodeKey) {
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("nodeKey", nodeKey);
        return baseDao.getList("analyTopologyDatalandMinMapper.getDataByDate", map);
    }

    @Override
    public int delAnalyTopologyDatalandByDateMin(String date) {
        if (!StringUtil.isEmpty(date)) {
            Map<String, String> map = new HashMap<>();
            map.put("endDate", date);
            return baseDao.delete("analyTopologyDatalandMinMapper.delByDate", map);
        } else {
            return -1;
        }
    }

    // 基于系统
    @Override
    public List<AnalyTopologySysDatalandEntity> getSysDataByDateHour(String beginDate, String endDate, String systemId) {
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        map.put("systemId", systemId);
        return baseDao.getList("argusSysTopologyDatalandMapper.getSysDataByDateHour", map);
    }

    @Override
    public boolean addSysTopologyDatalandHour(AnalyTopologySysDatalandEntity analyTopologySysDatalandEntity) {
        if (StringUtil.isEmpty(analyTopologySysDatalandEntity.getId())) {
            analyTopologySysDatalandEntity.setId(UuidUtil.getUUID());
        }
        return baseDao.insert("argusSysTopologyDatalandMapper.insert", analyTopologySysDatalandEntity);
    }

    @Override
    public int delSysDataTopologyDatalandByM(String date) {
        if (!StringUtil.isEmpty(date)) {
            Map<String, String> map = new HashMap<>();
            map.put("endDate", date);
            return baseDao.delete("argusSysTopologyDatalandMapper.delSysDataTopologyDatalandByM", map);
        } else {
            return -1;
        }
    }

    @Override
    public List<ArgusTopologyMorSysSumEntity> getMorSysSumData(String beginDate, String endDate) {
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        return baseDao.getList("argusTopologyDatalandMapper.getMorSysSumData", map);
    }

    @Override
    public List<AnalyTopologyDatalandLogDiff> getDiffLogBytes(String beginDate, String endDate) {
        Map<String, String> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        return baseDao.getList("argusTopologyDatalandMapper.getDiffLogBytes", map);
    }

    @Override
    public AnalyTopologyDatalandEntity getDiffLogCountByKey(String nodeKey) {

        return baseDao.get("analyTopologyDatalandMinMapper.getDiffLogCountByKey", nodeKey);
    }

    @Override
    public boolean addAnalyTopologyDayUV(AnalyTopologyDayUVEntity analyTopologyDayUVEntity) {
        if (StringUtil.isEmpty(analyTopologyDayUVEntity.getId())) {
            analyTopologyDayUVEntity.setId(UuidUtil.getUUID());
        }
        return baseDao.insert("analyTopologyDayUVMapper.insert", analyTopologyDayUVEntity);
    }

    @Override
    public boolean addAnalyTopologyDayIT(AnalyTopologyHourITEntity analyTopologyHourITEntity) {
        return baseDao.insert("analyTopologyDayITMapper.insert", analyTopologyHourITEntity);
    }

    @Override
    public int delAnalyTopologyDayIT(String date) {
        logger.info("delAnalyTopologyDayIT,param:{}", date);
        if (!StringUtil.isEmpty(date)) {
            Map<String, String> map = new HashMap<>();
            map.put("endDate", date);
            return baseDao.delete("analyTopologyDayITMapper.delByDate", map);
        } else {
            return -1;
        }
    }

}
