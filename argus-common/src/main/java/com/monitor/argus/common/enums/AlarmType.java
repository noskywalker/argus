package com.monitor.argus.common.enums;

/**
 * Created by Administrator on 2016/7/15.
 */
public enum AlarmType {
    MAIL(1),PHONE(2),WEIXIN(3),QQ(4),OTHER(5);

    private int code;

    public static AlarmType getAlarmType(int c){
        for(AlarmType type:AlarmType.values()){
            if(type.code==c){
                return type;
            }
        }
        return MAIL;
    }

    public int getCode(){return this.code;}
    private AlarmType(int c){
        this.code=c;
    }
}
