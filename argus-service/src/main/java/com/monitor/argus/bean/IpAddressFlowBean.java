package com.monitor.argus.bean;

/**
 * Created by wangfeng on 16/11/8.
 */
public class IpAddressFlowBean {
    private Integer num;
    private String cityName;
    private String cityNameMd5;
    private String ipFlow;
    private String percent;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityNameMd5() {
        return cityNameMd5;
    }

    public void setCityNameMd5(String cityNameMd5) {
        this.cityNameMd5 = cityNameMd5;
    }

    public String getIpFlow() {
        return ipFlow;
    }

    public void setIpFlow(String ipFlow) {
        this.ipFlow = ipFlow;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
