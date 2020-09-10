package com.monitor.argus.mis.task;

import com.monitor.argus.common.enums.GlobalParam;
import com.monitor.argus.common.util.security.Md5Util;
import com.monitor.argus.dao.ipaddress.IIpConfigDao;
import com.monitor.argus.mis.task.annotations.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by wangfeng on 16/11/8.
 */
@Component("analyTopologyCityAddressTask")
public class AnalyTopologyCityAddressTask {

    private static Logger logger = LoggerFactory.getLogger(AnalyTopologyCityAddressTask.class);

    @Autowired
    private IIpConfigDao ipConfigDao;

    @Job(name = "地区信息缓存刷新任务", cron="0 0 0/3 * * ?")
    @PostConstruct
    public void fetch() {
        // logger.info("从数据库获取城市信息线程开始。。。");
        try {
            List<String> addressList = ipConfigDao.getShortAddress();
            logger.info("从数据库获取城市信息线程抓取后数据量:-{}", addressList.size());
            for (String str : addressList) {
                String addressKey = Md5Util.digist(str);
                GlobalParam.addressMd5Map.put(addressKey, str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

