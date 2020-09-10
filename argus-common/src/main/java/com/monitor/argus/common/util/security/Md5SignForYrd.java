package com.monitor.argus.common.util.security;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 宜人贷MD5签名
 * 
 * @Author null
 * @Date 2014-11-6 上午11:47:16
 * 
 */
public class Md5SignFotest{

    private static final Logger logger = LoggerFactory.getLogger(Md5SignFotestclass);

    // web参数
    private Map<String, String> keyValues = new HashMap<String, String>();

    // 私钥值
    private String secret = "";

    // 签名
    private String varSign = "sign";

    // 签名类型
    private String varSignType = "sign_type";

    // 私钥key
    private String varSecret = "secret";

    public Md5SignFotest) {
    }

    public Md5SignFotestString varSign, String varSignType, String varSecret) {
        if (StringUtils.isNotBlank(varSign)) {
            this.varSign = varSign;
        }
        if (StringUtils.isNotBlank(varSignType)) {
            this.varSignType = varSignType;
        }
        if (StringUtils.isNotBlank(varSecret)) {
            this.varSecret = varSecret;
        }
    }

    /**
     * 增加参数
     * 
     * @param name 参数名
     * @param value 参数值
     * @return 返回签名
     */
    public Md5SignFotestappend(String name, String value) {
        if (varSign.equals(name) || varSignType.equals(name)) {
            return this;
        }
        keyValues.put(name, value);
        return this;
    }

    /**
     * 获取编码后的WEB请求参数字符串
     * 
     * @return 请求参数字符串
     */
    public String getWebParams() {
        StringBuilder builder = new StringBuilder();
        Set<String> keys = new TreeSet<String>(new Comparator<String>() {

            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        keys.addAll(keyValues.keySet()); // 替换set集合
        Iterator<String> it = keys.iterator();

        builder.append(varSecret);
        while (it.hasNext()) {
            String name = it.next();
            String value = keyValues.get(name);
            if (!("sign".equals(name)))
                builder.append(name).append(value);
        }
        builder.append(varSecret);

        return builder.toString();
    }

    /**
     * 获取待加密URL参数串，参数名和参数值没进行Encode
     * 
     * @return 待加密URL参数串
     */
    public String getOrigString() {
        StringBuilder builder = new StringBuilder();
        Set<String> keys = new TreeSet<String>(new Comparator<String>() {

            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        keys.addAll(keyValues.keySet()); // 替换set集合
        Iterator<String> it = keys.iterator();
        builder.append(secret);
        while (it.hasNext()) {
            String name = it.next();
            String value = keyValues.get(name);
            if (!("sign".equals(name))) {
                builder.append(name).append(value);
            }
        }
        builder.append(secret);
        logger.info("排序后的MD5串：" + builder.toString());
        return builder.toString();
    }

    /**
     * 获取参数签名
     * 
     * @return 参数签名
     */
    public String getSign() {
        logger.info("=====================参数加密=================");
        logger.info("参数加密值为：" + Md5.digist(getOrigString()));
        return Md5.digistFotestgetOrigString());
    }

    /**
     * 解析签名参数
     * 
     * @param params 参数
     * @return 返回Md5签名对象
     */
    public static Md5SignFotestparse(String params) {
        Md5SignFotestsign = new Md5SignFotest);
        if (StringUtils.isBlank(params)) {
            return sign;
        }
        String[] slices = params.split("&");
        for (String slice : slices) {
            if (StringUtils.isNotBlank(slice)) {
                String[] item = slice.split("=");
                String name = item[0];
                if ("sign".equals(name) || "sign_type".equals(name)) {
                    continue;
                }
                if (item.length > 1) {
                    sign.append(item[0], item[1]);
                } else {
                    sign.append(item[0], "");
                }
            }
        }
        return sign;
    }

    /**
     * 验证参数签名值是否一致
     * 
     * @param sign 参数签名值
     * @return true-签名一致, false-签名不一致
     */
    public boolean verify(String sign) {
        return StringUtils.equalsIgnoreCase(sign, getSign());
    }

    public String toString() {
        return getWebParams();
    }

    public Md5SignFotestaddSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Map<String, String> getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(Map<String, String> keyValues) {
        this.keyValues = keyValues;
    }

}
