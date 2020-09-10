package com.monitor.argus.alarm.msg.impl;

import com.monitor.argus.alarm.msg.MessageGenerator;
import com.monitor.argus.bean.log.AlarmEntityDTO;
import com.monitor.argus.common.util.ArgusUtils;

/**
 * Created by Administrator on 2016/7/15.
 */
public class SmsMessageGenerator implements MessageGenerator<AlarmEntityDTO> {
    @Override
    public Object generate(AlarmEntityDTO alarmEntity) {
        return String.format(ArgusUtils.SMS_MESSAGE_TEMPLATE,alarmEntity.getLevel().getLevelName(),alarmEntity.getSystemName(),alarmEntity.getTotalAlarmCount(),alarmEntity.getIp());
    }
}
