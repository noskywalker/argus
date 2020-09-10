package com.monitor.argus.dao.dataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 数据库连接线程
 * 
 * @author null
 * @date 下午6:08:33
 * 
 */
public class DataBaseContextHolder {

    private static Logger logger = LoggerFactory.getLogger(DataBaseContextHolder.class);

    private static ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static final String DATASOURCE_TYPE_1 = "dataSourceKey1";
    public static final String DATASOURCE_TYPE_2 = "dataSourceKey2";

    /**
     * 获取本线程的databaseType
     * 
     * @author null
     * @date 下午6:27:42
     * 
     * @return
     */
    public static String getDatabaseType() {
        String databaseType = contextHolder.get();
        if (databaseType == null) {
            return DATASOURCE_TYPE_1;
        }
        return databaseType;
    }

    /**
     * 设置本线程的databaseType
     * 
     * @author null
     * @date 下午6:29:40
     * 
     * @param databaseType
     */
    public static void setDatabaseType(String databaseType) {
        contextHolder.set(databaseType);
    }

    /**
     * 
     * 清理数据库连接
     * 
     * @author null
     * @date 下午6:29:36
     * 
     */
    public static void cleanDatabaseType() {
        contextHolder.remove();
    }

}
