package com.monitor.argus.alarm;

import com.monitor.argus.alarm.service.AlarmHandlerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Email:alex zhang
 * Creator:usr
 * CreatedDate:七月
 * Version:V1.0.0
 */
public class RunAlarmService {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("spring-alarm.xml");
        AlarmHandlerService service= (AlarmHandlerService) applicationContext.getBean("alarmHandlerService");
        service.alarm();
    }
}
