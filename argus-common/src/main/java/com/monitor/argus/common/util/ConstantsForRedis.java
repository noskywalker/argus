package com.monitor.argus.common.util;

/**
 * @Description:redis 常量
 * 
 * @Author: alex zhang
 * @CreateDate: 2015-4-30 下午01:15:32
 * @Version: V1.0
 */
public class ConstantsForRedis {

    /** mis角色对应菜单模块前缀 */
    public static final String MGR_ROLE_MODULE_PRE = "mis:role:module:";
    /** mis用户登录前缀 */
    public static final String MGR_LOGIN_USER_PRE = "mis:login:user:";
    /** mis用户登录过期时间 */
    public static final int MGR_LOGIN_USER_OVERDATETIME = 30 * 60;
    /** mis用户登录手机验证码前缀 */
    public static final String MGR_LOGIN_SMSCHECKCODE_PRE = "mis:login:smsCheckCode:";
    /** mis用户登录手机验证码过期时间 */
    public static final int MGR_LOGIN_SMSCHECKCODE_OVERDATETIME = 10 * 60;

}
