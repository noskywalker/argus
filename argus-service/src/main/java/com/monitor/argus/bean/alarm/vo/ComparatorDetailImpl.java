package com.monitor.argus.bean.alarm.vo;

import java.util.Comparator;

/**
 * Created by usr on 2016/11/12.
 */
public class ComparatorDetailImpl implements Comparator<SystemMonitorAlarmDetailEntity> {

    @Override
    public int compare(SystemMonitorAlarmDetailEntity s1, SystemMonitorAlarmDetailEntity s2) {

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
