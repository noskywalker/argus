package com.monitor.argus.alarm.msg.impl;

import com.monitor.argus.alarm.msg.MessageGenerator;
import com.monitor.argus.bean.MailEntity;
import com.monitor.argus.bean.log.AlarmEntityDTO;

/**
 * Created by Administrator on 2016/7/15.
 */
public class EmailMessageGenerator implements MessageGenerator<AlarmEntityDTO> {
    @Override
    public Object generate(AlarmEntityDTO alarmEntityDTO) {
        return MailEntity.generateByAlarmEntity(alarmEntityDTO);
    }
}
