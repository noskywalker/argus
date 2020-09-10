package com.monitor.argus.alarm.handler.persist;

import com.monitor.argus.bean.alarm.AlarmInfoEntity;
import com.monitor.argus.dao.alarm.IAlarmDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/7/16.
 */
@Service
public class AlarmEntityPersistService {

    @Autowired
    IAlarmDao alarmDao;
    public void insertSync(AlarmInfoEntity entity){
        alarmDao.insertAlarmInfo(entity);
    }

    public void insertAsync(final AlarmInfoEntity entity){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                insertSync(entity);
            }
        };

        new Thread(runnable).start();
    }
}
