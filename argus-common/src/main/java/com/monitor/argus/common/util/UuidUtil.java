package com.monitor.argus.common.util;

import java.util.UUID;

/**
 * 生成唯一的32位字符串UUID
 * 
 * @author null
 * 
 */
public class UuidUtil {

    /**
     * 获得一个去掉"-"的UUID 32bits
     * <p>
     * 例: "863a74e5c9a54395862179721918d7a7"
     * 
     * @author null
     * @return String UUID
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        // 去掉"-"符号
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    /**
     * 获得指定数目的UUID
     * <p>
     * 例:{"e7d83456191d4f8fa37c722df3af4017",
     * "196a0e99817f46679fb82d52d4ec39a3"}
     * 
     * @author null
     * @param number int 需要获得的UUID数量
     * @param number
     * @return String[] UUID数组
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ustr = new String[number];
        for (int i = 0; i < number; i++) {
            ustr[i] = getUUID();
        }
        return ustr;
    }

}
