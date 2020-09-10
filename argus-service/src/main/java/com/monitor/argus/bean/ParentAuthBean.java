package com.monitor.argus.bean;

import com.monitor.argus.bean.system.AuthBean;

import java.util.List;

/**
 * Created by wangfeng on 16/8/22.
 */
public class ParentAuthBean extends AuthBean {
    private List<AuthBean> kidList;
    private AuthBean auth;

    public List<AuthBean> getKidList() {
        return kidList;
    }

    public void setKidList(List<AuthBean> kidList) {
        this.kidList = kidList;
    }
}
