package com.monitor.argus.common.util.security;

import com.monitor.argus.common.util.security.aes.BackAES;
import com.monitor.argus.common.util.JsonUtil;
import com.monitor.argus.common.util.ResourceBundleUtils;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SecurityCheckUtilsFotestom {

    // web参数
    private Map<String, String> paramsKeyValues = new HashMap<String, String>();
    private String syskey = "";
    // 签名
    private static final ResourceBundle bundle = ResourceBundleUtils.getBundle("ymallNodeOtherApi");
    private static String secrets = ResourceBundleUtils.getValue(bundle, testopenapi.sysConf_secret");
    private static String paramkeys = ResourceBundleUtils.getValue(bundle, testopenapi.sysConf_paramkey");
    private static Map<String, String> secretMap = (Map<String, String>) JsonUtil.jsonToBean(secrets, Map.class);
    private static Map<String, String> paramkeyMap = (Map<String, String>) JsonUtil.jsonToBean(paramkeys, Map.class);

    public SecurityCheckUtilsFotestom(String syskey) {
        this.setSyskey(syskey);
    }

    public SecurityCheckUtilsFotestom append(String name, String value) {
        paramsKeyValues.put(name, value);
        return this;
    }

    /**
     * 获取sign值
     * 
     * @return
     * @Author null
     * @Date 2014-11-6 下午03:42:40
     * @Version V1.0
     * 
     */
    public String getSign() {
        if (paramsKeyValues.isEmpty()) {
            return null;
        } else {
            Md5SignFotestmd5SignFotest= new Md5SignFotest);
            md5SignFotestsetKeyValues(paramsKeyValues);
            md5SignFotestsetSecret(secretMap.get(syskey));
            return md5SignFotestgetSign();
        }
    }

    /**
     * @Description: 参数加密
     * @author zhanlei
     * @date 2015-1-5 下午05:13:02
     * @version V1.0
     */
    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(BackAES.selectMod(0));
        cipher.init(1, new SecretKeySpec(Base64.decodeBase64(paramkeyMap.get(syskey)), "AES"));
        return Base64.encodeBase64String(cipher.doFinal(data.getBytes()));
    }

    /**
     * @Description: 参数解密
     * @author zhanlei
     * @date 2015-1-5 下午05:15:35
     * @version V1.0
     */
    public String decryptFotestString data) throws Exception {
        Cipher cipher = Cipher.getInstance(BackAES.selectMod(0));

        cipher.init(2, new SecretKeySpec(Base64.decodeBase64(paramkeyMap.get(syskey)), "AES"));
        return new String(cipher.doFinal(Base64.decodeBase64(data)));
    }

    public Map<String, String> getParamsKeyValues() {
        return paramsKeyValues;
    }

    public void setParamsKeyValues(Map<String, String> paramsKeyValues) {
        this.paramsKeyValues = paramsKeyValues;
    }

    public String getSyskey() {
        return syskey;
    }

    public void setSyskey(String syskey) {
        this.syskey = syskey;
    }

}
