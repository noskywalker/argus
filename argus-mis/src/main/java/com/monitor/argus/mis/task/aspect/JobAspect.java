package com.monitor.argus.mis.task.aspect;

import com.monitor.argus.mis.task.annotations.Job;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by hxl on 2016/12/1.
 */
@Aspect
@Component
public class JobAspect {

    @Autowired
    private JobMonitorStrategyExe jobMonitorStrategyExe;

    private static Logger logger = LoggerFactory.getLogger(JobAspect.class);

    @Around("@annotation(job)")
    public Object jobAspectMethod(ProceedingJoinPoint pjp, Job job) throws Throwable {
        String name = "";
        String cron = "";
        if (job != null) {
            name = job.name();
            cron = job.cron();
        }
        String logHeader = name + "(" + cron + ")";
        long nowtime = System.currentTimeMillis();
        logger.info("{}", logHeader + "开始####");
        Object executes = pjp.proceed();
        // 执行完毕获取校验数据
        // jobMonitorStrategyExe.executeAsyc(name, executes);
        long subtime = System.currentTimeMillis() - nowtime;
        logger.info("{},耗时:{}", logHeader + "结束####", String.valueOf(subtime));
        return executes;
    }
}
