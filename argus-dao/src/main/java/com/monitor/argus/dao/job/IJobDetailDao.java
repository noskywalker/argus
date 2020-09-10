package com.monitor.argus.dao.job;

import com.monitor.argus.bean.job.JobDetailEntity;

import java.util.List;

/**
 * Created by hxl on 2016/11/28.
 */
public interface IJobDetailDao {

    JobDetailEntity findJobByName(String jobName);

    boolean insertJob(JobDetailEntity jobDetailEntity);

    boolean updateJob(JobDetailEntity jobDetailEntity);

    List<JobDetailEntity> getAllJob();

    JobDetailEntity findJobById(String id);

    void cancelMonitor(String id);

}
