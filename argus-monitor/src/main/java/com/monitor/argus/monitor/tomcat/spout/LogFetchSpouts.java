/**
 * 
 */
package com.monitor.argus.monitor.tomcat.spout;

import com.monitor.argus.common.util.ArgusUtils;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * All rights Reserved, Designed By alex zhang
 *
 * @Title: LogMonitorSpouts.java
 * @Package com.monitor.argus.monitor.tomcat.spout
 * @Description: TODO
 * @author: alex zhang
 * @date: 2016年7月7日 上午9:43:40
 * @version V1.0
 */
public class LogFetchSpouts extends BaseRichSpout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3649278157759055973L;
	private SpoutOutputCollector collector;
	public static String mockJSON = 
			"{\n" +
					"\"message\": \"2016-07-05 16:27:40.403 | 10.130.82.79 | 115.193.104.26, 115.193.104.26 | android | 4.4.2 | HM NOTE 1TD | 2.9.0 | 0 | 28e31f763748 | 25.041308=102.739233 | /init/getBannerList | xxxx | resCode:0000,resMsg:transaction failed \",\n" +
					"\"@version\": \"1\",\n" + 
					"\"@timestamp\": \"2016-07-05T08:27:40.876Z\",\n" + 
					"\"count\": 1,\n" + 
					"\"log_ip\": \"127.0.0.1\",\n" +
					"\"log_project\": \"laas\",\n" + 
					"\"log_topic\": \"mobile\",\n" + 
					"\"offset\": 162789885,\n" + 
					"\"source\": \"/opttestlogs/tomcat_8080_laas/apiinfo/laas-apiinfo.log\",\n" +
					"\"type\": \"mobile\",\n" + 
					"\"host\": \"ovz-loan-laas-01testhost.online.com\",\n" +
					"\"log_filename\": \"laas-apiinfo.log\",\n" + 
					"\"log_timestamp\": \"2016-07-05 16:27:40.403\",\n" + 
					"\"log_date\": \"2016-07-05\",\n" + 
					"\"kafka\": {\n" + 
					"\"msg_size\": 1354,\n" + 
					"\"topic\": \"mobile\",\n" + 
					"\"consumer_group\": \"mobile\",\n" + 
					"\"partition\": 2,\n" + 
					"\"key\": null\n" + 
					"},\n" + 
					"\"tags\": [\n" + 
					"\"_grokparsefailure\"\n" + 
					"]\n" + 
					"}";


	/*
	 * (non-Javadoc)
	 * 
	 * @see backtype.storm.spout.ISpout#open(java.util.Map,
	 * backtype.storm.task.TopologyContext,
	 * backtype.storm.spout.SpoutOutputCollector)
	 * 当一个Task被初始化时会调用此open方法，一般都会在此方法中初始化发送Tuple的对象SpoutOutputCollector和配置对象TopologyContext
	 */
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see backtype.storm.spout.ISpout#nextTuple()
	 * 发射一个Tuple到Topology都是通过该方法。
	 */
	@Override
	public void nextTuple() {
//		Map<String,Object> jsonObj=(Map<String, Object>) JsonUtil.jsonToBean(mockJSON, Map.class);
		
		while (true) {
			collector.emit(new Values(mockJSON));
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * backtype.storm.topology.IComponent#declareOutputFields(backtype.storm.
	 * topology.OutputFieldsDeclarer)
	 * 声明当前Spout的Tuple发送流
	 * 告诉组件发出数据流包含ENTRY_LOG_KEY字段
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields(ArgusUtils.ENTRY_LOG_KEY));
	}

}
