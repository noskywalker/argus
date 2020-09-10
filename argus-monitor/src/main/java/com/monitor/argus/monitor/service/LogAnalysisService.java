package com.monitor.argus.monitor.service;

import com.google.common.collect.Sets;
import com.monitor.argus.bean.log.LogEntityDTO;
import com.monitor.argus.bean.node.NodeEntity;
import com.monitor.argus.monitor.statistics.Statistics;
import com.monitor.argus.monitor.strategy.config.AsycConfigCacheService;
import com.monitor.argus.redis.RedisService;
import com.monitor.argus.common.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huxiaolei on 2016/9/20.
 */
@Service("logAnalysisService")
public class LogAnalysisService extends BaseLogService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RedisService redisService;

    @Autowired
    AsycConfigCacheService asycConfigCacheService;

    public RedisService getRedisService() {
        return redisService;
    }

    //配置信息是否加载完毕。消息是否为空。IP是否为空。系统ID是否已配置。
    public void doAnalysis(LogEntityDTO entity, String ip, String uvid) {
        while (!asycConfigCacheService.IS_LOADED) {
            logger.info("waiting for the asycConfigCacheService loading finished!");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String msg = entity.getFullMessage();

        if (StringUtils.isEmpty(msg)) {
            return;
        }
        if (StringUtils.isEmpty(ip)) {
            return;
        }
        Set<String> sysKeys = getSysKeysByIp(ip);
        if (CollectionUtils.isEmpty(sysKeys)) {
            return;
        }

        // 匹配信息统计数据
        statisticsDataByKey(msg, sysKeys, uvid, entity.getTimes());

    }

    private void statisticsDataByKey(String msg, Set<String> sysKeys, String uvid, String times) {
        ConcurrentHashMap<String, Pattern> configInfo = asycConfigCacheService.getConfigInfos();
        if (MapUtils.isEmpty(configInfo)) {
            return;
        }
        Set<String> configInfoKeys = configInfo.keySet();
        if (CollectionUtils.isEmpty(configInfoKeys)) {
            return;
        }
        for (String configInfoKey : configInfoKeys) {
            if (!StringUtil.isEmpty(configInfoKey) && sysKeys.contains(configInfoKey)) {
                Pattern configInfoValue = configInfo.get(configInfoKey);
                if (configInfoValue != null && configInfoValue.matcher(msg).find()) {
                    // 满足条件计算
                    if (!MapUtils.isEmpty(Statistics.statisticsData01) && Statistics.statisticsData01.get(configInfoKey) != null) {
                        AtomicLong totalCounts = Statistics.statisticsData01.get(configInfoKey);
                        totalCounts.incrementAndGet();
                    }
                    // 统计UV
                    if (!StringUtil.isEmpty(uvid) && !MapUtils.isEmpty(Statistics.nodeUVMap) && Statistics.nodeUVMap.get(configInfoKey) != null) {
                        Set nodeUVSet = Statistics.nodeUVMap.get(configInfoKey);
                        nodeUVSet.add(uvid);
                    }
                    // 接口计算
                    if (!StringUtil.isEmpty(configInfoKey) && !CollectionUtils.isEmpty(AsycConfigCacheService.interInfoSets)
                            && AsycConfigCacheService.interInfoSets.contains(configInfoKey)) {
                        if (!StringUtil.isEmpty(times)) {
                            times = times.trim();
                            Matcher ipMatcher = AsycConfigCacheService.numPattern.matcher(times);
                            if (ipMatcher != null && ipMatcher.find()) {
                                if (!MapUtils.isEmpty(Statistics.nodeInterCountMap) && Statistics.nodeInterCountMap.get(configInfoKey) != null) {
                                    List nodeInterCountList = Statistics.nodeInterCountMap.get(configInfoKey);
                                    nodeInterCountList.add(times);
                                }
                            }
                        }
                    }
                }
            }
        }


    }

    private Set<String> getSysKeysByIp(String ip) {
        Set<String> sysKeys = Sets.newHashSet();
        String systemId = getSystemIdByIp(ip);

        if (!StringUtil.isEmpty(systemId)) {
            List<NodeEntity> allNodeConfigs = asycConfigCacheService.getAllNodeConfigs();
            if (!CollectionUtils.isEmpty(allNodeConfigs)) {
                for (NodeEntity allNodeConfig : allNodeConfigs) {
                    if (allNodeConfig != null && systemId.equals(allNodeConfig.getNodeSystemId())) {
                        sysKeys.add(allNodeConfig.getNodeKey());
                    }
                }
            }
        }
        return sysKeys;
    }

}
