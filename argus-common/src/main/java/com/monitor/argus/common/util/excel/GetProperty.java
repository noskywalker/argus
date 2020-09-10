package com.monitor.argus.common.util.excel;

import java.lang.reflect.Method;

/**
 * 获取属性值
 * 
 * @Author null
 * @Date 2014-3-13 下午02:24:56
 * 
 */
public class GetProperty {

	/**
	 * 
	 * @param propertyName
	 *            方法名
	 * @param cls
	 *            类Class
	 * @param obj
	 *            对象
	 * @return 返回对象obj.propertyName的执行结果
	 */
	public Object getProperty(String propertyName, Class<?> cls, Object obj) {
		try {
			if (propertyName != null && !"".equals(propertyName)) {
				Method method = cls.getMethod(propertyName);
				return method.invoke(obj);
			}
			return null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
}
