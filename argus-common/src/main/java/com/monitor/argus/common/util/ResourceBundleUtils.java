package com.monitor.argus.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * 资源文件ResourceBundle工具类
 * 
 * @author null
 * @date 2016年1月7日 下午4:54:57
 * 
 */
public class ResourceBundleUtils {

    private static Logger logger = LoggerFactory.getLogger(ResourceBundleUtils.class);

    /**
     * 根据资源名称获取资源文件ResourceBundle
     * 
     * @author null
     * @date 2016年1月7日 下午5:03:32
     * 
     * @param ResourceName
     * @return
     */
    public static ResourceBundle getBundle(String ResourceName) {
        if (StringUtil.isEmpty(ResourceName)) {
            logger.error("ResourceName is null(资源文件名称错误或不存在)");
            return null;
        } else {
            return ResourceBundle.getBundle(ResourceName);
        }
    }

    /**
     * 根据资源文件ResourceBundle和key获取value值
     * 
     * @author null
     * @date 2016年1月7日 下午5:02:49
     * 
     * @param bundle
     * @param key
     * @return
     */
    public static String getValue(ResourceBundle bundle, String key) {
        String value = null;
        if (bundle == null) {
            return value;
        }

        if (StringUtil.isEmpty(key)) {
            logger.error("key is null(资源文件key错误或不存在)");
            return value;
        }

        try {
            value = bundle.getString(key);
        } catch (Exception e) {
            logger.error("key is null(资源文件key错误或不存在)", e);
            return value;
        }

        return value;
    }
}
