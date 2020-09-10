package com.monitor.argus.dao.dataSource;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源
 * 
 * @author null
 * @date 下午1:40:12
 * 
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 自动查找datasource（根据当前线程）
     * 
     * @author null
     * @date 下午6:33:30
     * 
     * @see AbstractRoutingDataSource#determineCurrentLookupKey()
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataBaseContextHolder.getDatabaseType();
    }
}
