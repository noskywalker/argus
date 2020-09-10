package com.monitor.argus.mis.task.config;

import com.monitor.argus.bean.job.JobDetailEntity;
import com.monitor.argus.mis.task.annotations.Job;
import com.monitor.argus.service.job.IJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by huxiaolei on 2016/11/28.
 */
@Component("taskConfig")
public class TaskConfig implements BeanPostProcessor,
        ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private ApplicationContext applicationContext;
    private final static Logger logger = LoggerFactory.getLogger(TaskConfig.class);
    private static final String COM_ROOT_PACKAGE_NAME = "com.monitor.argus.mis.task";
    private Set<JobDetailEntity> jobDetails = new HashSet<JobDetailEntity>();

    @Autowired
    private IJobService jobService;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        final String beanAllName = bean.getClass().getCanonicalName();
        if (!beanAllName.startsWith(COM_ROOT_PACKAGE_NAME)) {
            return bean;
        }
        final Class<?> targetClass = AopUtils.getTargetClass(bean);
        ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback() {
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                Job annotation = AnnotationUtils.getAnnotation(method, Job.class);
                if (annotation != null) {
                    logger.info("依次初始化定时任务step1,class:{},method:{}", beanAllName, method.getName());
                    JobDetailEntity job = new JobDetailEntity();
                    job.setCronExp(annotation.cron());
                    job.setJobClass(beanAllName);
                    job.setJobName(annotation.name());
                    job.setValid(true);
                    job.setMethodName(method.getName());
                    jobDetails.add(job);
                }
            }
        });
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() != this.applicationContext) {
            return;
        }
        logger.info("依次初始化定时任务step2,jobDetails:{}", jobDetails.size());
        for (JobDetailEntity job : jobDetails) {
            if (job.getValid()) {
                boolean isNew = true;
                JobDetailEntity jobDetail = jobService.findJobByName(job.getJobName());
                if (jobDetail == null) {
                    isNew = true;
                    jobDetail = job;
                } else {
                    isNew = false;
                    jobDetail.setMethodName(job.getMethodName());
                    jobDetail.setJobClass(job.getJobClass());
                    jobDetail.setCronExp(job.getCronExp());
                }
                if (jobDetail.getValid()) {
                    jobService.schedule(jobDetail, isNew);
                }
            }
        }
    }

}
