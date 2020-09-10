/**
 * 
 */
package com.monitor.argus.monitor.parser.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.monitor.argus.bean.base.EntityBase;
import com.monitor.argus.bean.log.LogEntityDTO;
import com.monitor.argus.monitor.parser.LogParserChain;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * All rights Reserved, Designed By alex zhang
 * @Title: 	LogParserLogIdChain.java 
 * @Package com.monitor.argus.service.log.parser
 * @Description: 	TODO 
 * @author:	alex zhang
 * @date:	2016年6月14日 下午4:20:54 
 * @version	V1.0   
 */
@Service("logParserLogIdChain")
public class LogParserLogIdChain implements LogParserChain<EntityBase> {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	@Qualifier("logParserTimeStampChain")
	LogParserChain<EntityBase> nextChain;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.monitor.argus.service.log.LogParserChain#parse(java.lang.Object,
	 * java.lang.String)
	 */
	@Override
	public EntityBase parse(EntityBase entity, Object logInfo) {
		LogEntityDTO logEntity = null;
		if (entity instanceof LogEntityDTO) {
			logEntity = (LogEntityDTO) entity;
		} else {
			logger.warn("log parser can not parse the logInfo:{}", JsonUtil.beanToJson(logInfo));
			return entity;
		}
		// HashMap<String,String> logMap=(HashMap<String, String>) logInfo;
		HashMap<String, String> attach = Maps.newHashMap();
		String message = logEntity.getFullMessage();
		Iterable<String> items = Splitter.on(ArgusUtils.LOG_PATTERN_SPLIT).split(message);
		int index = 0;
		//TODO get logid
		for (String item : items) {
			switch (index) {
			case 6:
				attach.put(ArgusUtils.CONS_CLIENT_IP, item);
				break;
			case 8:
				logEntity.setTokenId(item);
				break;
			case 9:
				logEntity.setLogId(item);
				break;
			case 10:
				logEntity.setDeviceId(item);
				break;
			case 14:
				logEntity.setTimes(item);
				break;
//			case 23:
//				logEntity.setFullMessage(item);
//				break;

			}
			index++;
		}
		logEntity.setAttach(attach);
		return nextChain.parse(entity, logInfo);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.monitor.argus.service.log.LogParserChain#setNextChain(com.monitor.argus.
	 * service.log.LogParserChain)
	 */
	@Override
	public void setNextChain(LogParserChain<EntityBase> chain) {
		if (this.nextChain == null) {
			nextChain = chain;
		}
	}

}
