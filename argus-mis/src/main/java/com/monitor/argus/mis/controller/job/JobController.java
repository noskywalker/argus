package com.monitor.argus.mis.controller.job;

import com.google.common.collect.Lists;
import com.monitor.argus.bean.job.JobDetailEntity;
import com.monitor.argus.bean.monitor.MonitorStrategyEntity;
import com.monitor.argus.service.job.IJobService;
import com.monitor.argus.service.monitor.IMonitorService;
import com.monitor.argus.common.util.BeanUtil;
import com.monitor.argus.common.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by hxl on 2016/11/29.
 */
@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private IJobService jobService;

    @Autowired
    private IMonitorService monitorService;

    @RequestMapping(value = "/getAllJob", method = RequestMethod.GET)
    public String getAllJob(Model model, String hanId) {
        List<JobDetailEntity> jobList = jobService.getAllJob();
        if (!CollectionUtils.isEmpty(jobList)) {
            for (JobDetailEntity job : jobList) {
                if (job.getLastExecTime() != null) {
                    job.setLastExecTimeStr(DateUtil.getDateLongTimePlusStr(job.getLastExecTime()));
                }

                if (job.getNextExecTime() != null) {
                    job.setNextExecTimeStr(DateUtil.getDateLongTimePlusStr(job.getNextExecTime()));
                }
            }
        }
        model.addAttribute("searchResult", jobList);
        model.addAttribute("hanId", hanId);
        return "/job/jobList";
    }

    @RequestMapping(value = "/execute", method = RequestMethod.GET)
    public String execute(@RequestParam(value = "id", required = true) String id,
                          @RequestParam(value = "hanId", required = true) String hanId, Model model) {

        JobDetailEntity jobe = jobService.findJobById(id);
        if (jobe != null) {
            jobService.execute(jobe.getJobName());
        }

        List<JobDetailEntity> jobList = jobService.getAllJob();
        if (!CollectionUtils.isEmpty(jobList)) {
            for (JobDetailEntity job : jobList) {
                if (job.getLastExecTime() != null) {
                    job.setLastExecTimeStr(DateUtil.getDateLongTimePlusStr(job.getLastExecTime()));
                }

                if (job.getNextExecTime() != null) {
                    job.setNextExecTimeStr(DateUtil.getDateLongTimePlusStr(job.getNextExecTime()));
                }
            }
        }
        model.addAttribute("searchResult", jobList);
        model.addAttribute("hanId", hanId);
        return "/job/jobList";
    }

    @RequestMapping(value = "/statusUpdate", method = RequestMethod.GET)
    public String statusUpdate(@RequestParam(value = "id", required = true) String id,
                          @RequestParam(value = "hanId", required = true) String hanId, Model model) {

        JobDetailEntity jobe = jobService.findJobById(id);
        if (jobe != null) {
            if (jobe.getValid()) {
                jobService.stopJob(jobe.getJobName());
            } else {
                jobService.startJob(jobe.getJobName());
            }
        }

        List<JobDetailEntity> jobList = jobService.getAllJob();
        if (!CollectionUtils.isEmpty(jobList)) {
            for (JobDetailEntity job : jobList) {
                if (job.getLastExecTime() != null) {
                    job.setLastExecTimeStr(DateUtil.getDateLongTimePlusStr(job.getLastExecTime()));
                }

                if (job.getNextExecTime() != null) {
                    job.setNextExecTimeStr(DateUtil.getDateLongTimePlusStr(job.getNextExecTime()));
                }
            }
        }
        model.addAttribute("searchResult", jobList);
        model.addAttribute("hanId", hanId);
        return "/job/jobList";
    }

    @RequestMapping(value = "/getJobMonitorList", method = RequestMethod.GET)
    public String getJobMonitorList(Model model, String hanId) {
        List<JobDetailEntity> jobList = jobService.getAllJob();
        if (!CollectionUtils.isEmpty(jobList)) {
            for (JobDetailEntity job : jobList) {
                if (job.getLastExecTime() != null) {
                    job.setLastExecTimeStr(DateUtil.getDateLongTimePlusStr(job.getLastExecTime()));
                }
                if (job.getNextExecTime() != null) {
                    job.setNextExecTimeStr(DateUtil.getDateLongTimePlusStr(job.getNextExecTime()));
                }
                if (StringUtils.isNotBlank(job.getMonitorId())) {
                    MonitorStrategyEntity monitorStrategyEntity = monitorService.getStrategyEntityById(job.getMonitorId());
                    job.setMonitorName(monitorStrategyEntity.getMonitorName());
                    job.setMonitorStrategy(monitorStrategyEntity.getMonitorStrategy());
                }
            }
        }
        model.addAttribute("searchResult", jobList);
        model.addAttribute("hanId", hanId);
        return "/job/jobMonitorList";
    }

    @RequestMapping(value = "/configStrategy", method = RequestMethod.GET)
    public String configStrategy(@RequestParam(value = "id", required = true) String id,
                               @RequestParam(value = "hanId", required = true) String hanId, Model model) {
        JobDetailEntity jobDetailEntity = jobService.findJobById(id);
        JobDetailEntity jobDetail = new JobDetailEntity();
        if (jobDetailEntity != null) {
            BeanUtil.copyProperties(jobDetail, jobDetailEntity);
        }
        List<MonitorStrategyEntity> monitorAll = monitorService.getAllEnableStrategies();
        List<MonitorStrategyEntity> monitorCheck = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(monitorAll)) {
            for (MonitorStrategyEntity mse : monitorAll) {
                if (mse.getIsRunTime() != null && mse.getIsRunTime().intValue() == 0) {
                    MonitorStrategyEntity msec = new MonitorStrategyEntity();
                    BeanUtil.copyProperties(msec, mse);
                    monitorCheck.add(msec);
                }
            }
        }
        model.addAttribute("monitorCheck", monitorCheck);
        model.addAttribute("jobDetail", jobDetail);
        model.addAttribute("hanId", hanId);
        return "/job/jobConfigStrategy";
    }

    @RequestMapping(value = "/configStrategyConfirm", method = RequestMethod.POST)
    public String configStrategyConfirm(Model model, String monitorId, String id, String hanId) {
        // 关联策略
        JobDetailEntity jobDetailEntity = jobService.findJobById(id);
        if (jobDetailEntity != null) {
            jobDetailEntity.setMonitorId(monitorId);
            jobService.updateJob(jobDetailEntity);
        }

        // 返回列表
        List<JobDetailEntity> jobList = jobService.getAllJob();
        if (!CollectionUtils.isEmpty(jobList)) {
            for (JobDetailEntity job : jobList) {
                if (job.getLastExecTime() != null) {
                    job.setLastExecTimeStr(DateUtil.getDateLongTimePlusStr(job.getLastExecTime()));
                }
                if (job.getNextExecTime() != null) {
                    job.setNextExecTimeStr(DateUtil.getDateLongTimePlusStr(job.getNextExecTime()));
                }
                if (StringUtils.isNotBlank(job.getMonitorId())) {
                    MonitorStrategyEntity monitorStrategyEntity = monitorService.getStrategyEntityById(job.getMonitorId());
                    job.setMonitorName(monitorStrategyEntity.getMonitorName());
                    job.setMonitorStrategy(monitorStrategyEntity.getMonitorStrategy());
                }
            }
        }
        model.addAttribute("searchResult", jobList);
        model.addAttribute("hanId", hanId);
        return "/job/jobMonitorList";
    }

    @RequestMapping(value = "/configStrategyCancel", method = RequestMethod.GET)
    public String configStrategyCancel(Model model, String id, String hanId) {
        // 取消监控策略
        jobService.cancelMonitor(id);
        // 返回列表
        List<JobDetailEntity> jobList = jobService.getAllJob();
        if (!CollectionUtils.isEmpty(jobList)) {
            for (JobDetailEntity job : jobList) {
                if (job.getLastExecTime() != null) {
                    job.setLastExecTimeStr(DateUtil.getDateLongTimePlusStr(job.getLastExecTime()));
                }
                if (job.getNextExecTime() != null) {
                    job.setNextExecTimeStr(DateUtil.getDateLongTimePlusStr(job.getNextExecTime()));
                }
                if (StringUtils.isNotBlank(job.getMonitorId())) {
                    MonitorStrategyEntity monitorStrategyEntity = monitorService.getStrategyEntityById(job.getMonitorId());
                    job.setMonitorName(monitorStrategyEntity.getMonitorName());
                    job.setMonitorStrategy(monitorStrategyEntity.getMonitorStrategy());
                }
            }
        }
        model.addAttribute("searchResult", jobList);
        model.addAttribute("hanId", hanId);
        return "/job/jobMonitorList";
    }

}
