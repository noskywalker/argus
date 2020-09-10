package com.monitor.argus.bean.system;

import java.util.List;

/**
 * Created by wangfeng on 16/8/31.
 */
public class MenuAuth extends AuthBean {
    private List<AuthBean> kidAuth;

    public List<AuthBean> getKidAuth() {
        return kidAuth;
    }

    public void setKidAuth(List<AuthBean> kidAuth) {
        this.kidAuth = kidAuth;
    }
}
