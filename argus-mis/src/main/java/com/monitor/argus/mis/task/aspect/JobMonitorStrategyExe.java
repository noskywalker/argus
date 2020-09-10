package com.monitor.argus.mis.task.aspect;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.monitor.argus.bean.job.JobDetailEntity;
import com.monitor.argus.bean.monitor.MonitorStrategyEntity;
import com.monitor.argus.service.job.impl.JobService;
import com.monitor.argus.service.monitor.IMonitorService;
import com.monitor.argus.common.enums.StrategyType;
import com.monitor.argus.common.util.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by hxl on 2016/12/19.
 */
@Service
public class JobMonitorStrategyExe {

    private static Logger logger = LoggerFactory.getLogger(JobMonitorStrategyExe.class);

    @Autowired
    private JobService jobService;

    @Autowired
    private IMonitorService monitorService;

    public void executeAsyc(final String jobName, final Object executes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JobDetailEntity jobDetailEntity = jobService.findJobByName(jobName);
                if (jobDetailEntity != null) {
                    String monitorId = jobDetailEntity.getMonitorId();
                    if (!StringUtil.isEmpty(monitorId)) {
                        MonitorStrategyEntity monitorStrategyEntity = monitorService.getStrategyEntityById(monitorId);
                        Integer isRunTime = monitorStrategyEntity.getIsRunTime();
                        int strategyType = monitorStrategyEntity.getStrategyType();
                        // 任务数据监控&非实时
                        if ((StrategyType.JOB.getTypeCode() == strategyType) && (isRunTime != null && isRunTime == 0)) {
                            logger.info("定时任务:{},监控策略执行开始==========", jobName);
                            String monitorStrategy = monitorStrategyEntity.getMonitorStrategy();
                            executeStrategy(monitorStrategy, executes);
                            long nowtime = System.currentTimeMillis();
                            logger.info("定时任务:{},监控策略执行完成,耗时:{}ms==========", jobName, String.valueOf(System.currentTimeMillis() - nowtime));
                        }
                    }
                }
            }
        }).start();
    }

    public void executeStrategy(String monitorStrategy, Object executes) {
        Map<String, String> monitorImplClass = JSON.parseObject(monitorStrategy, new TypeReference<Map<String, String>>() {});
        if (!MapUtils.isEmpty(monitorImplClass)) {
            String classStr = monitorImplClass.get("monitorImplClass");
        }
    }

}
