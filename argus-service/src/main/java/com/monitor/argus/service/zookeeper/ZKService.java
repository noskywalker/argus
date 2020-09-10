package com.monitor.argus.service.zookeeper;

import com.monitor.argus.service.BaseService;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class ZKService extends BaseService {

	@Autowired
	ZkClient zkClient;
	Logger logger =LoggerFactory.getLogger(getClass());
	@PostConstruct
	public void test(){
		logger.info("begin");
	}
	public boolean exists(String nodeName){
		try {
//			String stat= zkClient.getCf().create().forPath("argus");
			Stat stat= zkClient.getCf().checkExists().forPath("argus2");
			logger.info(stat.toString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
