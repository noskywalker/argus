package com.monitor.argus.service.dataland;

import com.monitor.argus.bean.dataland.AnalyTopologyDatalandLogDiff;
import com.monitor.argus.bean.dataland.ArgusTopologyDatalandEntity;
import com.monitor.argus.bean.dataland.ArgusTopologyMorSysSumEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by huxiaolei on 2016/9/20.
 */
public interface IArgusTopologyDatalandService {

    boolean addArgusTopologyDataland(ArgusTopologyDatalandEntity argusTopologyDatalandEntity);

    Map<String, List<ArgusTopologyDatalandEntity>> getDataByDate(Integer hours);

    int delArgusTopologyDataByDate(String date);

    List<ArgusTopologyMorSysSumEntity> getMorSysSumData();

    List<AnalyTopologyDatalandLogDiff> getDiffLogBytes(Integer days);

    List<ArgusTopologyDatalandEntity> getSumMonitorByDay(Integer days);

}
