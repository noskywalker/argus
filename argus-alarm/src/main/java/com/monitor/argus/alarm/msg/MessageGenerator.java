package com.monitor.argus.alarm.msg;

/**
 * Created by Administrator on 2016/7/15.
 */
public interface MessageGenerator<T> {
    public Object generate(T t);
}
