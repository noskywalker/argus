/**
 * 
 */
package com.monitor.argus.monitor.parser;


/**
 * All rights Reserved, Designed By alex zhang
 * @Title: 	Crawler.java 
 * @Package com.monitor.argus.service.log
 * @Description: 	TODO 
 * @author:	alex zhang
 * @date:	2016年6月14日 下午3:48:28 
 * @version	V1.0   
 */
public interface LogParserChain<T> {

	/**
	 * parse the log,save the attribute into entity
	 * @param entity 
	 * @param logInfo
	 * @return <T>
	 */
	public  T parse(T entity,Object logInfo);
	
	/**next parser to parse the log ingo
	 * @param chain
	 * @return T
	 */
	public void setNextChain(LogParserChain<T> chain);
}
