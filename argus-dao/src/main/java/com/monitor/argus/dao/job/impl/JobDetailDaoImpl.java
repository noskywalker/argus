package com.monitor.argus.dao.job.impl;

import com.monitor.argus.bean.job.JobDetailEntity;
import com.monitor.argus.dao.job.IJobDetailDao;
import com.monitor.argus.dao.mybatis.IBaseDao;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.common.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hxl on 2016/11/28.
 */
@Repository("jobDetailDao")
public class JobDetailDaoImpl implements IJobDetailDao {

    @Autowired
    private IBaseDao baseDao;
    private final Logger logger = LoggerFactory.getLogger(JobDetailDaoImpl.class);

    @Override
    public JobDetailEntity findJobByName(String jobName) {
        return baseDao.get("jobDetailMapper.findJobByName", jobName);
    }

    @Override
    public boolean insertJob(JobDetailEntity jobDetailEntity) {
        if (StringUtil.isEmpty(jobDetailEntity.getId())) {
            String uuid = UuidUtil.getUUID();
            jobDetailEntity.setId(uuid);
        }
        boolean result = baseDao.insert("jobDetailMapper.insert", jobDetailEntity);
        return result;
    }

    @Override
    public boolean updateJob(JobDetailEntity jobDetailEntity) {
        return baseDao.update("jobDetailMapper.updateJob", jobDetailEntity);
    }

    @Override
    public List<JobDetailEntity> getAllJob() {
        return baseDao.getList("jobDetailMapper.getAllJob");
    }

    @Override
    public JobDetailEntity findJobById(String id) {
        return baseDao.get("jobDetailMapper.findJobById", id);
    }

    @Override
    public void cancelMonitor(String id) {
        baseDao.update("jobDetailMapper.cancelMonitor", id);
    }
}
