package com.monitor.argus.monitor.service;

import com.monitor.argus.bean.monitor.MonitorHostEntity;
import com.monitor.argus.monitor.strategy.config.AsycConfigCacheService;
import com.monitor.argus.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by huxiaolei on 2016/11/7.
 */
public class BaseLogService {

    @Autowired
    AsycConfigCacheService asycConfigCacheService;

    public String getSystemIdByIp(String ip) {
        String systemId = "";
        List<MonitorHostEntity> mhosts = asycConfigCacheService.getAllHosts();
        if (!CollectionUtils.isEmpty(mhosts)) {
            for (MonitorHostEntity mhost : mhosts) {
                if (mhost != null && !StringUtil.isEmpty(ip)
                        && ip.equals(mhost.getIp())) {
                    systemId = mhost.getSystemId();
                    break;
                }
            }
        }
        return systemId;
    }
}
