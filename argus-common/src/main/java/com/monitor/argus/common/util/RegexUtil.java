package com.monitor.argus.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 * 
 * 此类中封装一些常用的字符串替换操作。
 * 
 * @Author null
 * @Date 2014-4-28 下午06:28:32
 * 
 */
public class RegexUtil {

    private static Logger logger = LoggerFactory.getLogger(RegexUtil.class);

    /**
     * 根据URL，获取domain
     * 
     * @author null
     * @date 2016年1月12日 下午3:52:27
     * 
     * @param url
     * @return
     */
    public static String getDomainNameFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        String domainName = url.replaceAll("^.*://([^/].*?)/.*$", "$1").replaceAll("^(.*?):.*$", "$1");
        logger.info("URL{}获取Domain Name为{}", url, domainName);
        return domainName;
    }

    /**
     * 姓名去掉第一个字，如：*阳锋疯
     * 
     * @author null
     * @date 下午4:20:44
     * 
     * @param name
     * @return
     */
    public static String nameRegexReplaceAsterisk(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        String nameAsterisk = name.replaceAll("^[\\s\\S]{1}(.*?)$",
                ((name.replaceAll("(^[\\s\\S]{1}).*?$", "$1")).replaceAll("[\\s\\S]", "*")) + "$1");
        logger.info("姓名{}替换为{}", name, nameAsterisk);
        return nameAsterisk;
    }

    /**
     * 手机号隐藏中间四位，如：188****0001
     * 
     * @author null
     * @date 下午4:20:47
     * 
     * @param mobileNo
     * @return
     */
    public static String mobileNoRegexReplaceAsterisk(String mobileNo) {
        if (mobileNo == null || mobileNo.isEmpty()) {
            return null;
        }
        if (!mobileNo.matches("^[\\+]?[\\d\\s]+$")) {
            logger.error("手机号格式错误：{}", mobileNo);
            return mobileNo;
        }
        String mobileNoAsterisk = mobileNo.replaceAll("(^[0-9]{3}).*?([0-9]{4}$)",
                "$1" + ((mobileNo.replaceAll("^[0-9]{3}(.*?)[0-9]{4}$", "$1")).replaceAll("[0-9]", "*")) + "$2");
        logger.info("手机号{}替换为{}", mobileNo, mobileNoAsterisk);
        return mobileNoAsterisk;
    }

    /**
     * 邮箱隐藏中间，如：a***@163.com,a***@iqqqqqq...
     * <p>
     * 如： www@qq.com.cnw
     * 
     * 
     * @author null
     * @date 下午4:20:50
     * 
     * @param email
     * @return
     */
    public static String emailRegexReplaceAsterisk(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }
        if (!email.matches("^[\\w\\W]+@[\\w\\W]+\\.[\\w\\W]+$")) {
            logger.error("邮箱格式错误：{}", email);
            return email;
        }
        String emailPre = email.replaceAll("^(.*?)@(.*?$)", "$1");
        String emailSuf = email.replaceAll("^(.*?)@(.*?$)", "$2");
        String emailAsterisk = email;
        if (emailPre.length() > 3) {
            emailAsterisk = emailAsterisk.replaceAll("(^[\\w\\W]{3}).*?@(.*?)$", "$1***@$2");
        }
        String emailAsteriskAndDots = emailAsterisk;
        if (emailSuf.length() > 7) {
            emailAsteriskAndDots = emailAsteriskAndDots.replaceAll("(^.*?)@([\\w\\W]{7}).*?$", "$1@$2...");
        }
        logger.info("邮箱{}替换为{}", email, emailAsteriskAndDots);
        return emailAsteriskAndDots;
    }

    /**
     * 根据版本全名，获取版本前缀
     * 
     * @author null
     * @date 2016年3月1日 下午5:51:42
     * 
     * @param clientVersion
     * @return
     */
    public static String getClientVersionPre(String clientVersion) {
        if (StringUtil.isEmpty(clientVersion)) {
            return null;
        }
        String clientVersionPre = clientVersion.replaceAll("^(\\d+[\\.]\\d+).*?$", "$1");
        return clientVersionPre;
    }

    /**
     * 手机号验证
     * 
     * @author null
     * @date 2016年4月8日 下午10:40:29
     * 
     * @param string 字符串
     * @return 验证通过返回true
     */
    public static boolean isMobileNo(String string) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(string);
        b = m.matches();
        return b;
    }
}
