/**
 * 
 */
package com.monitor.argus.monitor.parser.impl;

import com.monitor.argus.bean.base.EntityBase;
import org.springframework.stereotype.Service;

import com.monitor.argus.monitor.parser.LogParserChain;


/**
 * All rights Reserved, Designed By alex zhang
 * @Title: 	LogParserTimeStampChain.java 
 * @Package com.monitor.argus.service.log.parser
 * @Description: 	TODO 
 * @author:	alex zhang
 * @date:	2016年6月14日 下午4:11:35 
 * @version	V1.0   
 */
@Service("logParserTimeStampChain")
public class LogParserTimeStampChain implements LogParserChain<EntityBase> {

	LogParserChain<EntityBase> nextChain;
	
	@Override
	public EntityBase parse(EntityBase entity, Object logInfo) {
		//nextChain.parse(entity, logInfo);
		return entity;
	}


	@Override
	public void setNextChain(LogParserChain<EntityBase> chain) {
		this.nextChain=chain;
	}

}
