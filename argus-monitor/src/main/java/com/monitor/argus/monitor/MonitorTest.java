package com.monitor.argus.monitor;

import com.monitor.argus.bean.log.LogEntityDTO;
import com.monitor.argus.monitor.strategy.config.strategy.KeyWordsStrategyConfig;
import com.monitor.argus.monitor.strategy.impl.KeyWordMonitorStrategy;
import com.monitor.argus.common.util.DateUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/13.
 */
public class MonitorTest {
    public static void main(String[] args) throws InterruptedException {
//        URL fileURL=Thread.currentThread().getClass().getResource("spring-storm.xml");
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring-storm.xml");
//        appContext = new FileSystemXmlApplicationContext("spring-storm.xml");
        KeyWordMonitorStrategy logMonitorService= (KeyWordMonitorStrategy) appContext.getBean("keyWordMonitorStrategy");
        int i=0;
        KeyWordsStrategyConfig config=new KeyWordsStrategyConfig();
        config.setMonitorCountBeforeMerge(10);
        config.setMonitorContent("entity");
        config.setIp("nxs-trade1");
        config.setIp("123");
        config.setStatus(1);
        config.setPattern(Pattern.compile(config.getMonitorContent()));
        while(true){
            i++;
            logMonitorService.process(generateLogEntity(i),config);
            Thread.sleep(new Random().nextInt(1500)+500);
        }
    }

    public static LogEntityDTO generateLogEntity(int i){
        LogEntityDTO dto = new LogEntityDTO();
        dto.setIp("nxs-trade1");
        dto.setFullMessage("\"iOSVersionPatch entity客户端版本jsPatch更新检测== -E {\\\"result\\\":\\\"success\\\",\\\"errorCode\\\":\\\"401\\\",\\\"msg\\\":\\\"transaction failed\\\"}\";\n");
        dto.setHostName("www.baidu.com");
        dto.setLogId("logid_12345567");
        dto.setTimeStamp(DateUtil.getDateLongTimePlusStr(new Date()));
        return dto;
    }
}
