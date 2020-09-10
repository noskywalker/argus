package com.monitor.argus.mis.task;

import com.monitor.argus.mis.task.annotations.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by huxiaolei on 2016/11/28.
 */
@Component
public class JobBaseTask {

    private final static Logger logger = LoggerFactory.getLogger(JobBaseTask.class);

    @Job(name = "接口信息汇总任务(示例)", cron = "0 0/5 * * * ?")
    public void analyTopologyHourITTask() {
        logger.info("接口信息汇总任务(示例)开始======");
    }

    @Job(name = "节点信息汇总任务(示例)", cron = "0 0/2 * * * ?")
    public void analyTopologyHourJDTask() {
        logger.info("节点信息汇总任务(示例)开始======");
    }

}
