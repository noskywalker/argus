package com.monitor.argus.common.annotation;

import java.lang.annotation.*;

/**
 * 验证
 * 
 * @Author null
 * @Date 2014-10-30 下午01:33:40
 * 
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {

    /**
     * @author null
     * @date 下午1:13:13
     * 
     * @return
     */
    String value() default "";

}
