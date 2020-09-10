package com.monitor.argus.common.util.sms;


import com.monitor.argus.common.util.UuidUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/6/10.
 */
public class SmsRunner implements Runnable {

    List<String> alMobileNO;
    String typeNo;

    public SmsRunner(List<String> alMobileNO, String typeNo) {
        this.alMobileNO = alMobileNO;
        this.typeNo = typeNo;
    }

    @Override
    public void run() {
        boolean batchSmsResult = SmsSendUtil.sendBatchSms(UuidUtil.getUUID(), alMobileNO, typeNo);
        System.out.println("smsRunner send sms :" + alMobileNO.toString() + ",result:" + batchSmsResult);
    }
}
