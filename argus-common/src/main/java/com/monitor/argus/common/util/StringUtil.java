package com.monitor.argus.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.*;
import java.util.regex.Pattern;

/**
 * String 工具类
 * 
 * 此类中封装一些常用的字符串操作。
 * 
 * @Author null
 * @Date 2014-4-28 下午06:28:32
 * 
 */
public class StringUtil {

    private static Logger logger = LoggerFactory.getLogger(StringUtil.class);

    public static String LINE_SEPARATOR = "\n";

    public static String getGuid() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public static double getAsDouble(String val) {
        if (val == null) {
            return 0d;
        }
        val = val.replaceAll(",", "");
        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            return 0d;
        }
    }

    /**
     * String类型转为货币格式
     * 
     * @param src
     * @return
     */
    public static String formatCurrency(String src) {
        logger.info("String类型转为货币格式-s{}", src);
        if (src != null && !src.isEmpty()) {
            BigDecimal bd = new BigDecimal(src);
            bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
            double bdn = bd.doubleValue();
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(bdn);
        } else {
            return null;
        }
    }

    /**
     * Double类型转为货币格式
     * 
     * @param src
     * @return
     */
    public static String formatCurrency(Double src) {
        if (src == null)
            return null;
        BigDecimal bd = new BigDecimal(src);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        double bdn = bd.doubleValue();
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(bdn);
    }

    /**
     * Double类型转为货币格式
     * 
     * @param src
     * @return
     */
    public static String formatCurrency(Double src, String format) {
        if (src == null)
            return null;
        if (isEmpty(format))
            format = "###0.00";
        DecimalFormat formater = new DecimalFormat(format);
        String s = formater.format(src);
        return s;
    }

    /**
     * 此方法将给出的字符串source使用delim划分为单词数组。
     * 
     * @param source 需要进行划分的原字符串
     * @param delim 单词的分隔字符串
     * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组，
     *         如果delim为null则使用逗号作为分隔字符串。
     * @since 0.1
     */
    public static synchronized String[] split(String source, String delim) {
        String[] wordLists;
        if (source == null) {
            wordLists = new String[1];
            wordLists[0] = source;
            return wordLists;
        }
        if (delim == null) {
            delim = ",";
        }
        StringTokenizer st = new StringTokenizer(source, delim);
        int total = st.countTokens();
        wordLists = new String[total];
        for (int i = 0; i < total; i++) {
            wordLists[i] = st.nextToken();
        }
        return wordLists;
    }

    /**
     * 此方法将给出的字符串source使用delim划分为单词数组。
     * 
     * @param source 需要进行划分的原字符串
     * @param delim 单词的分隔字符
     * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组。
     * @since 0.2
     */
    public static synchronized String[] split(String source, char delim) {
        return split(source, String.valueOf(delim));
    }

    /**
     * 此方法将给出的字符串source使用逗号划分为单词数组。
     * 
     * @param source 需要进行划分的原字符串
     * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组。
     * @since 0.1
     */
    public static synchronized String[] split(String source) {
        return split(source, ",");
    }

    /**
     * 循环打印字符串数组。 字符串数组的各元素间以指定字符分隔，如果字符串中已经包含指定字符则在字符串的两端加上双引号。
     * 
     * @param strings 字符串数组
     * @param delim 分隔符
     * @param out 打印到的输出流
     * @since 0.4
     */
    public static synchronized void printStrings(String[] strings, String delim, OutputStream out) {
        try {
            if (strings != null) {
                int length = strings.length - 1;
                for (int i = 0; i < length; i++) {
                    if (strings[i] != null) {
                        if (strings[i].indexOf(delim) > -1) {
                            out.write(("\"" + strings[i] + "\"" + delim).getBytes());
                        } else {
                            out.write((strings[i] + delim).getBytes());
                        }
                    } else {
                        out.write("null".getBytes());
                    }
                }
                if (strings[length] != null) {
                    if (strings[length].indexOf(delim) > -1) {
                        out.write(("\"" + strings[length] + "\"").getBytes());
                    } else {
                        out.write(strings[length].getBytes());
                    }
                } else {
                    out.write("null".getBytes());
                }
            } else {
                out.write("null".getBytes());
            }
            out.write(LINE_SEPARATOR.getBytes());
        } catch (IOException e) {

        }
    }

    /**
     * 返回去除时间的日期（yyyy-MM-dd）
     * 
     * @return
     */
    public static synchronized Date getDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 循环打印字符串数组到标准输出。 字符串数组的各元素间以指定字符分隔，如果字符串中已经包含指定字符则在字符串的两端加上双引号。
     * 
     * @param strings 字符串数组
     * @param delim 分隔符
     * @since 0.4
     */
    public static synchronized void printStrings(String[] strings, String delim) {
        printStrings(strings, delim, System.out);
    }

    /**
     * 循环打印字符串数组。 字符串数组的各元素间以逗号分隔，如果字符串中已经包含逗号则在字符串的两端加上双引号。
     * 
     * @param strings 字符串数组
     * @param out 打印到的输出流
     * @since 0.2
     */
    public static synchronized void printStrings(String[] strings, OutputStream out) {
        printStrings(strings, ",", out);
    }

    /**
     * 循环打印字符串数组到系统标准输出流System.out。 字符串数组的各元素间以逗号分隔，如果字符串中已经包含逗号则在字符串的两端加上双引号。
     * 
     * @param strings 字符串数组
     * @since 0.2
     */
    public static synchronized void printStrings(String[] strings) {
        printStrings(strings, ",", System.out);
    }

    /**
     * 将字符串中的变量使用values数组中的内容进行替换。 替换的过程是不进行嵌套的，即如果替换的内容中包含变量表达式时不会替换。
     * 
     * @param prefix 变量前缀字符串
     * @param source 带参数的原字符串
     * @param values 替换用的字符串数组
     * @return 替换后的字符串。 如果前缀为null则使用“%”作为前缀；
     *         如果source或者values为null或者values的长度为0则返回source；
     *         如果values的长度大于参数的个数，多余的值将被忽略；
     *         如果values的长度小于参数的个数，则后面的所有参数都使用最后一个值进行替换。
     * @since 0.2
     */
    public static synchronized String getReplaceString(String prefix, String source, String[] values) {
        String result = source;
        if (source == null || values == null || values.length < 1) {
            return source;
        }
        if (prefix == null) {
            prefix = "%";
        }

        for (int i = 0; i < values.length; i++) {
            String argument = prefix + Integer.toString(i + 1);
            int index = result.indexOf(argument);
            if (index != -1) {
                String temp = result.substring(0, index);
                if (i < values.length) {
                    temp += values[i];
                } else {
                    temp += values[values.length - 1];
                }
                temp += result.substring(index + 2);
                result = temp;
            }
        }
        return result;
    }

    /**
     * 将字符串中的变量（以“%”为前导后接数字）使用values数组中的内容进行替换。
     * 替换的过程是不进行嵌套的，即如果替换的内容中包含变量表达式时不会替换。
     * 
     * @param source 带参数的原字符串
     * @param values 替换用的字符串数组
     * @return 替换后的字符串
     * @since 0.1
     */
    public static synchronized String getReplaceString(String source, String[] values) {
        return getReplaceString("%", source, values);
    }

    /**
     * 字符串数组中是否包含指定的字符串。
     * 
     * @param strings 字符串数组
     * @param string 字符串
     * @param caseSensitive 是否大小写敏感
     * @return 包含时返回true，否则返回false
     * @since 0.4
     */
    public static synchronized boolean contains(String[] strings, String string, boolean caseSensitive) {
        for (int i = 0; i < strings.length; i++) {
            if (caseSensitive == true) {
                if (strings[i].equals(string)) {
                    return true;
                }
            } else {
                if (strings[i].equalsIgnoreCase(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 字符串数组中是否包含指定的字符串。大小写敏感。
     * 
     * @param strings 字符串数组
     * @param string 字符串
     * @return 包含时返回true，否则返回false
     * @since 0.4
     */
    public static synchronized boolean contains(String[] strings, String string) {
        return contains(strings, string, true);
    }

    /**
     * 不区分大小写判定字符串数组中是否包含指定的字符串。
     * 
     * @param strings 字符串数组
     * @param string 字符串
     * @return 包含时返回true，否则返回false
     * @since 0.4
     */
    public static synchronized boolean containsIgnoreCase(String[] strings, String string) {
        return contains(strings, string, false);
    }

    /**
     * 将字符串数组使用指定的分隔符合并成一个字符串。
     * 
     * @param array 字符串数组
     * @param delim 分隔符，为null的时候使用""作为分隔符（即没有分隔符）
     * @return 合并后的字符串
     * @since 0.4
     */
    public static synchronized String combineStringArray(String[] array, String delim) {
        int length = array.length - 1;
        if (delim == null) {
            delim = "";
        }
        StringBuffer result = new StringBuffer(length * 8);
        for (int i = 0; i < length; i++) {
            result.append(array[i]);
            result.append(delim);
        }
        result.append(array[length]);
        return result.toString();
    }

    /**
     * 以指定的字符和长度生成一个该字符的指定长度的字符串。
     * 
     * @param c 指定的字符
     * @param length 指定的长度
     * @return 最终生成的字符串
     * @since 0.6
     */
    public static synchronized String fillString(char c, int length) {
        String ret = "";
        for (int i = 0; i < length; i++) {
            ret += c;
        }
        return ret;
    }

    /**
     * 去除左边多余的空格。
     * 
     * @param value 待去左边空格的字符串
     * @return 去掉左边空格后的字符串
     * @since 0.6
     */
    public static synchronized String trimLeft(String value) {
        String result = value;
        if (result == null)
            return result;
        char ch[] = result.toCharArray();
        int index = -1;
        for (int i = 0; i < ch.length; i++) {
            if (Character.isWhitespace(ch[i])) {
                index = i;
            } else {
                break;
            }
        }
        if (index != -1) {
            result = result.substring(index + 1);
        }
        return result;
    }

    /**
     * 去除右边多余的空格。
     * 
     * @param value 待去右边空格的字符串
     * @return 去掉右边空格后的字符串
     * @since 0.6
     */
    public static synchronized String trimRight(String value) {
        String result = value;
        if (result == null)
            return result;
        char ch[] = result.toCharArray();
        int endIndex = -1;
        for (int i = ch.length - 1; i > -1; i--) {
            if (Character.isWhitespace(ch[i])) {
                endIndex = i;
            } else {
                break;
            }
        }
        if (endIndex != -1) {
            result = result.substring(0, endIndex);
        }
        return result;
    }

    /**
     * 替换双字节空格并去除首尾空格
     * 
     * @param value
     * @return
     */
    public static synchronized String trim(String value) {
        if (value == null)
            return null;
        value = value.replace('　', ' ');
        return value.trim();
    }

    /**
     * 根据转义列表对字符串进行转义。
     * 
     * @param source 待转义的字符串
     * @param escapeCharMap 转义列表
     * @return 转义后的字符串
     * @since 0.6
     */
    public static synchronized String escapeCharacter(String source, HashMap<String, Object> escapeCharMap) {
        if (source == null || source.length() == 0)
            return source;
        if (escapeCharMap.size() == 0)
            return source;
        StringBuffer sb = new StringBuffer();
        StringCharacterIterator sci = new StringCharacterIterator(source);
        for (char c = sci.first(); c != StringCharacterIterator.DONE; c = sci.next()) {
            String character = String.valueOf(c);
            if (escapeCharMap.containsKey(character))
                character = (String) escapeCharMap.get(character);
            sb.append(character);
        }
        return sb.toString();
    }

    /**
     * 得到字符串的字节长度。
     * 
     * @param source 字符串
     * @return 字符串的字节长度
     * @since 0.6
     */
    public static synchronized int getByteLength(String source) {
        int len = 0;
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            int highByte = c >>> 8;
            len += highByte == 0 ? 1 : 2;
        }
        return len;
    }

    /**
     * 得到字符串中的子串的个数。
     * 
     * @param source 字符串
     * @param sub 子串
     * @return 字符串中的子串的个数
     * @since 0.6
     */
    public static synchronized int getSubtringCount(String source, String sub) {
        if (source == null || source.length() == 0) {
            return 0;
        }
        int count = 0;
        int index = source.indexOf(sub);
        while (index >= 0) {
            count++;
            index = source.indexOf(sub, index + 1);
        }
        return count;
    }

    /**
     * Replace all occurences of a substring within a string with another
     * string.
     * 
     * @param inString String to examine
     * @param oldPattern String to replace
     * @param newPattern String to insert
     * @return a String with the replacements
     */
    public static synchronized String replace(String inString, String oldPattern, String newPattern) {
        if (inString == null) {
            return null;
        }
        if (oldPattern == null || newPattern == null) {
            return inString;
        }

        StringBuffer sbuf = new StringBuffer();
        // output StringBuffer we'll build up
        int pos = 0; // our position in the old string
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sbuf.append(inString.substring(pos, index));
            sbuf.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sbuf.append(inString.substring(pos));

        // remember to append any characters to the right of a match
        return sbuf.toString();
    }

    /**
     * 获取引号字符
     * 
     * @param source 字符串
     * @return 加引号的字符串
     * @since 0.6
     */
    public static synchronized String getQuotedStr(String source) {
        return "'" + replace(source, "'", "''") + "'";

    }

    /**
     * 获取HashCode
     * 
     * @param source
     * @return
     */
    public static synchronized int getHashCode(String source) {
        return source == null ? 0 : source.hashCode();
    }

    /**
     * 比较字符串相等
     * 
     * @param str1
     * @param str2
     * @return
     */
    public static synchronized boolean strEqual(String str1, String str2) {
        if (str1 == str2)
            return true;
        if (str1 == null && str2 == null)
            return true;
        if (str1 == null && str2 != null)
            return false;
        return str1.equals(str2);
    }

    /**
     * 获取日期字符串
     * 
     * @param date
     * @return
     */
    public static synchronized String getDateFourStr(Date date) {
        if (date == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取日期字符串
     * 
     * @param date
     * @return
     */
    public static synchronized String getDateString(Date date) {
        if (date == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 获取指定格式的日期字符串
     * 
     * @param date
     * @param format
     * @return
     */
    public static synchronized String getDateString(Date date, String format) {
        if (date == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * @param value
     * @param defaultValue
     * @return
     */
    public static synchronized long getAsLong(String value, long defaultValue) {
        try {
            value = value.replaceAll(",", "");
            return Long.valueOf(value).intValue();
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * @param value
     * @param defaultValue
     * @return
     */
    public static synchronized int getAsInt(String value, int defaultValue) {
        try {
            value = value.replaceAll(",", "");
            return Integer.valueOf(value).intValue();
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * 获取日期及时间字符串
     * 
     * @param date
     * @return
     */
    public static synchronized String getDateTimeString(Date date) {
        if (date == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 判断字符串是否为空
     * 
     * @param source
     * @return
     */
    public static synchronized boolean isEmpty(String source) {
        if (source == null || (source != null && source.trim().isEmpty()))
            return true;
        else
            return false;
    }

    /**
     * 判断是否包含汉字
     * 
     * @param source
     * @return
     */
    public static synchronized boolean isChinese(String source) {
        if (source == null)
            return false;
        for (int i = 0; i < source.length(); i++) {
            char chr = source.charAt(i);
            int value = (int) chr;
            if ((value >= 0x2E80 && value <= 0x9FFF) || (value >= 0xE800 && value <= 0xE87F)
                    || (value >= 0xF900 && value <= 0xFAFF)
                    // 全角符号
                    || (value >= 0xFF00 && value <= 0xFF5E)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否未合法的短日期格式yyyy-MM-dd
     * 
     * @param strDate
     * @return
     */
    public static synchronized boolean validateDateString(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(strDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断字符串是否未合法的长日期格式yyyy-MM-dd HH:mm:ss
     * 
     * @param strDate
     * @return
     */
    public static synchronized boolean validateDateTimeString(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sdf.parse(strDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param n the integer to convert to hex form
     * @return a String in the form 0xNNNNNNNN where the value of N is in 8
     *         digit hex
     */
    public static synchronized String toHexString(final int n) {
        final StringBuffer sb = new StringBuffer(10);
        final String s = toUpperCase(Integer.toHexString(n));
        sb.append("0x");
        for (int i = 0; i < 8 - s.length(); i++) {
            sb.append("0");
        }
        sb.append(s);
        return sb.toString();
    }

    /**
     * @param n the long to convert to hex form
     * @return a String in the form 0xNNNNNNNNNNNNNNNN where the value of N is
     *         in 16 digit hex
     */
    public static synchronized String toHexString(final long n) {
        final StringBuffer sb = new StringBuffer(18);
        final String s = toUpperCase(Long.toHexString(n));
        sb.append("0x");
        for (int i = 0; i < 16 - s.length(); i++) {
            sb.append("0");
        }
        sb.append(s);
        return sb.toString();
    }

    /**
     * @param n the integer to convert to hex form
     * @return a String in the form 0xNNNN where the value of N is in 4 digit
     *         hex
     */
    public static synchronized String toHexString(final short n) {
        final StringBuffer sb = new StringBuffer(10);
        final String s;
        if (n >= 0) {
            s = toUpperCase(Integer.toHexString(n));
        } else {
            s = toUpperCase(Integer.toHexString(n)).substring(4);
        }
        sb.append("0x");
        for (int i = 0; i < 4 - s.length(); i++) {
            sb.append("0");
        }
        sb.append(s);
        return sb.toString();
    }

    /**
     * @param n the integer to convert to hex form
     * @return a String in the form 0xNN where the value of N is in 2 digit hex
     */
    public static synchronized String toHexString(final byte n) {
        final StringBuffer sb = new StringBuffer(10);
        final String s;
        if (n >= 0) {
            s = toUpperCase(Integer.toHexString(n));
        } else {
            s = toUpperCase(Integer.toHexString(n)).substring(6);
        }
        sb.append("0x");
        for (int i = 0; i < 2 - s.length(); i++) {
            sb.append("0");
        }
        sb.append(s);
        return sb.toString();
    }

    /**
     * Converts a <tt>String</tt> to lower case. This method assumes that the
     * string is in english. Specifically it only converts characters from A
     * (0x41) through Z (0x5A) to lower case.
     * 
     * @param s the <tt>String</tt> to convert
     * @return the converted string
     */
    public static synchronized String toLowerCase(final String s) {
        final char[] ca = s.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            final char c = ca[i];
            if (c >= 'A' && c <= 'Z')
                ca[i] += 32;
        }
        return new String(ca);
    }

    /**
     * Converts a <tt>String</tt> to upper case. This method assumes that the
     * string is in english. Specifically it only converts characters from a
     * (0x61) through z (0x7A) to upper case.
     * 
     * @param s the <tt>String</tt> to convert
     * @return the converted string
     */
    public static synchronized String toUpperCase(final String s) {
        final char[] ca = s.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            final char c = ca[i];
            if (c >= 'a' && c <= 'z')
                ca[i] -= 32;
        }
        return new String(ca);
    }

    /** */
    public static synchronized String toAlphaNumeric(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetterOrDigit(s.charAt(i)))
                sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    /**
     * @param inputString the input String
     * @param setString the set of characters to include/exclude
     * @param includes whether the set is of includes
     * @return the stripped String
     */
    public static synchronized String strip(final String inputString, final String setString, final boolean includes) {
        final char[] input = inputString.toCharArray();
        final char[] set = setString.toCharArray();
        Arrays.sort(set);
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < input.length; i++) {
            if ((Arrays.binarySearch(set, input[i]) >= 0) == includes) {
                sb.append(input[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 按指定日期格式取得日期值
     * 
     * @param date
     * @param dateFormat
     * @return
     */
    public static synchronized Date getAsDate(String date, String dateFormat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            return format.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 按缺省日期格式取得日期值
     * 
     * @param date
     * @return
     */
    public static synchronized Date getAsDate(String date) {
        return getAsDate(date, "yyyy-MM-dd");
    }

    /**
     * 按字节长度截取字符串
     * 
     * @param str 将要截取的字符串参数
     * @param toCount 截取的字节长度
     * @return 返回截取后的字符串
     */
    public static String substring(String str, int toCount) {
        int reInt = 0;
        String reStr = "";
        if (str == null)
            return "";
        char[] tempChar = str.toCharArray();
        for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {
            String s1 = String.valueOf(tempChar[kk]);
            byte[] b = s1.getBytes();
            reInt += b.length;
            reStr += tempChar[kk];
        }
        return reStr;
    }

    /**
     * @param s_value
     * @param delim
     * @return
     */
    public static synchronized String[] splitByStr(String s_value, String delim) {
        int pos = 0;
        String s_list[];

        if (s_value != null && delim != null) {

            ArrayList<String> list = new ArrayList<String>();

            pos = s_value.indexOf(delim);
            int len = delim.length();

            while (pos >= 0) {
                if (pos > 0)
                    list.add(s_value.substring(0, pos));
                if ((pos + len) < s_value.length())
                    s_value = s_value.substring(len + pos);
                else
                    s_value = null;
                if (s_value != null)
                    pos = s_value.indexOf(delim);
                else
                    pos = -1;
            }
            if (s_value != null)
                list.add(s_value);
            s_list = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                s_list[i] = (String) list.get(i);
            }
        } else {
            s_list = new String[0];
        }
        return s_list;
    }

    /**
     * @param val
     * @return
     */
    public static synchronized boolean getAsBoolean(String val) {
        if ("true".equalsIgnoreCase(val) || "1".equals(val))
            return true;
        else
            return false;
    }

    /**
     * @param val
     * @return
     */
    public static synchronized boolean getAsBoolean(String val, boolean def) {
        try {
            return Boolean.valueOf(val);
        } catch (Exception e) {
            return def;
        }
    }

    public static String gbkToIso8859(String val) {
        if (val == null)
            return null;

        try {
            return new String(val.getBytes("GBK"), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String iso8859ToGBK(String val) {
        if (val == null)
            return null;

        try {
            return new String(val.getBytes("ISO8859-1"), "GBK");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getdirid(final String string) {
        if (string == null)
            return null;
        String curstr = string;
        if (string.length() == 1)
            curstr = "00" + curstr;
        else if (string.length() == 2)
            curstr = "0" + string;
        else
            curstr = string;
        String result = curstr.substring(curstr.length() - 3);
        return result;
    }

    /**
     * 时间的比较
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static long getInterval(Date date1, Date date2) {
        long s1 = date1.getTime();
        long s2 = date2.getTime();
        if (s1 - s2 <= 0)
            return 0;

        long interlVal = (s1 - s2) / (1000 * 60 * 60);
        return interlVal;
    }

    /**
     * 将一个以特殊符号分割的字符串组装成数组，空白的地方用""代替
     * 
     * @param str 要分割的字符串
     * @param delimiter 分隔符
     * */
    public String[] convertStrToArray(String str, String delimiter) {
        if (str == null || "".equals(str.trim())) {
            return null;
        }
        StringTokenizer st1 = new StringTokenizer(str, delimiter, true);
        StringTokenizer st2 = new StringTokenizer(str, delimiter);

        int lth = st1.countTokens() - st2.countTokens() + 1;
        String[] tmp = new String[lth];

        int i = 0;
        String last = null;
        while (st1.hasMoreTokens()) {
            String current = st1.nextToken();
            if (!delimiter.equals(current)) {
                tmp[i] = current;
                i++;
            } else if (last == null || delimiter.equals(last) || !st1.hasMoreTokens()) {
                tmp[i] = "";
                i++;
                if (delimiter.equals(last) && !st1.hasMoreTokens()) {
                    tmp[i] = "";
                }
            }
            last = current;
        }
        return tmp;
    }

    /**
     * 通过字符串转换成相应的整型，并返回。
     * 
     * @param strValue String 待转换的字符串
     * @return int 转换完成的整型
     * */
    public static int getStrToInt(String strValue) {
        if (null == strValue) {
            return 0;
        }
        int iValue = 0;
        try {
            iValue = new java.lang.Integer(strValue.trim()).intValue();
        } catch (Exception ex) {
            iValue = 0;
        }
        return iValue;
    }

    /**
     * 得到非空的字符串，若字符串对象为null，则返回""。
     * 
     * @param objValue Object待转换的原字符串对象
     * @return String 转换后的字符串
     * */
    public static String getNotNullStr(Object objValue) {
        return (objValue == null ? "" : objValue.toString());
    }

    /**
     * 分隔数字用, 每3个数字用1个, intValue 不超过9位
     * 
     * @param i
     * @return
     */
    public static final String splitWithSymbol(int intValue) {
        String str = Integer.toString(intValue);
        String s = null;
        int length = str.length();
        if (6 >= length && length > 3) {
            s = str.substring(0, length - 3) + "," + str.substring(length - 3);
        }
        if (9 > length && length > 6) {
            s = str.substring(0, length - 6) + "," + str.substring(length - 6, length - 3) + ","
                    + str.substring(length - 3);
        }
        if (length <= 3) {
            s = str;
        }
        return s;
    }

    /**
     * 截取字符串
     * 
     */
    public String stringformat(String str, int n) {
        String temp = null;
        if (str.length() <= n)// 如果长度比需要的长度n小,返回原字符串
        {
            temp = str;
        } else {
            String s = str.substring(0, n - 3);

            temp = s + "...";
        }
        return temp;
    }

    /**
     * 正则加密字符串
     * 
     * 
     * @param sourceStr 原数据18800000001
     * @param startIndex 字符串前保留的位数（startIndex=3：188)
     * @param endIndex 字符串后保留的位数（endIndex=3：001)
     * @return
     * @Author null
     * @Date 2014-12-9 下午04:55:00
     * @Version V1.0
     * 
     */
    public static String encryptRegexReplace(String sourceStr, Integer startIndex, Integer endIndex) {
        if (sourceStr != null && startIndex != null && endIndex != null) {
            return sourceStr.replaceAll("(^[\\s\\S]{" + startIndex + "}).*?([\\s\\S]{" + endIndex + "}$)", "$1"
                    + sourceStr.replaceAll("^[\\s\\S]{" + startIndex + "}(.*?)[\\s\\S]{" + endIndex + "}$", "$1")
                            .replaceAll("[\\s\\S]", "*") + "$2");
        } else if (sourceStr != null && startIndex != null && endIndex == null) {
            return sourceStr.replaceAll(
                    "(^[\\s\\S]{" + startIndex + "}).*?$",
                    "$1"
                            + sourceStr.replaceAll("^[\\s\\S]{" + startIndex + "}(.*?)$", "$1").replaceAll("[\\s\\S]",
                                    "*"));
        } else if (sourceStr != null && startIndex == null && endIndex != null) {
            return sourceStr.replaceAll("^.*?([\\s\\S]{" + endIndex + "})$",
                    sourceStr.replaceAll("^(.*?)[\\s\\S]{" + endIndex + "}$", "$1").replaceAll("[\\s\\S]", "*") + "$1");
        }
        return sourceStr;
    }

    public static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }

    /**
     * @Description: 获取指定位数的随机数
     * 
     * @Author: alex zhang
     * @CreateDate: 2014-3-23 下午01:47:25
     * @Version: V1.0
     * 
     * @param length
     * @return
     * 
     */
    public static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 格式化金额
     * 
     * @Author dyc
     * @Date 2014年12月4日 下午6:08:06
     */
    public static String formatMoney(String str1) {
        if (str1.indexOf(".") > 0) {
            str1 = str1.replaceAll("0+?$", "");// 去掉多余的0
            str1 = str1.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        str1 = new StringBuilder(str1).reverse().toString();
        String str2 = "";
        for (int i = 0; i < str1.length(); i++) {
            if (i * 3 + 3 > str1.length()) {
                str2 += str1.substring(i * 3, str1.length());
                break;
            }
            str2 += str1.substring(i * 3, i * 3 + 3) + ",";
        }
        if (str2.endsWith(",")) {
            str2 = str2.substring(0, str2.length() - 1);
        }
        // 最后再将顺序反转过来
        String result = new StringBuilder(str2).reverse().toString();
        return result;
    }

    /**
     * 判断字符串是否为数字
     * 
     * @author null
     * @date 下午3:57:26
     * 
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str != null && !str.isEmpty()) {
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str).matches();
        } else {
            return false;
        }
    }

    /**
     * 从url中获取文件名称
     * <p>
     * <p>
     * 返回：hello.jsp
     * 
     * @author null
     * @date 2016年3月3日 下午2:43:07
     * 
     * @param url
     * @return
     */
    public static String getFileNameFromUrl(String url) {
        if (StringUtil.isEmpty(url)) {
            return null;
        }
        String fileName = ConstantsForCommon.EMPTY_STRING;
        if (url.indexOf(ConstantsForCommon.URL_QUESTION) > 0) {
            fileName = url.substring(url.lastIndexOf(ConstantsForCommon.URL_SLASH) + 1,
                    url.indexOf(ConstantsForCommon.URL_QUESTION));
        } else {
            fileName = url.substring(url.lastIndexOf(ConstantsForCommon.URL_SLASH) + 1);
        }
        return fileName;
    }

    /**
     * 从url中获取文件的路径（去除文件名）
     * <p>
     * @author null
     * @date 2016年3月3日 下午2:43:24
     * 
     * @param url
     * @return
     */
    public static String getFilePathFromUrl(String url) {
        if (StringUtil.isEmpty(url)) {
            return null;
        }
        return url.substring(0, url.lastIndexOf(ConstantsForCommon.URL_SLASH) + 1);
    }

    /**
     * 根据设定的除数、小数位数、取舍方式，对字符串做除法，获取结果
     * 
     * @author null
     * @date 2016年4月13日 下午4:16:42
     * 
     * @param src 元数据
     * @param divisor 除数
     * @param newScale 保留小数位数
     * @param roundingMode BigDecimal的ROUND_UP = 0; ROUND_DOWN = 1;
     *            ROUND_CEILING = 2; ROUND_FLOOR = 3; ROUND_HALF_UP = 4;
     *            ROUND_HALF_DOWN = 5; ROUND_HALF_EVEN = 6; ROUND_UNNECESSARY =
     *            7;
     * @return
     */
    public static Double getDivisionByDivisor(String src, Double divisor, int newScale, int roundingMode) {
        double resultD = Double.parseDouble(src) / divisor;
        return new BigDecimal(resultD).setScale(newScale, roundingMode).doubleValue();
    }

    /**
     * 根据米数获取千米数
     * 
     * @author null
     * @date 2016年4月13日 下午4:16:42
     * 
     * @param metersStr 米数
     * @param newScale 保留小数位数
     * @return
     */
    public static Double getKilometersByMetersStr(String metersStr, int newScale) {
        return StringUtil.getDivisionByDivisor(metersStr, 1000.0, newScale, BigDecimal.ROUND_DOWN);
    }
}
