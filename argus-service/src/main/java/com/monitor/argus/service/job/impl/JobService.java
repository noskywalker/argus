package com.monitor.argus.service.job.impl;

import com.google.common.collect.Maps;
import com.monitor.argus.service.job.IJobService;
import com.monitor.argus.bean.job.JobDetailEntity;
import com.monitor.argus.common.util.SpringContextUtil;
import com.monitor.argus.dao.job.IJobDetailDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by huxiaolei on 2016/11/28.
 */
@Service("jobService")
public class JobService implements IJobService {

    @Autowired
    IJobDetailDao jobDetailDao;

    private final Map<String, ScheduledFuture<?>> scheduledFutures = Maps.newConcurrentMap();
    private TaskScheduler taskScheduler = new ConcurrentTaskScheduler(Executors.newScheduledThreadPool(1));
    private final static Logger logger = LoggerFactory.getLogger(JobService.class);


    // 取消指定的任务
    private void cancel(String jobName) {
        ScheduledFuture r = scheduledFutures.get(jobName);
        if (r != null) {
            r.cancel(true);
        }
    }

    // 执行指定的任务
    public void execute(String jobName) {
        JobDetailEntity job = this.findJobByName(jobName);
        if (job != null) {
            try {
                Object o = SpringContextUtil.getBean(Class.forName(job.getJobClass()));
                ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(o.getClass(), job.getMethodName()), o);
                job.setSuccess(true);
            } catch (Exception e) {
                job.setSuccess(false);
                e.printStackTrace();
            }
            Date d = new Date();
            job.setLastExecTime(d);
            jobDetailDao.updateJob(job);
        }
    }

    // 停止指定的任务
    public void stopJob(String jobName) {
        JobDetailEntity job = this.findJobByName(jobName);
        if (job != null) {
            job.setValid(false);
            jobDetailDao.updateJob(job);
        }
        this.cancel(jobName);
    }

    // 重启指定的任务
    public void startJob(String jobName) {
        JobDetailEntity job = this.findJobByName(jobName);
        if (job != null) {
            job.setValid(true);
            // jobDetailDao.updateJob(job);
            this.schedule(job, false);
        }
    }

    // 重置cron表达式
    public void resetCron(String jobName, String cron) {
        JobDetailEntity job = this.findJobByName(jobName);
        if (job != null) {
            job.setCronExp(cron);
            // jobDetailDao.updateJob(job);
            if (job.getValid()) {
                this.cancel(jobName);
                this.schedule(job, false);
            }
        }
    }

    public void schedule(final JobDetailEntity job, boolean isNew) {
        CronTrigger trigger = new CronTrigger(job.getCronExp());
        final CronSequenceGenerator sequenceGenerator =
                new CronSequenceGenerator(job.getCronExp(), TimeZone.getDefault());
        job.setNextExecTime(sequenceGenerator.next(new Date()));
        if (isNew) {
            jobDetailDao.insertJob(job);
        } else {
            jobDetailDao.updateJob(job);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 每一次执行任务都需要获取最新的任务状态
                JobDetailEntity currentJob = SpringContextUtil.getBean(JobService.class).findJobByName(job.getJobName());
                if (currentJob != null && currentJob.getValid()) {
                    try {
                        Object o = SpringContextUtil.getBean(Class.forName(currentJob.getJobClass()));
                        ReflectionUtils.invokeMethod(
                                ReflectionUtils.findMethod(o.getClass(), currentJob.getMethodName()), o);
                        currentJob.setSuccess(true);
                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                        currentJob.setSuccess(false);
                    }
                    Date d = new Date();
                    currentJob.setLastExecTime(d);
                    currentJob.setNextExecTime(sequenceGenerator.next(new Date()));
                    jobDetailDao.updateJob(currentJob);
                } else {
                    cancel(currentJob.getJobName());
                }
            }
        };
        this.scheduledFutures.put(job.getJobName(), taskScheduler.schedule(runnable, trigger));
    }

    public JobDetailEntity findJobByName(String jobName) {
        JobDetailEntity jobDetailEntity = jobDetailDao.findJobByName(jobName);
        return jobDetailEntity;
    }


    @Override
    public List<JobDetailEntity> getAllJob() {
        return jobDetailDao.getAllJob();
    }

    @Override
    public boolean insertJob(JobDetailEntity jobDetailEntity) {
        return jobDetailDao.insertJob(jobDetailEntity);
    }

    @Override
    public boolean updateJob(JobDetailEntity jobDetailEntity) {
        return jobDetailDao.updateJob(jobDetailEntity);
    }

    @Override
    public JobDetailEntity findJobById(String id) {
        return jobDetailDao.findJobById(id);
    }

    @Override
    public void cancelMonitor(String id) {
        jobDetailDao.cancelMonitor(id);
    }
}
