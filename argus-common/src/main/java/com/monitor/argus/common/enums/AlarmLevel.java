package com.monitor.argus.common.enums;

/**
 * Created by Administrator on 2016/7/15.
 */
public enum AlarmLevel {
    NORMAL("一般",1),IMPORTANT("重要",2),FATAL("紧急",3);
    private String level;
    private int code;
    private AlarmLevel(String l, int code){
        this.level=l;
        this.code=code;
    }
    public String getLevelName(){
        return level;
    }
    public int getLevelCode(){
        return code;
    }

    public static AlarmLevel getAlarmLevel(int code){
        for(AlarmLevel level:AlarmLevel.values()){
            if(code==level.getLevelCode()){
                return level;
            }
        }
        return IMPORTANT;
    }

    @Override
    public String toString() {
        return level;
    }
}
