package com.monitor.argus.monitor.strategy;

import com.monitor.argus.monitor.strategy.config.StrategyConfig;
import com.monitor.argus.monitor.strategy.config.enums.StrategyType;
import com.monitor.argus.monitor.strategy.config.strategy.*;
import com.monitor.argus.monitor.strategy.config.strategy.*;
import org.springframework.context.ApplicationContext;

/**
 * Email:alex zhang
 * Creator:usr
 * CreatedDate:九月
 * Version:V1.0.0
 */
public class StrategyFactory {
    public static BaseMonitorStrategy getStrategy(StrategyType type, ApplicationContext ctx){
        BaseMonitorStrategy monitorStrategy=null;
        switch(type){
            case KEYWORD:monitorStrategy= (BaseMonitorStrategy) ctx.getBean("keyWordMonitorStrategy");break;
            case BUSINESS_NODE:monitorStrategy=(BaseMonitorStrategy) ctx.getBean("businessNodeMonitorStrategy");break;
            case BUSINESS_NODE_PERCENT:monitorStrategy=(BaseMonitorStrategy) ctx.getBean("BusinessNodePercentMonitorStrategy");break;
            case BUSINESS_NODE_NUMCOMPARE:monitorStrategy=(BaseMonitorStrategy) ctx.getBean("businessNodeNumCompareMonitorStrategy");break;
            case BUSINESS_INTERFACE:monitorStrategy=(BaseMonitorStrategy) ctx.getBean("businessInterfaceMonitorStrategy");break;
            default:monitorStrategy=null;break;
        }
        return monitorStrategy;
    }

    public static StrategyConfig getStrategyConfig(StrategyType thisType){
        StrategyConfig strategyConfig=null;
        switch(thisType){
            case KEYWORD:strategyConfig=new KeyWordsStrategyConfig();break;
            case BUSINESS_NODE:strategyConfig=new BusinessNodeStrategyConfig();break;
            case BUSINESS_NODE_PERCENT:strategyConfig=new BusinessNodePercentStrategyConfig();break;
            case BUSINESS_NODE_NUMCOMPARE:strategyConfig=new BusinessNodeNumCompareStrategyConfig();break;
            case BUSINESS_INTERFACE:strategyConfig=new BusinessInterfaceMonitorStrategyyConfig();break;
            default:strategyConfig=null;break;
        }
        return strategyConfig;
    }
}
