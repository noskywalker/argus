package com.monitor.argus.common.annotation;

import java.lang.annotation.*;

/**
 * 验证
 * 
 * @Author null
 * @Date 2014-10-30 下午01:33:40
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface Auth {
	/**
	 * 是否验证登陆 true=验证 ,false = 不验证
	 * 
	 * @return
	 */
	public boolean verifyLogin() default true;

	/**
	 * 是否验证URL true=验证 ,false = 不验证
	 * 
	 * @return
	 */
	public boolean verifyURL() default true;

}
