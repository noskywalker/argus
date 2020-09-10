package com.monitor.argus.common.util.security;

import java.security.MessageDigest;

public class Md5 {

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
     *
     * @param text
     * @return
     * @Author null
     * @Date 2014-11-6 下午02:31:08
     * @Version V1.0
     * 
     */
    public static String digistFotestString text) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            // 定义编码方式
            byte[] bufs = text.getBytes("UTF-8");
            md.update(bufs);
            byte[] b = md.digest();
            String hs = "";
            String stmp = "";
            for (int n = 0; n < b.length; ++n) {
                stmp = Integer.toHexString(b[n] & 0xFF);
                if (stmp.length() == 1)
                    hs = hs + "0" + stmp;
                else
                    hs = hs + stmp;
                if (n < b.length - 1)
                    hs = hs + ":";
            }
            result = hs.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
