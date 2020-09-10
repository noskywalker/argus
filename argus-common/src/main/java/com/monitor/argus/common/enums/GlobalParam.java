package com.monitor.argus.common.enums;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangfeng on 16/10/26.
 */

@Component
public class GlobalParam {

    public static Map<String, String> diffNodeKeyValue = new HashMap<String, String>();
    public static Map<String, String> addressMd5Map = new HashMap<>();

    public static String getValueByKey(String nodeKey) {
        if (diffNodeKeyValue == null || diffNodeKeyValue.size() == 0) {
            return "100";
        }
        String value = diffNodeKeyValue.get(nodeKey);
        if (value != null) {
            return value;
        } else {
            return "100";
        }

    }
}
