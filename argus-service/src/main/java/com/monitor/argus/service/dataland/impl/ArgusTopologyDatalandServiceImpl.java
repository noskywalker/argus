package com.monitor.argus.service.dataland.impl;

import com.monitor.argus.bean.dataland.AnalyTopologyDatalandLogDiff;
import com.monitor.argus.bean.dataland.ArgusTopologyDatalandEntity;
import com.monitor.argus.bean.dataland.ArgusTopologyMorSysSumEntity;
import com.monitor.argus.common.util.DateUtil;
import com.monitor.argus.dao.dataland.IArgusTopologyDatalandDao;
import com.monitor.argus.service.dataland.IArgusTopologyDatalandService;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huxiaolei on 2016/9/20.
 */
@Service("argusTopologyDatalandService")
public class ArgusTopologyDatalandServiceImpl implements IArgusTopologyDatalandService {

    @Autowired
    IArgusTopologyDatalandDao iArgusTopologyDatalandDao;
    private final Logger logger = LoggerFactory.getLogger(ArgusTopologyDatalandServiceImpl.class);

    @Override
    public boolean addArgusTopologyDataland(ArgusTopologyDatalandEntity argusTopologyDatalandEntity) {
        return iArgusTopologyDatalandDao.addArgusTopologyDataland(argusTopologyDatalandEntity);
    }

    @Override
    public Map<String, List<ArgusTopologyDatalandEntity>> getDataByDate(Integer hours) {
        logger.info("Service 获取时间段内的日志数据");
        if (hours == null || hours == 0) {
            hours = 24;
        }
        Date endDate = new Date();
        Date beginDate = DateUtils.addHours(endDate, -12);
        String beginStr = DateUtil.getDateLongTimePlusNoMinuteStr(beginDate);
        String endStr = DateUtil.getDateLongTimePlusNoMinuteStr(endDate);


        List<ArgusTopologyDatalandEntity> list1 = iArgusTopologyDatalandDao.getDataByDate(beginStr, endStr);

        beginDate = DateUtils.addHours(beginDate, -hours);
        endDate = DateUtils.addHours(endDate, -hours);
        beginStr = DateUtil.getDateLongTimePlusNoMinuteStr(beginDate);
        endStr = DateUtil.getDateLongTimePlusNoMinuteStr(endDate);

        List<ArgusTopologyDatalandEntity> list2 = iArgusTopologyDatalandDao.getDataByDate(beginStr, endStr);

        Map<String, List<ArgusTopologyDatalandEntity>> map = new HashMap<>();
        map.put("todayData", list1);
        map.put("yesterdayData", list2);
        return map;


    }

    @Override
    public int delArgusTopologyDataByDate(String date) {
        return iArgusTopologyDatalandDao.delArgusTopologyDataByDate(date);
    }

    @Override
    public List<ArgusTopologyMorSysSumEntity> getMorSysSumData() {
        Date end = new Date();
        Date begin = DateUtils.addDays(end, -14);
        String beginDate = DateUtil.getDateLongTimePlusStr(begin);
        String endDate = DateUtil.getDateLongTimePlusStr(end);
        return iArgusTopologyDatalandDao.getMorSysSumData(beginDate, endDate);
    }

    @Override
    public List<AnalyTopologyDatalandLogDiff> getDiffLogBytes(Integer days) {
        Date endDate = new Date();
        Date beginDate = DateUtils.addDays(endDate, -days);
        endDate = DateUtils.addDays(endDate, 1);
        String beginDateStr = DateUtil.getSimpleDateTimeStr(beginDate);
        String endDateStr = DateUtil.getSimpleDateTimeStr(endDate);
        return iArgusTopologyDatalandDao.getDiffLogBytes(beginDateStr, endDateStr);
    }

    @Override
    public List<ArgusTopologyDatalandEntity> getSumMonitorByDay(Integer hours) {
        Date endDate = new Date();
        Date beginDate = DateUtils.addHours(endDate, -hours);
        String beginDateStr = DateUtil.getDateLongTimePlusStr(beginDate);
        String endDateStr = DateUtil.getDateLongTimePlusStr(endDate);
        return iArgusTopologyDatalandDao.getDataByDate(beginDateStr, endDateStr);

    }

}
