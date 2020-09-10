package com.monitor.argus;

import com.monitor.argus.bean.alarm.AlarmStrategyEntity;
import com.monitor.argus.service.alarm.IAlarmService;
import com.monitor.argus.common.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by wangfeng on 16/9/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-webtest.xml")
public class AlarmTest {
    @Autowired
    IAlarmService alarmService;

    @Test
    public void getList() {
        AlarmStrategyEntity entity = new AlarmStrategyEntity();
        entity.setAlarmName("");

        List<AlarmStrategyEntity> list = alarmService.getAlarmStrategyByCondition(entity);
        for (AlarmStrategyEntity a : list) {
            System.out.println(JsonUtil.beanToJson(a));
        }
    }

}
