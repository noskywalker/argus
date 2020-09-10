package com.monitor.argus.common.util;

import org.apache.commons.lang.StringUtils;

public class EmojiUtil {

    /**
     * 检测是否有emoji字符
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        if (StringUtils.isBlank(source)) {
            return false;
        }
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                // 判断到了这里表明，确认有表情字符
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        // 如果不包含，直接返回
        if (!containsEmoji(source)) {
            return source;
        }
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }
                buf.append(codePoint);
            } else {
            }
        }
        // 如果没有找到 emoji表情，则返回源字符串
        if (buf == null) {
            return source;
        } else {
            // 这里的意义在于尽可能少的toString，因为会重新生成字符串
            if (buf.length() == len) {
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println(EmojiUtil.filterEmoji("﹊∩∩﹊丫頭 😓😘义阳"));
    }
}
