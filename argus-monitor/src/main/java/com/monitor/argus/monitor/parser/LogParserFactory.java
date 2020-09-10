/**
 * 
 */
package com.monitor.argus.monitor.parser;

import com.monitor.argus.bean.base.EntityBase;
import org.springframework.context.ApplicationContext;

/**
 * All rights Reserved, Designed By alex zhang
 * @Title: 	LogParserFactory.java 
 * @Package com.monitor.argus.service.log
 * @Description: 	TODO 
 * @author:	alex zhang
 * @date:	2016年7月6日 上午9:23:12 
 * @version	V1.0   
 */
public class LogParserFactory {

	@SuppressWarnings("unchecked")
	public static LogParserChain<EntityBase> getLogParser(ApplicationContext appContext){
		return (LogParserChain<EntityBase>) appContext.getBean("logParserBaseChain");
	}
	
	@SuppressWarnings("unchecked")
	public static LogParserChain<EntityBase> getDefaultParser(ApplicationContext appContext){
		return (LogParserChain<EntityBase>) appContext.getBean("logParserBaseChain");
	}
}
