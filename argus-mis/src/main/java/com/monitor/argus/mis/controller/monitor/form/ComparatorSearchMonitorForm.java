package com.monitor.argus.mis.controller.monitor.form;

import java.util.Comparator;

/**
 * Created by usr on 2016/11/10.
 */
public class ComparatorSearchMonitorForm implements Comparator<SearchMonitorForm> {
    @Override
    public int compare(SearchMonitorForm s1, SearchMonitorForm s2) {
        if (null != s1 && null != s2) {
            return s1.getSystemName().compareTo(s2.getSystemName());
        }
        return 0;
    }
}
