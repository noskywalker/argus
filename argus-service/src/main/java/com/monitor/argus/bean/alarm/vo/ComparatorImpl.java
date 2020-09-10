package com.monitor.argus.bean.alarm.vo;

import java.util.Comparator;

/**
 * Created by wangfeng on 16/11/3.
 */
public class ComparatorImpl implements Comparator<SystemMonitorAlarmEntity> {

    @Override
    public int compare(SystemMonitorAlarmEntity s1, SystemMonitorAlarmEntity s2) {

        if (null != s1 && null != s2) {
            if (Long.parseLong(s1.getCount()) > Long.parseLong(s2.getCount())) {
                return -1;
            } else if (Long.parseLong(s1.getCount()) < Long.parseLong(s2.getCount())) {
                return 1;
            }
        }
        return 0;
    }

}