package com.monitor.argus.common.util.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * MD5工具类
 * 
 * @date 2014-2-13 下午01:25:50
 * @author null
 */
public class Md5Util {

    private static Logger logger = LoggerFactory.getLogger(Md5Util.class);

    private static final String SYS_USER_PSW_SALT = "43894A0E4A801FC3";

    /**
     * Digest-MD5认证
     * 
     * @date 2015年10月23日 下午2:06:27
     * 
     * @param text
     * @return
     */
    public static String digist(String text) {
        String result = "";

        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            // 定义编码方式
            byte[] bufs = text.getBytes("UTF-8");
            md.update(bufs);
            byte[] b = md.digest();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取系统用户密码的MD5值（加盐）
     * 
     * @author null
     * @date 2016年6月3日 下午6:30:23
     * 
     * @param passwordPlaintext
     * @return
     */
    public static String getSysUserPasswordMd5(String passwordPlaintext) {
        return Md5Util.digist(passwordPlaintext + SYS_USER_PSW_SALT);
    }

    /**
     * 
     * @param in
     * @return
     */
    public static String getMd5(InputStream in) {
        String value = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[4096];
            int length = -1;
            while ((length = in.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }

        }
        return value;
    }

    public static String getMd5(byte[] bs) {
        String value = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(bs, 0, bs.length);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            logger.error("", e);
        } finally {

        }
        return value;
    }

    public static void main(String[] args) {
        System.out.println(Md5Util.getSysUserPasswordMd5("123456"));
    }
}
