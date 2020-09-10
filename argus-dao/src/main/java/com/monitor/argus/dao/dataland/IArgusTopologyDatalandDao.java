package com.monitor.argus.dao.dataland;

import com.monitor.argus.bean.dataland.*;
import com.monitor.argus.bean.dataland.*;

import java.util.List;

/**
 * Created by huxiaolei on 2016/9/20.
 */
public interface IArgusTopologyDatalandDao {

    // 总日志
    boolean addArgusTopologyDataland(ArgusTopologyDatalandEntity argusTopologyDatalandEntity);

    List<ArgusTopologyDatalandEntity> getDataByDate(String beginDate, String endDate);

    int delArgusTopologyDataByDate(String date);

    // 节点日志-小时
    boolean addAnalyTopologyDatalandHour(AnalyTopologyDatalandEntity analyTopologyDatalandEntity);

    List<AnalyTopologyDatalandEntity> getAnalyDataByDateHour(String beginDate, String endDate, String nodeKey);

    int delAnalyTopologyDatalandByDateHour(String date);

    // 节点日志-分钟
    boolean addAnalyTopologyDatalandMin(AnalyTopologyDatalandEntity analyTopologyDatalandEntity);

    List<AnalyTopologyDatalandEntity> getAnalyDataByDateMin(String beginDate, String endDate, String nodeKey);

    int delAnalyTopologyDatalandByDateMin(String date);

    // 基于系统
    List<AnalyTopologySysDatalandEntity> getSysDataByDateHour(String beginDate, String endDate, String systemId);

    boolean addSysTopologyDatalandHour(AnalyTopologySysDatalandEntity analyTopologySysDatalandEntity);

    int delSysDataTopologyDatalandByM(String date);

    List<ArgusTopologyMorSysSumEntity> getMorSysSumData(String beginDate, String endDate);

    List<AnalyTopologyDatalandLogDiff> getDiffLogBytes(String beginDate, String endDate);

    AnalyTopologyDatalandEntity getDiffLogCountByKey(String nodeKey);

    // uv数据记录
    boolean addAnalyTopologyDayUV(AnalyTopologyDayUVEntity analyTopologyDayUVEntity);

    // 接口数据记录
    boolean addAnalyTopologyDayIT(AnalyTopologyHourITEntity analyTopologyHourITEntity);

    int delAnalyTopologyDayIT(String date);
}
