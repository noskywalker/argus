package com.monitor.argus.bean.alarm.vo;

import com.monitor.argus.bean.IpAddressFlowBean;

import java.util.Comparator;

/**
 * Created by wangfeng on 16/11/8.
 */
public class ComparatorSystemFlowImpl implements Comparator<IpAddressFlowBean> {

    @Override
    public int compare(IpAddressFlowBean s1, IpAddressFlowBean s2) {

        if (null != s1 && null != s2) {
            if (Long.parseLong(s1.getIpFlow()) > Long.parseLong(s2.getIpFlow())) {
                return -1;
            } else if (Long.parseLong(s1.getIpFlow()) < Long.parseLong(s2.getIpFlow())) {
                return 1;
            }
        }
        return 0;
    }

}