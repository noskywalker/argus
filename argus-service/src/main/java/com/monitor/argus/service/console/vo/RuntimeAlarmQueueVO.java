package com.monitor.argus.service.console.vo;

/**
 * Created by Administrator on 2016/7/16.
 */
public class RuntimeAlarmQueueVO {
    private String host;
    private String strategyId;
    private int queueSize;

    public static void main(String[] args) {
        String queueName="alarm:127.0.1:1111";
        System.out.println(queueName.substring(queueName.indexOf(":")+1,queueName.lastIndexOf(":")));
    }
    public RuntimeAlarmQueueVO(String queueName){
        this.host=queueName.substring(queueName.indexOf(":")+1,queueName.lastIndexOf(":"));
        this.strategyId=queueName.substring(queueName.lastIndexOf("")+1);
    }
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}
