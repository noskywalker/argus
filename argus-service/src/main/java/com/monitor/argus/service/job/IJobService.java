package com.monitor.argus.service.job;

import com.monitor.argus.bean.job.JobDetailEntity;

import java.util.List;

/**
 * Created by hxl on 2016/11/28.
 */
public interface IJobService {

    JobDetailEntity findJobByName(String jobName);

    void execute(String jobName);

    void stopJob(String jobName);

    void startJob(String jobName);

    void schedule(final JobDetailEntity job, boolean isNew);

    List<JobDetailEntity> getAllJob();

    boolean insertJob(JobDetailEntity jobDetailEntity);

    boolean updateJob(JobDetailEntity jobDetailEntity);

    JobDetailEntity findJobById(String id);

    void cancelMonitor(String id);

}
