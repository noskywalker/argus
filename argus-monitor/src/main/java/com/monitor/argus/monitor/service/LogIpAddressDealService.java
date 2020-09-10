package com.monitor.argus.monitor.service;

import com.monitor.argus.bean.log.LogEntityDTO;
import com.monitor.argus.monitor.statistics.Statistics;
import com.monitor.argus.monitor.strategy.config.IpConfigCache;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * Created by huxiaolei on 2016/11/7.
 */
@Service("logIpAddressDealService")
public class LogIpAddressDealService extends BaseLogService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    IpConfigCache ipConfigCache;

    @Autowired
    RedisService redisService;

    public RedisService getRedisService() {
        return redisService;
    }

    public boolean doIpAddressDeal(LogEntityDTO logEntity) {
        if (IpConfigCache.IS_LOADED_IPCONFIG) {
            String systemId = getSystemIdByIp(logEntity.getIp());
            if (!StringUtil.isEmpty(systemId) && !CollectionUtils.isEmpty(IpConfigCache.sysSets)
                    && IpConfigCache.sysSets.contains(systemId)) {
                ipAddressHandle(logEntity, systemId);
            }
        }
        return true;
    }

    private void ipAddressHandle(LogEntityDTO logEntity, String systemId) {
        Map<String, String> attachIps = logEntity.getAttach();
        if (!MapUtils.isEmpty(attachIps)) {
            String attachIpStr = attachIps.get(ArgusUtils.CONS_CLIENT_IP);
            if (!StringUtil.isEmpty(attachIpStr)) {
                String[] cips = attachIpStr.split(",");
                if (cips != null && cips.length > 0) {
                    ipAddressRecord(cips[0], systemId);
                }
            }
        }
    }

    private void ipAddressRecord(String cip, String systemId) {
        if (StringUtil.isEmpty(cip) || StringUtil.isEmpty(systemId)) {
            return;
        }
        long ipnum = ipConfigCache.getIpnumByIp(cip);
        if (ipnum != -1) {
            if (!MapUtils.isEmpty(Statistics.sysIpnums) && Statistics.sysIpnums.get(systemId) != null) {
                Set ipnums = Statistics.sysIpnums.get(systemId);
                ipnums.add(ipnum);
            }
        }
    }


}
