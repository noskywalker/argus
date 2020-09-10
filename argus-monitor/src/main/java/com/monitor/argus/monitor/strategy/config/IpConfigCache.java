package com.monitor.argus.monitor.strategy.config;

import com.monitor.argus.bean.ipaddress.IpConfigEntity;
import com.monitor.argus.bean.monitor.MonitorSystemEntity;
import com.monitor.argus.dao.ipaddress.IIpConfigDao;
import com.monitor.argus.dao.monitor.IMonitorDao;
import com.monitor.argus.monitor.statistics.Statistics;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.IpUtil;
import com.monitor.argus.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huxiaolei on 2016/11/7.
 */
@Service
public class IpConfigCache {

    Logger logger = LoggerFactory.getLogger(getClass());

    public volatile static boolean IS_LOADED_IPCONFIG = false;

    // 所有IP映射关系
    public volatile static List<IpConfigEntity> ipConfigs = new ArrayList<IpConfigEntity>();

    // ip正则
    public volatile static Pattern ipPattern = Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");

    // 可采集地区数据的系统
    public volatile static Set sysSets = Collections.synchronizedSet(new HashSet<String>());

    ReentrantLock reloadLockAsyIp = new ReentrantLock();

    @Autowired
    IIpConfigDao ipConfigDao;

    @Autowired
    IMonitorDao monitorDao;

    @PostConstruct
    public void loadConfig() {
        IS_LOADED_IPCONFIG = false;
        long startTime = System.currentTimeMillis();
        logger.info("ip地址信息刷新cache开始======时间:{},IS_LOADED:{}", startTime, String.valueOf(IS_LOADED_IPCONFIG));

        java.util.List<MonitorSystemEntity> allSystems = monitorDao.getAllSystems();
        if (!CollectionUtils.isEmpty(allSystems)) {
            for (MonitorSystemEntity monitorSystemEntity : allSystems) {
                if (monitorSystemEntity != null) {
                    if (monitorSystemEntity.getIsIp() == 1) {
                        // 可采集
                        sysSets.add(monitorSystemEntity.getId());
                        Statistics.sysIpnums.put(monitorSystemEntity.getId(), Collections.synchronizedSet(new HashSet<Long>()));
                        Statistics.statisticsSysIpAddressCount.put(monitorSystemEntity.getId(), new  ConcurrentHashMap<String, AtomicLong>());
                    }
                }
            }
        }
        List<IpConfigEntity> ipConfigList = ipConfigDao.getShortIpConfigList();
        if (!CollectionUtils.isEmpty(ipConfigList)) {
            for (IpConfigEntity ipConfigEntity : ipConfigList) {
                Set<String> systemIds = Statistics.statisticsSysIpAddressCount.keySet();
                if (!CollectionUtils.isEmpty(systemIds)) {
                    for (String systemId : systemIds) {
                        if (!StringUtil.isEmpty(systemId)) {
                            ConcurrentHashMap<String, AtomicLong> statisticsIpAddressCount = Statistics.statisticsSysIpAddressCount.get(systemId);
                            if (statisticsIpAddressCount != null && !statisticsIpAddressCount.containsKey(ipConfigEntity.getAddressShort())) {
                                statisticsIpAddressCount.put(ipConfigEntity.getAddressShort(), new AtomicLong(0L));
                            }
                        }
                    }
                }
                ipConfigs.add(ipConfigEntity);
            }
        }

        logger.info("ip地址信息刷新cache结束======ipConfigs:{}," +
                "sysIpnums:{},statisticsSysIpAddressCount:{},sysSets:{}" +
                "耗时:{}ms,IS_LOADED:{}",
                ipConfigs != null ? ipConfigs.size() : 0,
                Statistics.sysIpnums != null ? Statistics.sysIpnums.size() : 0,
                Statistics.statisticsSysIpAddressCount != null ? Statistics.statisticsSysIpAddressCount.size() : 0,
                sysSets != null ? sysSets.size() : 0,
                System.currentTimeMillis() - startTime,
                String.valueOf(IS_LOADED_IPCONFIG));

        // 定时刷新系统采集过滤
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        logger.info("开始采集IP地址信息刷新系统列表(每隔{}毫秒重新加载)..", ArgusUtils.INTERVAL_FOR_IPADDR_CONFIG_RELOAD);
                        ipSystemConfig();
                        Thread.sleep(ArgusUtils.INTERVAL_FOR_IPADDR_CONFIG_RELOAD);
                    } catch (InterruptedException e) {
                        logger.warn("采集IP地址信息刷新系统列表出现异常，原因为:{}", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t = new Thread(thread);
        t.start();
        if (!CollectionUtils.isEmpty(ipConfigs)) {
            IS_LOADED_IPCONFIG = true;
        }
    }

    private void ipSystemConfig() {
        try {
            while(currentThreadReloadShouldAwait()) {
                logger.info("采集IP地址信息刷新系统列表时线程并发冲撞，等待{}毫秒再试!tid:{}",
                        ArgusUtils.INTERVAL_FOR_IPADDR_CONFIG_RELOAD, Thread.currentThread().getId());
                Thread.sleep(ArgusUtils.INTERVAL_FOR_IPADDR_CONFIG_RELOAD);
            }
            reloadLockAsyIp.lock();

            logger.info("采集IP地址信息刷新系统列表开始....");
            sysSets.clear();
            java.util.List<MonitorSystemEntity> allSystems = monitorDao.getAllSystems();
            if (!CollectionUtils.isEmpty(allSystems)) {
                for (MonitorSystemEntity monitorSystemEntity : allSystems) {
                    if (monitorSystemEntity != null) {
                        if (monitorSystemEntity.getIsIp() == 1) {
                            // 可采集
                            sysSets.add(monitorSystemEntity.getId());
                        }
                    }
                }
            }
            logger.info("采集IP地址信息刷新系统列表完毕....,sysSets:{}", sysSets != null ? sysSets.size() : 0);
        } catch (InterruptedException e) {
            logger.warn("采集IP地址信息刷新系统列表出现异常，原因为:{}", e.getMessage());
            e.printStackTrace();
        }  finally {
            reloadLockAsyIp.unlock();
        }
    }

    private boolean currentThreadReloadShouldAwait() {
        if (reloadLockAsyIp.isLocked() && !reloadLockAsyIp.isHeldByCurrentThread()) {
            return true;
        } else {
            return false;
        }
    }

    public long getIpnumByIp(String ip) {
        long ipNum = -1;
        if (!StringUtil.isEmpty(ip)) {
            ip = ip.trim();
            Matcher ipMatcher = ipPattern.matcher(ip);
            if (ipMatcher != null && ipMatcher.find()) {
                ipNum = IpUtil.ipToLong(ip);
            }
        }
        return ipNum;
    }

}
