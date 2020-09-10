package com.monitor.argus.common.util.sms;

import java.security.MessageDigest;

public class SecurityUtil {

    public static String sha1Digest(String src) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] b = md.digest(src.getBytes("UTF-8"));
        return byte2HexStr(b);
    }

    public static String sha1Digest(String src, byte[] salt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(salt);
        md.update(src.getBytes("UTF-8"));
        byte[] b = md.digest();
        return byte2HexStr(b);
    }

    private static String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte bt : b) {
            String s = Integer.toHexString(bt & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s.toUpperCase());
        }
        return sb.toString();
    }
}
