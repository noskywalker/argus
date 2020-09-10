package com.monitor.argus.monitor.strategy.config;

import com.monitor.argus.bean.monitor.MonitorHostEntity;
import com.monitor.argus.bean.monitor.MonitorStrategyEntity;
import com.monitor.argus.bean.monitor.MonitorSystemEntity;
import com.monitor.argus.dao.monitor.IMonitorDao;
import com.monitor.argus.monitor.statistics.Statistics;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.monitor.strategy.StrategyFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Email:alex zhang
 * Creator:usr
 * CreatedDate:七月
 * Version:V1.0.0
 */
@Service
public class StrategyCache {

    public volatile boolean IS_LOADED=false;
    Logger logger= LoggerFactory.getLogger(getClass());

    ReentrantLock reloadLock=new ReentrantLock();

    @Autowired
    IMonitorDao monitorDao;

    static final int DEFAULT_CONCURRENT_LEVEL=32;

    final ConcurrentHashMap<String,List<StrategyConfig>> monitorStrategies=new ConcurrentHashMap<String,List<StrategyConfig>>(DEFAULT_CONCURRENT_LEVEL);


    public ConcurrentHashMap<String,List<StrategyConfig>> getMonitorStrategieConfigs(){
        return monitorStrategies;
    }
    public List<StrategyConfig> getStrategyConfig(String ip){
       return  monitorStrategies.get(ip);
    }

    private boolean currentThreadReloadShouldAwait() {

        if (reloadLock.isLocked() && !reloadLock.isHeldByCurrentThread()) {
            return true;
        } else {
            return false;
        }
    }


    private void loadConfig(){
        try {
            /**if the lock is not locked by myself,then sleep a reload cycle and retry*/
            while(currentThreadReloadShouldAwait()){
                logger.info("监控信息同步守护线程同步并发冲撞，等待一段时间再试!tid:{}",Thread.currentThread().getId());
                Thread.sleep(ArgusUtils.INTERVAL_FOR_MONITOR_CONFIG_RELOAD);
            }
            reloadLock.lock();

            logger.info("清除监控并重新加载监控配置信息..");
            monitorStrategies.clear();

            long begin = System.currentTimeMillis();
            java.util.List<MonitorStrategyEntity> allStrategies = monitorDao.getAllEnableStrategies();
            java.util.List<MonitorSystemEntity> allSystems = monitorDao.getAllSystems();
            java.util.List<MonitorHostEntity> allHosts = monitorDao.getAllHosts();

            // 初始化系统统计信息
            if (!CollectionUtils.isEmpty(allHosts)) {
                for (MonitorHostEntity monitorHostEntity : allHosts) {
                    if (Statistics.ipStatisticsData != null &&
                            !Statistics.ipStatisticsData.containsKey(monitorHostEntity.getIp())) {
                        Statistics.ipStatisticsData.put(monitorHostEntity.getIp(), new AtomicLong(0L));
                    }
                }
            }
            // 初始化系统监控报警信息
            if (!CollectionUtils.isEmpty(allSystems)) {
                for (MonitorSystemEntity systemEntity : allSystems) {
                    if (Statistics.statisticsSystemMonitor != null &&
                            !Statistics.statisticsSystemMonitor.containsKey(systemEntity.getId())) {
                        Statistics.statisticsSystemMonitor.put(systemEntity.getId(), new AtomicLong(0L));
                    }
                    if (Statistics.statisticsSystemAlarm != null &&
                            !Statistics.statisticsSystemAlarm.containsKey(systemEntity.getId())) {
                        Statistics.statisticsSystemAlarm.put(systemEntity.getId(), new AtomicLong(0L));
                    }
                    Statistics.statisticsSystemMXMonitor.put(systemEntity.getId(), new  ConcurrentHashMap<String, AtomicLong>());
                    Statistics.statisticsSystemMXAlarm.put(systemEntity.getId(), new  ConcurrentHashMap<String, AtomicLong>());
                }
            }

            for (MonitorStrategyEntity strategyEntity : allStrategies) {
                Integer isRunTime = strategyEntity.getIsRunTime();
                if (isRunTime != null && isRunTime.intValue() == 0) {
                    continue;
                }

                if (StringUtil.isEmpty(strategyEntity.getMonitorStrategy())) {
                    continue;
                }

                StrategyType thisType = StrategyType.typeOf(strategyEntity.getStrategyType());
                StrategyConfig configCheck = StrategyFactory.getStrategyConfig(thisType);
                if (configCheck == null) {
                    continue;
                }


                ConcurrentHashMap<String, AtomicLong> statisticsMonitorCount = Statistics.statisticsSystemMXMonitor.get(strategyEntity.getSystemId());
                if (statisticsMonitorCount != null && !statisticsMonitorCount.containsKey(strategyEntity.getId())) {
                    statisticsMonitorCount.put(strategyEntity.getId(), new AtomicLong(0L));
                }

                ConcurrentHashMap<String, AtomicLong> statisticsAlarmCount = Statistics.statisticsSystemMXAlarm.get(strategyEntity.getSystemId());
                if (statisticsAlarmCount != null && !statisticsAlarmCount.containsKey(strategyEntity.getAlarmId())) {
                    statisticsAlarmCount.put(strategyEntity.getAlarmId(), new AtomicLong(0L));
                }

                MonitorSystemEntity systemEntity = getSystemEntityBySystemId(strategyEntity.getSystemId(), allSystems);

                List<MonitorHostEntity> hosts = getMonitorhostsBySystemId(strategyEntity.getSystemId(), allHosts);
                for (MonitorHostEntity hostEntity : hosts) {

                    StrategyConfig config = StrategyFactory.getStrategyConfig(thisType);

                    config.setLevel(strategyEntity.getPriority());
                    config.setSystemId(systemEntity.getId());
                    config.setSystemName(systemEntity.getSystemName());
                    config.setHostName(hostEntity.getHostName());
                    config.setIp(hostEntity.getIp());
                    config.setMonitorId(strategyEntity.getId());
                    config.setAlarmId(strategyEntity.getAlarmId());
                    config.setStrategyName(strategyEntity.getMonitorName());
                    config.setStatus(strategyEntity.getStrategyStatus());
                    config.setStrategyType(thisType);
                    if (strategyEntity.getSendContent() == null) {
                        config.setSendContent("");
                    } else {
                        config.setSendContent(strategyEntity.getSendContent());
                    }
                    config.setIsSendContent(strategyEntity.getIsSendContent());

//                    config.setSecondsBeforeMerge((int) cycle);
//                    config.setMonitorCountBeforeMerge((int) monitorCountBeforeMerge);
//                    config.setWaitSecondsAfterMerge((int) mergeWaitTimes);
                    /**this content is the full config message comes from the text control*/
                    config.setMonitorStrategyContent(strategyEntity.getMonitorStrategy());
                    config.parseStrategy();
                    List<StrategyConfig> configOfip = monitorStrategies.get(config.getIp());
                    if (configOfip == null) {
                        configOfip =new CopyOnWriteArrayList<>();
                        configOfip.add(config);
                        monitorStrategies.put(config.getIp(), configOfip);
                    } else if (!configOfip.contains(config)) {
                        configOfip.add(config);
                    }
                }
            }
            IS_LOADED=true;
            logger.info("报警策略配置信息加载完毕，耗时:{}ms",System.currentTimeMillis()-begin);
        }catch(Exception e){
            IS_LOADED=false;
            logger.error("报警策略配置信息加载出现异常，监控系统无法启动：{}",e);
        }finally{
            reloadLock.unlock();
        }
    }

    public List<MonitorHostEntity> getMonitorhostsBySystemId(String systemId,List<MonitorHostEntity> hosts){
        List<MonitorHostEntity> hostsList=new CopyOnWriteArrayList<>();
        for(MonitorHostEntity hostEntity:hosts){
            if(StringUtils.equals(systemId,hostEntity.getSystemId())){
                hostsList.add(hostEntity);
            }
        }
        return hostsList;
    }

    public MonitorSystemEntity getSystemEntityBySystemId(String systemId,List<MonitorSystemEntity> systems){
        for(MonitorSystemEntity systemEntity:systems){
            if(StringUtils.equals(systemId,systemEntity.getId())){
                return systemEntity;
            }
        }
        return null;
    }
    @PostConstruct
    public void reload() throws InterruptedException {
//        loadConfig();
        Runnable thread=new Runnable(){
            @Override
            public void run() {

                while (true) {
                    try {
                        logger.info("开始重新加载监控配置信息(每隔1分钟重新加载)..");
                        loadConfig();
                        Thread.sleep(ArgusUtils.INTERVAL_FOR_MONITOR_CONFIG_RELOAD);
                    } catch (InterruptedException e) {
                        logger.warn("重新加载报警配置信息出现异常，原因为:{}", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t=new Thread(thread);
        t.start();
        logger.info("监控配置信息重新加载守护线程启动..");
    }

}
