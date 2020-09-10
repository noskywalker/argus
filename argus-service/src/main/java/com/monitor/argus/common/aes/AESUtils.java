package com.monitor.argus.common.aes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * AES加密与解密工具类
 */
public class AESUtils {

    private static Logger logger = LoggerFactory.getLogger(AESUtils.class);
    /**
     * AES加密解密使用的key
     */
    private static String SECURITY_AESKEY = "argus123";

    /**
     * yingApi -> App: 借款人信息接口返回值中的敏感字段
     */
    public static String encrypt(String content) {
        try {
            return new String(BackAES.encrypt(content, SECURITY_AESKEY, 0));
        } catch (GeneralSecurityException | IOException e) {
            logger.error("字段加密失败, content=" + content, e);
        }
        return null;
    }

    /**
     * App -> yingApi: 各种接口调用参数中的敏感字段，例如银行卡号、手机号
     */
    public static String decrypt(String content) {
        String decryptString = null;
        try {
            decryptString = BackAES.decrypt(content, SECURITY_AESKEY, 0);
        } catch (GeneralSecurityException | IOException e) {
            logger.error("字段解密失败, content=" + content, e);
        }
        logger.info("AES解密 参数{}返回{}", content, decryptString);
        return decryptString;
    }
}
