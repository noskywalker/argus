package com.monitor.argus.monitor.strategy.config;

import com.google.common.collect.Sets;
import com.monitor.argus.bean.monitor.MonitorHostEntity;
import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.dao.monitor.IMonitorDao;
import com.monitor.argus.dao.node.IMonitorNodeDao;
import com.monitor.argus.monitor.statistics.Statistics;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * Created by huxiaolei on 2016/9/1.
 */
@Service
public class AsycConfigCacheService {

    public volatile boolean IS_LOADED = false;
    static final int DEFAULT_CONCURRENT_LEVEL = 32;
    Logger logger = LoggerFactory.getLogger(getClass());

    ReentrantLock reloadLockAsy = new ReentrantLock();
    // key:URI 配置信息
    ConcurrentHashMap<String, Pattern> configInfo = new ConcurrentHashMap<String, Pattern>(DEFAULT_CONCURRENT_LEVEL);
    // 接口集合
    public volatile static Set interInfoSets = Collections.synchronizedSet(new HashSet<String>());

    // 所有host
    List<MonitorHostEntity> allHosts = new ArrayList<MonitorHostEntity>();

    // 所有节点配置信息
    List<NodeEntity> allNodeConfigs = new ArrayList<NodeEntity>();

    // ip正则
    public volatile static Pattern numPattern = Pattern.compile("^(0|[1-9][0-9]*)$");

    @Autowired
    IMonitorDao monitorDao;

    @Autowired
    IMonitorNodeDao monitorNodeDao;

    public ConcurrentHashMap<String, Pattern> getConfigInfos(){
        return  configInfo;
    }

    public List<MonitorHostEntity> getAllHosts(){
        return  allHosts;
    }
    public List<NodeEntity> getAllNodeConfigs(){
        return  allNodeConfigs;
    }

    private boolean currentThreadReloadShouldAwait() {
        if (reloadLockAsy.isLocked() && !reloadLockAsy.isHeldByCurrentThread()) {
            return true;
        } else {
            return false;
        }
    }

    private void loadConfig() {
        try {
            while(currentThreadReloadShouldAwait()) {
                logger.info("节点配置信息同步到cache时线程并发冲撞，等待{}毫秒再试!tid:{}",
                        ArgusUtils.INTERVAL_FOR_ANALYSIS_CONFIG_RELOAD, Thread.currentThread().getId());
                Thread.sleep(ArgusUtils.INTERVAL_FOR_ANALYSIS_CONFIG_RELOAD);
            }
            reloadLockAsy.lock();

            logger.info("清除cache中节点配置信息并重新加载....");
            configInfo.clear();
            interInfoSets.clear();
            allHosts.clear();
            allNodeConfigs.clear();

            Set<String> validKeys = Sets.newHashSet();
            long begin = System.currentTimeMillis();

            // 初始化所有host
            List<MonitorHostEntity> newAllHosts = monitorDao.getAllHosts();
            if (!CollectionUtils.isEmpty(newAllHosts)) {
                for (MonitorHostEntity newAllHost : newAllHosts) {
                    allHosts.add(newAllHost);
                }
            }

            // 从数据库中获取有效的所有节点信息
            List<NodeEntity> newAllNodes = monitorNodeDao.getAllEnableNodeList();
            if (!CollectionUtils.isEmpty(newAllNodes)) {
                for (NodeEntity newAllNode : newAllNodes) {
                    allNodeConfigs.add(newAllNode);
                }
            }

            if (!CollectionUtils.isEmpty(allNodeConfigs)) {
                for (NodeEntity nodeConfigInfo : allNodeConfigs) {
                    if (nodeConfigInfo != null && !StringUtil.isEmpty(nodeConfigInfo.getNodeKey())) {
                        // 当次刷新时所有有效的key
                        validKeys.add(nodeConfigInfo.getNodeKey());

                        // 刷新配置信息cache，做内容匹配
                        configInfo.put(nodeConfigInfo.getNodeKey(), Pattern.compile(nodeConfigInfo.getNodeUrl()));
                        if (nodeConfigInfo.getIsInterface() == 1) {
                            interInfoSets.add(nodeConfigInfo.getNodeKey());
                        }

                        if (Statistics.statisticsData01 != null &&
                                !Statistics.statisticsData01.containsKey(nodeConfigInfo.getNodeKey())) {
                            Statistics.statisticsData01.put(nodeConfigInfo.getNodeKey(), new AtomicLong(0L));
                        }

                        // 需要采集uv数据
                        if (nodeConfigInfo.getIsUv() == 1) {
                            if (Statistics.nodeUVMap != null &&
                                    !Statistics.nodeUVMap.containsKey(nodeConfigInfo.getNodeKey())) {
                                Statistics.nodeUVMap.put(nodeConfigInfo.getNodeKey(), Collections.synchronizedSet(new HashSet<String>()));
                            }
                        } else {
                            if (Statistics.nodeUVMap != null &&
                                    Statistics.nodeUVMap.containsKey(nodeConfigInfo.getNodeKey())) {
                                Statistics.nodeUVMap.remove(nodeConfigInfo.getNodeKey());
                            }
                        }

                        // 需要进行接口计算
                        if (nodeConfigInfo.getIsInterface() == 1) {
                            if (Statistics.nodeInterCountMap != null &&
                                    !Statistics.nodeInterCountMap.containsKey(nodeConfigInfo.getNodeKey())) {
                                Statistics.nodeInterCountMap.put(nodeConfigInfo.getNodeKey(), Collections.synchronizedList(new ArrayList<String>()));
                            }
                        } else {
                            if (Statistics.nodeInterCountMap != null &&
                                    Statistics.nodeInterCountMap.containsKey(nodeConfigInfo.getNodeKey())) {
                                Statistics.nodeInterCountMap.remove(nodeConfigInfo.getNodeKey());
                            }
                        }
                    }
                }
            }
            if (!MapUtils.isEmpty(Statistics.statisticsData01)) {
                Iterator<Map.Entry<String, AtomicLong>> it = Statistics.statisticsData01.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry<String, AtomicLong> entry = it.next();
                    if (entry != null && !StringUtil.isEmpty(entry.getKey())
                            && !validKeys.contains(entry.getKey())) {
                        it.remove();
                    }
                }
            }

            /*if (!MapUtils.isEmpty(nodeUVMap)) {
                Iterator<Map.Entry<String, Set<String>>> itUv = nodeUVMap.entrySet().iterator();
                while(itUv.hasNext()){
                    Map.Entry<String, Set<String>> entryUv = itUv.next();
                    if (entryUv != null && !StringUtil.isEmpty(entryUv.getKey())
                            && !validKeys.contains(entryUv.getKey())) {
                        itUv.remove();
                    }
                }
            }*/

            IS_LOADED = true;
            logger.info("节点配置信息加载完毕，耗时:{}ms，configInfo:{}，statisticsData01:{}, nodeUVMap:{}, nodeInterCountMap:{}",
                    System.currentTimeMillis() - begin,
                    configInfo != null ? configInfo.size() : 0,
                    Statistics.statisticsData01 != null ? Statistics.statisticsData01.size() : 0,
                    Statistics.nodeUVMap != null ? Statistics.nodeUVMap.size() : 0,
                    Statistics.nodeInterCountMap != null ? Statistics.nodeInterCountMap.size() : 0);
        } catch(Exception e) {
            IS_LOADED = false;
            logger.error("节点配置信息加载到cache时出现异常，监控系统无法启动：{}", e);
        } finally {
            reloadLockAsy.unlock();
        }
    }

    @PostConstruct
    public void reload() throws InterruptedException {
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        logger.info("开始重新加载节点配置信息(每隔{}毫秒重新加载)..", ArgusUtils.INTERVAL_FOR_ANALYSIS_CONFIG_RELOAD);
                        loadConfig();
                        Thread.sleep(ArgusUtils.INTERVAL_FOR_ANALYSIS_CONFIG_RELOAD);
                    } catch (InterruptedException e) {
                        logger.warn("重新加载节点配置信息出现异常，原因为:{}", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t = new Thread(thread);
        t.start();
        logger.info("========####节点配置信息重新加载守护线程启动####========");
    }

}
