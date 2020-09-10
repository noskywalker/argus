/**
 * 
 */
package com.monitor.argus.monitor.parser.impl;

import com.monitor.argus.bean.base.EntityBase;
import com.monitor.argus.bean.log.LogEntityDTO;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.LogType;
import com.monitor.argus.monitor.parser.LogParserChain;
import com.monitor.common.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


/**
 * All rights Reserved, Designed By alex zhang
 * @Title: 	LogParserBaseChain.java 
 * @Package com.monitor.argus.service.log.parser
 * @Description: 	TODO 
 * @author:	alex zhang
 * @date:	2016年6月14日 下午5:23:32 
 * @version	V1.0   
 */
@Service("logParserBaseChain")
public class LogParserBaseChain implements LogParserChain<EntityBase> {

	Logger log= LoggerFactory.getLogger(getClass());
	@Autowired
	@Qualifier("logParserLogIdChain")
	LogParserChain<EntityBase> nextChain;
	/* (non-Javadoc)
	 * @see com.monitor.argus.service.log.LogParserChain#parse(java.lang.Object, java.lang.String)
	 */
	@Override
	public EntityBase parse(EntityBase entity, Object logInfo) {
		LogEntityDTO logEntity=new LogEntityDTO();
		if(logInfo instanceof Map){
			@SuppressWarnings("unchecked")
			Map<String,String> jsonObj=(Map<String,String>)logInfo;
			String msg= StringUtils.defaultIfEmpty(jsonObj.get(ArgusUtils.KEY_LOG_MESSAGE),"");
			String timeStamp=jsonObj.get(ArgusUtils.KEY_LOG_TIMESTAMP);
			String logIp= jsonObj.get(ArgusUtils.KEY_LOG_IP);
			logEntity.setHostName("empty");
			logEntity.setTimeStamp(timeStamp);
			logEntity.setSystemTime(DateUtils.getFormatDate("yyyy-MM-dd HH:mm:ss", new Date()));
			logEntity.setIp(logIp);
			logEntity.setFullMessage(msg);
			logEntity.setLogLength(logEntity.getFullMessage().length());
			logEntity.setLogType(LogType.TOMCAT);
		}
		return nextChain.parse(logEntity, logInfo);
	}

	/* (non-Javadoc)
	 * @see com.monitor.argus.service.log.LogParserChain#setNextChain(com.monitor.argus.service.log.LogParserChain)
	 */
	@Override
	public void setNextChain(LogParserChain<EntityBase> chain) {
		if(nextChain==null){
			this.nextChain=chain;
		}
	}


}
