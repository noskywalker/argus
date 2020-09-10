/**
 *
 */
package com.monitor.argus.monitor.tomcat.bolts;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.monitor.argus.bean.base.EntityBase;
import com.monitor.argus.bean.log.LogEntityDTO;
import com.monitor.argus.common.util.ArgusUtils;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.LogType;
import com.monitor.argus.common.util.StringUtil;
import com.monitor.argus.monitor.parser.LogParserChain;
import com.monitor.argus.monitor.parser.LogParserFactory;
import com.monitor.argus.monitor.service.LogAnalysisService;
import com.monitor.argus.monitor.service.LogIpAddressDealService;
import com.monitor.argus.monitor.service.LogMonitorService;
import com.monitor.argus.monitor.statistics.Statistics;
import com.monitor.common.utils.DateUtils;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * All rights Reserved, Designed By alex zhang
 *
 * @version V1.0
 * @Title: LogMonitorBolts.java
 * @Package com.monitor.argus.monitor.tomcat.bolts
 * @Description: TODO
 * @author: alex zhang
 * @date: 2016年7月7日 上午9:44:57
 */
public class LogNormalizeBolts extends BaseRichBolt {

	Logger logger = LoggerFactory.getLogger(getClass());
	private OutputCollector collector;
	public static volatile ReentrantLock syncBoltLock = new ReentrantLock();
	public static ApplicationContext appContext;
	LogParserChain<LogEntityDTO> logParser;
	LogMonitorService logMonitorService;
	LogAnalysisService logAnalysisService;
	LogIpAddressDealService logIpAddressDealService;
	public static AtomicLong atomicLong=new AtomicLong(System.currentTimeMillis());
	public static AtomicLong atomicLong2=new AtomicLong(System.currentTimeMillis());

	/*
     * (non-Javadoc)
     *
     * @see backtype.storm.task.IBolt#prepare(java.util.Map,
     * backtype.storm.task.TopologyContext, backtype.storm.task.OutputCollector)
     * prepare方法为Bolt提供了OutputCollector，用来从Bolt中发送Tuple，在Bolt中载入新的线程异步处理，
     * OutputCollector是线程安全的。Bolt中prepare、execute、cleanup等方法中进行
     */
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		try {
			syncBoltLock.lock();
			logger.info("LogNormalizeBolts初始化开始========");
			// TODO Auto-generated method stub
			logger.info("LogNormalizeBolts初始化1========collector");
			this.collector = collector;
			logger.info("LogNormalizeBolts初始化2========spring");
			if (appContext == null) {
				appContext = new ClassPathXmlApplicationContext("classpath:spring-storm.xml");
			}
			logParser = (LogParserChain<LogEntityDTO>) appContext.getBean("logParserBaseChain");
			logMonitorService = (LogMonitorService) appContext.getBean("logMonitorService");
			logAnalysisService = (LogAnalysisService) appContext.getBean("logAnalysisService");
			logIpAddressDealService = (LogIpAddressDealService) appContext.getBean("logIpAddressDealService");
			logger.info("LogNormalizeBolts初始化3========Statistics");
			Statistics.setRedisService(logMonitorService.getRedisService());
			Statistics.startSyncStatistics();
			Statistics.startSyncStatistics01();
			Statistics.startSyncStatistics02();
			Statistics.startSyncStatistics03();
		} catch (Exception e) {
			logger.info("LogNormalizeBolts异常" + e.getMessage());
			// e.printStackTrace();
		} finally {
			syncBoltLock.unlock();
			logger.info("LogNormalizeBolts初始化完成========");
		}
	}


	/*
     * (non-Javadoc)
     *
     * @see backtype.storm.task.IBolt#execute(backtype.storm.tuple.Tuple)
     * 以一个Tuple作为输入，Bolt使用OutPutColector来发射Tuple，Bolt必须为他处理的每一个Tuple调用ack方法，以通知Storm该Tuple处理完毕了，从而通知该Tuple的发射者Spout
     */
	@Override
	public void execute(Tuple input) {
		LogEntityDTO entity = new LogEntityDTO();
		String st = (String) input.getValueByField(ArgusUtils.ENTRY_LOG_KEY);

		ArrayList<LogEntityDTO> dtos= Lists.newArrayList();

		Map<String, Object> jsonObject= (Map) JsonUtil.jsonToBean(st, Map.class);

		if(jsonObject==null){
			logger.warn("日志格式有问题无法反序列化");
		}
		if(jsonObject!=null&&jsonObject.keySet().size()==3){
			dtos=parse(jsonObject);
		}else{
			LogParserChain<EntityBase> logParser = LogParserFactory.getLogParser( appContext);
			entity = (LogEntityDTO) logParser.parse(entity, jsonObject);
			dtos.add(entity);
		}

		try {

			for(LogEntityDTO dto:dtos){
				handler(dto);
			}

		} catch (Exception e) {
			logger.warn("msg:{}",st);
			logger.error("Log NormalSpouts cache a error ,msg:", e);
		} finally {
			this.collector.ack(input);
		}
	}

	private ArrayList<LogEntityDTO> parse(Map<String, Object> jsonObject) {
		ArrayList<LogEntityDTO> entityDTOS=Lists.newArrayList();
		Map<String,Integer> index=parseIndex(jsonObject);
		List loads= (ArrayList) jsonObject.get("payload");
		for(Object map:loads){
			Map m= (Map) map;
			List list= (List) m.get("tuple");
			String nameOrIp= (String) list.get(index.get(ArgusUtils.KEY_QUEXIN_LOG_SYS_NAME));
			String log= (String) list.get(index.get(ArgusUtils.KEY_QUEXIN_LOG_TXT));
			String time= (String) list.get(index.get(ArgusUtils.KEY_QUEXIN_LOG_TIMESTAMP));
			LogEntityDTO bean=new LogEntityDTO();
			bean.setFullMessage(log);
			bean.setTimeStamp(time);
			bean.setIp(nameOrIp);
			bean.setSystemTime(DateUtils.getFormatDate("yyyy-MM-dd HH:mm:ss", new Date()));
			bean.setLogType(LogType.TOMCAT);
			bean.setLogLength(bean.getFullMessage().length());
			entityDTOS.add(bean);
		}
		return entityDTOS;
	}

	private Map<String,Integer> parseIndex(Map<String, Object> jsonObject){
		Map<String,Integer> index= Maps.newHashMap();
		List fields= (List) ((Map)jsonObject.get("schema")).get("fields");
		for(int i=0;i<fields.size();i++){
			Map item= (Map) fields.get(i);
			String name= (String) item.get("name");
			if("ums_ts_".equals(name)){
				index.put(ArgusUtils.KEY_QUEXIN_LOG_TIMESTAMP, i);
			}
			if("log".equals(name)){
				index.put(ArgusUtils.KEY_QUEXIN_LOG_TXT, i);
			}
			if("snm".equals(name)){
				index.put(ArgusUtils.KEY_QUEXIN_LOG_SYS_NAME, i);
			}
		}
		return index;
	}
	private void handler(LogEntityDTO entity) {
		Statistics.totalLogBytes.getAndAdd(entity.getLogLength());
		Statistics.totalLogCounts.incrementAndGet();
		String ip = entity.getIp();
		// 获取用户标识
		String uvid = "";
		if (!StringUtil.isEmpty(ip)) {

//			// 检测用户标识是否存在
//			uvid = getUVIDCheck(uvid, entity);
//
//			// PV&UV
//			logAnalysisService.doAnalysis(entity, ip, uvid);

			// 监控和报警
			logMonitorService.doMonitor(entity, appContext);

			// ip地址实时记录
			logIpAddressDealService.doIpAddressDeal(entity);
		}
	}

	public void handler(){

	}
	public String getUVIDCheck(String uvid, LogEntityDTO entity) {
		String uvidNew = uvid;
		if (StringUtil.isEmpty(uvidNew) || "X".equals(uvidNew.trim())) {
			// 用户登录
			uvidNew = entity.getTokenId();
		}
		if (StringUtil.isEmpty(uvidNew) || "X".equals(uvidNew.trim())) {
			// 用户未登录
			uvidNew = entity.getDeviceId();
		}
		return uvidNew;
	}


	/*
     * (non-Javadoc)
     *
     * @see
     * backtype.storm.topology.IComponent#declareOutputFields(backtype.storm.
     * topology.OutputFieldsDeclarer)
     * 声明当前Bolt发送的Tuple中包含的字段；Bolt可以发射多条消息流，使用OutputFieldsDeclarer.declareStream方法来定义流，之后使用OutputCollector.emit来选择要发射的流
     */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("message"));
	}

	@Override
	public void cleanup() {
		super.cleanup();
		logger.info("LogNormalizeBolts的线程退出");
	}

}
