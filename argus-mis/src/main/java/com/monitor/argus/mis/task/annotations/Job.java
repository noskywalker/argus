package com.monitor.argus.mis.task.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by huxiaolei on 2016/11/28.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Job {
    /**
     * @return 任务名称
     */
    String name();

    /**
     * @return 任务的cron表达式
     */
    String cron();

}
